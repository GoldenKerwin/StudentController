package xyz.teikou.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
@Slf4j
@Controller
public class CustomErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private static final String ERROR_PATH = "/error";

    /**
     * 处理错误页面请求
     */
    @RequestMapping(ERROR_PATH)
    public ModelAndView handleError(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        
        // 获取错误状态码
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            log.error("发生错误: 状态码 = {}", statusCode);
            
            // 根据状态码设置不同的错误页面
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                mv.setViewName("error/404");
                mv.addObject("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                mv.setViewName("error/403");
                mv.addObject("message", "访问被拒绝");
                mv.addObject("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
            } else {
                mv.setViewName("error/500");
                mv.addObject("message", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
                mv.addObject("error", request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
                mv.addObject("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
            }
        } else {
            // 如果状态码为空，返回通用错误页面
            mv.setViewName("error/500");
            mv.addObject("message", "发生未知错误");
        }
        
        return mv;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
} 