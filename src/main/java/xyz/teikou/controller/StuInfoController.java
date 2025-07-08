package xyz.teikou.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import xyz.teikou.entity.StuInfo;
import xyz.teikou.entity.User;
import xyz.teikou.form.StuForm;
import xyz.teikou.service.StuInfoService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/stuInfo")
public class StuInfoController {
    
    @Autowired
    private StuInfoService stuInfoService;

    /**
     * 学生添加自己的信息
     */
    @RequestMapping("/addInfo")
    @RequiresRoles("1")
    public ModelAndView addStuInfo(@Valid StuForm stuForm, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMsg = new StringBuilder();
            for (FieldError error : errors) {
                errorMsg.append(error.getDefaultMessage()).append("<br>");
            }
            mv.setViewName("addStuInfo");
            mv.addObject("info", errorMsg.toString());
            return mv;
        }
        
        StuInfo stuInfo = new StuInfo();
        BeanUtils.copyProperties(stuForm, stuInfo);
        stuInfoService.addInfo(stuInfo);
        mv.setViewName("success");
        return mv;
    }

    /**
     * 学生查看自己的信息
     */
    @RequestMapping("/myInfo")
    @RequiresRoles("1")
    public ModelAndView myInfo(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        Integer id = user.getId();
        StuInfo stuInfo = stuInfoService.myInfo(id);
        try {
            String schNumber = stuInfo.getSchNumber();
            mv.setViewName("myInfo");
            mv.addObject("myInfos", stuInfo);
            return mv;
        } catch (NullPointerException e) {
            mv.setViewName("addStuInfo");
            return mv;
        }
    }

    /**
     * 学生更新自己的信息
     */
    @RequestMapping("/updateInfo")
    @RequiresRoles("1")
    public ModelAndView updateInfo(@Valid StuForm stuForm, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMsg = new StringBuilder();
            for (FieldError error : errors) {
                errorMsg.append(error.getDefaultMessage()).append("<br>");
            }
            mv.setViewName("myInfo");
            mv.addObject("info", errorMsg.toString());
            return mv;
        }
        
        StuInfo stuInfo = new StuInfo();
        BeanUtils.copyProperties(stuForm, stuInfo);
        stuInfoService.updateInfo(stuInfo);
        mv.setViewName("success");
        return mv;
    }

    /**
     * 教师查询所有学生信息
     */
    @RequestMapping("/allInfo")
    @RequiresRoles("2")
    public ModelAndView allInfo() {
        ModelAndView mv = new ModelAndView("allInfo");
        List<StuInfo> stuInfos = stuInfoService.allInfo();
        mv.addObject("allInfos", stuInfos);
        return mv;
    }

    /**
     * 教师查询特定学生信息
     */
    @RequestMapping("/theInfo")
    @RequiresRoles("2")
    public ModelAndView theInfo(String schNumber) {
        ModelAndView mv = new ModelAndView();
        StuInfo stuInfo = stuInfoService.theInfo(schNumber);
        mv.setViewName("allInfo");
        mv.addObject("allInfos", stuInfo);
        return mv;
    }
} 