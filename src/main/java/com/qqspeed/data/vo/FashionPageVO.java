package com.qqspeed.data.vo;

import lombok.Data;

/**
 * 装备分页查询展示VO，精简字段
 */
@Data
public class FashionPageVO {

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
     * 状态 0下架 1上架
     */
    private Integer status;

    /**
     * 封面图
     */
    private String coverUrl;
}