package com.qqspeed.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("track_map")
public class TrackMap {
    /**
     * 主键：前端完全不可见、不可传
     */
    @TableId(type = IdType.AUTO) // 数据库自增，优先级最高
    @JsonIgnore // 序列化/反序列化全程忽略，前端无感知
    private Long id;

    /**
     * 地图名称
     */
    private String name;

    /**
     * 难度星级 1~7星
     */
    private Integer star;

    /**
     * 地图圈数 1/2/3
     */
    private Integer laps;

    /**
     * 地图类型 平地/腾空/加速带/长图/短图
     */
    private String type;

    /**
     * 状态 联赛图/非联赛图/怀旧图
     */
    private String status;

    /**
     * 腾空点个数
     */
    private Integer liftOffPoint;

    /**
     * 加速带个数
     */
    private Integer accelerationLanes;

    /**
     * 近道个数
     */
    private Integer shortCuts;

    /**
     * 上线时间
     */
    private LocalDate onlineTime;

    /**
     * 地图描述/背景
     */
    private String description;

    /**
     * 地图封面
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
