package xyz.teikou.controller; // 您的包路径

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import xyz.teikou.controller.api.FileApiController;
import xyz.teikou.entity.FileUpload;
import xyz.teikou.entity.User;
import xyz.teikou.service.FileUploadService;
import xyz.teikou.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/file")
public class FileController implements FileApiController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private UserService userService;

    @Value("${file.upload-dir:D:/student-system-uploads-default/}")
    private String uploadDir;

    @Override
    @GetMapping("/teacher")
    @RequiresRoles("2")
    public ModelAndView teacherFilePage() {
        ModelAndView mv = new ModelAndView("teacher_files");
        mv.addObject("students", userService.findAllStudents());
        mv.addObject("ppts", fileUploadService.lambdaQuery().eq(FileUpload::getFileType, "PPT").orderByDesc(FileUpload::getCreatedAt).list());
        mv.addObject("homeworks", fileUploadService.lambdaQuery().eq(FileUpload::getFileType, "HOMEWORK").orderByDesc(FileUpload::getCreatedAt).list());
        return mv;
    }

    @Override
    @GetMapping("/student")
    @RequiresRoles("1")
    public ModelAndView studentFilePage() {
        ModelAndView mv = new ModelAndView("student_files");
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        mv.addObject("ppts", fileUploadService.lambdaQuery().eq(FileUpload::getFileType, "PPT").orderByDesc(FileUpload::getCreatedAt).list());
        mv.addObject("myHomeworks", fileUploadService.lambdaQuery().eq(FileUpload::getFileType, "HOMEWORK").eq(FileUpload::getUploaderId, currentUser.getId()).orderByDesc(FileUpload::getCreatedAt).list());
        mv.addObject("gradedHomeworks", fileUploadService.lambdaQuery().eq(FileUpload::getFileType, "GRADED_HOMEWORK").eq(FileUpload::getStudentId, currentUser.getId()).orderByDesc(FileUpload::getCreatedAt).list());
        return mv;
    }

    @Override
    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> handleFileUpload(MultipartFile file, String fileType, String courseName, Integer studentId) {
        Map<String, Object> response = new HashMap<>();
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        try {
            String originalFilename = file.getOriginalFilename();
            String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path targetLocation = uploadPath.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            FileUpload fileUpload = new FileUpload();
            fileUpload.setOriginalFilename(originalFilename);
            fileUpload.setStoredFilename(storedFilename);
            fileUpload.setFilePath(targetLocation.toString());
            fileUpload.setFileSize(file.getSize());
            fileUpload.setFileType(fileType);
            fileUpload.setUploaderId(currentUser.getId());
            fileUpload.setCourseName(courseName);
            fileUpload.setStudentId(studentId);
            fileUpload.setCreatedAt(LocalDateTime.now());
            fileUploadService.save(fileUpload);
            response.put("success", true);
            response.put("message", "文件 '" + originalFilename + "' 上传成功！");
        } catch (Exception ex) {
            log.error("文件上传失败", ex);
            response.put("success", false);
            response.put("message", "文件上传失败：" + ex.getMessage());
        }
        return response;
    }

    @Override
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(Integer fileId) {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        FileUpload fileUpload = fileUploadService.getById(fileId);

        // 为 if 语句添加大括号
        if (fileUpload == null) {
            return ResponseEntity.notFound().build();
        }

        boolean hasPermission = false;
        if (currentUser.getRoleId() == 2) {
            hasPermission = true;
        } else if (currentUser.getRoleId() == 1) {
            String fileType = fileUpload.getFileType();
            if ("PPT".equals(fileType) || fileUpload.getUploaderId().equals(currentUser.getId()) || (fileUpload.getStudentId() != null && fileUpload.getStudentId().equals(currentUser.getId()))) {
                hasPermission = true;
            }
        }

        if (!hasPermission) {
            return ResponseEntity.status(403).build();
        }

        try {
            Path filePath = Paths.get(fileUpload.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                String encodedFilename = new String(fileUpload.getOriginalFilename().getBytes("UTF-8"), "ISO-8859-1");
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("文件不存在或无法读取: " + fileUpload.getOriginalFilename());
            }
        } catch (Exception ex) {
            log.error("文件下载失败: fileId=" + fileId, ex);
            return ResponseEntity.status(500).build();
        }
    }
}