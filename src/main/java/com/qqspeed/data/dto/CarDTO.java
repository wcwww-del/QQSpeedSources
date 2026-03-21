package com.qqspeed.data.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 前后台参数传递DTO
 */
@Data
public class CarDTO {

    /**
     * 赛车名称
     */
    private String name;

    /**
     * 等级 T/A/B/C/D/勋章/联赛/悬浮
     */
    private String level;

    /**
     * 赛车类型 机甲/联赛/年限/勋章/悬浮/免费
     */
    private String type;

    /**
     * 赛车特性
     */
    private String feature;

    /**
     * 改装推荐
     */
    private String recommendModify;

    /**
     * 适配模式
     */
    private String adaptMode;

    /**
     * 速度属性
     */
    private Integer attrSpeed;

    /**
     * 氮气动力
     */
    private Integer nitrogenPower;

    /**
     *小喷动力
     */
    private Integer jetPower;

    /**
     * 转弯属性
     */
    private Integer attrTurn;

    /**
     * 集气属性
     */
    private Integer attrGas;

    /**
     * 漂移属性
     */
    private Integer attrDrift;

    /**
     * 上线时间
     */
    private LocalDate onlineTime;

    /**
     * 获取方式
     */
    private String obtainWay;

    /**
     * 状态 0下架 1上架
     */
    private Integer status;

    /**
     * 封面图
     */
    private String coverUrl;
}
