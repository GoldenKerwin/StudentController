package xyz.teikou.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import xyz.teikou.entity.StuInfo;
import xyz.teikou.service.GradeService;
import xyz.teikou.service.StuInfoService;

import java.util.List;

/**
 * Creat by TeiKou
 * 2019/10/11 16:08
 */
@RequestMapping("/teacher")
@Controller
@RequiresRoles("2")
public class TStuInfoController {
    @Autowired
    StuInfoService stuInfoService;
    @RequestMapping("allInfo")
    public ModelAndView allInfo(){
        ModelAndView mv=new ModelAndView("allInfo");
        List<StuInfo> stuInfos = stuInfoService.allInfo();
        mv.addObject("allInfos",stuInfos);
        return mv;
    }

    @RequestMapping("/theInfo")
    public ModelAndView theInfo(String schNumber){
        ModelAndView mv=new ModelAndView();
            StuInfo stuInfo = stuInfoService.theInfo(schNumber);
            mv.setViewName("allInfo");
            mv.addObject("allInfos",stuInfo);
            return mv;
    }
}
