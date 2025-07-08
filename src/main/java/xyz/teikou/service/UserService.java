package xyz.teikou.service;

import xyz.teikou.entity.User;

import java.util.List;

public interface UserService {
    public Integer findUserName(String username);
    public Integer findSchNumber(String schNumber);
    public void addUser(User user);
    public String findPasswordByUsername(String username);
    public User findUserByUsername(String username);
    public void userUpdate(User user);
    public List<User> findAllUser();
    public User findUserById(Integer id);
    public User findUserBySchNumber(String schNumber);
    public List<User> findAllStudents();
}
