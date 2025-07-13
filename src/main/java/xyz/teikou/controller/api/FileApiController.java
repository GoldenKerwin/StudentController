package xyz.teikou.controller.api;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;
public interface FileApiController {

    ModelAndView teacherFilePage();

    ModelAndView studentFilePage();

    Map<String, Object> handleFileUpload(
            MultipartFile file,
            String fileType,
            String courseName,
            Integer studentId
    );

    ResponseEntity<Resource> downloadFile(Integer fileId);

    /**
     * 处理学生更新作业的接口
     */
    Map<String, Object> updateHomework(
            Integer fileId,
            MultipartFile file
    );
}