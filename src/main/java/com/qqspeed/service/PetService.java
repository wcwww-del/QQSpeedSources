package com.qqspeed.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qqspeed.data.entity.Pet;

/**
 * 宠物服务接口
 */
public interface PetService extends IService<Pet> {

    /**
     * 分页查询宠物（支持条件筛选）
     * @param page 分页参数
     * @param pet 筛选条件（名称、等级、状态）
     * @return 分页结果
     */
    IPage<Pet> pageQuery(Page<Pet> page, Pet pet, String sortDirection);

    /**
     * 根据宠物名称查询单条宠物信息
     * @param name 宠物名称
     * @return 宠物详情
     */
    Pet getPetByName(String name);

    /**
     * 新增宠物（后台管理）
     * @param pet 宠物信息
     * @return 操作结果
     */
    boolean save(Pet pet);

    /**
     * 修改宠物（后台管理）
     * @param pet 宠物信息
     * @return 操作结果
     */
    boolean updateByPetName(Pet pet);

    /**
     * 删除操作 （后台管理）
     * @param name
     * @return 操作结果
     */
    boolean removeByPetName(String name);
}
