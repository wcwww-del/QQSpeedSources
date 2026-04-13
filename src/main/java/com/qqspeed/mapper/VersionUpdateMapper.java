package com.qqspeed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.data.entity.VersionUpdate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 版本更新Mapper（MyBatis-Plus BaseMapper已封装CURD）
 */
@Mapper
public interface VersionUpdateMapper extends BaseMapper<VersionUpdate> {

    /**
     * 根据版本号查询版本信息
     */
    @Select("SELECT * FROM version_update WHERE version = #{version} AND status = 1")
    VersionUpdate getByVersion(String version);

    /**
     * 获取最新版本
     */
    @Select("SELECT * FROM version_update WHERE status = 1 AND platform = #{platform} ORDER BY update_time DESC LIMIT 1")
    VersionUpdate getLatestVersion(String platform);

    /**
     * 获取版本历史列表
     */
    @Select("SELECT * FROM version_update WHERE status = 1 AND platform = #{platform} ORDER BY update_time DESC")
    List<VersionUpdate> getVersionHistory(String platform);

    /**
     * 获取所有已发布的版本
     */
    @Select("SELECT * FROM version_update WHERE status = 1 ORDER BY update_time DESC")
    List<VersionUpdate> getAllPublishedVersions();

    /**
     * 更新发布状态
     */
    @Update("UPDATE version_update SET status = #{status}, update_time_field = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 统计各平台版本数量
     */
    @Select("SELECT platform, COUNT(*) as count FROM version_update WHERE status = 1 GROUP BY platform")
    List<java.util.Map<String, Object>> countByPlatform();
}
