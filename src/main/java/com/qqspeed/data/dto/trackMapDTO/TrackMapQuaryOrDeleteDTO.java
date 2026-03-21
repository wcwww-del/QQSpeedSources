package com.qqspeed.data.dto.trackMapDTO;

import lombok.Data;

/**
 *  查询/删除单个地图信息时controller层使用的DTO
 */
@Data
public class TrackMapQuaryOrDeleteDTO {
    /**
     * 地图名称
     */
    private String name;
}
