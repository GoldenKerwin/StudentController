package xyz.teikou.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.teikou.entity.Grade;
import xyz.teikou.service.GradeService;

import java.util.List;
import java.util.Map;

@Api(tags = "成绩管理API")
@RestController
@RequestMapping("/api/grade")
public class GradeApiController {
    
    @Autowired
    private GradeService gradeService;
    
    @ApiOperation("获取所有成绩")
    @GetMapping
    @RequiresRoles("2")
    public ResponseEntity<List<Grade>> getAllGrades() {
        return ResponseEntity.ok(gradeService.selectAllGrade());
    }
    
    @ApiOperation("获取单个学生成绩")
    @GetMapping("/student/{schNumber}")
    @RequiresRoles({"1", "2"})
    public ResponseEntity<List<Grade>> getStudentGrades(
            @ApiParam(value = "学号", required = true) @PathVariable String schNumber) {
        return ResponseEntity.ok(gradeService.selectMyGrade(schNumber));
    }
    
    @ApiOperation("添加学生成绩")
    @PostMapping
    @RequiresRoles("2")
    public ResponseEntity<Void> addGrade(@RequestBody Grade grade) {
        gradeService.addGrade(grade);
        return ResponseEntity.ok().build();
    }
    
    @ApiOperation("更新学生成绩")
    @PutMapping("/{id}")
    @RequiresRoles("2")
    public ResponseEntity<Void> updateGrade(
            @ApiParam(value = "成绩ID", required = true) @PathVariable Integer id,
            @RequestBody Grade grade) {
        grade.setId(id);
        gradeService.updateGrade(grade);
        return ResponseEntity.ok().build();
    }
    
    @ApiOperation("获取单个成绩详情")
    @GetMapping("/{id}")
    @RequiresRoles("2")
    public ResponseEntity<Grade> getGradeById(
            @ApiParam(value = "成绩ID", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(gradeService.seleTheGrade(id));
    }
    
    @ApiOperation("获取成绩统计")
    @GetMapping("/statistics/{schNumber}")
    @RequiresRoles({"1", "2"})
    public ResponseEntity<Map<String, Object>> getGradeStatistics(
            @ApiParam(value = "学号", required = true) @PathVariable String schNumber) {
        return ResponseEntity.ok(gradeService.getGradeStatistics(schNumber));
    }
} 