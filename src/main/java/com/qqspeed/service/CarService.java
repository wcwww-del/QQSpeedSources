package com.qqspeed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qqspeed.data.entity.Car;
import com.qqspeed.data.dto.CarDTO;

/**
 * 赛车服务接口
 */
public interface CarService extends IService<Car> {

    // ====================== 前后台通用方法 ======================
    /**
     * 分页查询赛车（支持条件筛选，前后台通用）
     * @param page 分页参数
     * @param car 筛选条件（名称、等级、状态）
     * @return 返回值是每条数据的精简信息
     */
    IPage<Car> pageQuery(Page<Car> page, Car car, String sortDirection);

    // ====================== 后台CURD方法 ======================
    /**
     * 根据赛车名称查询单条赛车信息（后台管理）
     * @param name 赛车名称
     * @return 赛车全部信息详情，包含ID、创建时间、更新时间、操作人等
     */
    Car getCarByName(String name);

    /**
     * 新增赛车（后台）
     * @param car 赛车信息
     * @return 操作结果
     */
    boolean save(Car car);

//    已更新新方法
//    /**
//     * 修改赛车（后台管理）
//     * @param car 赛车信息
//     * @return 操作结果
//     */
//    boolean updateByCarName(Car car);

//    已更新新方法
//    /**
//     * 删除操作
//     * @param name
//     * @return 操作结果
//     */
//    boolean removeByCarName(String name);

     /**
     * 重写修改赛车信息（后台）：主动失效缓存（保证数据一致性）
     * 此方法需优化
     * @param car 赛车信息
     * @return 操作结果
     */
    boolean updateByCarName(Car car);

    /**
     * 重写删除赛车信息（后台）：主动失效缓存（保证数据一致性）
     * @param name 赛车信息
     * @return 操作结果
     */
    boolean removeByCarName(String name);

    // ====================== 前台缓存方法 ======================

    /**
     * 前台查赛车详情（带缓存）
     * 缓存策略：缓存30分钟，更新/删除时主动失效缓存
     */
    CarDTO getCarDetailWithCache(String name);

////这个没必要，后面其他服务或许会用到
//    /**
//     * 前台查热门赛车（首页TOP10，带缓存）
//     * 缓存策略：缓存1小时，每天凌晨预热
//     */
//    List<CarDTO> getHotCarsWithCache();


}