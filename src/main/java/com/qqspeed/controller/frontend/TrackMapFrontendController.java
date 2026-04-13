package com.qqspeed.controller.frontend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.entity.TrackMap;
import com.qqspeed.data.vo.TrackMapPageVO;
import com.qqspeed.service.TrackMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 地图前台接口（普通用户访问，无权限校验）
 */
@RestController
@RequestMapping("/frontend/trackmap")
@Tag(name = "地图前台接口", description = "普通用户浏览地图信息的接口")
public class TrackMapFrontendController {

    @Autowired
    private TrackMapService trackMapService;

    /**
     * 前台分页查询地图
     */
    @GetMapping("/page")
    @Operation(summary = "前台分页查地图", description = "支持地图名称、难度星级、圈数、地图类型、状态查询")
    public Result<IPage<TrackMapPageVO>> pageQuery(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(description = "地图名称") @RequestParam(required = false) String name,
            @Parameter(description = "难度星级") @RequestParam(required = false) Integer star,
            @Parameter(description = "圈数") @RequestParam(required = false) Integer laps,
            @Parameter(description = "地图类型") @RequestParam(required = false) String type,
            @Parameter(description = "地图状态") @RequestParam(required = false) String status,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        // 1. 构造查询条件
        TrackMap trackMapQuery = new TrackMap();
        trackMapQuery.setName(name);
        trackMapQuery.setStar(star);
        trackMapQuery.setLaps(laps);
        trackMapQuery.setType(type);
        trackMapQuery.setStatus(status);

        // 2. 创建分页对象
        Page<TrackMap> page = new Page<>(pageNum, pageSize);

        // 3. 调用服务层查询
        IPage<TrackMap> trackMapPage = trackMapService.pageQuery(page, trackMapQuery, sortDirection);

        // 4. 转换为VO
        IPage<TrackMapPageVO> voPage = trackMapPage.convert(trackMap -> {
            TrackMapPageVO vo = new TrackMapPageVO();
            BeanUtils.copyProperties(trackMap, vo);
            return vo;
        });

        return Result.success(voPage);
    }

    /**
     * 前台查地图详情
     */
    @GetMapping("/detail/{name}")
    @Operation(summary = "前台查地图详情", description = "根据地图名称获取完整地图信息")
    public Result<TrackMap> getDetail(
            @Parameter(description = "地图名称") @PathVariable String name
    ) {
        TrackMap trackMap = trackMapService.getTrackMapByName(name);
        if (trackMap == null) {
            return Result.error("地图不存在");
        }
        return Result.success(trackMap);
    }

    /**
     * 按难度星级查询地图
     */
    @GetMapping("/star/{star}")
    @Operation(summary = "按难度星级查询地图", description = "获取指定难度星级下的所有地图")
    public Result<IPage<TrackMapPageVO>> getByStar(
            @Parameter(description = "难度星级") @PathVariable Integer star,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        TrackMap trackMapQuery = new TrackMap();
        trackMapQuery.setStar(star);
        trackMapQuery.setStatus("联赛图"); // 默认只查询联赛图

        Page<TrackMap> page = new Page<>(pageNum, pageSize);
        IPage<TrackMap> trackMapPage = trackMapService.pageQuery(page, trackMapQuery, "desc");

        IPage<TrackMapPageVO> voPage = trackMapPage.convert(trackMap -> {
            TrackMapPageVO vo = new TrackMapPageVO();
            BeanUtils.copyProperties(trackMap, vo);
            return vo;
        });

        return Result.success(voPage);
    }

    /**
     * 按地图类型查询
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "按地图类型查询", description = "获取指定类型的所有地图")
    public Result<IPage<TrackMapPageVO>> getByType(
            @Parameter(description = "地图类型") @PathVariable String type,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        TrackMap trackMapQuery = new TrackMap();
        trackMapQuery.setType(type);

        Page<TrackMap> page = new Page<>(pageNum, pageSize);
        IPage<TrackMap> trackMapPage = trackMapService.pageQuery(page, trackMapQuery, "desc");

        IPage<TrackMapPageVO> voPage = trackMapPage.convert(trackMap -> {
            TrackMapPageVO vo = new TrackMapPageVO();
            BeanUtils.copyProperties(trackMap, vo);
            return vo;
        });

        return Result.success(voPage);
    }
}