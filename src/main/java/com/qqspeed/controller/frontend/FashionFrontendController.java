package com.qqspeed.controller.frontend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.dto.FashionDTO;
import com.qqspeed.data.entity.Fashion;
import com.qqspeed.data.vo.FashionPageVO;
import com.qqspeed.service.FashionService;
import com.qqspeed.utils.RedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 装备前台接口（普通用户访问，无权限校验）
 */
@RestController
@RequestMapping("/frontend/fashion")
@Tag(name = "装备前台接口", description = "普通用户浏览装备信息的接口")
public class FashionFrontendController {

    @Autowired
    private FashionService fashionService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 前台分页查询装备（仅展示VO字段）
     */
    @GetMapping("/page")
    @Operation(summary = "前台分页查装备", description = "支持名称、类型、品质、状态进行筛选，支持按ID升序/降序排序")
    public Result<IPage<FashionPageVO>> pageQuery(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "20") Integer pageSize, // 前台每页展示更多
            @Parameter(description = "装备名称") @RequestParam(required = false) String name,
            @Parameter(description = "装备类型") @RequestParam(required = false) String type,
            @Parameter(description = "品质") @RequestParam(required = false) String quality,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "desc") String sortDirection // 只有desc和asc两种方式
    ) {
        // 1. 构造查询条件
        Fashion fashionQuery = new Fashion();
        fashionQuery.setName(name);
        fashionQuery.setType(type);
        fashionQuery.setQuality(quality);
        fashionQuery.setStatus(1); // 前台默认只查询上架的

        // 2. 创建分页对象
        Page<Fashion> page = new Page<>(pageNum, pageSize);

        // 3. 调用服务层查询
        IPage<Fashion> fashionPage = fashionService.pageQuery(page, fashionQuery, sortDirection);

        // 4. 转换为VO
        IPage<FashionPageVO> voPage = fashionPage.convert(fashion -> {
            FashionPageVO vo = new FashionPageVO();
            BeanUtils.copyProperties(fashion, vo);
            return vo;
        });

        return Result.success(voPage);
    }

    /**
     * 前台查装备详情
     */
    @GetMapping("/detail/{name}")
    @Operation(summary = "前台查装备详情", description = "根据装备名称获取完整装备信息")
    public Result<FashionDTO> getDetail(
            @Parameter(description = "装备名称") @PathVariable String name
    ) {
        FashionDTO fashionDTO = fashionService.getFashionDetailWithCache(name);
        if (fashionDTO == null) {
            return Result.error("装备不存在");
        }
        return Result.success(fashionDTO);
    }

    /**
     * 前台按类型查询装备列表
     */
    @GetMapping("/list/{type}")
    @Operation(summary = "按类型查询装备列表", description = "根据装备类型获取该类型下的所有上架装备")
    public Result<List<FashionDTO>> getListByType(
            @Parameter(description = "装备类型") @PathVariable String type
    ) {
        List<FashionDTO> fashionList = fashionService.getFashionListByTypeWithCache(type);
        return Result.success(fashionList);
    }

    /**
     * 前台查询热门装备
     */
    @GetMapping("/hot")
    @Operation(summary = "查询热门装备", description = "获取属性值最高的热门装备，用于首页展示")
    public Result<List<FashionDTO>> getHotFashion(
            @Parameter(description = "获取数量") @RequestParam(defaultValue = "10") Integer topCount
    ) {
        List<FashionDTO> hotFashions = fashionService.getHotFashionWithCache(topCount);
        return Result.success(hotFashions);
    }

    /**
     * 前台按品质查询装备
     */
    @GetMapping("/quality/{quality}")
    @Operation(summary = "按品质查询装备", description = "根据装备品质获取对应等级的装备列表")
    public Result<List<FashionDTO>> getByQuality(
            @Parameter(description = "装备品质") @PathVariable String quality
    ) {
        // 使用缓存机制查询特定品质的装备
        String cacheKey = "fashion:list:quality:" + quality;
        List<FashionDTO> fashionList = (List<FashionDTO>) redisUtils.get(cacheKey);

        if (fashionList == null) {
            // 从数据库查询
            LambdaQueryWrapper<Fashion> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Fashion::getQuality, quality)
                       .eq(Fashion::getStatus, 1) // 只查询上架的
                       .orderByDesc(Fashion::getAttrValue) // 按属性值降序
                       .orderByAsc(Fashion::getName); // 同属性值按名称升序

            List<Fashion> fashions = fashionService.getBaseMapper().selectList(queryWrapper);

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
        }

        return Result.success(fashionList);
    }
}