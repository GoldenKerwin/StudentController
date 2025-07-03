package xyz.teikou.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.teikou.entity.Attendance;
import xyz.teikou.entity.StuInfo;
import xyz.teikou.mapper.AttendanceMapper;
import xyz.teikou.mapper.StuInfoMapper;
import xyz.teikou.service.AttendanceService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考勤服务实现类
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {
    
    private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);
    
    @Autowired
    private AttendanceMapper attendanceMapper;
    
    @Autowired
    private StuInfoMapper stuInfoMapper;
    
    @Override
    public void addAttendance(Attendance attendance) {
        attendanceMapper.insert(attendance);
    }
    
    @Override
    public void updateAttendance(Attendance attendance) {
        attendanceMapper.updateById(attendance);
    }
    
    @Override
    public void deleteAttendance(Integer id) {
        attendanceMapper.deleteById(id);
    }
    
    @Override
    public Attendance getAttendanceById(Integer id) {
        return attendanceMapper.selectById(id);
    }
    
    @Override
    public List<Attendance> getStudentAttendance(String schNumber) {
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sch_number", schNumber);
        queryWrapper.orderByDesc("date");
        return attendanceMapper.selectList(queryWrapper);
    }

    /**
     * @param date
     * @return
     */
    @Override
    public List<Attendance> getAttendanceByDate(Date date) {
        if (date == null) {
            return Collections.emptyList();
        }
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);

        // 使用数据库的 DATE() 函数进行精确的日期匹配，忽略时间部分
        queryWrapper.apply("DATE(date) = {0}", dateString);

        return attendanceMapper.selectList(queryWrapper);
    }

    @Override
    public List<Attendance> getAttendanceByCourse(String course) {
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course", course);
        return attendanceMapper.selectList(queryWrapper);
    }


    @Override
    public List<Attendance> getClassAttendance(String className) {
        // 首先获取该班级所有学生
        QueryWrapper<StuInfo> stuQueryWrapper = new QueryWrapper<>();
        stuQueryWrapper.eq("class_name", className);
        List<StuInfo> students = stuInfoMapper.selectList(stuQueryWrapper);
        
        if (students == null || students.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 获取所有学号
        List<String> schNumbers = students.stream()
                .map(StuInfo::getSchNumber)
                .collect(Collectors.toList());
        
        // 查询这些学生的考勤记录
        QueryWrapper<Attendance> attendanceQueryWrapper = new QueryWrapper<>();
        attendanceQueryWrapper.in("sch_number", schNumbers);
        attendanceQueryWrapper.orderByDesc("date");
        
        return attendanceMapper.selectList(attendanceQueryWrapper);
    }
    
    @Override
    public Map<String, Object> getAttendanceStatistics(String schNumber) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 获取该学生的所有考勤记录
        List<Attendance> attendanceList = getStudentAttendance(schNumber);
        
        if (attendanceList == null || attendanceList.isEmpty()) {
            statistics.put("total", 0);
            statistics.put("normalCount", 0);
            statistics.put("lateCount", 0);
            statistics.put("earlyCount", 0);
            statistics.put("absentCount", 0);
            statistics.put("leaveCount", 0);
            statistics.put("normalRate", 0);
            statistics.put("attendanceList", Collections.emptyList());
            return statistics;
        }
        
        // 统计各状态的数量
        int normalCount = 0;
        int lateCount = 0;
        int earlyCount = 0;
        int absentCount = 0;
        int leaveCount = 0;
        
        for (Attendance attendance : attendanceList) {
            String status = attendance.getStatus();
            logger.debug("Student attendance status: {}", status);
            
            if (status == null) {
                logger.warn("Found null status for attendance record: {}", attendance.getId());
                continue;
            }
            
            switch (status) {
                case "正常":
                    normalCount++;
                    break;
                case "迟到":
                    lateCount++;
                    break;
                case "早退":
                    earlyCount++;
                    break;
                case "缺席":
                    absentCount++;
                    break;
                case "请假":
                    leaveCount++;
                    break;
                default:
                    logger.warn("Unknown attendance status: {}", status);
                    break;
            }
        }
        
        int total = attendanceList.size();
        double normalRate = total > 0 ? (double) normalCount / total * 100 : 0;
        
        statistics.put("total", total);
        statistics.put("normalCount", normalCount);
        statistics.put("lateCount", lateCount);
        statistics.put("earlyCount", earlyCount);
        statistics.put("absentCount", absentCount);
        statistics.put("leaveCount", leaveCount);
        statistics.put("normalRate", Math.round(normalRate * 10) / 10.0);
        statistics.put("attendanceList", attendanceList);
        
        // 获取考勤状态统计
        List<Map<String, Object>> statusCounts = attendanceMapper.countAttendanceByStatus(schNumber);
        
        // 记录日志，显示查询结果内容
        logger.info("Status counts size: {}", statusCounts != null ? statusCounts.size() : 0);
        
        if (statusCounts != null) {
            // 转换状态统计数据，确保字段名一致
            List<Map<String, Object>> normalizedStatusCounts = new ArrayList<>();
            
            for (Map<String, Object> statusCount : statusCounts) {
                logger.debug("Status count entry: {}", statusCount);
                
                // 创建新的规范化Map
                Map<String, Object> normalizedMap = new HashMap<>();
                
                // 处理status字段
                Object statusValue = null;
                for (String key : new String[]{"status", "STATUS", "Status"}) {
                    if (statusCount.containsKey(key)) {
                        statusValue = statusCount.get(key);
                        break;
                    }
                }
                if (statusValue != null) {
                    normalizedMap.put("status", statusValue);
                    logger.debug("Found status: {}", statusValue);
                } else {
                    logger.warn("Status field not found in query result");
                    normalizedMap.put("status", "未知");
                }
                
                // 处理count字段
                Object countValue = null;
                for (String key : new String[]{"count", "COUNT", "Count"}) {
                    if (statusCount.containsKey(key)) {
                        countValue = statusCount.get(key);
                        break;
                    }
                }
                if (countValue != null) {
                    normalizedMap.put("count", countValue);
                    logger.debug("Found count: {}", countValue);
                } else {
                    logger.warn("Count field not found in query result");
                    normalizedMap.put("count", 0);
                }
                
                normalizedStatusCounts.add(normalizedMap);
            }
            
            statistics.put("statusCounts", normalizedStatusCounts);
        } else {
            statistics.put("statusCounts", Collections.emptyList());
        }
        
        return statistics;
    }
    
    @Override
    public Map<String, Object> getClassAttendanceStatistics(String className) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 获取班级考勤记录
        List<Attendance> attendanceList = getClassAttendance(className);
        
        if (attendanceList == null || attendanceList.isEmpty()) {
            statistics.put("total", 0);
            statistics.put("normalCount", 0);
            statistics.put("lateCount", 0);
            statistics.put("earlyCount", 0);
            statistics.put("absentCount", 0);
            statistics.put("leaveCount", 0);
            statistics.put("normalRate", 0);
            statistics.put("statusCounts", Collections.emptyList());
            return statistics;
        }
        
        // 统计各状态的数量
        int normalCount = 0;
        int lateCount = 0;
        int earlyCount = 0;
        int absentCount = 0;
        int leaveCount = 0;
        
        for (Attendance attendance : attendanceList) {
            String status = attendance.getStatus();
            logger.debug("Class attendance status: {}", status);
            
            if (status == null) {
                logger.warn("Found null status for attendance record: {}", attendance.getId());
                continue;
            }
            
            switch (status) {
                case "正常":
                    normalCount++;
                    break;
                case "迟到":
                    lateCount++;
                    break;
                case "早退":
                    earlyCount++;
                    break;
                case "缺席":
                    absentCount++;
                    break;
                case "请假":
                    leaveCount++;
                    break;
                default:
                    logger.warn("Unknown attendance status: {}", status);
                    break;
            }
        }
        
        int total = attendanceList.size();
        double normalRate = total > 0 ? (double) normalCount / total * 100 : 0;
        
        statistics.put("total", total);
        statistics.put("normalCount", normalCount);
        statistics.put("lateCount", lateCount);
        statistics.put("earlyCount", earlyCount);
        statistics.put("absentCount", absentCount);
        statistics.put("leaveCount", leaveCount);
        statistics.put("normalRate", Math.round(normalRate * 10) / 10.0);
        
        // 获取考勤状态统计
        List<Map<String, Object>> statusCounts = attendanceMapper.countClassAttendanceByStatus(className);
        
        // 记录日志，显示查询结果内容
        logger.info("Class status counts size: {}", statusCounts != null ? statusCounts.size() : 0);
        
        if (statusCounts != null) {
            // 转换状态统计数据，确保字段名一致
            List<Map<String, Object>> normalizedStatusCounts = new ArrayList<>();
            
            for (Map<String, Object> statusCount : statusCounts) {
                logger.debug("Class status count entry: {}", statusCount);
                
                // 创建新的规范化Map
                Map<String, Object> normalizedMap = new HashMap<>();
                
                // 处理status字段
                Object statusValue = null;
                for (String key : new String[]{"status", "STATUS", "Status"}) {
                    if (statusCount.containsKey(key)) {
                        statusValue = statusCount.get(key);
                        break;
                    }
                }
                if (statusValue != null) {
                    normalizedMap.put("status", statusValue);
                    logger.debug("Found class status: {}", statusValue);
                } else {
                    logger.warn("Status field not found in class query result");
                    normalizedMap.put("status", "未知");
                }
                
                // 处理count字段
                Object countValue = null;
                for (String key : new String[]{"count", "COUNT", "Count"}) {
                    if (statusCount.containsKey(key)) {
                        countValue = statusCount.get(key);
                        break;
                    }
                }
                if (countValue != null) {
                    normalizedMap.put("count", countValue);
                    logger.debug("Found class count: {}", countValue);
                } else {
                    logger.warn("Count field not found in class query result");
                    normalizedMap.put("count", 0);
                }
                
                normalizedStatusCounts.add(normalizedMap);
            }
            
            statistics.put("statusCounts", normalizedStatusCounts);
        } else {
            statistics.put("statusCounts", Collections.emptyList());
        }
        
        return statistics;
    }
} 