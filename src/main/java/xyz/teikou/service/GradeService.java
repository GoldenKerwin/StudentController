package xyz.teikou.service;

import xyz.teikou.entity.Grade;

import java.util.List;

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
}
