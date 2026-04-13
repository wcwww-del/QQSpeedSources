package com.qqspeed.controller.backend;

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
 * 地图后台管理接口
 */
@RestController
@RequestMapping("/backend/trackmap")
@Tag(name = "地图管理", description = "地图的CRUD接口")
public class TrackMapBackendController {

    @Autowired
    private TrackMapService trackMapService;

    /**
     * 分页查询地图（后台）
     */
    @GetMapping("/page")
    @Operation(summary = "后台管理员分页查地图", description = "支持地图名称、难度星级、圈数、地图类型、状态查询")
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
     * 根据地图名称查地图详情（后台）
     */
    @GetMapping("/detail/{name}")
    @Operation(summary = "后台管理员查地图详情", description = "根据地图名称获取完整地图信息")
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
     * 新增地图（后台）
     */
    @PostMapping("/save")
    @Operation(summary = "新增地图", description = "后台新增地图信息")
    public Result<Boolean> save(
            @Parameter(description = "地图信息") @RequestBody TrackMap trackMap
    ) {
        try {
            boolean result = trackMapService.save(trackMap);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改地图（后台）
     */
    @PostMapping("/update")
    @Operation(summary = "修改地图", description = "后台修改地图信息")
    public Result<Boolean> update(
            @Parameter(description = "地图信息") @RequestBody TrackMap trackMap
    ) {
        try {
            boolean result = trackMapService.updateByTrackMapName(trackMap);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除地图（后台）
     */
    @PostMapping("/delete/{name}")
    @Operation(summary = "删除地图", description = "根据地图名称删除地图信息")
    public Result<Boolean> delete(
            @Parameter(description = "地图名称") @PathVariable String name
    ) {
        try {
            boolean result = trackMapService.removeByTrackMapName(name);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除地图（后台）
     */
    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除地图", description = "批量删除多个地图")
    public Result<Boolean> batchDelete(
            @Parameter(description = "地图名称列表") @RequestBody java.util.List<String> names
    ) {
        try {
            boolean allSuccess = true;
            for (String name : names) {
                boolean result = trackMapService.removeByTrackMapName(name);
                if (!result) {
                    allSuccess = false;
                }
            }
            return Result.success(allSuccess);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}