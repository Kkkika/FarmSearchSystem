package com.gala.farmsearchbackend.test;

import com.gala.farmsearchbackend.model.domain.City;
import com.gala.farmsearchbackend.model.domain.Province;
import com.gala.farmsearchbackend.service.CityService;
import com.gala.farmsearchbackend.service.FarmBatchService;
import com.gala.farmsearchbackend.service.ProvinceService;
import com.gala.farmsearchbackend.utils.CacheTestUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProvinceCityCacheTest {

    @Resource
    private FarmBatchService farmBatchService;

    @Resource
    private CacheTestUtil cacheTestUtil;
    private static final Logger log = LoggerFactory.getLogger(ProvinceCityCacheTest.class);
    @Resource
    private ProvinceService provinceService;

    @Resource
    private CityService cityService;


    @Test
    void testProvinceListCache() {
        log.info("=== 测试省份列表缓存 ===");

        // 清理缓存
        cacheTestUtil.clearTestCaches();

        // 第一次查询 - 应该访问数据库
        long startTime1 = System.currentTimeMillis();
        List<Province> provinces1 = provinceService.listProvinceAll();
        long duration1 = System.currentTimeMillis() - startTime1;
        log.info("第一次查询耗时: {}ms", duration1);

        // 验证缓存是否设置
        assertTrue(cacheTestUtil.isCached("province:list"), "省份列表应该被缓存");

        // 第二次查询 - 应该从缓存获取
        long startTime2 = System.currentTimeMillis();
        List<Province> provinces2 = provinceService.listProvinceAll();
        long duration2 = System.currentTimeMillis() - startTime2;
        log.info("第二次查询耗时: {}ms", duration2);

        // 验证结果一致
        assertEquals(provinces1.size(), provinces2.size(), "两次查询结果应该一致");

        // 验证缓存提升性能（第二次应该更快）
        assertTrue(duration2 < duration1, "缓存查询应该更快");

        log.info("省份列表缓存测试通过");
    }

    @Test
    void testCityListCache() {
        log.info("=== 测试城市列表缓存 ===");

        Integer testProvId = 1; // 使用存在的省份ID

        // 第一次查询
        long startTime1 = System.currentTimeMillis();
        List<City> cities1 = cityService.listCityByProvId(testProvId);
        long duration1 = System.currentTimeMillis() - startTime1;
        log.info("第一次城市查询耗时: {}ms", duration1);

        // 验证缓存
        assertTrue(cacheTestUtil.isCached("city:list:" + testProvId), "城市列表应该被缓存");

        // 第二次查询
        long startTime2 = System.currentTimeMillis();
        List<City> cities2 = cityService.listCityByProvId(testProvId);
        long duration2 = System.currentTimeMillis() - startTime2;
        log.info("第二次城市查询耗时: {}ms", duration2);

        assertEquals(cities1.size(), cities2.size());
        assertTrue(duration2 < duration1);

        log.info("城市列表缓存测试通过");
    }

    @Test
    void testCacheExpiration() throws InterruptedException {
        log.info("=== 测试缓存过期 ===");

        // 获取数据，设置缓存
        provinceService.listProvinceAll();
        assertTrue(cacheTestUtil.isCached("province:list"), "缓存应该存在");

        Long initialTtl = cacheTestUtil.getCacheTtl("province:list");
        log.info("缓存初始TTL: {}秒", initialTtl);

        // 等待一段时间（根据实际配置的过期时间调整）
        Thread.sleep(2000);

        Long remainingTtl = cacheTestUtil.getCacheTtl("province:list");
        log.info("缓存剩余TTL: {}秒", remainingTtl);

        assertTrue(remainingTtl < initialTtl, "缓存TTL应该减少");

        log.info("缓存过期测试通过");
    }
}
