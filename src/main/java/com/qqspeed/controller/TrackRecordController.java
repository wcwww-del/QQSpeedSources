package com.qqspeed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.entity.TrackRecord;
import com.qqspeed.data.dto.trackRecordDTO.TrackRecordDTO;
import com.qqspeed.data.dto.trackRecordDTO.TrackRecordQuaryOrDeleteDTO;
import com.qqspeed.service.TrackRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trackrecord")
@Tag(name = "国服记录管理", description = "国服记录的CURD接口")
public class TrackRecordController {

    @Autowired
    private TrackRecordService trackRecordService;

    /**
     * 分页查询国服记录（后台/前台通用）
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param trackRecord 筛选条件
     * @return 分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询国服记录", description = "支持地图名称、T/A、车手、赛车、宠物、ECU、创造纪录时间查询")
    public Result<IPage<TrackRecord>> pageQuery(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "asc") String sortDirection,
            @Parameter(description = "筛选条件") TrackRecord trackRecord
    ) {
        Page<TrackRecord> page = new Page<>(pageNum, pageSize);
        IPage<TrackRecord> trackRecordPage = trackRecordService.pageQuery(page, trackRecord, sortDirection);
        return Result.success(trackRecordPage);
    }

    /**
     * 根据地图查询单条国服记录信息
     * @param trackRecordQuaryOrDeleteDTO 地图
     * @return 国服记录详情
     */
    @GetMapping("/information")
    @Operation(summary = "根据地图查询国服记录", description = "前台详情页/后台编辑用")
    public Result<TrackRecord> getByTrackMap(@Parameter(description = "地图") @RequestBody TrackRecordQuaryOrDeleteDTO trackRecordQuaryOrDeleteDTO) {
        TrackRecord trackRecord = trackRecordService.getTrackRecordByTrackMap(trackRecordQuaryOrDeleteDTO.getTrackMap());
        return Result.success(trackRecord);
    }

    /**
     * 新增国服记录（后台管理）
     * @param trackRecordDTO 赛车信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "新增国服记录", description = "后台管理员新增国服记录信息")
    public Result<?> add(@Parameter(description = "国服记录信息") @RequestBody TrackRecordDTO trackRecordDTO) {
        TrackRecord trackRecord = new TrackRecord();
        BeanUtils.copyProperties(trackRecordDTO, trackRecord);
        boolean save = trackRecordService.save(trackRecord);
        return save ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改国服记录（后台管理）
     * @param trackRecordDTO 赛车信息
     * @return 操作结果
     */
    @PutMapping
    @Operation(summary = "修改国服记录", description = "后台管理员修改国服记录信息")
    public Result<?> update(@Parameter(description = "国服记录信息") @RequestBody TrackRecordDTO trackRecordDTO) {
        TrackRecord trackRecord = new TrackRecord();
        BeanUtils.copyProperties(trackRecordDTO, trackRecord);
        boolean update = trackRecordService.updateByTrackMap(trackRecord);
        return update ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除国服记录（后台管理）
     * @param trackRecordQuaryOrDeleteDTO 地图
     * @return 操作结果
     */
    @DeleteMapping
    @Operation(summary = "删除国服记录", description = "后台管理员删除国服记录信息")
    public Result<?> delete(@Parameter(description = "trackMap") @RequestBody TrackRecordQuaryOrDeleteDTO trackRecordQuaryOrDeleteDTO) {
        boolean remove = trackRecordService.removeByTrackMap(trackRecordQuaryOrDeleteDTO.getTrackMap());
        return remove ? Result.success() : Result.error("删除失败");
    }
}
