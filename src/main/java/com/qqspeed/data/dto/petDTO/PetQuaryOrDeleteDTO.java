package com.qqspeed.data.dto.petDTO;

import lombok.Data;

/**
 *  查询/删除单个宠物信息时controller层使用的DTO
 */
@Data
public class PetQuaryOrDeleteDTO {
    /**
     * 宠物名称
     */
    private String name;
}
