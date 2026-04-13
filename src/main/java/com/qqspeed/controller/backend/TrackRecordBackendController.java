package com.qqspeed.controller.backend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.entity.TrackRecord;
import com.qqspeed.data.vo.TrackRecordPageVO;
import com.qqspeed.service.TrackRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 国服记录后台管理接口
 */
@RestController
@RequestMapping("/backend/trackrecord")
@Tag(name = "国服记录管理", description = "国服记录的CRUD接口")
public class TrackRecordBackendController {

    @Autowired
    private TrackRecordService trackRecordService;

    /**
     * 分页查询国服记录（后台）
     */
    @GetMapping("/page")
    @Operation(summary = "后台管理员分页查国服记录", description = "支持地图名称、T/A车手、赛车、宠物、ECU查询")
    public Result<IPage<TrackRecordPageVO>> pageQuery(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(description = "地图名称") @RequestParam(required = false) String trackMap,
            @Parameter(description = "T车车手") @RequestParam(required = false) String playerNameT,
            @Parameter(description = "T车赛车") @RequestParam(required = false) String carNameT,
            @Parameter(description = "T车宠物") @RequestParam(required = false) String petNameT,
            @Parameter(description = "A车车手") @RequestParam(required = false) String playerNameA,
            @Parameter(description = "A车赛车") @RequestParam(required = false) String carNameA,
            @Parameter(description = "A车宠物") @RequestParam(required = false) String petNameA,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        // 1. 构造查询条件
        TrackRecord trackRecordQuery = new TrackRecord();
        trackRecordQuery.setTrackMap(trackMap);
        trackRecordQuery.setPlayerNameT(playerNameT);
        trackRecordQuery.setCarNameT(carNameT);
        trackRecordQuery.setPetNameT(petNameT);
        trackRecordQuery.setPlayerNameA(playerNameA);
        trackRecordQuery.setCarNameA(carNameA);
        trackRecordQuery.setPetNameA(petNameA);

        // 2. 创建分页对象
        Page<TrackRecord> page = new Page<>(pageNum, pageSize);

        // 3. 调用服务层查询
        IPage<TrackRecord> trackRecordPage = trackRecordService.pageQuery(page, trackRecordQuery, sortDirection);

        // 4. 转换为VO
        IPage<TrackRecordPageVO> voPage = trackRecordPage.convert(trackRecord -> {
            TrackRecordPageVO vo = new TrackRecordPageVO();
            BeanUtils.copyProperties(trackRecord, vo);
            return vo;
        });

        return Result.success(voPage);
    }

    /**
     * 根据地图名称查国服记录详情（后台）
     */
    @GetMapping("/detail/{trackMap}")
    @Operation(summary = "后台管理员查国服记录详情", description = "根据地图名称获取完整国服记录信息")
    public Result<TrackRecord> getDetail(
            @Parameter(description = "地图名称") @PathVariable String trackMap
    ) {
        TrackRecord trackRecord = trackRecordService.getTrackRecordByTrackMap(trackMap);
        if (trackRecord == null) {
            return Result.error("国服记录不存在");
        }
        return Result.success(trackRecord);
    }

    /**
     * 新增国服记录（后台）
     */
    @PostMapping("/save")
    @Operation(summary = "新增国服记录", description = "后台新增国服记录信息")
    public Result<Boolean> save(
            @Parameter(description = "国服记录信息") @RequestBody TrackRecord trackRecord
    ) {
        try {
            boolean result = trackRecordService.save(trackRecord);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改国服记录（后台）
     */
    @PostMapping("/update")
    @Operation(summary = "修改国服记录", description = "后台修改国服记录信息")
    public Result<Boolean> update(
            @Parameter(description = "国服记录信息") @RequestBody TrackRecord trackRecord
    ) {
        try {
            boolean result = trackRecordService.updateByTrackMap(trackRecord);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除国服记录（后台）
     */
    @PostMapping("/delete/{trackMap}")
    @Operation(summary = "删除国服记录", description = "根据地图名称删除国服记录信息")
    public Result<Boolean> delete(
            @Parameter(description = "地图名称") @PathVariable String trackMap
    ) {
        try {
            boolean result = trackRecordService.removeByTrackMap(trackMap);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除国服记录（后台）
     */
    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除国服记录", description = "批量删除多个国服记录")
    public Result<Boolean> batchDelete(
            @Parameter(description = "地图名称列表") @RequestBody List<String> trackMaps
    ) {
        try {
            boolean allSuccess = true;
            for (String trackMap : trackMaps) {
                boolean result = trackRecordService.removeByTrackMap(trackMap);
                if (!result) {
                    allSuccess = false;
                }
            }
            return Result.success(allSuccess);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有国服记录（后台）
     */
    @GetMapping("/all")
    @Operation(summary = "获取所有国服记录", description = "后台获取所有国服记录列表")
    public Result<List<TrackRecord>> getAll() {
        TrackRecord query = new TrackRecord();
        Page<TrackRecord> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<TrackRecord> result = trackRecordService.pageQuery(page, query, "desc");
        return Result.success(result.getRecords());
    }

    /**
     * 统计国服记录数量
     */
    @GetMapping("/count")
    @Operation(summary = "统计国服记录数量", description = "统计国服记录总数")
    public Result<Long> count() {
        long count = trackRecordService.count();
        return Result.success(count);
    }
}