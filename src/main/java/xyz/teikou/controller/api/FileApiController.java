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
 * 定义了文件上传、下载及相关页面的查看功能。
 */
public interface FileApiController {

    /**
     * 获取教师的文件管理页面。
     * @return 包含渲染页面所需数据的 ModelAndView 对象。
     */
    ModelAndView teacherFilePage();

    /**
     * 获取学生的文件管理页面。
     * @return 包含渲染页面所需数据的 ModelAndView 对象。
     */
    ModelAndView studentFilePage();

    /**
     * 处理文件上传的通用接口。
     * @param file 上传的文件 (MultipartFile)。
     * @param fileType 文件类型 (PPT, HOMEWORK, GRADED_HOMEWORK)。
     * @param courseName 关联的课程名 (可选)。
     * @param studentId 关联的学生ID (可选, 用于老师上传批改作业)。
     * @return 一个表示上传结果的字符串消息。
     */
    Map<String, Object> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType,
            @RequestParam(value = "courseName", required = false) String courseName,
            @RequestParam(value = "studentId", required = false) Integer studentId
    );

    /**
     * 处理文件下载的通用接口。
     * @param fileId 要下载的文件的数据库ID。
     * @return 包含文件流的 ResponseEntity<Resource>，以便浏览器下载。
     */
    ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId);
}