package com.qqspeed.controller.frontend;

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

/**
 * 国服记录前台接口（普通用户访问，无权限校验）
 */
@RestController
@RequestMapping("/frontend/trackrecord")
@Tag(name = "国服记录前台接口", description = "普通用户浏览国服记录信息的接口")
public class TrackRecordFrontendController {

    @Autowired
    private TrackRecordService trackRecordService;

    /**
     * 前台分页查询国服记录
     */
    @GetMapping("/page")
    @Operation(summary = "前台分页查国服记录", description = "支持地图名称、T/A车手、赛车、宠物、ECU查询")
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
     * 前台查国服记录详情
     */
    @GetMapping("/detail/{trackMap}")
    @Operation(summary = "前台查国服记录详情", description = "根据地图名称获取完整国服记录信息")
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
     * 查询T车国服排行榜
     */
    @GetMapping("/ranking/t")
    @Operation(summary = "T车国服排行榜", description = "获取T车的国服记录排行榜")
    public Result<IPage<TrackRecordPageVO>> getTRanking(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        TrackRecord trackRecordQuery = new TrackRecord();

        Page<TrackRecord> page = new Page<>(pageNum, pageSize);
        // 默认按T车纪录时间升序（时间越短越好）
        IPage<TrackRecord> trackRecordPage = trackRecordService.pageQuery(page, trackRecordQuery, "asc");

        IPage<TrackRecordPageVO> voPage = trackRecordPage.convert(trackRecord -> {
            TrackRecordPageVO vo = new TrackRecordPageVO();
            BeanUtils.copyProperties(trackRecord, vo);
            return vo;
        });

        return Result.success(voPage);
    }

    /**
     * 查询A车国服排行榜
     */
    @GetMapping("/ranking/a")
    @Operation(summary = "A车国服排行榜", description = "获取A车的国服记录排行榜")
    public Result<IPage<TrackRecordPageVO>> getARanking(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        TrackRecord trackRecordQuery = new TrackRecord();

        Page<TrackRecord> page = new Page<>(pageNum, pageSize);
        // 默认按A车纪录时间升序（时间越短越好）
        IPage<TrackRecord> trackRecordPage = trackRecordService.pageQuery(page, trackRecordQuery, "asc");

        IPage<TrackRecordPageVO> voPage = trackRecordPage.convert(trackRecord -> {
            TrackRecordPageVO vo = new TrackRecordPageVO();
            BeanUtils.copyProperties(trackRecord, vo);
            return vo;
        });

        return Result.success(voPage);
    }

    /**
     * 按地图查询国服记录
     */
    @GetMapping("/by-track/{trackMap}")
    @Operation(summary = "按地图查询国服记录", description = "获取指定地图的国服记录")
    public Result<TrackRecord> getByTrack(
            @Parameter(description = "地图名称") @PathVariable String trackMap
    ) {
        TrackRecord trackRecord = trackRecordService.getTrackRecordByTrackMap(trackMap);
        return Result.success(trackRecord);
    }

    /**
     * 搜索国服记录
     */
    @GetMapping("/search")
    @Operation(summary = "搜索国服记录", description = "根据车手、赛车、宠物搜索国服记录")
    public Result<IPage<TrackRecordPageVO>> search(
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        TrackRecord trackRecordQuery = new TrackRecord();
        // 根据关键词设置查询条件
        trackRecordQuery.setTrackMap(keyword);

        Page<TrackRecord> page = new Page<>(pageNum, pageSize);
        IPage<TrackRecord> trackRecordPage = trackRecordService.pageQuery(page, trackRecordQuery, "desc");

        IPage<TrackRecordPageVO> voPage = trackRecordPage.convert(trackRecord -> {
            TrackRecordPageVO vo = new TrackRecordPageVO();
            BeanUtils.copyProperties(trackRecord, vo);
            return vo;
        });

        return Result.success(voPage);
    }
}