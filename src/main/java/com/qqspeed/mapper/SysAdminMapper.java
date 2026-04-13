package com.qqspeed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qqspeed.data.entity.SysAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * 系统管理员Mapper（MyBatis-Plus BaseMapper已封装CURD）
 */
@Mapper
public interface SysAdminMapper extends BaseMapper<SysAdmin> {

    /**
     * 根据用户名查询管理员
     */
    @Select("SELECT * FROM sys_admin WHERE username = #{username} AND status = 1")
    SysAdmin findByUsername(String username);

    /**
     * 更新最后登录信息
     */
    @Update("UPDATE sys_admin SET last_login_time = #{lastLoginTime}, last_login_ip = #{lastLoginIp}, update_time = NOW() WHERE id = #{id}")
    int updateLastLoginInfo(@Param("id") Long id, @Param("lastLoginTime") LocalDateTime lastLoginTime, @Param("lastLoginIp") String lastLoginIp);
}
