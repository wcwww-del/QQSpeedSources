package com.qqspeed.controller.backend;

import com.qqspeed.common.result.Result;
import com.qqspeed.data.entity.SysAdmin;
import com.qqspeed.data.dto.SysAdminLoginDTO;
import com.qqspeed.data.dto.SysAdminDTO;
import com.qqspeed.service.SysAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统管理员管理接口
 */
@RestController
@RequestMapping("/backend/admin")
@Tag(name = "管理员管理", description = "管理员相关的认证和管理接口")
public class SysAdminController {

    @Autowired
    private SysAdminService sysAdminService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    @Operation(summary = "管理员登录", description = "管理员用户登录系统")
    public Result<Map<String, Object>> login(
            @Parameter(description = "登录参数") @RequestBody SysAdminLoginDTO loginDTO,
            HttpServletRequest request
    ) {
        try {
            String token = sysAdminService.login(loginDTO);

            // 获取登录后的管理员信息
            SysAdmin admin = sysAdminService.getByUsername(loginDTO.getUsername());
            SysAdminDTO adminDTO = new SysAdminDTO();
            BeanUtils.copyProperties(admin, adminDTO);

            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("adminInfo", adminDTO);

            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前登录管理员信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取管理员信息", description = "获取当前登录的管理员信息")
    public Result<SysAdminDTO> getAdminInfo() {
        // 这里应该从token中获取管理员ID，示例中使用固定ID
        Long adminId = 1L;
        SysAdmin admin = sysAdminService.getById(adminId);
        if (admin == null) {
            return Result.error("管理员不存在");
        }

        SysAdminDTO adminDTO = new SysAdminDTO();
        BeanUtils.copyProperties(admin, adminDTO);
        return Result.success(adminDTO);
    }

    /**
     * 修改密码
     */
    @PostMapping("/password")
    @Operation(summary = "修改密码", description = "修改当前登录管理员密码")
    public Result<Boolean> updatePassword(
            @Parameter(description = "原密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword
    ) {
        try {
            Long adminId = 1L; // 应该从token中获取
            boolean result = sysAdminService.updatePassword(adminId, oldPassword, newPassword);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset-password/{adminId}")
    @Operation(summary = "重置密码", description = "重置指定管理员的密码（超级管理员专用）")
    public Result<Boolean> resetPassword(
            @Parameter(description = "管理员ID") @PathVariable Long adminId,
            @Parameter(description = "新密码") @RequestParam String newPassword
    ) {
        try {
            boolean result = sysAdminService.resetPassword(adminId, newPassword);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改管理员状态
     */
    @PostMapping("/status/{adminId}")
    @Operation(summary = "修改管理员状态", description = "修改指定管理员的状态（超级管理员专用）")
    public Result<Boolean> updateStatus(
            @Parameter(description = "管理员ID") @PathVariable Long adminId,
            @Parameter(description = "状态：0-禁用，1-启用") @RequestParam Integer status
    ) {
        try {
            boolean result = sysAdminService.updateStatus(adminId, status);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}