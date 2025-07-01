package xyz.teikou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.teikou.entity.FileUpload;

@Mapper
public interface FileUploadMapper extends BaseMapper<FileUpload> {
}