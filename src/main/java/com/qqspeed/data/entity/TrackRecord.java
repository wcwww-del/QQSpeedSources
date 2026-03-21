package com.qqspeed.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 地图国服记录实体类（对应track_record表）
 */
@Data
@TableName("track_record")
public class TrackRecord {
    /**
     * 主键：前端完全不可见、不可传
     */
    @TableId(type = IdType.AUTO) // 数据库自增，优先级最高
    @JsonIgnore // 序列化/反序列化全程忽略，前端无感知
    private Long id;

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
