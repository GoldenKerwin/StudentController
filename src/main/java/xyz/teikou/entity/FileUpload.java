package xyz.teikou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FileUpload implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String originalFilename;
    private String storedFilename;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private Integer uploaderId;
    private Integer studentId;
    private String courseName;
    private LocalDateTime createdAt;
}