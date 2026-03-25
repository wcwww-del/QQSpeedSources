package com.qqspeed.controller.backend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.entity.Pet;
import com.qqspeed.data.dto.PetDTO;
import com.qqspeed.data.vo.CarPageVO;
import com.qqspeed.data.vo.PetPageVO;
import com.qqspeed.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 宠物接口（后台管理）
 */
@RestController
@RequestMapping("/backend/pet")
@Tag(name = "宠物管理", description = "宠物的CURD接口")
public class PetBackendController {

    @Autowired
    private PetService petService;

    /**
     * 分页查询宠物（后台）
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果
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
     * 根据宠物名称查询单条宠物信息
     * @param name 宠物名称
     * @return 宠物详情
     */
    @GetMapping("/detail")
    @Operation(summary = "根据宠物名称查询宠物", description = "按名称查，返回完整展示信息")
    public Result<Pet> getByName(@Parameter(description = "宠物名称") String name) {
        Pet pet = petService.getPetByName(name);
        return Result.success(pet);
    }

    /**
     * 新增宠物（后台管理）
     * @param petDTO 宠物信息
     * @return 操作结果
     */
    @PostMapping("/add")
    @Operation(summary = "新增宠物", description = "后台管理员新增宠物信息")
    public Result<?> add(@Parameter(description = "宠物信息") @RequestBody PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        boolean save = petService.save(pet);
        return save ? Result.success() : Result.error("新增宠物失败");
    }

    /**
     * 修改宠物（后台管理）
     * @param petDTO 宠物信息
     * @return 操作结果
     */
    @PutMapping("/update")
    @Operation(summary = "修改宠物", description = "后台管理员修改宠物信息")
    public Result<?> update(@Parameter(description = "宠物信息") @RequestBody PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        boolean update = petService.updateByPetName(pet);
        return update ? Result.success() : Result.error("修改宠物信息失败");
    }

    /**
     * 删除操作（后台管理）
     * @param name
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除宠物", description = "后台管理员删除宠物信息")
    public Result<?> delete(@Parameter(description = "宠物名称") String name) {
        boolean remove = petService.removeByPetName(name);
        return remove ? Result.success() : Result.error("删除宠物失败");
    }
}
