package com.qqspeed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qqspeed.data.entity.Fashion;
import com.qqspeed.data.dto.FashionDTO;

/**
 * 装备服务接口
 */
public interface FashionService extends IService<Fashion> {

    // ====================== 前后台通用方法 ======================
    /**
     * 分页查询装备（支持条件筛选，前后台通用）
     * @param page 分页参数
     * @param fashion 筛选条件（名称、类型、品质、状态）
     * @param sortDirection 排序方向
     * @return 返回值是每条数据的精简信息
     */
    IPage<Fashion> pageQuery(Page<Fashion> page, Fashion fashion, String sortDirection);

    // ====================== 后台CURD方法 ======================
    /**
     * 根据装备名称查询单条装备信息（后台管理）
     * @param name 装备名称
     * @return 装备全部信息详情，包含ID、创建时间、更新时间、操作人等
     */
    Fashion getFashionByName(String name);

    /**
     * 新增装备（后台）
     * @param fashion 装备信息
     * @return 操作结果
     */
    boolean save(Fashion fashion);

    /**
     * 修改装备信息（后台）
     * @param fashion 装备信息
     * @return 操作结果
     */
    boolean updateByFashionName(Fashion fashion);

    /**
     * 删除装备（后台）
     * @param name 装备名称
     * @return 操作结果
     */
    boolean removeByFashionName(String name);

    // ====================== 前台缓存方法 ======================

    /**
     * 前台查装备详情（带缓存）
     * 缓存策略：缓存30分钟，更新/删除时主动失效缓存
     * @param name 装备名称
     * @return 装备DTO信息
     */
    FashionDTO getFashionDetailWithCache(String name);

    /**
     * 前台查装备列表（按类型分组，带缓存）
     * 缓存策略：缓存1小时，更新时主动失效
     * @param type 装备类型
     * @return 装备列表
     */
    java.util.List<FashionDTO> getFashionListByTypeWithCache(String type);

    /**
     * 前台查热门装备（首页展示，带缓存）
     * 缓存策略：缓存1小时，每天凌晨预热
     * @param topCount 获取数量
     * @return 热门装备列表
     */
    java.util.List<FashionDTO> getHotFashionWithCache(int topCount);
}
