package xyz.teikou.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.teikou.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author teikou
 * @since 2019-10-09
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
