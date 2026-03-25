package com.qqspeed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqspeed.common.exception.BusinessException;
import com.qqspeed.data.dto.CarDTO;
import com.qqspeed.data.dto.PetDTO;
import com.qqspeed.data.entity.Car;
import com.qqspeed.data.entity.Pet;
import com.qqspeed.mapper.PetMapper;
import com.qqspeed.service.PetService;
import com.qqspeed.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 宠物服务实现类
 */
@Service
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements PetService {

    @Autowired
    private RedisUtils redisUtils;

    // ====================== 前后台通用方法 ======================
    @Override
    public IPage<Pet> pageQuery(Page<Pet> page, Pet pet, String sortDirection) {
        // 构造查询条件
        LambdaQueryWrapper<Pet> queryWrapper = new LambdaQueryWrapper<>();

        // 名称模糊查询
        if (StringUtils.isNotBlank(pet.getName())) {
            queryWrapper.like(Pet::getName, pet.getName());
        }

        //品级精确查询
        if (StringUtils.isNotBlank(pet.getGrade())) {
            queryWrapper.eq(Pet::getGrade, pet.getGrade());
        }

        //适配模式精确查询
        if (StringUtils.isNotBlank(pet.getAdaptMode())) {
            queryWrapper.eq(Pet::getAdaptMode, pet.getAdaptMode());
        }

        //获取方式精确查询
        if (StringUtils.isNotBlank(pet.getObtainWay())) {
            queryWrapper.eq(Pet::getObtainWay, pet.getObtainWay());
        }

        //上架状态精确查询
        if (pet.getStatus() != null) {
            queryWrapper.eq(Pet::getStatus, pet.getStatus());
        }

        // 先校验排序方向参数，避免非法值
        if ("asc".equalsIgnoreCase(sortDirection)) {
            // 升序：上架时间从早到晚
            queryWrapper.orderByAsc(Pet::getOnlineTime);
        } else {
            // 降序（默认）：上架时间从晚到早
            queryWrapper.orderByDesc(Pet::getOnlineTime);
        }

        return baseMapper.selectPage(page, queryWrapper);
    }

    // ====================== 后台CURD方法 ======================
    @Override
    public Pet getPetByName(String name) {
        Long id = getIdByName(name);
        return baseMapper.selectById(id);
    }

    @Override
    public boolean save(Pet pet) {
        // 校验名称重复（唯一索引兜底，业务层再校验）
        if (this.count(new LambdaQueryWrapper<Pet>().eq(Pet::getName, pet.getName())) > 0) {
            throw new BusinessException("宠物名称【" + pet.getName() + "】已存在");
        }
        return super.save(pet);
    }

//    已更新新方法
//    @Override
//    public boolean updateByPetName(Pet pet) {
//        // 1. 查ID
//        Long id = getIdByName(pet.getName());
//        // 2. 删除对应ID的宠物信息，然后新增更改后的宠物信息
//        this.removeById(id);
//        return this.save(pet);
//    }
//
//    @Override
//    public boolean removeByPetName(String name) {
//        // 1. 查ID
//        Long id = getIdByName(name);
//        // 2. 用ID删除
//        return this.removeById(id);
//    }

    @Override
    public boolean updateByPetName(Pet pet) {
        // 1. 查ID
        Long id = getIdByName(pet.getName());
        // 2. 删除对应ID的宠物信息，然后新增更改后的宠物信息
        this.removeById(id);
        boolean result = this.save(pet);
        // 3. 修改后失效缓存
        String cacheKey = redisUtils.getPetDetailKey(pet.getName());
        redisUtils.delete(cacheKey);
//        // 热门宠物缓存也失效（可选，或每天预热）
//        redisUtils.delete(redisUtils.getHotPetKey());
        return result;
    }

    @Override
    public boolean removeByPetName(String name) {
        // 1. 查ID
        Long id = getIdByName(name);
        // 2. 用ID删除
        boolean result = this.removeById(id);
        // 3. 删除后失效缓存
        String cacheKey = redisUtils.getPetDetailKey(name);
        redisUtils.delete(cacheKey);
//        redisUtils.delete(redisUtils.getHotPetKey());
        return result;
    }

    // ====================== 前台缓存方法 ======================
    @Override
    public PetDTO getPetDetailWithCache(String name) {
        // 1. 先查缓存
        String cacheKey = redisUtils.getPetDetailKey(name);
        PetDTO cacheVO = (PetDTO) redisUtils.get(cacheKey);
        if (cacheVO != null) {
            return cacheVO; // 缓存命中，直接返回
        }

        // 2. 缓存未命中，查库
        LambdaQueryWrapper<Pet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Pet::getName, name);
        Pet pet = this.getOne(queryWrapper);
        if (pet == null) {
            throw new BusinessException("未找到该宠物！");
        }

        // 3. 实体转VO
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);

        // 4. 存入缓存（30分钟过期）
        redisUtils.set(cacheKey, petDTO, 30, TimeUnit.MINUTES);

        return petDTO;
    }

    /**
     * 内部工具：通过名称查ID（前端无感知）
     */
    private Long getIdByName(String name) {
        LambdaQueryWrapper<Pet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Pet::getName, name)
                .select(Pet::getId); // 仅查ID，性能最优
        Pet pet = this.getOne(queryWrapper, false); // 不抛重复异常（已加唯一索引）
        if (pet == null) {
            throw new BusinessException("未找到名称为【" + name + "】的宠物");
        }
        return pet.getId();
    }
}