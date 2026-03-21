package com.qqspeed.data.vo;

import lombok.Data;

/**
 * 前台分页查询展示VO，精简字段
 */
@Data
public class CarPageVO {

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
     * 适配模式
     */
    private String adaptMode;

    /**
     * 状态 0下架 1上架
     */
    private Integer status;

    /**
     * 封面图
     */
    private String coverUrl;
}
