package xyz.teikou.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import xyz.teikou.entity.StuInfo;
import xyz.teikou.entity.User;
import xyz.teikou.service.StuInfoService;

import javax.servlet.http.HttpServletRequest;

/**
 * Creat by TeiKou
 * 2019/10/11 14:20
 */
@Controller
@RequiresRoles("1")
@RequestMapping("/student")
public class SStuInfoController {
    @Autowired
    StuInfoService stuInfoService;

    @RequestMapping("/addInfo")
    public String addStuInfo(StuInfo stuInfo) {
        stuInfoService.addInfo(stuInfo);
        return ("/success");
    }

//    @RequestMapping("/addStuInfo")
//    public String addStuInfo() {
//        return "addStuInfo";
//    }

    @RequestMapping("/myInfo")
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
        } catch (NullPointerException e){
            mv.setViewName("addStuInfo");
            return mv;
        }
    }

    @RequestMapping("/updateInfo")
    public String updateInfo(StuInfo stuInfo){
        stuInfoService.updateInfo(stuInfo);
        return "success";
    }
}
