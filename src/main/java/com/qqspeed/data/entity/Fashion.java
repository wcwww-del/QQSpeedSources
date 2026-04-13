package com.qqspeed.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 装备实体类（对应fashion表）
 */
@Data
@TableName("fashion")
public class Fashion {
    /**
     * 主键：前端完全不可见、不可传
     */
    @TableId(type = IdType.AUTO) // 数据库自增，优先级最高
    @JsonIgnore // 序列化/反序列化全程忽略，前端无感知
    private Long id;

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
