package xyz.teikou.service;

import xyz.teikou.entity.User;

import java.util.List;

/**
 * Creat by TeiKou
 * 2019/10/9 11:50
 */
public interface UserService {
    public Integer findUserName(String username);
    public Integer findSchNumber(String schNumber);
    public void addUser(User user);
    public String findPasswordByUsername(String username);
    public User findUserByUsername(String username);
    public void userUpdate(User user);
    public List<User> findAllUser();
}
