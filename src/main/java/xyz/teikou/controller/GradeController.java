package xyz.teikou.controller;

import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import xyz.teikou.entity.Grade;
import xyz.teikou.entity.User;
import xyz.teikou.form.GradeForm;
import xyz.teikou.service.GradeService;
import xyz.teikou.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        // ==================== 关键修改在这里 ====================
        // 调用新方法 findAllStudents()，只获取学生列表
        List<User> allStudents = userService.findAllStudents();
        mv.addObject("schList", allStudents);
        // ========================================================

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

            // ==================== 这里的逻辑也需要同步修改 ====================
            // 如果表单验证失败返回，也要确保 schList 里只有学生
            List<User> allStudents = userService.findAllStudents();
            mv.addObject("schList", allStudents);
            // =============================================================

            mv.addObject("info", "表单验证失败，请检查输入");
            return mv;
        }
        Grade grade = new Grade();
        BeanUtils.copyProperties(gradeForm, grade);
        gradeService.addGrade(grade);
        mv.setViewName("success");
        return mv;
    }

    // --- 后面的方法保持不变 ---

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
    @RequestMapping("/updateTheGrade")
    @RequiresRoles("2")
    public ModelAndView updateTheGrade(@Valid GradeForm gradeForm, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        if (bindingResult.hasErrors()) {
            mv.setViewName("updateGrades");
            mv.addObject("info", "表单验证失败，请检查输入");
            return mv;
        }
        Grade grade = new Grade();
        BeanUtils.copyProperties(gradeForm, grade);
        gradeService.updateGrade(grade);
        mv.setViewName("success");
        return mv;
    }

    /**
     * 学生查看自己的成绩统计分析
     */
    @RequestMapping("/myStatistics")
    @RequiresRoles("1")
    public ModelAndView myStatistics(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("gradeStatistics");
        User user = (User) request.getSession().getAttribute("user");
        String schNumber = user.getSchNumber();
        Map<String, Object> statistics = gradeService.getGradeStatistics(schNumber);
        mv.addObject("statistics", statistics);
        return mv;
    }

    /**
     * 教师查看班级成绩统计
     */
    @RequestMapping("/classStatistics")
    @RequiresRoles("2")
    public ModelAndView classStatistics(@RequestParam(value = "className", required = false) String className) {
        ModelAndView mv = new ModelAndView("classStatistics");
        if (className != null && !className.isEmpty()) {
            Map<String, Object> statistics = gradeService.getClassStatistics(className);
            mv.addObject("statistics", statistics);
            mv.addObject("className", className);
        }
        return mv;
    }


    @RequestMapping("/subjectStatistics")
    @RequiresRoles("2")
    public ModelAndView subjectStatistics(
            @RequestParam(value = "subjectName", required = false) String subjectName,
            @RequestParam(value = "testNo", required = false) String testNo,
            @RequestParam(value = "sortField", required = false, defaultValue = "subject") String sortField,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder) {

        ModelAndView mv = new ModelAndView("subjectStatistics");

        // 调用改造后的 Service 方法
        List<Map<String, Object>> subjectStats = gradeService.getSubjectAverages(subjectName, testNo, sortField, sortOrder);
        // 获取所有学期用于下拉框
        List<String> distinctTerms = gradeService.getDistinctTerms();

        mv.addObject("subjectStats", subjectStats);
        mv.addObject("distinctTerms", distinctTerms);

        // 将所有参数传回前端，用于保持页面状态
        mv.addObject("searchKeyword", subjectName);
        mv.addObject("selectedTerm", testNo);
        mv.addObject("sortField", sortField);
        mv.addObject("sortOrder", sortOrder);

        return mv;
    }

    /**
     * ==================== 核心修改 2: 新增此方法用于导出数据 ====================
     */
    @GetMapping("/export/subjectStatistics")
    @RequiresRoles("2")
    public void exportSubjectStatistics(HttpServletResponse response,
                                        @RequestParam(value = "subjectName", required = false) String subjectName,
                                        @RequestParam(value = "term", required = false) String term) throws IOException {

        response.setContentType("text/csv;charset=UTF-8");
        // 添加 BOM 头，防止 Excel 打开中文乱码
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write('\ufeff');

        String fileName = "subject_statistics_export.csv";
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

        // 查询数据时，不应用排序，通常导出原始顺序即可
        List<Map<String, Object>> subjectStats = gradeService.getSubjectAverages(subjectName, term, null, null);

        try (PrintWriter writer = response.getWriter()) {
            // 写入 CSV 表头
            writer.println("科目,平均分,最高分,最低分,考试人数,及格率(%)");

            // 遍历数据并写入行
            for (Map<String, Object> stat : subjectStats) {
                writer.println(
                        String.join(",",
                                "\"" + stat.get("subject") + "\"", // 用引号包裹科目名，防止其中有逗号
                                String.valueOf(stat.get("average")),
                                String.valueOf(stat.get("max")),
                                String.valueOf(stat.get("min")),
                                String.valueOf(stat.get("count")),
                                String.valueOf(stat.get("passRate"))
                        )
                );
            }
        }
    }

}