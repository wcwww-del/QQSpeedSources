package com.qqspeed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qqspeed.data.entity.Pet;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宠物Mapper（MyBatis-Plus BaseMapper已封装CURD）
 */
@Mapper
public interface PetMapper extends BaseMapper<Pet> {
    // 无需写基础CURD，BaseMapper已包含：selectList、selectById、insert、updateById、deleteById等
}
