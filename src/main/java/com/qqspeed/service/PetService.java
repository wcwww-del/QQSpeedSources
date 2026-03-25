package com.qqspeed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qqspeed.data.dto.CarDTO;
import com.qqspeed.data.dto.PetDTO;
import com.qqspeed.data.entity.Car;
import com.qqspeed.data.entity.Pet;

/**
 * 宠物服务接口
 */
public interface PetService extends IService<Pet> {

    // ====================== 前后台通用方法 ======================
    /**
     * 分页查询宠物（支持条件筛选）
     * @param page 分页参数
     * @param pet 筛选条件（名称、等级、状态）
     * @return 返回值是每条数据的精简信息
     */
    IPage<Pet> pageQuery(Page<Pet> page, Pet pet, String sortDirection);

    // ====================== 后台CURD方法 ======================
    /**
     * 根据宠物名称查询单条宠物信息（后台管理）
     * @param name 宠物名称
     * @return 宠物全部信息详情，包含ID、创建时间、更新时间、操作人等
     */
    Pet getPetByName(String name);

    /**
     * 新增宠物（后台管理）
     * @param pet 宠物信息
     * @return 操作结果
     */
    boolean save(Pet pet);

//    已更新新方法
//    /**
//     * 修改宠物（后台管理）
//     * @param pet 宠物信息
//     * @return 操作结果
//     */
//    boolean updateByPetName(Pet pet);

//    已更新新方法
//    /**
//     * 删除操作 （后台管理）
//     * @param name
//     * @return 操作结果
//     */
//    boolean removeByPetName(String name);

    /**
     * 重写修改宠物信息（后台管理）：主动失效缓存（保证数据一致性）
     * 此方法需优化
     * @param pet 宠物信息
     * @return 操作结果
     */
    boolean updateByPetName(Pet pet);

    /**
     * 重写删除宠物信息（后台管理）：主动失效缓存（保证数据一致性）
     * @param name 宠物信息
     * @return 操作结果
     */
    boolean removeByPetName(String name);

    // ====================== 前台缓存方法 ======================
    /**
     * 前台查宠物详情（带缓存）
     * 缓存策略：缓存30分钟，更新/删除时主动失效缓存
     */
    PetDTO getPetDetailWithCache(String name);
}
