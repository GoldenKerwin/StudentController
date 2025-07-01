package xyz.teikou.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.teikou.entity.User;
import xyz.teikou.form.UserForm;
import xyz.teikou.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/reg")
    public String reg() { return "/reg"; }

    @RequestMapping("/doLogin")
    public String doLogin() { return "/login"; }

    @RequestMapping("/index")
    public ModelAndView index() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            User user = (User) subject.getPrincipal();
            ModelAndView mv = new ModelAndView();
            mv.addObject("user", user);
            switch (user.getRoleId()) {
                case 1: mv.setViewName("index1"); break;
                case 2: mv.setViewName("index2"); break;
                default: mv.setViewName("error"); break;
            }
            return mv;
        }
        return new ModelAndView("redirect:/doLogin");
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid UserForm userForm, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMsg = new StringBuilder();
            for (FieldError error : errors) { errorMsg.append(error.getDefaultMessage()).append("<br>"); }
            mv.setViewName("/reg");
            mv.addObject("info", errorMsg.toString());
            return mv;
        }
        if (!userForm.getPassword().equals(userForm.getPassword1())) {
            mv.setViewName("/reg");
            mv.addObject("info", "密码不一致");
            return mv;
        }
        if (userService.findUserByUsername(userForm.getUsername()) != null || userService.findSchNumber(userForm.getSchNumber()) != 0) {
            mv.setViewName("/reg");
            mv.addObject("info", "用户名或学号已被占用");
            return mv;
        }
        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        String salt = RandomStringUtils.random(5, "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPLKJHGFDSAZXCVBNM");
        user.setSalt(salt);
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), user.getSalt(), 1);
        user.setPassword(md5Hash.toString());
        userService.addUser(user);
        mv.setViewName("success");
        return mv;
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpServletRequest httpServletRequest) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            User user = (User) subject.getPrincipal();
            httpServletRequest.getSession().setAttribute("user", user);
            user.setLoginCount(user.getLoginCount() + 1);
            userService.userUpdate(user);
            return "redirect:/index";
        } catch (UnknownAccountException e) {
            model.addAttribute("redirectToRegister", true);
            model.addAttribute("username", username);
            return "/login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("error", "密码输入不正确，请重新输入！");
            model.addAttribute("username", username);
            return "/login";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "登录失败，请联系管理员。");
            return "/login";
        }
    }

    @RequestMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/doLogin";
    }

    @GetMapping("/check/username")
    @ResponseBody
    public Map<String, Object> checkUsername(@RequestParam("username") String username) {
        Map<String, Object> response = new HashMap<>();
        if (userService.findUserByUsername(username) != null) {
            response.put("valid", false);
            response.put("message", "该用户名已被占用");
        } else { response.put("valid", true); }
        return response;
    }

    @GetMapping("/check/schNumber")
    @ResponseBody
    public Map<String, Object> checkSchNumber(@RequestParam("schNumber") String schNumber) {
        Map<String, Object> response = new HashMap<>();
        if (userService.findSchNumber(schNumber) != 0) {
            response.put("valid", false);
            response.put("message", "该学号/工号已被占用");
        } else { response.put("valid", true); }
        return response;
    }

    @GetMapping("/check/mail")
    @ResponseBody
    public Map<String, Object> checkMail(@RequestParam("mail") String mail) {
        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        return response;
    }
}