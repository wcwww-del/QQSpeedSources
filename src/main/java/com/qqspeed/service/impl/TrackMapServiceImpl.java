package com.qqspeed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqspeed.common.exception.BusinessException;
import com.qqspeed.data.entity.TrackMap;
import com.qqspeed.mapper.TrackMapMapper;
import com.qqspeed.service.TrackMapService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 地图服务实现类
 */
@Service
public class TrackMapServiceImpl extends ServiceImpl<TrackMapMapper, TrackMap> implements TrackMapService {

    /**
     * 分页查询（支持地图名称、难度星级、圈数、地图类型、状态查询）
     */
    @Override
    public IPage<TrackMap> pageQuery(Page<TrackMap> page, TrackMap trackMap, String sortDirection) {
        // 构造查询条件
        LambdaQueryWrapper<TrackMap> queryWrapper = new LambdaQueryWrapper<>();

        // 名称模糊查询
        if (StringUtils.isNotBlank(trackMap.getName())){
            queryWrapper.like(TrackMap::getName, trackMap.getName());
        }

        // 难度星级精确查询
        if (trackMap.getStar() != null){
            queryWrapper.eq(TrackMap::getStar, trackMap.getStar());
        }

        //圈数精确查询
        if (trackMap.getLaps() != null){
            queryWrapper.eq(TrackMap::getLaps, trackMap.getLaps());
        }

        //地图类型精确查询
        if (StringUtils.isNotBlank(trackMap.getType())){
            queryWrapper.eq(TrackMap::getType, trackMap.getType());
        }

        //状态（联赛图/非联赛图/怀旧图）精确查询
        if (StringUtils.isNotBlank(trackMap.getStatus())){
            queryWrapper.eq(TrackMap::getStatus, trackMap.getStatus());
        }

        // 先校验排序方向参数，避免非法值
        if ("desc".equalsIgnoreCase(sortDirection)) {
            // 降序：星级从大到小
            queryWrapper.orderByDesc(TrackMap::getStar);
        } else {
            // 升序（默认）：星级从小到大
            queryWrapper.orderByAsc(TrackMap::getStar);
        }

        return baseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 根据地图名称查询单条地图信息
     * @param name 地图名称
     * @return 地图详情
     */
    @Override
    public TrackMap getTrackMapByName(String name) {
        Long id = getIdByName(name);
        return baseMapper.selectById(id);
    }

    /**
     * 新增地图（后台管理）
     * @param trackMap 地图信息
     * @return 操作结果
     */
    @Override
    public boolean save(TrackMap trackMap) {
        // 校验名称重复（唯一索引兜底，业务层再校验）
        if (this.count(new LambdaQueryWrapper<TrackMap>().eq(TrackMap::getName, trackMap.getName())) > 0) {
            throw new BusinessException("地图名称【" + trackMap.getName() + "】已存在");
        }
        return super.save(trackMap);
    }

    /**
     * 修改地图（后台管理）
     * @param trackMap 地图信息
     * @return 操作结果
     */
    @Override
    public boolean updateByTrackMapName(TrackMap trackMap) {
        // 1. 查ID
        Long id = getIdByName(trackMap.getName());
        // 2. 删除对应ID的地图信息，然后新增更改后的地图信息
        this.removeById(id);
        return this.save(trackMap);
    }

    /**
     * 删除操作
     * @param name
     * @return 操作结果
     */
    @Override
    public boolean removeByTrackMapName(String name) {
        // 1. 查ID
        Long id = getIdByName(name);
        // 2. 用ID删除
        return this.removeById(id);
    }

    /**
     * 内部工具：通过名称查ID（前端无感知）
     */
    private Long getIdByName(String name) {
        LambdaQueryWrapper<TrackMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrackMap::getName, name)
                .select(TrackMap::getId); // 仅查ID，性能最优
        TrackMap trackMap = this.getOne(queryWrapper, false); // 不抛重复异常（已加唯一索引）
        if (trackMap == null) {
            throw new BusinessException("未找到名称为【" + name + "】的地图");
        }
        return trackMap.getId();
    }
}
