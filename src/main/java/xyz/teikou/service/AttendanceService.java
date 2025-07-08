package xyz.teikou.service;

import xyz.teikou.entity.Attendance;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 考勤服务接口
 */
public interface AttendanceService {
    
    /**
     * 添加考勤记录
     * @param attendance 考勤对象
     */
    void addAttendance(Attendance attendance);
    
    /**
     * 更新考勤记录
     * @param attendance 考勤对象
     */
    void updateAttendance(Attendance attendance);
    
    /**
     * 删除考勤记录
     * @param id 考勤ID
     */
    void deleteAttendance(Integer id);
    
    /**
     * 获取考勤详情
     * @param id 考勤ID
     * @return 考勤对象
     */
    Attendance getAttendanceById(Integer id);
    
    /**
     * 获取学生考勤记录
     * @param schNumber 学号
     * @return 考勤列表
     */
    List<Attendance> getStudentAttendance(String schNumber);
    
    /**
     * 获取特定日期的考勤记录
     * @param date 日期
     * @return 考勤列表
     */
    List<Attendance> getAttendanceByDate(String dateString);
    
    /**
     * 获取特定课程的考勤记录
     * @param course 课程
     * @return 考勤列表
     */
    List<Attendance> getAttendanceByCourse(String course);
    
    /**
     * 获取班级考勤记录
     * @param className 班级名称
     * @return 考勤列表
     */
    List<Attendance> getClassAttendance(String className);
    
    /**
     * 获取学生考勤统计数据
     * @param schNumber 学号
     * @return 统计数据
     */
    Map<String, Object> getAttendanceStatistics(String schNumber);
    
    /**
     * 获取班级考勤统计数据
     * @param className 班级名称
     * @return 统计数据
     */
    Map<String, Object> getClassAttendanceStatistics(String className);
} 