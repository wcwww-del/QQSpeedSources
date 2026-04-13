package com.qqspeed.data.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统文件展示VO（精简字段）
 */
@Data
public class SysFileVO {

    /**
     * 文件ID
     */
    private Long id;

    /**
     * 文件原始名称
     */
    private String originalName;

    /**
     * 文件URL访问地址
     */
    private String fileUrl;

    /**
     * 文件大小（显示格式）
     */
    private String fileSizeDisplay;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件分类
     */
    private String category;

    /**
     * 上传者名称
     */
    private String uploaderName;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}