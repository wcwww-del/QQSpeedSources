package com.qqspeed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qqspeed.data.entity.TrackMap;

/**
 * 地图服务接口
 */
public interface TrackMapService extends IService<TrackMap> {

    /**
     * 分页查询地图（支持条件筛选）
     * @param page 分页参数
     * @param trackMap 筛选条件（名称、等级、状态）
     * @return 分页结果
     */
    IPage<TrackMap> pageQuery(Page<TrackMap> page, TrackMap trackMap, String sortDirection);

    /**
     * 根据地图名称查询单条地图信息
     * @param name 地图名称
     * @return 地图详情
     */
    TrackMap getTrackMapByName(String name);

    /**
     * 新增地图（后台管理）
     * @param trackMap 地图信息
     * @return 操作结果
     */
    boolean save(TrackMap trackMap);

    /**
     * 修改地图（后台管理）
     * @param trackMap 地图信息
     * @return 操作结果
     */
    boolean updateByTrackMapName(TrackMap trackMap);

    /**
     * 删除操作
     * @param name
     * @return 操作结果
     */
    boolean removeByTrackMapName(String name);
}
