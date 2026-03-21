package com.qqspeed.data.dto.trackRecordDTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 新增/修改/前端用户OV使用的DTO
 */
@Data
public class TrackRecordDTO {

    /**
     * 对应地图名称
     */
    private String trackMap;

    /**
     * T车国服纪录
     */
    private LocalTime recordTimeT;

    /**
     * 使用赛车
     */
    private String carNameT;

    /**
     * 使用ECU
     */
    private String ECUNameT;

    /**
     * 使用宠物
     */
    private  String petNameT;

    /**
     * T纪录保持者
     */
    private String playerNameT;

    /**
     * 创造纪录时间
     */
    private LocalDate recordTimeDateT;

    /**
     * A车国服记录
     */
    private LocalTime recordTimeA;

    /**
     * 使用赛车
     */
    private String carNameA;

    /**
     * 使用ECU
     */
    private String ECUNameA;

    /**
     * 使用宠物
     */
    private  String petNameA;

    /**
     * A纪录保持者
     */
    private String playerNameA;

    /**
     * 创造纪录时间
     */
    private LocalDate recordTimeDateA;
}
