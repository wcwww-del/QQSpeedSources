package com.qqspeed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qqspeed.data.entity.SysFile;
import com.qqspeed.data.dto.SysFileDTO;
import com.qqspeed.data.vo.SysFileVO;

import java.util.List;
import java.util.Map;

/**
 * 系统文件服务接口
 */
public interface SysFileService extends IService<SysFile> {

    /**
     * 上传文件
     * @param file 文件信息
     * @param uploaderId 上传者ID
     * @param uploaderName 上传者名称
     * @return 文件信息
     */
    SysFile uploadFile(SysFile file, Long uploaderId, String uploaderName);

    /**
     * 分页查询文件
     * @param page 分页参数
     * @param category 文件分类
     * @param keyword 搜索关键词
     * @param uploaderId 上传者ID
     * @return 分页结果
     */
    IPage<SysFileVO> pageQuery(Page<SysFile> page, String category, String keyword, Long uploaderId);

    /**
     * 根据URL获取文件信息
     * @param fileUrl 文件URL
     * @return 文件信息
     */
    SysFile getFileByUrl(String fileUrl);

    /**
     * 根据分类获取文件列表
     * @param category 文件分类
     * @return 文件列表
     */
    List<SysFileVO> getFilesByCategory(String category);

    /**
     * 增加下载次数并获取文件
     * @param id 文件ID
     * @return 文件信息
     */
    SysFile increaseDownloadCountAndGet(Long id);

    /**
     * 获取热门文件
     * @param category 文件分类
     * @param limit 限制数量
     * @return 热门文件列表
     */
    List<SysFileVO> getHotFiles(String category, Integer limit);

    /**
     * 根据上传者获取文件
     * @param uploaderId 上传者ID
     * @return 文件列表
     */
    List<SysFileVO> getFilesByUploader(Long uploaderId);

    /**
     * 搜索文件
     * @param keyword 搜索关键词
     * @return 文件列表
     */
    List<SysFileVO> searchFiles(String keyword);

    /**
     * 删除文件
     * @param id 文件ID
     * @return 操作结果
     */
    boolean deleteFile(Long id);

    /**
     * 批量删除文件
     * @param ids 文件ID列表
     * @return 操作结果
     */
    boolean batchDeleteFiles(List<Long> ids);

    /**
     * 获取文件统计信息
     * @return 统计信息
     */
    Map<String, Object> getFileStatistics();
}
