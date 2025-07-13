package xyz.teikou.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理表单绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBindException(BindException e) {
        log.error("数据校验错误：{}", e.getMessage());
        return createErrorModelAndView(e.getBindingResult());
    }

    /**
     * 处理 @RequestBody 注解的参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("数据校验错误：{}", e.getMessage());
        return createErrorModelAndView(e.getBindingResult());
    }

    /**
     * 处理单个参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleConstraintViolationException(ConstraintViolationException e) {
        log.error("数据校验错误：{}", e.getMessage());
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        
        StringBuilder errorMsg = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            errorMsg.append(violation.getMessage()).append("<br>");
        }
        mv.addObject("info", errorMsg.toString());
        return mv;
    }

    /**
     * 创建包含错误信息的ModelAndView
     */
    private ModelAndView createErrorModelAndView(BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        
        List<FieldError> errors = bindingResult.getFieldErrors();
        StringBuilder errorMsg = new StringBuilder();
        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                   .append(": ")
                   .append(error.getDefaultMessage())
                   .append("<br>");
        }
        mv.addObject("info", errorMsg.toString());
        return mv;
    }
} 