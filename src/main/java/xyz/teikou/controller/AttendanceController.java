package xyz.teikou.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import xyz.teikou.entity.Attendance;
import xyz.teikou.entity.StuInfo;
import xyz.teikou.entity.User;
import xyz.teikou.form.AttendanceForm;
import xyz.teikou.service.AttendanceService;
import xyz.teikou.service.StuInfoService;
import xyz.teikou.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考勤管理控制器
 */
@Slf4j
@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    @Autowired
    private StuInfoService stuInfoService;

    /**
     * 跳转到录入考勤页面
     */
    @RequestMapping("/record")
    @RequiresPermissions("teacher:attendance:manage")
    public ModelAndView recordAttendance() {
        ModelAndView mv = new ModelAndView("recordAttendance");
        List<User> students = userService.findAllUser();
        mv.addObject("students", students != null ? students : Collections.emptyList());
        return mv;
    }

    /**
     * 保存考勤记录
     */
    @RequestMapping("/save")
    @RequiresPermissions("teacher:attendance:manage")
    public ModelAndView saveAttendance(@Valid AttendanceForm form, BindingResult result, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (result.hasErrors()) {
            mv.setViewName("recordAttendance");
            List<User> students = userService.findAllUser();
            mv.addObject("students", students != null ? students : Collections.emptyList());
            mv.addObject("info", "表单验证失败，请检查输入");
            return mv;
        }
        User user = (User) request.getSession().getAttribute("user");
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(form, attendance);
        attendance.setOperator(user != null ? user.getName() : "未知用户");
        try {
            attendanceService.addAttendance(attendance);
            mv.setViewName("success");
        } catch (Exception e) {
            log.error("保存考勤记录失败", e);
            mv.setViewName("recordAttendance");
            List<User> students = userService.findAllUser();
            mv.addObject("students", students != null ? students : Collections.emptyList());
            mv.addObject("info", "保存考勤记录失败，请稍后重试");
        }
        return mv;
    }

    /**
     * 学生查看自己的考勤记录
     */
    @RequestMapping("/myAttendance")
    @RequiresPermissions("stu:attendance:view")
    public ModelAndView myAttendance(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("myAttendance");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            mv.addObject("statistics", null);
            return mv;
        }
        String schNumber = user.getSchNumber();
        Map<String, Object> statistics = attendanceService.getAttendanceStatistics(schNumber);
        mv.addObject("statistics", statistics);
        return mv;
    }

    /**
     * ==================== 关键修改 1: 增强此方法以传递学生姓名 ====================
     * 教师查看学生考勤记录
     */
    @RequestMapping("/studentAttendance")
    @RequiresPermissions("teacher:attendance:manage")
    public ModelAndView studentAttendance(@RequestParam("schNumber") String schNumber) {
        ModelAndView mv = new ModelAndView("studentAttendance");
        if (schNumber == null || schNumber.isEmpty()) {
            mv.addObject("statistics", null);
            mv.addObject("schNumber", "");
            return mv;
        }

        // 新增逻辑：通过学号查询学生信息，以便在详情页显示姓名
        User student = userService.findUserBySchNumber(schNumber);
        if (student != null) {
            mv.addObject("studentName", student.getName());
        }

        Map<String, Object> statistics = attendanceService.getAttendanceStatistics(schNumber);
        mv.addObject("statistics", statistics);
        mv.addObject("schNumber", schNumber);
        return mv;
    }

    /**
     * 教师查看班级考勤统计
     */
    @RequestMapping("/classAttendance")
    @RequiresPermissions("teacher:attendance:manage")
    public ModelAndView classAttendance(@RequestParam(value = "className", required = false) String className) {
        ModelAndView mv = new ModelAndView("classAttendance");
        if (className != null && !className.isEmpty()) {
            Map<String, Object> statistics = attendanceService.getClassAttendanceStatistics(className);
            mv.addObject("statistics", statistics);
            mv.addObject("className", className);
        }
        return mv;
    }

    /**
     * 教师查看班级考勤详细统计
     */
    @RequestMapping("/classAttendanceDetail")
    @RequiresPermissions("teacher:attendance:manage")
    public ModelAndView classAttendanceDetail(@RequestParam(value = "className", required = false) String className) {
        ModelAndView mv = new ModelAndView("classAttendanceDetail");
        if (className != null && !className.isEmpty()) {
            Map<String, Object> statistics = attendanceService.getClassAttendanceStatistics(className);
            mv.addObject("statistics", statistics);
            List<Attendance> attendanceList = attendanceService.getClassAttendance(className);
            mv.addObject("attendanceList", attendanceList != null ? attendanceList : Collections.emptyList());
            List<StuInfo> students = stuInfoService.findStudentsByClass(className);
            Map<String, String> studentNames = Collections.emptyMap();
            if (students != null && !students.isEmpty()) {
                studentNames = students.stream()
                        .collect(Collectors.toMap(
                                StuInfo::getSchNumber,
                                StuInfo::getName,
                                (existing, replacement) -> existing
                        ));
            }
            mv.addObject("studentNames", studentNames);
            mv.addObject("className", className);
        }
        return mv;
    }

    /**
     * ==================== 核心修复在这里 ====================
     * 教师查看日期考勤记录
     */
    @RequestMapping("/dateAttendance")
    @RequiresPermissions("teacher:attendance:manage")
    public ModelAndView dateAttendance(@RequestParam(value = "date", required = false) String dateString) {

        ModelAndView mv = new ModelAndView("dateAttendance");

        // 只有当用户确实提交了一个非空日期时，才进行查询和数据传递
        if (dateString != null && !dateString.isEmpty()) {
            List<Attendance> attendanceList = attendanceService.getAttendanceByDate(dateString);

            mv.addObject("attendanceList", attendanceList != null ? attendanceList : Collections.emptyList());

            if (attendanceList == null || attendanceList.isEmpty()) {
                mv.addObject("message", "没有找到 " + dateString + " 的考勤记录");
            }
        }

        // 始终回显用户查询的日期（如果是首次访问，则为null）
        mv.addObject("queryDate", dateString);

        return mv;
    }

    /**
     * 教师删除考勤记录
     */
    @RequestMapping("/delete")
    @RequiresPermissions("teacher:attendance:manage")
    public ModelAndView deleteAttendance(@RequestParam("id") Integer id, @RequestParam(value = "returnUrl", required = false) String returnUrl) {
        ModelAndView mv = new ModelAndView();
        try {
            attendanceService.deleteAttendance(id);
            if (returnUrl != null && !returnUrl.isEmpty()) {
                mv.setViewName("redirect:" + returnUrl);
            } else {
                mv.setViewName("success");
            }
        } catch (Exception e) {
            log.error("删除考勤记录失败", e);
            mv.setViewName("error");
            mv.addObject("message", "删除考勤记录失败，请稍后重试");
        }
        return mv;
    }

    /**
     * 跳转到编辑考勤页面
     */
    @RequestMapping("/edit")
    @RequiresPermissions("teacher:attendance:manage")
    public ModelAndView editAttendance(@RequestParam("id") Integer id) {
        ModelAndView mv = new ModelAndView("editAttendance");
        try {
            Attendance attendance = attendanceService.getAttendanceById(id);
            if (attendance == null) {
                mv.setViewName("error");
                mv.addObject("message", "未找到该考勤记录，请检查ID是否正确");
                return mv;
            }
            mv.addObject("attendance", attendance);
        } catch (Exception e) {
            log.error("获取考勤记录失败", e);
            mv.setViewName("error");
            mv.addObject("message", "获取考勤记录失败，请稍后重试");
        }
        return mv;
    }

    /**
     * 更新考勤记录
     */
    @RequestMapping("/update")
    @RequiresPermissions("teacher:attendance:manage")
    public ModelAndView updateAttendance(@Valid AttendanceForm form, BindingResult result, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (result.hasErrors()) {
            mv.setViewName("editAttendance");
            mv.addObject("attendance", form);
            mv.addObject("info", "表单验证失败，请检查输入");
            return mv;
        }
        try {
            User user = (User) request.getSession().getAttribute("user");
            Attendance attendance = new Attendance();
            BeanUtils.copyProperties(form, attendance);
            attendance.setOperator(user != null ? user.getName() : "未知用户");
            attendanceService.updateAttendance(attendance);
            mv.setViewName("success");
        } catch (Exception e) {
            log.error("更新考勤记录失败", e);
            mv.setViewName("editAttendance");
            mv.addObject("attendance", form);
            mv.addObject("info", "更新考勤记录失败，请稍后重试");
        }
        return mv;
    }

    /**
     * 跳转到学生考勤查询的搜索页面
     */
    @RequestMapping("/studentSearch")
    @RequiresPermissions("teacher:attendance:manage")
    public String studentSearchPage() {
        return "studentAttendanceSearch"; // 返回搜索页面的HTML文件名
    }
}