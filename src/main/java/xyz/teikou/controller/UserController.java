package xyz.teikou.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import xyz.teikou.entity.User;
import xyz.teikou.form.UserForm;
import xyz.teikou.service.UserService;
import xyz.teikou.shiro.UserRealm;


import javax.servlet.http.HttpServletRequest;


/**
 * Creat by TeiKou
 * 2019/10/9 14:40
 */
@Slf4j
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/reg")
    public String reg() {
        return "/reg";
    }

    @PostMapping("/register")
    public ModelAndView register(UserForm userForm) {
        ModelAndView mv = new ModelAndView();
        if (!userForm.getPassword().equals(userForm.getPassword1())) {
            mv.setViewName("/reg");
            mv.addObject("info", "密码不一致");
            return mv;
        }
        if (userService.findSchNumber(userForm.getSchNumber()) != 0) {
            mv.setViewName("/reg");
            mv.addObject("info", "用户名已存在");
            return mv;
        }
        if (userService.findSchNumber(userForm.getSchNumber()) != 0) {
            mv.setViewName("reg");
            mv.addObject("info", "学号已被占用");
            return mv;
        }
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        String salt = RandomStringUtils.random(5,"qwertyuiopasdfghjklzxcvbnmQWERTYUIOPLKJHGFDSAZXCVBNM");
        user.setSalt(salt);
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), user.getSalt());
        user.setPassword(md5Hash.toString());
        userService.addUser(user);
        mv.setViewName("success");
        return mv;
    }

    @RequestMapping("/doLogin")
    public String doLogin() {
        return "/login";
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Integer roleId = user.getRoleId();
        switch (roleId) {
            case 1:
                return "index1";

            case 2:
                return "index2";

            default:
                return "error";
        }
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpServletRequest httpServletRequest) {
        UserRealm userRealm = new UserRealm();
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(userRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        try {
            User user = userService.findUserByUsername(username);
            String salt=user.getSalt();
            Md5Hash md5Hash = new Md5Hash(password, salt);
            String password1 = md5Hash.toString();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password1);
            subject.login(token);
            httpServletRequest.getSession().setAttribute("user", user);
            user.setLoginCount(user.getLoginCount() + 1);
            //TODo 上次登陆时间未达预期
            userService.userUpdate(user);
            return "/success";
        } catch (RuntimeException e) {
            return "/login";
        }

    }

    @RequestMapping("/logout")
    public String logout() {
        try {
            SecurityUtils.getSubject().logout();
            return ("/success");
        } catch (Exception e) {
            return null;
        }
    }
}
