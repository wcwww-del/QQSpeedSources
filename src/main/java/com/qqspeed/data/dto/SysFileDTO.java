package com.qqspeed.data.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统文件DTO（用于前后台数据传输）
 */
@Data
public class SysFileDTO {

    /**
     * 文件ID
     */
    private Long id;

    /**
     * 文件原始名称
     */
    private String originalName;

    /**
     * 文件存储路径
     */
    private String filePath;

    /**
     * 文件URL访问地址
     */
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件大小（显示格式）
     */
    private String fileSizeDisplay;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * MIME类型
     */
    private String mimeType;

    /**
     * 文件分类 图片/文档/视频/其他
     */
    private String category;

    /**
     * 上传者ID
     */
    private Long uploaderId;

    /**
     * 上传者名称
     */
    private String uploaderName;

    /**
     * 状态
     */
    private Integer status;

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

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}