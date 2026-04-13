package com.qqspeed.data.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 装备前后台参数传递DTO
 */
@Data
public class FashionDTO {

    /**
     * 装备名称
     */
    private String name;

    /**
     * 装备类型 头部/面部/背部/手部/副驾/秘境/宠物/通用
     */
    private String type;

    /**
     * 品质/等级 普通/R/SR/SSR/传奇/神话
     */
    private String quality;

    /**
     * 属性类型 速度/氮气/小喷/漂移/转弯/集气/金币经验
     */
    private String attrType;

    /**
     * 属性数值
     */
    private Integer attrValue;

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