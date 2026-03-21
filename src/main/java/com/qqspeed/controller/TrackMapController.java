package com.qqspeed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.entity.TrackMap;
import com.qqspeed.data.dto.trackMapDTO.TrackMapDTO;
import com.qqspeed.data.dto.trackMapDTO.TrackMapQuaryOrDeleteDTO;
import com.qqspeed.service.TrackMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trackmap")
@Tag(name = "地图管理", description = "地图的CURD接口")
public class TrackMapController {
    
    @Autowired
    private TrackMapService trackMapService;

    /**
     * 分页查询地图（后台/前台通用）
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param trackMap 筛选条件
     * @return 分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询地图", description = "持地图名称、难度星级、圈数、地图类型、状态查询")
    public Result<IPage<TrackMap>> pageQuery(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "asc") String sortDirection,
            @Parameter(description = "筛选条件") TrackMap trackMap
    ) {
        Page<TrackMap> page = new Page<>(pageNum, pageSize);
        IPage<TrackMap> trackMapPage = trackMapService.pageQuery(page, trackMap, sortDirection);
        return Result.success(trackMapPage);
    }

    /**
     * 根据地图名称查询单条地图信息
     * @param trackMapQuaryOrDeleteDTO 地图名称
     * @return 地图详情
     */
    @GetMapping("/{name}")
    @Operation(summary = "根据地图名称查询地图", description = "前台详情页/后台编辑用")
    public Result<TrackMap> getByName(@Parameter(description = "地图名称") @RequestBody TrackMapQuaryOrDeleteDTO trackMapQuaryOrDeleteDTO) {
        TrackMap trackMap = trackMapService.getTrackMapByName(trackMapQuaryOrDeleteDTO.getName());
        return Result.success(trackMap);
    }

    /**
     * 新增地图（后台管理）
     * @param trackMapDTO 地图信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "新增地图", description = "后台管理员新增地图信息")
    public Result<?> add(@Parameter(description = "地图信息") @RequestBody TrackMapDTO trackMapDTO) {
        TrackMap trackMap = new TrackMap();
        BeanUtils.copyProperties(trackMapDTO, trackMap);
        boolean save = trackMapService.save(trackMap);
        return save ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改地图（后台管理）
     * @param trackMapDTO 地图信息
     * @return 操作结果
     */
    @PutMapping
    @Operation(summary = "修改地图", description = "后台管理员修改地图信息")
    public Result<?> update(@Parameter(description = "地图信息") @RequestBody TrackMapDTO trackMapDTO) {
        TrackMap trackMap = new TrackMap();
        BeanUtils.copyProperties(trackMapDTO, trackMap);
        boolean update = trackMapService.updateByTrackMapName(trackMap);
        return update ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除操作
     * @param trackMapQuaryOrDeleteDTO
     * @return 操作结果
     */
    @DeleteMapping
    @Operation(summary = "删除地图", description = "后台管理员删除地图信息")
    public Result<?> delete(@Parameter(description = "地图名称") @RequestBody TrackMapQuaryOrDeleteDTO trackMapQuaryOrDeleteDTO) {
        boolean remove = trackMapService.removeByTrackMapName(trackMapQuaryOrDeleteDTO.getName());
        return remove ? Result.success() : Result.error("删除失败");
    }
}
