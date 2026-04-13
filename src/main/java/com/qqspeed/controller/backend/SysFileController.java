package com.qqspeed.controller.backend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.entity.SysFile;
import com.qqspeed.data.vo.SysFileVO;
import com.qqspeed.service.SysFileService;
import com.qqspeed.utils.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 系统文件管理接口
 */
@RestController
@RequestMapping("/backend/file")
@Tag(name = "文件管理", description = "文件的上传、下载、管理接口")
public class SysFileController {

    @Autowired
    private SysFileService sysFileService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件到服务器")
    public Result<SysFile> upload(
            @Parameter(description = "文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "文件分类") @RequestParam(defaultValue = "其他") String category,
            @Parameter(description = "描述") @RequestParam(required = false) String description
    ) {
        try {
            // 这里应该是实际的文件上传逻辑
            // 临时模拟一个文件对象
            SysFile sysFile = new SysFile();
            sysFile.setOriginalName(file.getOriginalFilename());
            sysFile.setFileSize(file.getSize());
            sysFile.setFileType(FileUtils.getFileType(file.getOriginalFilename()));
            sysFile.setCategory(category);
            sysFile.setDescription(description);
            sysFile.setFileUrl("/api/files/" + file.getOriginalFilename());
            sysFile.setFilePath("/files/" + file.getOriginalFilename());

            // 实际项目中应该调用文件存储服务
            // sysFile = fileStorageService.upload(file);

            // 保存到数据库
            sysFile = sysFileService.uploadFile(sysFile, 1L, "管理员");

            return Result.success(sysFile);
        } catch (Exception e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询文件
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询文件", description = "支持分类、关键词、上传者筛选")
    public Result<IPage<SysFileVO>> pageQuery(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "文件分类") @RequestParam(required = false) String category,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "上传者ID") @RequestParam(required = false) Long uploaderId
    ) {
        Page<SysFile> page = new Page<>(pageNum, pageSize);
        IPage<SysFileVO> result = sysFileService.pageQuery(page, category, keyword, uploaderId);
        return Result.success(result);
    }

    /**
     * 根据分类获取文件列表
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "按分类获取文件", description = "获取指定分类下的所有文件")
    public Result<List<SysFileVO>> getFilesByCategory(
            @Parameter(description = "文件分类") @PathVariable String category
    ) {
        List<SysFileVO> files = sysFileService.getFilesByCategory(category);
        return Result.success(files);
    }

    /**
     * 获取热门文件
     */
    @GetMapping("/hot")
    @Operation(summary = "获取热门文件", description = "获取下载次数最多的热门文件")
    public Result<List<SysFileVO>> getHotFiles(
            @Parameter(description = "文件分类") @RequestParam(defaultValue = "图片") String category,
            @Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit
    ) {
        List<SysFileVO> files = sysFileService.getHotFiles(category, limit);
        return Result.success(files);
    }

    /**
     * 搜索文件
     */
    @GetMapping("/search")
    @Operation(summary = "搜索文件", description = "根据文件名称搜索文件")
    public Result<List<SysFileVO>> searchFiles(
            @Parameter(description = "搜索关键词") @RequestParam String keyword
    ) {
        List<SysFileVO> files = sysFileService.searchFiles(keyword);
        return Result.success(files);
    }

    /**
     * 删除文件
     */
    @PostMapping("/delete/{id}")
    @Operation(summary = "删除文件", description = "删除指定文件")
    public Result<Boolean> deleteFile(
            @Parameter(description = "文件ID") @PathVariable Long id
    ) {
        try {
            boolean result = sysFileService.deleteFile(id);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除文件
     */
    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除文件", description = "批量删除多个文件")
    public Result<Boolean> batchDeleteFiles(
            @Parameter(description = "文件ID列表") @RequestBody List<Long> ids
    ) {
        try {
            boolean result = sysFileService.batchDeleteFiles(ids);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{id}")
    @Operation(summary = "下载文件", description = "下载指定文件")
    public Result<String> downloadFile(
            @Parameter(description = "文件ID") @PathVariable Long id
    ) {
        try {
            // 增加下载次数
            SysFile file = sysFileService.increaseDownloadCountAndGet(id);
            if (file == null) {
                return Result.error("文件不存在");
            }

            // 实际项目中应该返回文件流或下载链接
            return Result.success(file.getFileUrl());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取文件统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取文件统计", description = "获取文件系统的统计信息")
    public Result<Map<String, Object>> getFileStatistics() {
        Map<String, Object> statistics = sysFileService.getFileStatistics();
        return Result.success(statistics);
    }
}