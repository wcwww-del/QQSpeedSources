package com.qqspeed.controller.frontend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.dto.PetDTO;
import com.qqspeed.data.entity.Pet;
import com.qqspeed.data.vo.PetPageVO;
import com.qqspeed.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 宠物前台接口（普通用户访问，无权限校验）
 */
@RestController
@RequestMapping("/frontend/pet")
@Tag(name = "宠物前台接口", description = "普通用户浏览宠物信息的接口")
public class PetFrontendController {

    @Autowired
    private PetService petService;

    /**
     * 前台分页查询宠物（仅展示VO字段）
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询宠物", description = "支持名称、品级、适配模式、获取方式、状态筛选，支持按上架时间顺序/倒序排序")
    public Result<IPage<PetPageVO>> pageQuery(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String adaptMode,
            @RequestParam(required = false) String obtainWay,
            @RequestParam(required = false) Integer status,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        // 1. 构造查询条件
        Pet petQuery = new Pet();
        petQuery.setName(name);
        petQuery.setGrade(grade);
        petQuery.setAdaptMode(adaptMode);
        petQuery.setObtainWay(obtainWay);
        petQuery.setStatus(status);

        // 2. 查库
        Page<Pet> page = new Page<>(pageNum, pageSize);
        IPage<Pet> petIPage = petService.pageQuery(page, petQuery, sortDirection);

        // 3. 实体转VO
        IPage<PetPageVO> petPageVOIPage = petIPage.convert(pet -> {
            PetPageVO petPageVO = new PetPageVO();
            BeanUtils.copyProperties(pet, petPageVO);
            return petPageVO;
        });

        return Result.success(petPageVOIPage);
    }

    /**
     * 当前台用户从页面点到某一具体宠物后，查询单个宠物详情（核心接口，必加缓存）
     */
    @GetMapping("/detail")
    @Operation(summary = "前台查宠物详情", description = "按名称查，返回完整展示信息")
    public Result<PetDTO> getPetDetail(@RequestParam String name) {
        // 核心逻辑：先查缓存，缓存没有再查库，查库后更新缓存
        PetDTO petDTO = petService.getPetDetailWithCache(name);
        return Result.success(petDTO);
    }
}