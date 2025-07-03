package xyz.teikou.controller.api;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;

/**
 * 文件管理 API 的接口定义 (API Contract)。
 * 注解都移到实现类中，这里只保留方法签名。
 */
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
     * 新增：处理学生更新作业的接口
     */
    Map<String, Object> updateHomework(
            Integer fileId,
            MultipartFile file
    );
}