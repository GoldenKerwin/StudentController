package xyz.teikou.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.teikou.entity.User;
import xyz.teikou.mapper.UserMapper;
import xyz.teikou.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Integer findUserName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer findSchNumber(String schNumber) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sch_number", schNumber);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public void addUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public String findPasswordByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        return user != null ? user.getPassword() : null;
    }

    @Override
    public User findUserByUsername(String username) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public void userUpdate(User user) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        userMapper.update(user,queryWrapper);
    }

    @Override
    public List<User> findAllUser() {
        return userMapper.selectList(null);
    }

    @Override
    public List<User> findAllStudents() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 假设学生的 role_id 在数据库中是 1
        queryWrapper.eq("role_id", 1);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findUserBySchNumber(String schNumber) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sch_number", schNumber);
        return userMapper.selectOne(queryWrapper);
    }
}