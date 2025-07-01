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

    /**
     * 通过ID查找用户
     * @param id 用户ID
     * @return 用户对象
     */
    public User findUserById(Integer id);

    /**
     * 通过学号查找用户
     * @param schNumber 学号
     * @return 用户对象
     */
    public User findUserBySchNumber(String schNumber);

    /**
     * 新增方法：只查询所有学生
     * @return 学生用户列表
     */
    public List<User> findAllStudents();
}
