package com.qqspeed.data.dto.trackMapDTO;

import lombok.Data;

import java.time.LocalDate;

/**
 * 新增/修改/前端用户OV使用的DTO
 */
@Data
public class TrackMapDTO {

    /**
     * 地图名称
     */
    private String name;

    /**
     * 难度星级 1~7星
     */
    private Integer star;

    /**
     * 地图圈数 1/2/3
     */
    private Integer laps;

    /**
     * 地图类型 平地/腾空/加速带/长图/短图
     */
    private String type;

    /**
     * 状态 联赛图/非联赛图/怀旧图
     */
    private String status;

    /**
     * 腾空点个数
     */
    private Integer liftOffPoint;

    /**
     * 加速带个数
     */
    private Integer accelerationLanes;

    /**
     * 近道个数
     */
    private Integer shortCuts;

    /**
     * 上线时间
     */
    private LocalDate onlineTime;

    /**
     * 地图描述/背景
     */
    private String description;

    /**
     * 地图封面
     */
    private String coverUrl;
}
