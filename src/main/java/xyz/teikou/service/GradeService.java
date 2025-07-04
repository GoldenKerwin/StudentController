package xyz.teikou.service;

import xyz.teikou.entity.Grade;

import java.util.List;
import java.util.Map;

/**
 * Creat by TeiKou
 * 2019/10/10 15:11
 */
public interface GradeService {

    public void addGrade(Grade grade);

    public List<Grade> selectMyGrade(String schNumber);

    public List<Grade> selectAllGrade();

    public List<Grade> seleOneGrade(String schNumber);

    public void updateGrade(Grade grade);

    public Grade seleTheGrade(Integer id);
    
    /**
     * 获取学生成绩统计信息，包括平均分、最高分、最低分等
     * @param schNumber 学号
     * @return 统计数据Map
     */
    public Map<String, Object> getGradeStatistics(String schNumber);
    
    /**
     * 获取班级成绩统计信息
     * @param className 班级名称
     * @return 统计数据Map
     */
    public Map<String, Object> getClassStatistics(String className);
    
    /**
     * 获取各科目平均分统计
     * @return 各科目平均分列表
     */
    List<Map<String, Object>> getSubjectAverages(String subjectName, String testNo, String sortField, String sortOrder);

    List<String> getDistinctTerms();
}

