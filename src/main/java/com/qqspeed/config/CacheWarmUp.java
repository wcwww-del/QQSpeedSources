//package com.qqspeed.config;
//
//import com.qqspeed.service.CarService;
//import com.qqspeed.service.PetService;
//import com.qqspeed.service.TrackMapService;
//import com.qqspeed.service.TrackRecordService;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 缓存预热（项目启动时加载热门数据到Redis）
// */
//@Component
//public class CacheWarmUp {
//
//    @Autowired
//    private CarService carService;
//
//    @Autowired
//    private PetService petService;
//
//    @Autowired
//    private TrackMapService trackMapService;
//
//    @Autowired
//    private TrackRecordService trackRecordService;
//
//    /**
//     * 项目启动后执行
//     */
//    @PostConstruct
//    public void warmUp() {
//        // 预热热门赛车缓存
//        carService.getHotCarsWithCache();
//        System.out.println("===== 热门赛车缓存预热完成 =====");
//
//        // 预热热门宠物缓存
//        petService.getHotPetsWithCache();
//        System.out.println("===== 热门宠物缓存预热完成 =====");
//
//        // 预热热门地图缓存
//        trackMapService.getHotTrackMapsWithCache();
//        System.out.println("===== 热门地图缓存预热完成 =====");
//
//        // 预热热门国服记录缓存
//        trackRecordService.getHotTrackRecordsWithCache();
//        System.out.println("===== 热门国服缓存预热完成 =====");
//
//    }
//}