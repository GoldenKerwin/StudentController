package xyz.teikou.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.teikou.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
