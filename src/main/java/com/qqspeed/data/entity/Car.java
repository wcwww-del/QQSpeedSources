package com.qqspeed.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 赛车实体类（对应car表）
 */
@Data
@TableName("car") // 关联数据库表名
public class Car {
    /**
     * 主键：前端完全不可见、不可传
     */
    @TableId(type = IdType.AUTO) // 数据库自增，优先级最高
    @JsonIgnore // 序列化/反序列化全程忽略，前端无感知
    private Long id;

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

    /**
     * 创建时间（前端无需传，MyBatis-Plus自动填充）
     */
    @JsonIgnore // 序列化/反序列化时忽略该字段，前端传了也接收不到
    @TableField(fill = FieldFill.INSERT) // 仅插入时填充
    private LocalDateTime createTime;

    /**
     * 更新时间（前端无需传，MyBatis-Plus自动填充）
     */
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入+更新时填充
    private LocalDateTime updateTime;
}