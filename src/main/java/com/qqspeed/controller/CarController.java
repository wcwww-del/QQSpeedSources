package com.qqspeed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.entity.Car;
import com.qqspeed.data.dto.CarDTO;
import com.qqspeed.data.vo.CarPageVO;
import com.qqspeed.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 赛车接口（后台管理+前台查询）
 */
@RestController
@RequestMapping("/car")
@Tag(name = "赛车管理", description = "赛车的CURD接口")
public class CarController {

    @Autowired
    private CarService carService;

    /**
     * 分页查询赛车（后台）
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果
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
     * 后台查询单条赛车信息
     * @param name
     * @return 赛车详情
     */
    @GetMapping("/detail")
    @Operation(summary = "前台查赛车详情", description = "按名称查，返回完整展示信息")
    public Result<Car> getCarDetail(@RequestParam String name) {
        Car car = carService.getCarByName(name);
        return Result.success(car);
    }

    /**
     * 新增赛车（后台）
     * @param carDTO 赛车信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "新增赛车", description = "后台管理员新增赛车信息")
    public Result<?> add(@Parameter(description = "赛车信息") @RequestBody CarDTO carDTO) {
        Car car = new Car();
        BeanUtils.copyProperties(carDTO, car);
        boolean save = carService.save(car);
        return save ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改赛车（后台）
     * @param carDTO 赛车信息
     * @return 操作结果
     */
    @PutMapping
    @Operation(summary = "修改赛车", description = "后台管理员修改赛车信息")
    public Result<?> update(@Parameter(description = "赛车信息") @RequestBody CarDTO carDTO) {
        Car car = new Car();
        BeanUtils.copyProperties(carDTO, car);
        boolean update = carService.updateByCarName(car);
        return update ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除赛车（后台）
     * @param name
     * @return 操作结果
     */
    @DeleteMapping
    @Operation(summary = "删除赛车", description = "后台管理员删除赛车信息")
    public Result<?> delete(@RequestParam String name) {
        boolean remove = carService.removeByCarName(name);
        return remove ? Result.success() : Result.error("删除失败");
    }

//    /**
//     * 上下架赛车（后台管理）
//     * @param id 主键
//     * @param status 状态 0下架 1上架
//     * @return 操作结果
//     */
//    @PutMapping("/status/{id}/{status}")
//    @Operation(summary = "上下架赛车", description = "后台管理员控制赛车是否显示")
//    public Result<?> updateStatus(
//            @Parameter(description = "赛车ID") @PathVariable Long id,
//            @Parameter(description = "状态 0下架 1上架") @PathVariable Integer status
//    ) {
//        Car car = new Car();
//        car.setId(id);
//        car.setStatus(status);
//        boolean update = carService.updateById(car);
//        return update ? Result.success() : Result.error("状态修改失败");
//    }
}