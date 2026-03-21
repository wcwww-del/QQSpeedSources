package com.qqspeed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qqspeed.data.entity.TrackMap;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地图Mapper（MyBatis-Plus BaseMapper已封装CURD）
 */
@Mapper
public interface TrackMapMapper extends BaseMapper<TrackMap> {
    // 无需写基础CURD，BaseMapper已包含：selectList、selectById、insert、updateById、deleteById等
}
