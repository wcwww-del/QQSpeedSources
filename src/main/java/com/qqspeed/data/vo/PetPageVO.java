package com.qqspeed.data.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 前台分页查询展示VO，精简字段
 */
@Data
public class PetPageVO {

    /**
     * 宠物名称
     */
    private String name;

    /**
     * 品级 骑宠/秘境宠/普通钻石宠/免费宠
     */
    private String grade;

    /**
     * 适配模式
     */
    private String adaptMode;

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
