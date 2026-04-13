package com.qqspeed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqspeed.common.exception.BusinessException;
import com.qqspeed.data.entity.SysAdmin;
import com.qqspeed.data.dto.SysAdminLoginDTO;
import com.qqspeed.data.dto.SysAdminDTO;
import com.qqspeed.mapper.SysAdminMapper;
import com.qqspeed.service.SysAdminService;
import com.qqspeed.utils.RedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

/**
 * 系统管理员服务实现类
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String login(SysAdminLoginDTO loginDTO) {
        // 1. 校验验证码
        String captchaKey = "captcha:" + loginDTO.getUsername();
        String captchaCode = (String) redisUtils.get(captchaKey);
        if (captchaCode == null || !captchaCode.equalsIgnoreCase(loginDTO.getCaptcha())) {
            throw new BusinessException("验证码错误或已过期");
        }
        // 删除验证码
        redisUtils.delete(captchaKey);

        // 2. 查询管理员
        SysAdmin admin = getByUsername(loginDTO.getUsername());
        if (admin == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 3. 校验状态
        if (admin.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        // 4. 校验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), admin.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 5. 生成token（简单实现，实际项目中应该使用JWT等工具）
        String token = UUID.randomUUID().toString().replace("-", "");

        // 6. 更新最后登录信息
        updateLastLoginInfo(admin.getId(), LocalDateTime.now(), "127.0.0.1");

        // 7. 缓存登录信息
        String loginKey = "admin:login:" + admin.getId();
        SysAdminDTO adminDTO = new SysAdminDTO();
        BeanUtils.copyProperties(admin, adminDTO);
        redisUtils.set(loginKey, adminDTO, 2, TimeUnit.HOURS);

        return token;
    }

    @Override
    public SysAdmin getByUsername(String username) {
        // 先从缓存获取
        String cacheKey = "admin:username:" + username;
        SysAdmin admin = (SysAdmin) redisUtils.get(cacheKey);
        if (admin != null) {
            return admin;
        }

        // 缓存未命中，从数据库查询
        admin = baseMapper.findByUsername(username);
        if (admin != null) {
            // 放入缓存，1小时过期
            redisUtils.set(cacheKey, admin, 1, TimeUnit.HOURS);
        }

        return admin;
    }

    @Override
    public void updateLastLoginInfo(Long adminId, LocalDateTime lastLoginTime, String lastLoginIp) {
        baseMapper.updateLastLoginInfo(adminId, lastLoginTime, lastLoginIp);

        // 清除缓存
        redisUtils.delete("admin:username:" + adminId);
    }

    @Override
    public boolean updatePassword(Long adminId, String oldPassword, String newPassword) {
        // 1. 查询管理员
        SysAdmin admin = getById(adminId);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        // 2. 校验原密码
        if (!passwordEncoder.matches(oldPassword, admin.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 3. 加密新密码
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 4. 更新密码
        admin.setPassword(encodedPassword);
        boolean result = updateById(admin);

        // 5. 清除缓存
        if (result) {
            redisUtils.delete("admin:username:" + adminId);
            redisUtils.delete("admin:login:" + adminId);
        }

        return result;
    }

    @Override
    public boolean resetPassword(Long adminId, String newPassword) {
        // 1. 查询管理员
        SysAdmin admin = getById(adminId);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        // 2. 加密新密码
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 3. 更新密码
        admin.setPassword(encodedPassword);
        boolean result = updateById(admin);

        // 4. 清除缓存
        if (result) {
            redisUtils.delete("admin:username:" + adminId);
            redisUtils.delete("admin:login:" + adminId);
        }

        return result;
    }

    @Override
    public boolean updateStatus(Long adminId, Integer status) {
        // 超级管理员不能被禁用
        SysAdmin admin = getById(adminId);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }

        if ("超级管理员".equals(admin.getRole()) && status == 0) {
            throw new BusinessException("超级管理员不能被禁用");
        }

        // 更新状态
        admin.setStatus(status);
        boolean result = updateById(admin);

        // 清除缓存
        if (result) {
            redisUtils.delete("admin:username:" + adminId);
            redisUtils.delete("admin:login:" + adminId);
        }

        return result;
    }
}
