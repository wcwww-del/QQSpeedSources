package com.qqspeed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqspeed.common.exception.BusinessException;
import com.qqspeed.data.entity.TrackRecord;
import com.qqspeed.mapper.TrackRecordMapper;
import com.qqspeed.service.TrackRecordService;
import org.springframework.stereotype.Service;

/**
 * 国服记录服务实现类
 */
@Service
public class TrackRecordServiceImpl extends ServiceImpl<TrackRecordMapper, TrackRecord> implements TrackRecordService {

    /**
     * 分页查询（支持地图名称、T/A、车手、国服记录、宠物、ECU、创造纪录时间查询）
     */
    @Override
    public IPage<TrackRecord> pageQuery(Page<TrackRecord> page, TrackRecord trackRecord, String sortDirection) {

        // 构造查询条件
        LambdaQueryWrapper<TrackRecord> queryWrapper = new LambdaQueryWrapper<>();

        // 地图名称模糊查询
        if (StringUtils.isNotBlank(trackRecord.getTrackMap())) {
            queryWrapper.like(TrackRecord::getTrackMap, trackRecord.getTrackMap());
        }

        // T车车手模糊查询
        if (StringUtils.isNotBlank(trackRecord.getPlayerNameT())){
            queryWrapper.like(TrackRecord::getPlayerNameT, trackRecord.getPlayerNameT());
        }

        // T车赛车名称模糊查询
        if (StringUtils.isNotBlank(trackRecord.getCarNameT())){
            queryWrapper.like(TrackRecord::getCarNameT, trackRecord.getCarNameT());
        }

        // T车宠物模糊查询
        if (StringUtils.isNotBlank(trackRecord.getPetNameT())){
            queryWrapper.like(TrackRecord::getPetNameT, trackRecord.getPetNameT());
        }

        // T车ECU模糊查询
        if (StringUtils.isNotBlank(trackRecord.getECUNameT())){
            queryWrapper.like(TrackRecord::getECUNameT, trackRecord.getECUNameT());
        }

        // T车创造纪录时间精确查询
        if (trackRecord.getRecordTimeDateT() != null){
            queryWrapper.eq(TrackRecord::getRecordTimeDateT, trackRecord.getRecordTimeDateT());
        }

        // A车车手模糊查询
        if (StringUtils.isNotBlank(trackRecord.getPlayerNameA())){
            queryWrapper.like(TrackRecord::getPlayerNameA, trackRecord.getPlayerNameA());
        }

        // A车赛车名称模糊查询
        if (StringUtils.isNotBlank(trackRecord.getCarNameA())){
            queryWrapper.like(TrackRecord::getCarNameA, trackRecord.getCarNameA());
        }

        // A车宠物模糊查询
        if (StringUtils.isNotBlank(trackRecord.getPetNameA())){
            queryWrapper.like(TrackRecord::getPetNameA, trackRecord.getPetNameA());
        }

        // A车ECU模糊查询
        if (StringUtils.isNotBlank(trackRecord.getECUNameA())){
            queryWrapper.like(TrackRecord::getECUNameA, trackRecord.getECUNameA());
        }

        // A车创造纪录时间精确查询
        if (trackRecord.getRecordTimeDateA() != null){
            queryWrapper.eq(TrackRecord::getRecordTimeDateA, trackRecord.getRecordTimeDateA());
        }

        // 先校验排序方向参数，避免非法值
        if ("desc".equalsIgnoreCase(sortDirection)) {
            // 降序：根据创造纪录时间从晚到早
            queryWrapper.orderByDesc(TrackRecord::getRecordTimeDateT);
        } else {
            // 升序（默认）：根据创造纪录时间从早到晚
            queryWrapper.orderByAsc(TrackRecord::getRecordTimeDateT);
        }

        return baseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 根据地图名称查询单条国服记录信息
     * @param trackMap 国服记录名称
     * @return 国服记录详情
     */
    @Override
    public TrackRecord getTrackRecordByTrackMap(String trackMap) {
        Long id = getIdByTrackMap(trackMap);
        return baseMapper.selectById(id);
    }

    /**
     * 新增国服记录（后台管理）
     * @param trackRecord 国服记录信息
     * @return 操作结果
     */
    @Override
    public boolean save(TrackRecord trackRecord) {
        // 校验地图名称重复（唯一索引兜底，业务层再校验）
        if (this.count(new LambdaQueryWrapper<TrackRecord>().eq(TrackRecord::getTrackMap, trackRecord.getTrackMap())) > 0) {
            throw new BusinessException("【" + trackRecord.getTrackMap() + "】的国服记录已存在");
        }
        return super.save(trackRecord);
    }

    /**
     * 修改国服记录（后台管理）
     * @param trackRecord 国服记录信息
     * @return 操作结果
     */
    @Override
    public boolean updateByTrackMap(TrackRecord trackRecord) {
        // 1. 查ID
        Long id = getIdByTrackMap(trackRecord.getTrackMap());
        // 2. 删除对应ID的国服记录信息，然后新增更改后的国服记录信息
        this.removeById(id);
        return this.save(trackRecord);
    }

    /**
     * 删除操作
     * @param trackMap
     * @return 操作结果
     */
    @Override
    public boolean removeByTrackMap(String trackMap) {
        // 1. 查ID
        Long id = getIdByTrackMap(trackMap);
        // 2. 用ID删除
        return this.removeById(id);
    }

    /**
     * 内部工具：通过名称查ID（前端无感知）
     */
    private Long getIdByTrackMap(String trackMap) {
        LambdaQueryWrapper<TrackRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrackRecord::getTrackMap, trackMap)
                .select(TrackRecord::getId); // 仅查ID，性能最优
        TrackRecord trackRecord = this.getOne(queryWrapper, false); // 不抛重复异常（已加唯一索引）
        if (trackRecord == null) {
            throw new BusinessException("未找到【" + trackMap + "】的国服记录");
        }
        return trackRecord.getId();
    }
}
