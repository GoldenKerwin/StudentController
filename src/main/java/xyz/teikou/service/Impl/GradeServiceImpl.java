package xyz.teikou.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.teikou.entity.Grade;
import xyz.teikou.mapper.GradeMapper;
import xyz.teikou.service.GradeService;

import java.util.List;

/**
 * Creat by TeiKou
 * 2019/10/10 15:13
 */
@Service
public class GradeServiceImpl implements GradeService {
    @Autowired
    GradeMapper gradeMapper;

    @Override
    public void addGrade(Grade grade) {
        gradeMapper.insert(grade);
    }

    @Override
    public List<Grade> selectMyGrade(String schNumber) {
        QueryWrapper<Grade> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("sch_number",schNumber);
        List<Grade> grades = gradeMapper.selectList(queryWrapper);
        return grades;
    }

    @Override
    public List<Grade> selectAllGrade() {
        List<Grade> grades = gradeMapper.selectList(null);
        return grades;
    }

    @Override
    public List<Grade> seleOneGrade(String schNumber) {
        QueryWrapper<Grade> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("sch_number",schNumber);
        List<Grade> grades = gradeMapper.selectList(queryWrapper);
        return grades;
    }

    @Override
    public void updateGrade(Grade grade) {
        gradeMapper.updateById(grade);
    }

    @Override
    public Grade seleTheGrade(Integer id) {

        Grade grade = gradeMapper.selectById(id);
        return grade;
    }
}
