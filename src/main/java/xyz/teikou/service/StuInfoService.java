package xyz.teikou.service;

import xyz.teikou.entity.StuInfo;

import java.util.List;

/**
 * Creat by TeiKou
 * 2019/10/11 14:12
 */
public interface StuInfoService {
    public void addInfo(StuInfo stuInfo);

    public StuInfo myInfo(Integer id);

    public void  updateInfo(StuInfo stuInfo);

    public List<StuInfo> allInfo();

    public StuInfo theInfo(String schNumber);
    
    /**
     * 根据班级查找学生
     * @param className 班级名称
     * @return 学生列表
     */
    public List<StuInfo> findStudentsByClass(String className);
}
