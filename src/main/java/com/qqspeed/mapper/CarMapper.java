package com.qqspeed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qqspeed.data.entity.Car;
import org.apache.ibatis.annotations.Mapper;

/**
 * 赛车Mapper（MyBatis-Plus BaseMapper已封装CURD）
 */
@Mapper
public interface CarMapper extends BaseMapper<Car> {
    // 无需写基础CURD，BaseMapper已包含：selectList、selectById、insert、updateById、deleteById等
}