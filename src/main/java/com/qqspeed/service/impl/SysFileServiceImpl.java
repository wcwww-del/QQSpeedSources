package com.qqspeed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqspeed.common.exception.BusinessException;
import com.qqspeed.data.entity.SysFile;
import com.qqspeed.data.dto.SysFileDTO;
import com.qqspeed.data.vo.SysFileVO;
import com.qqspeed.mapper.SysFileMapper;
import com.qqspeed.service.SysFileService;
import com.qqspeed.utils.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统文件服务实现类
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

    @Override
    public SysFile uploadFile(SysFile file, Long uploaderId, String uploaderName) {
        // 设置上传信息
        file.setUploaderId(uploaderId);
        file.setUploaderName(uploaderName);
        file.setStatus(1);
        file.setDownloadCount(0);

        // 格式化文件大小
        if (file.getFileSize() != null) {
            file.setFileSizeDisplay(FileUtils.formatFileSize(file.getFileSize()));
        }

        // 保存到数据库
        save(file);
        return file;
    }

    @Override
    public IPage<SysFileVO> pageQuery(Page<SysFile> page, String category, String keyword, Long uploaderId) {
        // 构造查询条件
        LambdaQueryWrapper<SysFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysFile::getStatus, 1); // 只查询正常的

        if (category != null && !category.isEmpty()) {
            queryWrapper.eq(SysFile::getCategory, category);
        }

        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like(SysFile::getOriginalName, keyword);
        }

        if (uploaderId != null) {
            queryWrapper.eq(SysFile::getUploaderId, uploaderId);
        }

        queryWrapper.orderByDesc(SysFile::getCreateTime);

        // 分页查询
        IPage<SysFile> filePage = baseMapper.selectPage(page, queryWrapper);

        // 转换为VO
        return filePage.convert(file -> {
            SysFileVO vo = new SysFileVO();
            BeanUtils.copyProperties(file, vo);
            return vo;
        });
    }

    @Override
    public SysFile getFileByUrl(String fileUrl) {
        return baseMapper.getFileByUrl(fileUrl);
    }

    @Override
    public List<SysFileVO> getFilesByCategory(String category) {
        List<SysFile> files = baseMapper.getFilesByCategory(category);
        return files.stream()
                .map(file -> {
                    SysFileVO vo = new SysFileVO();
                    BeanUtils.copyProperties(file, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public SysFile increaseDownloadCountAndGet(Long id) {
        SysFile file = getById(id);
        if (file == null) {
            throw new BusinessException("文件不存在");
        }

        baseMapper.increaseDownloadCount(id);
        // 重新查询以获取最新数据
        return getById(id);
    }

    @Override
    public List<SysFileVO> getHotFiles(String category, Integer limit) {
        List<SysFile> files = baseMapper.getHotFiles(category, limit);
        return files.stream()
                .map(file -> {
                    SysFileVO vo = new SysFileVO();
                    BeanUtils.copyProperties(file, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SysFileVO> getFilesByUploader(Long uploaderId) {
        List<SysFile> files = baseMapper.getFilesByUploader(uploaderId);
        return files.stream()
                .map(file -> {
                    SysFileVO vo = new SysFileVO();
                    BeanUtils.copyProperties(file, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SysFileVO> searchFiles(String keyword) {
        List<SysFile> files = baseMapper.searchFiles(keyword);
        return files.stream()
                .map(file -> {
                    SysFileVO vo = new SysFileVO();
                    BeanUtils.copyProperties(file, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteFile(Long id) {
        // 逻辑删除，将状态设为0
        SysFile file = new SysFile();
        file.setId(id);
        file.setStatus(0);
        return updateById(file);
    }

    @Override
    public boolean batchDeleteFiles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaQueryWrapper<SysFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysFile::getId, ids);

        SysFile updateFile = new SysFile();
        updateFile.setStatus(0);

        return update(updateFile, queryWrapper);
    }

    @Override
    public Map<String, Object> getFileStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总文件数
        Long totalFiles = count(new LambdaQueryWrapper<SysFile>().eq(SysFile::getStatus, 1));
        statistics.put("totalFiles", totalFiles);

        // 分类统计
        String[] categories = {"图片", "文档", "视频", "其他"};
        Map<String, Long> categoryStats = new HashMap<>();
        for (String category : categories) {
            Long count = count(new LambdaQueryWrapper<SysFile>()
                    .eq(SysFile::getCategory, category)
                    .eq(SysFile::getStatus, 1));
            categoryStats.put(category, count);
        }
        statistics.put("categoryStats", categoryStats);

        // 总下载次数
        Long totalDownloads = getBaseMapper().selectList(
                new LambdaQueryWrapper<SysFile>().eq(SysFile::getStatus, 1))
                .stream()
                .mapToLong(SysFile::getDownloadCount)
                .sum();
        statistics.put("totalDownloads", totalDownloads);

        // 今日上传数
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        Long todayUploads = count(new LambdaQueryWrapper<SysFile>()
                .ge(SysFile::getCreateTime, todayStart)
                .eq(SysFile::getStatus, 1));
        statistics.put("todayUploads", todayUploads);

        return statistics;
    }
}
