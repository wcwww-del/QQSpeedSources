package com.qqspeed.data.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 国服记录分页查询展示VO，精简字段
 */
@Data
public class TrackRecordPageVO {

    /**
     * 地图名称
     */
    private String trackMap;

    /**
     * T车国服纪录时间
     */
    private LocalTime recordTimeT;

    /**
     * T车使用赛车
     */
    private String carNameT;

    /**
     * T车使用ECU
     */
    private String ECUNameT;

    /**
     * T车使用宠物
     */
    private String petNameT;

    /**
     * T纪录保持者
     */
    private String playerNameT;

    /**
     * 创造纪录时间（T）
     */
    private LocalDate recordTimeDateT;

    /**
     * A车国服记录时间
     */
    private LocalTime recordTimeA;

    /**
     * A车使用赛车
     */
    private String carNameA;

    /**
     * A车使用ECU
     */
    private String ECUNameA;

    /**
     * A车使用宠物
     */
    private String petNameA;

    /**
     * A纪录保持者
     */
    private String playerNameA;

    /**
     * 创造纪录时间（A）
     */
    private LocalDate recordTimeDateA;
}