package com.qqspeed.data.dto.trackRecordDTO;

import lombok.Data;

/**
 *  查询/删除单个国服记录信息时controller层使用的DTO
 */
@Data
public class TrackRecordQuaryOrDeleteDTO {

    /**
     * 地图名称
     */
    private String trackMap;
}
