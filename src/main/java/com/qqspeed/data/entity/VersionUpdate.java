package com.qqspeed.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 版本更新实体类（对应version_update表）
 */
@Data
@TableName("version_update")
public class VersionUpdate {
    /**
     * 主键：前端完全不可见、不可传
     */
    @TableId(type = IdType.AUTO) // 数据库自增，优先级最高
    @JsonIgnore // 序列化/反序列化全程忽略，前端无感知
    private Long id;

    /**
     * 版本号
     */
    private String version;

    /**
     * 更新类型 大版本/小版本/紧急更新
     */
    private String updateType;

    /**
     * 更新标题
     */
    private String title;

    /**
     * 更新内容
     */
    private String content;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否强制更新
     */
    private Integer forceUpdate;

    /**
     * 下载链接
     */
    private String downloadUrl;

    /**
     * 更新包大小
     */
    private Long packageSize;

    /**
     * 更新包大小（显示格式）
     */
    private String packageSizeDisplay;

    /**
     * 适用平台 Android/iOS/全平台
     */
    private String platform;

    /**
     * 状态 0-草稿 1-已发布 2-已撤回
     */
    private Integer status;

    /**
     * 封面图
     */
    private String coverUrl;

    /**
     * 备注
     */
    private String remark;

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
    private LocalDateTime updateTimeField;
}
