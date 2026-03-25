package com.qqspeed.data.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 新增/修改/前端用户OV使用的DTO
 */
@Data
public class PetDTO {
    /**
     * 宠物名称
     */
    private String name;

    /**
     * 品级 骑宠/秘境宠/普通钻石宠/免费宠
     */
    private String grade;

    /**
     * 满级效果
     */
    private String fullLevelEffect;

    /**
     * 适配模式
     */
    private String adaptMode;

    /**
     * 羁绊
     */
    private String relation;

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
