package com.qqspeed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqspeed.common.exception.BusinessException;
import com.qqspeed.data.entity.Fashion;
import com.qqspeed.data.dto.FashionDTO;
import com.qqspeed.mapper.FashionMapper;
import com.qqspeed.service.FashionService;
import com.qqspeed.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 装备服务实现类
 */
@Service
public class FashionServiceImpl extends ServiceImpl<FashionMapper, Fashion> implements FashionService {

    @Autowired
    private RedisUtils redisUtils;

    // ====================== 前后台通用方法 ======================
    @Override
    public IPage<Fashion> pageQuery(Page<Fashion> page, Fashion fashion, String sortDirection) {
        // 构造查询条件
        LambdaQueryWrapper<Fashion> queryWrapper = new LambdaQueryWrapper<>();

        // 名称模糊查询
        if (StringUtils.isNotBlank(fashion.getName())) {
            queryWrapper.like(Fashion::getName, fashion.getName());
        }

        // 装备类型精确查询
        if (StringUtils.isNotBlank(fashion.getType())) {
            queryWrapper.eq(Fashion::getType, fashion.getType());
        }

        // 品质精确查询
        if (StringUtils.isNotBlank(fashion.getQuality())) {
            queryWrapper.eq(Fashion::getQuality, fashion.getQuality());
        }

        // 状态精确查询
        if (fashion.getStatus() != null) {
            queryWrapper.eq(Fashion::getStatus, fashion.getStatus());
        }

        // 先校验排序方向参数，避免非法值
        if ("asc".equalsIgnoreCase(sortDirection)) {
            // 升序：按ID从小到大
            queryWrapper.orderByAsc(Fashion::getId);
        } else {
            // 降序（默认）：按ID从大到小
            queryWrapper.orderByDesc(Fashion::getId);
        }

        // 分页查询
        return baseMapper.selectPage(page, queryWrapper);
    }

    // ====================== 后台CURD方法 ======================
    @Override
    public Fashion getFashionByName(String name) {
        Long id = getIdByName(name);
        return baseMapper.selectById(id);
    }

    @Override
    public boolean save(Fashion fashion) {
        // 校验名称重复（唯一索引兜底，业务层再校验）
        if (this.count(new LambdaQueryWrapper<Fashion>().eq(Fashion::getName, fashion.getName())) > 0) {
            throw new BusinessException("装备名称【" + fashion.getName() + "】已存在");
        }
        return super.save(fashion);
    }

    @Override
    public boolean updateByFashionName(Fashion fashion) {
        // 1. 查ID
        Long id = getIdByName(fashion.getName());
        if (id == null) {
            throw new BusinessException("装备名称【" + fashion.getName() + "】不存在");
        }

        // 2. 设置ID
        fashion.setId(id);

        // 3. 更新并失效缓存
        boolean result = super.updateById(fashion);
        if (result) {
            // 失效相关缓存
            clearFashionCache(fashion.getName(), fashion.getType());
        }
        return result;
    }

    @Override
    public boolean removeByFashionName(String name) {
        // 1. 查ID
        Long id = getIdByName(name);
        if (id == null) {
            throw new BusinessException("装备名称【" + name + "】不存在");
        }

        // 2. 删除并失效缓存
        Fashion fashion = baseMapper.selectById(id);
        boolean result = super.removeById(id);
        if (result && fashion != null) {
            // 失效相关缓存
            clearFashionCache(name, fashion.getType());
        }
        return result;
    }

    // ====================== 前台缓存方法 ======================
    @Override
    public FashionDTO getFashionDetailWithCache(String name) {
        String cacheKey = "fashion:detail:" + name;

        // 先从缓存获取
        FashionDTO fashionDTO = (FashionDTO) redisUtils.get(cacheKey);
        if (fashionDTO != null) {
            return fashionDTO;
        }

        // 缓存未命中，从数据库查询
        Fashion fashion = getFashionByName(name);
        if (fashion == null) {
            return null;
        }

        // 转换为DTO
        fashionDTO = new FashionDTO();
        BeanUtils.copyProperties(fashion, fashionDTO);

        // 放入缓存，30分钟过期
        redisUtils.set(cacheKey, fashionDTO, 30, TimeUnit.MINUTES);

        return fashionDTO;
    }

    @Override
    public List<FashionDTO> getFashionListByTypeWithCache(String type) {
        String cacheKey = "fashion:list:type:" + type;

        // 先从缓存获取
        List<FashionDTO> fashionList = (List<FashionDTO>) redisUtils.get(cacheKey);
        if (fashionList != null) {
            return fashionList;
        }

        // 缓存未命中，从数据库查询
        LambdaQueryWrapper<Fashion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Fashion::getType, type)
                   .eq(Fashion::getStatus, 1) // 只查询上架的
                   .orderByDesc(Fashion::getQuality) // 按品质降序
                   .orderByAsc(Fashion::getName); // 同品质按名称升序

        List<Fashion> fashions = baseMapper.selectList(queryWrapper);

        // 转换为DTO
        fashionList = fashions.stream()
                .map(fashion -> {
                    FashionDTO dto = new FashionDTO();
                    BeanUtils.copyProperties(fashion, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        // 放入缓存，1小时过期
        redisUtils.set(cacheKey, fashionList, 1, TimeUnit.HOURS);

        return fashionList;
    }

    @Override
    public List<FashionDTO> getHotFashionWithCache(int topCount) {
        String cacheKey = "fashion:hot:" + topCount;

        // 先从缓存获取
        List<FashionDTO> hotFashions = (List<FashionDTO>) redisUtils.get(cacheKey);
        if (hotFashions != null) {
            return hotFashions;
        }

        // 缓存未命中，从数据库查询热门装备
        LambdaQueryWrapper<Fashion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Fashion::getStatus, 1) // 只查询上架的
                   .orderByDesc(Fashion::getAttrValue) // 按属性值降序（热门度）
                   .orderByDesc(Fashion::getId); // 同属性值按ID降序

        List<Fashion> fashions = baseMapper.selectList(queryWrapper);

        // 截取前topCount条
        List<Fashion> topFashions = fashions.stream()
                .limit(topCount)
                .collect(Collectors.toList());

        // 转换为DTO
        hotFashions = topFashions.stream()
                .map(fashion -> {
                    FashionDTO dto = new FashionDTO();
                    BeanUtils.copyProperties(fashion, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        // 放入缓存，1小时过期
        redisUtils.set(cacheKey, hotFashions, 1, TimeUnit.HOURS);

        return hotFashions;
    }

    // ====================== 私有辅助方法 ======================
    /**
     * 根据名称获取ID
     */
    private Long getIdByName(String name) {
        LambdaQueryWrapper<Fashion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Fashion::getId)
                   .eq(Fashion::getName, name);
        Fashion fashion = baseMapper.selectOne(queryWrapper);
        return fashion != null ? fashion.getId() : null;
    }

    /**
     * 清除装备相关缓存
     */
    private void clearFashionCache(String name, String type) {
        // 清除详情缓存
        redisUtils.delete("fashion:detail:" + name);
        // 清除类型列表缓存
        redisUtils.delete("fashion:list:type:" + type);
        // 清除热门缓存
        redisUtils.delete("fashion:hot:10");
        redisUtils.delete("fashion:hot:20");
    }
}
