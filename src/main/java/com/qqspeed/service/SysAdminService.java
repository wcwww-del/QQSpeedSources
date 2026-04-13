package com.qqspeed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qqspeed.data.entity.SysAdmin;
import com.qqspeed.data.dto.SysAdminLoginDTO;
import com.qqspeed.data.dto.SysAdminDTO;

import java.time.LocalDateTime;

/**
 * 系统管理员服务接口
 */
public interface SysAdminService extends IService<SysAdmin> {

    /**
     * 管理员登录
     * @param loginDTO 登录参数
     * @return token信息
     */
    String login(SysAdminLoginDTO loginDTO);

    /**
     * 根据用户名查询管理员
     * @param username 用户名
     * @return 管理员信息
     */
    SysAdmin getByUsername(String username);

    /**
     * 更新最后登录信息
     * @param adminId 管理员ID
     * @param lastLoginTime 最后登录时间
     * @param lastLoginIp 最后登录IP
     */
    void updateLastLoginInfo(Long adminId, LocalDateTime lastLoginTime, String lastLoginIp);

    /**
     * 修改密码
     * @param adminId 管理员ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 操作结果
     */
    boolean updatePassword(Long adminId, String oldPassword, String newPassword);

    /**
     * 重置密码
     * @param adminId 管理员ID
     * @param newPassword 新密码
     * @return 操作结果
     */
    boolean resetPassword(Long adminId, String newPassword);

    /**
     * 修改管理员状态
     * @param adminId 管理员ID
     * @param status 状态
     * @return 操作结果
     */
    boolean updateStatus(Long adminId, Integer status);
}
