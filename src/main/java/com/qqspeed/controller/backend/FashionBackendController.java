package com.qqspeed.controller.backend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qqspeed.common.result.Result;
import com.qqspeed.data.entity.Fashion;
import com.qqspeed.data.vo.FashionPageVO;
import com.qqspeed.service.FashionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 装备接口（后台管理）
 */
@RestController
@RequestMapping("/backend/fashion")
@Tag(name = "装备管理", description = "装备的CURD接口")
public class FashionBackendController {

    @Autowired
    private FashionService fashionService;

    /**
     * 分页查询装备（后台）
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "后台管理员分页查装备", description = "支持名称、类型、品质、状态进行筛选，支持按ID升序/降序排序")
    public Result<IPage<FashionPageVO>> pageQuery(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String quality,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "desc") String sortDirection // 只有desc和asc两种方式
    ) {
        // 1. 构造查询条件
        Fashion fashionQuery = new Fashion();
        fashionQuery.setName(name);
        fashionQuery.setType(type);
        fashionQuery.setQuality(quality);

        // 2. 创建分页对象
        Page<Fashion> page = new Page<>(pageNum, pageSize);

        // 3. 调用服务层查询
        IPage<Fashion> fashionPage = fashionService.pageQuery(page, fashionQuery, sortDirection);

        // 4. 转换为VO
        IPage<FashionPageVO> voPage = fashionPage.convert(fashion -> {
            FashionPageVO vo = new FashionPageVO();
            BeanUtils.copyProperties(fashion, vo);
            return vo;
        });

        return Result.success(voPage);
    }

    /**
     * 根据名称查装备详情（后台）
     * @param name 装备名称
     * @return 装备详情
     */
    @GetMapping("/detail/{name}")
    @Operation(summary = "后台管理员查装备详情", description = "根据装备名称获取完整装备信息")
    public Result<Fashion> getDetail(
            @Parameter(description = "装备名称") @PathVariable String name
    ) {
        Fashion fashion = fashionService.getFashionByName(name);
        if (fashion == null) {
            return Result.error("装备不存在");
        }
        return Result.success(fashion);
    }

    /**
     * 新增装备（后台）
     * @param fashion 装备信息
     * @return 操作结果
     */
    @PostMapping("/save")
    @Operation(summary = "新增装备", description = "后台新增装备信息")
    public Result<Boolean> save(
            @Parameter(description = "装备信息") @RequestBody Fashion fashion
    ) {
        try {
            boolean result = fashionService.save(fashion);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改装备（后台）
     * @param fashion 装备信息
     * @return 操作结果
     */
    @PostMapping("/update")
    @Operation(summary = "修改装备", description = "后台修改装备信息")
    public Result<Boolean> update(
            @Parameter(description = "装备信息") @RequestBody Fashion fashion
    ) {
        try {
            boolean result = fashionService.updateByFashionName(fashion);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除装备（后台）
     * @param name 装备名称
     * @return 操作结果
     */
    @PostMapping("/delete/{name}")
    @Operation(summary = "删除装备", description = "根据装备名称删除装备信息")
    public Result<Boolean> delete(
            @Parameter(description = "装备名称") @PathVariable String name
    ) {
        try {
            boolean result = fashionService.removeByFashionName(name);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}