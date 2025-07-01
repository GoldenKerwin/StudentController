package xyz.teikou.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import xyz.teikou.entity.Grade;
import xyz.teikou.entity.User;
import xyz.teikou.form.GradeForm;
import xyz.teikou.service.GradeService;
import xyz.teikou.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 成绩管理控制器
 * 整合学生端和教师端成绩管理功能
 */
@Slf4j
@Controller
@RequestMapping("/grade")
public class GradeController {
    
    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private UserService userService;

    /**
     * 学生查询自己的成绩
     */
    @RequestMapping("/myGrades")
    @RequiresRoles("1")
    public ModelAndView myGrades(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("myGrage");
        User user = (User) request.getSession().getAttribute("user");
        String schNumber = user.getSchNumber();
        
        try {
            List<Grade> grades = gradeService.selectMyGrade(schNumber);
            mv.addObject("grades", grades != null ? grades : new ArrayList<>());
        } catch (Exception e) {
            log.error("查询成绩时出错: " + e.getMessage(), e);
            mv.addObject("grades", new ArrayList<>());
            mv.addObject("errorMsg", "查询成绩时出错，可能是数据库中没有对应记录");
        }
        
        return mv;
    }

    /**
     * 教师添加成绩页面
     */
    @RequestMapping("/insert")
    @RequiresRoles("2")
    public ModelAndView insert() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("addGrade");
        List<User> allUser = userService.findAllUser();
        mv.addObject("schList", allUser);
        return mv;
    }

    /**
     * 教师添加成绩
     */
    @RequestMapping("/addGrade")
    @RequiresRoles("2")
    public ModelAndView addGrade(@Valid GradeForm gradeForm, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        
        if (bindingResult.hasErrors()) {
            mv.setViewName("addGrade");
            List<User> allUser = userService.findAllUser();
            mv.addObject("schList", allUser);
            mv.addObject("info", "表单验证失败，请检查输入");
            return mv;
        }
        
        Grade grade = new Grade();
        BeanUtils.copyProperties(gradeForm, grade);
        gradeService.addGrade(grade);
        mv.setViewName("success");
        return mv;
    }

    /**
     * 教师查询所有成绩
     */
    @RequestMapping("/findAll")
    @RequiresRoles("2")
    public ModelAndView findAll() {
        ModelAndView mv = new ModelAndView("allGrades");
        
        try {
            List<Grade> grades = gradeService.selectAllGrade();
            mv.addObject("grades", grades != null ? grades : new ArrayList<>());
        } catch (Exception e) {
            log.error("查询所有成绩时出错: " + e.getMessage(), e);
            mv.addObject("grades", new ArrayList<>());
            mv.addObject("errorMsg", "查询成绩时出错，可能是数据库中没有对应记录");
        }
        
        return mv;
    }

    /**
     * 教师查询单个学生成绩
     */
    @RequestMapping("/oneGrade")
    @RequiresRoles("2")
    public ModelAndView oneGrade(@RequestParam("schNumber") String schNumber) {
        ModelAndView mv = new ModelAndView("allGrades");
        
        try {
            List<Grade> grades = gradeService.seleOneGrade(schNumber);
            mv.addObject("grades", grades != null ? grades : new ArrayList<>());
        } catch (Exception e) {
            log.error("查询学生成绩时出错: " + e.getMessage(), e);
            mv.addObject("grades", new ArrayList<>());
            mv.addObject("errorMsg", "查询成绩时出错，可能是数据库中没有对应记录");
        }
        
        return mv;
    }

    /**
     * 教师跳转到更新成绩页面
     */
    @RequestMapping("/updateGrade")
    @RequiresRoles("2")
    public ModelAndView updateGrade(@RequestParam("id") Integer id) {
        ModelAndView mv = new ModelAndView();
        
        try {
            Grade theGrade = gradeService.seleTheGrade(id);
            mv.setViewName("updateGrades");
            mv.addObject("grade", theGrade);
        } catch (Exception e) {
            log.error("查询成绩详情时出错: " + e.getMessage(), e);
            mv.setViewName("error");
            mv.addObject("errorMsg", "查询成绩详情时出错，可能是数据库中没有对应记录");
        }
        
        return mv;
    }

    /**
     * 教师更新成绩
     */
    @RequestMapping("/updateGrades")
    @RequiresRoles("2")
    public ModelAndView updateGrades(@Valid GradeForm gradeForm, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        
        if (bindingResult.hasErrors()) {
            mv.setViewName("updateGrades");
            mv.addObject("grade", gradeForm);
            mv.addObject("info", "表单验证失败，请检查输入");
            return mv;
        }
        
        Grade grade = new Grade();
        BeanUtils.copyProperties(gradeForm, grade);
        gradeService.updateGrade(grade);
        mv.setViewName("success");
        return mv;
    }
} 