package xyz.teikou.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import xyz.teikou.entity.Grade;
import xyz.teikou.entity.User;
import xyz.teikou.service.GradeService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Creat by TeiKou
 * 2019/10/10 16:44
 * 学生成绩管理
 */
@Controller
@RequestMapping("/student")
@RequiresRoles("1")
public class SGradeController {
    @Autowired
    GradeService gradeService;

    @RequestMapping("/myGrades")
    public ModelAndView seleMyGrade(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("myGrage");
        User user = (User) request.getSession().getAttribute("user");
        String schNumber = user.getSchNumber();
        List<Grade> grades = gradeService.selectMyGrade(schNumber);
        mv.addObject("grades", grades);
        return mv;
    }
}
