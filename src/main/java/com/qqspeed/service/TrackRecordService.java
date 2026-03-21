package com.qqspeed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qqspeed.data.entity.TrackRecord;

/**
 * 地图国服记录服务接口
 */
public interface TrackRecordService extends IService<TrackRecord> {

    /**
     * 分页查询地图国服记录情况（支持条件筛选）
     * @param page 分页参数
     * @param trackRecord 筛选条件（地图名称、T/A、车手、赛车、宠物、ECU、创造纪录时间）
     * @return 分页结果
     */
    IPage<TrackRecord> pageQuery(Page<TrackRecord> page, TrackRecord trackRecord, String sortDirection);

    /**
     * 根据地图名称查询单条国服记录信息
     * @param trackMap 国服记录名称
     * @return 国服记录详情
     */
    public TrackRecord getTrackRecordByTrackMap(String trackMap);

    /**
     * 新增国服记录（后台管理）
     * @param trackRecord 国服记录信息
     * @return 操作结果
     */
    public boolean save(TrackRecord trackRecord);

    /**
     * 修改国服记录（后台管理）
     * @param trackRecord 国服记录信息
     * @return 操作结果
     */
    public boolean updateByTrackMap(TrackRecord trackRecord);

    /**
     * 删除操作
     * @param trackMap
     * @return 操作结果
     */
    public boolean removeByTrackMap(String trackMap);
}
