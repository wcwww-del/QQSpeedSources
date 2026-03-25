package com.qqspeed.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqspeed.common.exception.BusinessException;
import com.qqspeed.data.entity.Car;
import com.qqspeed.data.dto.CarDTO;
import com.qqspeed.mapper.CarMapper;
import com.qqspeed.service.CarService;
import com.qqspeed.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 赛车服务实现类
 */
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {

    @Autowired
    private RedisUtils redisUtils;

    // ====================== 前后台通用方法 ======================
    @Override
    public IPage<Car> pageQuery(Page<Car> page, Car car, String sortDirection) {
        // 构造查询条件
        LambdaQueryWrapper<Car> queryWrapper = new LambdaQueryWrapper<>();

        // 名称模糊查询
        if (StringUtils.isNotBlank(car.getName())) {
            queryWrapper.like(Car::getName, car.getName());
        }

        // 等级精确查询
        if(StringUtils.isNotBlank(car.getLevel())){
            queryWrapper.eq(Car::getLevel, car.getLevel());
        }

        //赛车类型精确查询
        if (StringUtils.isNotBlank(car.getType())){
            queryWrapper.eq(Car::getType, car.getType());
        }

        //适配模式精确查询
        if (StringUtils.isNotBlank(car.getAdaptMode())){
            queryWrapper.eq(Car::getAdaptMode, car.getAdaptMode());
        }

        // 上架状态精确查询
        if (car.getStatus() != null) {
            queryWrapper.eq(Car::getStatus, car.getStatus());
        }

        // 先校验排序方向参数，避免非法值
        if ("asc".equalsIgnoreCase(sortDirection)) {
            // 升序：上架时间从早到晚
            queryWrapper.orderByAsc(Car::getOnlineTime);
        } else {
            // 降序（默认）：上架时间从晚到早
            queryWrapper.orderByDesc(Car::getOnlineTime);
        }

        // 分页查询
        return baseMapper.selectPage(page, queryWrapper);
    }

    // ====================== 后台CURD方法 ======================
    @Override
    public Car getCarByName(String name) {
        Long id = getIdByName(name);
        return baseMapper.selectById(id);
    }

    @Override
    public boolean save(Car car) {
        // 校验名称重复（唯一索引兜底，业务层再校验）
        if (this.count(new LambdaQueryWrapper<Car>().eq(Car::getName, car.getName())) > 0) {
            throw new BusinessException("赛车名称【" + car.getName() + "】已存在");
        }
        return super.save(car);
    }

//    已更新新方法
//    /**
//     * 修改赛车（后台管理）
//     * @param car 赛车信息
//     * @return 操作结果
//     */
//    @Override
//    public boolean updateByCarName(Car car) {
//        // 1. 查ID
//        Long id = getIdByName(car.getName());
//        // 2. 删除对应ID的赛车信息，然后新增更改后的赛车信息
//        this.removeById(id);
//        return this.save(car);
//    }

//    已更新新方法
//    /**
//     * 删除操作
//     * @param name
//     * @return 操作结果
//     */
//    @Override
//    public boolean removeByCarName(String name) {
//        // 1. 查ID
//        Long id = getIdByName(name);
//        // 2. 用ID删除
//        return this.removeById(id);
//    }

    @Override
    public boolean updateByCarName(Car car) {
        // 1. 查ID
        Long id = getIdByName(car.getName());
        // 2. 删除对应ID的赛车信息，然后新增更改后的赛车信息
        this.removeById(id);
        boolean result = this.save(car);
        // 3. 修改后失效缓存
        String cacheKey = redisUtils.getCarDetailKey(car.getName());
        redisUtils.delete(cacheKey);
//        // 热门赛车缓存也失效（可选，或每天预热）
//        redisUtils.delete(redisUtils.getHotCarKey());
        return result;
    }

    @Override
    public boolean removeByCarName(String name) {
        // 1. 查ID
        Long id = getIdByName(name);
        // 2. 用ID删除
        boolean result = this.removeById(id);
        // 3. 删除后失效缓存
        String cacheKey = redisUtils.getCarDetailKey(name);
        redisUtils.delete(cacheKey);
//        redisUtils.delete(redisUtils.getHotCarKey());
        return result;
    }

    // ====================== 前台缓存方法 ======================
    @Override
    public CarDTO getCarDetailWithCache(String name) {
        // 1. 先查缓存
        String cacheKey = redisUtils.getCarDetailKey(name);
        CarDTO cacheVO = (CarDTO) redisUtils.get(cacheKey);
        if (cacheVO != null) {
            return cacheVO; // 缓存命中，直接返回
        }

        // 2. 缓存未命中，查库
        LambdaQueryWrapper<Car> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Car::getName, name);
        Car car = this.getOne(queryWrapper);
        if (car == null) {
            throw new BusinessException("未找到该赛车！");
        }

        // 3. 实体转VO
        CarDTO carDTO = new CarDTO();
        BeanUtils.copyProperties(car, carDTO);

        // 4. 存入缓存（30分钟过期）
        redisUtils.set(cacheKey, carDTO, 30, TimeUnit.MINUTES);

        return carDTO;
    }

//    @Override
//    public List<CarDTO> getHotCarsWithCache() {
//        // 1. 先查缓存
//        String cacheKey = redisUtils.getHotCarKey();
//        List<CarDTO> cacheVOList = (List<CarDTO>) redisUtils.get(cacheKey);
//        if (cacheVOList != null && !cacheVOList.isEmpty()) {
//            return cacheVOList;
//        }
//
//        // 2. 查库：按排序取TOP10上架赛车
//        LambdaQueryWrapper<Car> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Car::getStatus, 1)
//                .orderByDesc(Car::getOnlineTime)
//                .last("LIMIT 10");
//        List<Car> carList = this.list(queryWrapper);
//
//        // 3. 转VO
//        List<CarDTO> carDTOList = carList.stream().map(car -> {
//            CarDTO vo = new CarDTO();
//            BeanUtils.copyProperties(car, vo);
//            return vo;
//        }).collect(Collectors.toList());
//
//        // 4. 存入缓存（1小时过期）
//        redisUtils.set(cacheKey, carDTOList, 1, TimeUnit.HOURS);
//
//        return carDTOList;
//    }

    /**
     * 内部工具：通过名称查ID（前端无感知）
     */
    private Long getIdByName(String name) {
        LambdaQueryWrapper<Car> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Car::getName, name)
                .select(Car::getId); // 仅查ID，性能最优
        Car car = this.getOne(queryWrapper, false); // 不抛重复异常（已加唯一索引）
        if (car == null) {
            throw new BusinessException("未找到名称为【" + name + "】的赛车");
        }
        return car.getId();
    }
}