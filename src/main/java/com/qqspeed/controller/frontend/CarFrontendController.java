package com.qqspeed.controller.frontend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.entity.Car;
import com.qqspeed.data.dto.CarDTO;
import com.qqspeed.data.vo.CarPageVO;
import com.qqspeed.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 赛车前台接口（普通用户访问，无权限校验）
 */
@RestController
@RequestMapping("/frontend/car")
@Tag(name = "赛车前台接口", description = "普通用户浏览赛车信息的接口")
public class CarFrontendController {

    @Autowired
    private CarService carService;

    /**
     * 前台分页查询赛车（仅展示VO字段）
     */
    @GetMapping("/page")
    @Operation(summary = "前台分页查赛车", description = "支持名称、等级（T/A/等）、类型、适配模式、" +
                                                      "上架状态进行筛选，支持按上架时间顺序/倒序排序")
    public Result<IPage<CarPageVO>> pageQuery(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize, // 前台每页展示更多
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String adaptMode,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "desc") String sortDirection // 只有desc和asc两种方式
    ) {
        // 1. 构造查询条件
        Car carQuery = new Car();
        carQuery.setName(name);
        carQuery.setLevel(level);
        carQuery.setType(type);
        carQuery.setAdaptMode(adaptMode);
        carQuery.setStatus(status);

        // 2. 查库
        Page<Car> page = new Page<>(pageNum, pageSize);
        IPage<Car> carPage = carService.pageQuery(page, carQuery, sortDirection);

        // 3. 实体转VO
        IPage<CarPageVO> carPageVOIPage = carPage.convert(car -> {
            CarPageVO carPageVO = new CarPageVO();
            BeanUtils.copyProperties(car, carPageVO);
            return carPageVO;
        });

        return Result.success(carPageVOIPage);
    }

    /**
     * 当前台用户从页面点到某一具体赛车后，查询单个赛车详情（核心接口，必加缓存）
     */
    @GetMapping("/detail")
    @Operation(summary = "前台查赛车详情", description = "按名称查，返回完整展示信息")
    public Result<CarDTO> getCarDetail(@RequestParam String name) {
        // 核心逻辑：先查缓存，缓存没有再查库，查库后更新缓存
        CarDTO carDTO = carService.getCarDetailWithCache(name);
        return Result.success(carDTO);
    }

//    /**
//     * 前台查热门赛车（首页展示，缓存预热）
//     */
//    @GetMapping("/hot")
//    @Operation(summary = "前台查热门赛车", description = "首页展示的TOP10赛车，缓存1小时")
//    public Result<List<CarDTO>> getHotCars() {
//        List<CarDTO> hotCarDTOList = carService.getHotCarsWithCache();
//        return Result.success(hotCarDTOList);
//    }
}