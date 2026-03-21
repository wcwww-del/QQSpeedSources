package com.qqspeed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qqspeed.data.entity.TrackRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地图国服记录Mapper（MyBatis-Plus BaseMapper已封装CURD）
 */
@Mapper
public interface TrackRecordMapper extends BaseMapper<TrackRecord> {
    // 无需写基础CURD，BaseMapper已包含：selectList、selectById、insert、updateById、deleteById等
}
