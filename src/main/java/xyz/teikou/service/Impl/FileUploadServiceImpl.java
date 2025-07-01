package xyz.teikou.service.Impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.teikou.entity.FileUpload;
import xyz.teikou.mapper.FileUploadMapper;
import xyz.teikou.service.FileUploadService;
@Service
public class FileUploadServiceImpl extends ServiceImpl<FileUploadMapper, FileUpload> implements FileUploadService {
}