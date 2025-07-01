package xyz.teikou.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.teikou.entity.StuInfo;
import xyz.teikou.service.StuInfoService;

import java.util.List;

@Api(tags = "学生信息API")
@RestController
@RequestMapping("/api/stuinfo")
public class StuInfoApiController {
    
    @Autowired
    private StuInfoService stuInfoService;
    
    @ApiOperation("获取所有学生信息")
    @GetMapping
    @RequiresRoles("2")
    public ResponseEntity<List<StuInfo>> getAllStuInfo() {
        return ResponseEntity.ok(stuInfoService.allInfo());
    }
    
    @ApiOperation("获取单个学生信息")
    @GetMapping("/{schNumber}")
    @RequiresRoles({"1", "2"})
    public ResponseEntity<StuInfo> getStuInfo(
            @ApiParam(value = "学号", required = true) @PathVariable String schNumber) {
        return ResponseEntity.ok(stuInfoService.theInfo(schNumber));
    }
    
    @ApiOperation("添加学生信息")
    @PostMapping
    @RequiresRoles("2")
    public ResponseEntity<Void> addStuInfo(@RequestBody StuInfo stuInfo) {
        stuInfoService.addInfo(stuInfo);
        return ResponseEntity.ok().build();
    }
    
    @ApiOperation("更新学生信息")
    @PutMapping("/{schNumber}")
    @RequiresRoles({"1", "2"})
    public ResponseEntity<Void> updateStuInfo(
            @ApiParam(value = "学号", required = true) @PathVariable String schNumber,
            @RequestBody StuInfo stuInfo) {
        stuInfo.setSchNumber(schNumber);
        stuInfoService.updateInfo(stuInfo);
        return ResponseEntity.ok().build();
    }
} 