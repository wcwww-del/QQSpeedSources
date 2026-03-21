package com.qqspeed.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 宠物实体类（对应pet表）
 */
@Data
@TableName("pet")
public class Pet {
    /**
     * 主键：前端完全不可见、不可传
     */
    @TableId(type = IdType.AUTO) // 数据库自增，优先级最高
    @JsonIgnore // 序列化/反序列化全程忽略，前端无感知
    private Long id;

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
