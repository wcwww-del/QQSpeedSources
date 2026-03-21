package com.qqspeed.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 字段自动填充处理器（统一处理创建/更新时间）
 */
@Component // 必须交给Spring管理，否则不生效
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增操作时填充字段
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 填充createTime：新增时为当前时间
        this.strictInsertFill(
                metaObject,
                "createTime", // 实体类中的字段名（驼峰）
                LocalDateTime.class,
                LocalDateTime.now() // 填充值：当前时间
        );
        // 填充updateTime：新增时和createTime一致
        this.strictInsertFill(
                metaObject,
                "updateTime",
                LocalDateTime.class,
                LocalDateTime.now()
        );
    }

    /**
     * 修改操作时填充字段
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充updateTime：修改时为当前时间
        this.strictUpdateFill(
                metaObject,
                "updateTime",
                LocalDateTime.class,
                LocalDateTime.now()
        );
    }
}