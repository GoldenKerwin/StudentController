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
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sch_number", schNumber);
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
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sch_number", schNumber);
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
            if (score > max) {
                max = score;
                bestSubject = grade.getSubName();
            }
            if (score < min) {
                min = score;
                weakestSubject = grade.getSubName();
            }
            if (score >= 60) {
                passCount++;
            }
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

    @Override
    public Map<String, Object> getClassStatistics(String className) {
        Map<String, Object> statistics = new HashMap<>();

        QueryWrapper<StuInfo> stuQueryWrapper = new QueryWrapper<>();
        stuQueryWrapper.eq("class_name", className);
        List<StuInfo> studentList = stuInfoMapper.selectList(stuQueryWrapper);

        if (studentList == null || studentList.isEmpty()) {
            statistics.put("classAverage", 0);
            statistics.put("studentCount", 0);
            statistics.put("passRate", 0);
            statistics.put("bestStudent", "无");
            statistics.put("subjects", Collections.emptyList());
            return statistics;
        }

        List<String> studentNumbers = studentList.stream().map(StuInfo::getSchNumber).collect(Collectors.toList());

        QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<>();
        gradeQueryWrapper.in("sch_number", studentNumbers);
        List<Grade> allGrades = gradeMapper.selectList(gradeQueryWrapper);

        if (allGrades == null || allGrades.isEmpty()) {
            statistics.put("classAverage", 0);
            statistics.put("studentCount", studentList.size());
            statistics.put("passRate", 0);
            statistics.put("bestStudent", "无");
            statistics.put("subjects", Collections.emptyList());
            return statistics;
        }

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

        statistics.put("classAverage", Math.round(classAverage * 10) / 10.0);
        statistics.put("studentCount", studentList.size());
        statistics.put("passRate", Math.round(passRate * 10) / 10.0);
        statistics.put("bestStudent", bestStudent);
        statistics.put("subjects", subjectStats);

        return statistics;
    }

    /**
     * ==================== 核心修改 1: 改造此方法以支持筛选和排序 ====================
     * 注意：您需要同步修改 GradeService 接口中的方法签名。
     */
    /**
     * ==================== 核心修改 1: 改造此方法以支持筛选和排序 ====================
     */
    @Override
    public List<Map<String, Object>> getSubjectAverages(String subjectName, String testNo, String sortField, String sortOrder) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();

        if (subjectName != null && !subjectName.isEmpty()) {
            queryWrapper.like("sub_name", subjectName);
        }
        if (testNo != null && !testNo.isEmpty()) {
            queryWrapper.eq("test_no", testNo);
        }

        List<Grade> allGrades = gradeMapper.selectList(queryWrapper);

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
            subjectStat.put("count", (long) subjectGrades.size());
            subjectStat.put("passRate", Math.round(passRate * 10) / 10.0);

            result.add(subjectStat);
        }

        // --- 排序逻辑的修正 ---
        if (sortField != null && !sortField.isEmpty()) {
            Comparator<Map<String, Object>> comparator;

            // 判断排序字段是否为数字类型
            if ("average".equals(sortField) || "max".equals(sortField) || "min".equals(sortField) || "count".equals(sortField) || "passRate".equals(sortField)) {
                // 如果是数字，创建一个比较 Double 值的比较器
                comparator = Comparator.comparing(m -> {
                    Object value = m.get(sortField);
                    if (value instanceof Number) {
                        return ((Number) value).doubleValue();
                    }
                    return 0.0; // 如果值不是数字类型，默认为0.0进行比较
                });
            } else {
                // 如果是字符串（如科目名称），创建一个比较 String 值的比较器
                comparator = Comparator.comparing(m ->
                        String.valueOf(m.getOrDefault(sortField, ""))
                );
            }

            // 如果排序顺序是降序，则反转比较器
            if ("desc".equalsIgnoreCase(sortOrder)) {
                comparator = comparator.reversed();
            }

            // 执行排序
            result.sort(comparator);
        }

        return result;
    }

    /**
     * ==================== 核心修改 2: 新增此方法用于获取学期列表 ====================
     * 注意：您需要同步修改 GradeService 接口中的方法签名。
     */
    @Override
    public List<String> getDistinctTerms() {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT test_no").isNotNull("test_no");

        List<Map<String, Object>> maps = gradeMapper.selectMaps(queryWrapper);

        return maps.stream()
                .map(m -> (String) m.get("test_no"))
                .filter(Objects::nonNull) // 过滤掉null值
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}