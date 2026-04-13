package com.qqspeed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qqspeed.data.entity.Fashion;
import org.apache.ibatis.annotations.Mapper;

/**
 * 装备Mapper（MyBatis-Plus BaseMapper已封装CURD）
 */
@Mapper
public interface FashionMapper extends BaseMapper<Fashion> {
    // 无需写基础CURD，BaseMapper已包含：selectList、selectById、insert、updateById、deleteById等
}
