package xyz.teikou.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.teikou.entity.Attendance;
import xyz.teikou.service.AttendanceService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/attendance")
@Api(tags = "考勤管理API")
public class AttendanceApiController {

    @Autowired
    private AttendanceService attendanceService;

    /**
     * 获取特定学生的考勤记录
     */
    @GetMapping("/student/{schNumber}")
    @ApiOperation("获取学生考勤记录")
    @RequiresPermissions({"teacher:attendance:manage", "stu:attendance:view"})
    public ResponseEntity<Map<String, Object>> getStudentAttendance(
            @ApiParam(value = "学号", required = true) @PathVariable("schNumber") String schNumber) {
        Map<String, Object> statistics = attendanceService.getAttendanceStatistics(schNumber);
        return ResponseEntity.ok(statistics);
    }

    /**
     * 获取特定日期的考勤记录
     */
    @GetMapping("/date/{date}")
    @ApiOperation("获取特定日期的考勤记录")
    @RequiresPermissions("teacher:attendance:manage")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(
            @ApiParam(value = "日期 (yyyy-MM-dd)", required = true) 
            @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        List<Attendance> attendanceList = attendanceService.getAttendanceByDate(date);
        return ResponseEntity.ok(attendanceList);
    }

    /**
     * 获取特定班级的考勤统计
     */
    @GetMapping("/class/{className}")
    @ApiOperation("获取班级考勤统计")
    @RequiresPermissions("teacher:attendance:manage")
    public ResponseEntity<Map<String, Object>> getClassAttendanceStatistics(
            @ApiParam(value = "班级名称", required = true) @PathVariable("className") String className) {
        Map<String, Object> statistics = attendanceService.getClassAttendanceStatistics(className);
        return ResponseEntity.ok(statistics);
    }

    /**
     * 获取特定课程的考勤记录
     */
    @GetMapping("/course/{course}")
    @ApiOperation("获取特定课程的考勤记录")
    @RequiresPermissions("teacher:attendance:manage")
    public ResponseEntity<List<Attendance>> getAttendanceByCourse(
            @ApiParam(value = "课程名称", required = true) @PathVariable("course") String course) {
        List<Attendance> attendanceList = attendanceService.getAttendanceByCourse(course);
        return ResponseEntity.ok(attendanceList);
    }

    /**
     * 添加考勤记录
     */
    @PostMapping
    @ApiOperation("添加考勤记录")
    @RequiresPermissions("teacher:attendance:manage")
    public ResponseEntity<Map<String, Object>> addAttendance(@RequestBody Attendance attendance) {
        attendanceService.addAttendance(attendance);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "考勤记录添加成功");
        return ResponseEntity.ok(result);
    }

    /**
     * 更新考勤记录
     */
    @PutMapping("/{id}")
    @ApiOperation("更新考勤记录")
    @RequiresPermissions("teacher:attendance:manage")
    public ResponseEntity<Map<String, Object>> updateAttendance(
            @ApiParam(value = "考勤ID", required = true) @PathVariable("id") Integer id,
            @RequestBody Attendance attendance) {
        attendance.setId(id);
        attendanceService.updateAttendance(attendance);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "考勤记录更新成功");
        return ResponseEntity.ok(result);
    }

    /**
     * 删除考勤记录
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除考勤记录")
    @RequiresPermissions("teacher:attendance:manage")
    public ResponseEntity<Map<String, Object>> deleteAttendance(
            @ApiParam(value = "考勤ID", required = true) @PathVariable("id") Integer id) {
        attendanceService.deleteAttendance(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "考勤记录删除成功");
        return ResponseEntity.ok(result);
    }

    /**
     * 获取单条考勤记录
     */
    @GetMapping("/{id}")
    @ApiOperation("获取单条考勤记录")
    @RequiresPermissions({"teacher:attendance:manage", "stu:attendance:view"})
    public ResponseEntity<Attendance> getAttendanceById(
            @ApiParam(value = "考勤ID", required = true) @PathVariable("id") Integer id) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(attendance);
    }
} 