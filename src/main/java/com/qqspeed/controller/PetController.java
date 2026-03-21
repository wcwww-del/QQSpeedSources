package com.qqspeed.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.entity.Pet;
import com.qqspeed.data.dto.petDTO.PetDTO;
import com.qqspeed.data.dto.petDTO.PetQuaryOrDeleteDTO;
import com.qqspeed.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pet")
@Tag(name = "宠物管理", description = "宠物的CURD接口")
public class PetController {

    @Autowired
    private PetService petService;

    /**
     * 分页查询宠物（后台/前台通用）
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param pet 筛选条件
     * @return 分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询宠物", description = "支持名称、品级、适配模式、获取方式、状态筛选")
    public Result<IPage<Pet>> pageQuery(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "desc") String sortDirection,
            @Parameter(description = "筛选条件") Pet pet
    ) {
        Page<Pet> page = new Page<>(pageNum, pageSize);
        IPage<Pet> petPage = petService.pageQuery(page, pet, sortDirection);
        return Result.success(petPage);
    }

    /**
     * 根据宠物名称查询单条宠物信息
     * @param petQuaryOrDeleteDTO 宠物名称
     * @return 宠物详情
     */
    @GetMapping("/information")
    @Operation(summary = "根据宠物名称查询宠物", description = "前台详情页/后台编辑用")
    public Result<Pet> getByName(@Parameter(description = "宠物名称") @RequestBody PetQuaryOrDeleteDTO petQuaryOrDeleteDTO) {
        Pet pet = petService.getPetByName(petQuaryOrDeleteDTO.getName());
        return Result.success(pet);
    }

    /**
     * 新增宠物（后台管理）
     * @param petDTO 宠物信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "新增宠物", description = "后台管理员新增宠物信息")
    public Result<?> add(@Parameter(description = "宠物信息") @RequestBody PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        boolean save = petService.save(pet);
        return save ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改宠物（后台管理）
     * @param petDTO 宠物信息
     * @return 操作结果
     */
    @PutMapping
    @Operation(summary = "修改宠物", description = "后台管理员修改宠物信息")
    public Result<?> update(@Parameter(description = "宠物信息") @RequestBody PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        boolean update = petService.updateByPetName(pet);
        return update ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除操作
     * @param petQuaryOrDeleteDTO
     * @return 操作结果
     */
    @DeleteMapping
    @Operation(summary = "删除宠物", description = "后台管理员删除宠物信息")
    public Result<?> delete(@Parameter(description = "宠物名称") @RequestBody PetQuaryOrDeleteDTO petQuaryOrDeleteDTO) {
        boolean remove = petService.removeByPetName(petQuaryOrDeleteDTO.getName());
        return remove ? Result.success() : Result.error("删除失败");
    }
}
