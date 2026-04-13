package com.qqspeed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.data.entity.SysFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 系统文件Mapper（MyBatis-Plus BaseMapper已封装CURD）
 */
@Mapper
public interface SysFileMapper extends BaseMapper<SysFile> {

    /**
     * 根据文件URL查询文件
     */
    @Select("SELECT * FROM sys_file WHERE file_url = #{fileUrl} AND status = 1")
    SysFile getFileByUrl(String fileUrl);

    /**
     * 根据分类查询文件列表
     */
    @Select("SELECT * FROM sys_file WHERE category = #{category} AND status = 1 ORDER BY create_time DESC")
    List<SysFile> getFilesByCategory(String category);

    /**
     * 增加下载次数
     */
    @Update("UPDATE sys_file SET download_count = download_count + 1, update_time = NOW() WHERE id = #{id}")
    int increaseDownloadCount(@Param("id") Long id);

    /**
     * 获取热门文件（按下载次数排序）
     */
    @Select("SELECT * FROM sys_file WHERE status = 1 AND category = #{category} ORDER BY download_count DESC LIMIT #{limit}")
    List<SysFile> getHotFiles(@Param("category") String category, @Param("limit") Integer limit);

    /**
     * 根据上传者查询文件
     */
    @Select("SELECT * FROM sys_file WHERE uploader_id = #{uploaderId} AND status = 1 ORDER BY create_time DESC")
    List<SysFile> getFilesByUploader(@Param("uploaderId") Long uploaderId);

    /**
     * 搜索文件
     */
    @Select("SELECT * FROM sys_file WHERE original_name LIKE CONCAT('%', #{keyword}, '%') AND status = 1 ORDER BY create_time DESC")
    List<SysFile> searchFiles(@Param("keyword") String keyword);
}
