package xyz.teikou.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.teikou.entity.Grade;
import xyz.teikou.entity.StuInfo;
import xyz.teikou.mapper.GradeMapper;
import xyz.teikou.mapper.StuInfoMapper;
import xyz.teikou.service.GradeService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Creat by TeiKou
 * 2019/10/10 15:13
 */
@Service
public class GradeServiceImpl implements GradeService {
    @Autowired
    GradeMapper gradeMapper;

    @Autowired
    StuInfoMapper stuInfoMapper;

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

    @Override
    public Map<String, Object> getGradeStatistics(String schNumber) {
        List<Grade> grades = this.selectMyGrade(schNumber);
        Map<String, Object> statistics = new HashMap<>();

        if (grades == null || grades.isEmpty()) {
            statistics.put("average", 0);
            statistics.put("max", 0);
            statistics.put("min", 0);
            statistics.put("bestSubject", "无");
            statistics.put("weakestSubject", "无");
            statistics.put("totalSubjects", 0);
            statistics.put("passRate", 0);
            return statistics;
        }

        double total = 0;
        double max = 0;
        double min = 100;
        String bestSubject = "";
        String weakestSubject = "";
        int passCount = 0;

        for (Grade grade : grades) {
            double score = grade.getResults();
            total += score;
            if (score > max) { max = score; bestSubject = grade.getSubName(); }
            if (score < min) { min = score; weakestSubject = grade.getSubName(); }
            if (score >= 60) { passCount++; }
        }

        double average = total / grades.size();
        double passRate = (double) passCount / grades.size() * 100;

        statistics.put("average", Math.round(average * 10) / 10.0);
        statistics.put("max", max);
        statistics.put("min", min);
        statistics.put("bestSubject", bestSubject);
        statistics.put("weakestSubject", weakestSubject);
        statistics.put("totalSubjects", grades.size());
        statistics.put("passRate", Math.round(passRate * 10) / 10.0);
        statistics.put("gradesList", grades);

        return statistics;
    }

    /**
     * ==================== 核心修改在这里 ====================
     */
    @Override
    public Map<String, Object> getClassStatistics(String className) {
        Map<String, Object> statistics = new HashMap<>();

        // 首先获取该班级所有学生
        QueryWrapper<StuInfo> stuQueryWrapper = new QueryWrapper<>();
        stuQueryWrapper.eq("class_name", className);
        List<StuInfo> studentList = stuInfoMapper.selectList(stuQueryWrapper);

        // 即使班级不存在或班级内没有学生，也要返回一个包含 'subjects' 键的 Map
        if (studentList == null || studentList.isEmpty()) {
            statistics.put("classAverage", 0);
            statistics.put("studentCount", 0);
            statistics.put("passRate", 0);
            statistics.put("bestStudent", "无");
            statistics.put("subjects", Collections.emptyList()); // 确保 subjects 键存在
            return statistics;
        }

        List<String> studentNumbers = studentList.stream().map(StuInfo::getSchNumber).collect(Collectors.toList());

        // 查询这些学生的所有成绩
        QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<>();
        gradeQueryWrapper.in("sch_number", studentNumbers);
        List<Grade> allGrades = gradeMapper.selectList(gradeQueryWrapper);

        // 即使班级有学生但没有任何成绩记录，也要返回包含 'subjects' 键的 Map
        if (allGrades == null || allGrades.isEmpty()) {
            statistics.put("classAverage", 0);
            statistics.put("studentCount", studentList.size());
            statistics.put("passRate", 0);
            statistics.put("bestStudent", "无");
            statistics.put("subjects", Collections.emptyList()); // 确保 subjects 键存在
            return statistics;
        }

        // --- 后续的计算逻辑保持不变 ---
        double totalScore = allGrades.stream().mapToDouble(Grade::getResults).sum();
        double classAverage = totalScore / allGrades.size();

        long passCount = allGrades.stream().filter(g -> g.getResults() >= 60).count();
        double passRate = (double) passCount / allGrades.size() * 100;

        Map<String, List<Grade>> gradesByStudent = allGrades.stream()
                .collect(Collectors.groupingBy(Grade::getSchNumber));

        String bestStudent = "无";
        double bestAverage = 0;

        for (Map.Entry<String, List<Grade>> entry : gradesByStudent.entrySet()) {
            List<Grade> studentGrades = entry.getValue();
            double studentAvg = studentGrades.stream().mapToDouble(Grade::getResults).average().orElse(0);
            if (studentAvg > bestAverage) {
                bestAverage = studentAvg;
                bestStudent = entry.getKey();
                for (StuInfo student : studentList) {
                    if (student.getSchNumber().equals(bestStudent)) {
                        bestStudent = student.getSchNumber() + " (" + student.getName() + ")";
                        break;
                    }
                }
            }
        }

        Map<String, List<Grade>> gradesBySubject = allGrades.stream()
                .collect(Collectors.groupingBy(Grade::getSubName));

        List<Map<String, Object>> subjectStats = new ArrayList<>();
        for (Map.Entry<String, List<Grade>> entry : gradesBySubject.entrySet()) {
            String subject = entry.getKey();
            List<Grade> subjectGrades = entry.getValue();
            double subjectAvg = subjectGrades.stream().mapToDouble(Grade::getResults).average().orElse(0);
            long subjectPassCount = subjectGrades.stream().filter(g -> g.getResults() >= 60).count();
            double subjectPassRate = (double) subjectPassCount / subjectGrades.size() * 100;

            Map<String, Object> subjectStat = new HashMap<>();
            subjectStat.put("subject", subject);
            subjectStat.put("average", Math.round(subjectAvg * 10) / 10.0);
            subjectStat.put("passRate", Math.round(subjectPassRate * 10) / 10.0);
            subjectStats.add(subjectStat);
        }

        // 组装结果
        statistics.put("classAverage", Math.round(classAverage * 10) / 10.0);
        statistics.put("studentCount", studentList.size());
        statistics.put("passRate", Math.round(passRate * 10) / 10.0);
        statistics.put("bestStudent", bestStudent);
        statistics.put("subjects", subjectStats); // 正常情况下，也将 'subjects' 放入

        return statistics;
    }

    @Override
    public List<Map<String, Object>> getSubjectAverages() {
        // ... 此方法保持您提供的原样 ...
        List<Grade> allGrades = this.selectAllGrade();
        if (allGrades == null || allGrades.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, List<Grade>> gradesBySubject = allGrades.stream()
                .collect(Collectors.groupingBy(Grade::getSubName));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Grade>> entry : gradesBySubject.entrySet()) {
            String subject = entry.getKey();
            List<Grade> subjectGrades = entry.getValue();
            double average = subjectGrades.stream().mapToDouble(Grade::getResults).average().orElse(0);
            double max = subjectGrades.stream().mapToDouble(Grade::getResults).max().orElse(0);
            double min = subjectGrades.stream().mapToDouble(Grade::getResults).min().orElse(0);
            long passCount = subjectGrades.stream().filter(g -> g.getResults() >= 60).count();
            double passRate = (double) passCount / subjectGrades.size() * 100;
            Map<String, Object> subjectStat = new HashMap<>();
            subjectStat.put("subject", subject);
            subjectStat.put("average", Math.round(average * 10) / 10.0);
            subjectStat.put("max", max);
            subjectStat.put("min", min);
            subjectStat.put("count", subjectGrades.size());
            subjectStat.put("passRate", Math.round(passRate * 10) / 10.0);
            result.add(subjectStat);
        }
        return result;
    }
}