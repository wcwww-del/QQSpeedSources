package com.qqspeed;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * 基本功能测试
 */
@SpringBootTest
public class BasicTest {

    @Test
    public void contextLoads(ApplicationContext context) {
        // 测试Spring上下文是否能正常加载
        System.out.println("Spring上下文加载成功！");
        System.out.println("Bean数量: " + context.getBeanDefinitionCount());

        // 打印一些重要的Bean
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            if (beanName.contains("Controller") || beanName.contains("Service") || beanName.contains("Config")) {
                System.out.println("Bean: " + beanName);
            }
        }
    }
}