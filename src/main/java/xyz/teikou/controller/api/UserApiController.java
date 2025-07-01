package xyz.teikou.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.teikou.entity.User;
import xyz.teikou.service.UserService;

import java.util.List;

@Api(tags = "用户管理API")
@RestController
@RequestMapping("/api/user")
public class UserApiController {
    
    @Autowired
    private UserService userService;
    
    @ApiOperation("获取所有用户")
    @GetMapping
    @RequiresRoles("2")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUser());
    }
    
    @ApiOperation("根据ID获取用户")
    @GetMapping("/{id}")
    @RequiresRoles("2")
    public ResponseEntity<User> getUserById(
            @ApiParam(value = "用户ID", required = true) @PathVariable Integer id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }
    
    @ApiOperation("根据学号查询用户")
    @GetMapping("/number/{schNumber}")
    @RequiresRoles("2")
    public ResponseEntity<User> getUserBySchNumber(
            @ApiParam(value = "学号", required = true) @PathVariable String schNumber) {
        return ResponseEntity.ok(userService.findUserBySchNumber(schNumber));
    }
} 