// TraceCodeServiceImpl.java
package com.gala.farmsearchbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gala.farmsearchbackend.annotation.CacheEvict;
import com.gala.farmsearchbackend.annotation.Cacheable;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import com.gala.farmsearchbackend.mapper.*;
import com.gala.farmsearchbackend.model.domain.*;
import com.gala.farmsearchbackend.model.dto.traceDto.TraceInfoDto;
import com.gala.farmsearchbackend.service.TraceCodeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class TraceCodeServiceImpl implements TraceCodeService {

    @Resource
    private RetaBatchMapper retaBatchMapper;

    @Resource
    private WholBatchMapper wholBatchMapper;

    @Resource
    private SlauBatchMapper slauBatchMapper;

    @Resource
    private FarmBatchMapper farmBatchMapper;

    @Resource
    private NodeInfoMapper nodeInfoMapper;

    @Resource
    private ProvinceMapper provinceMapper;

    @Resource
    private CityMapper cityMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    @CacheEvict(key = "'reta:detail:' + #rbId", allEntries = true)
    public String generateAndSaveTraceCode(Integer rbId) {
        RetaBatch retaBatch = retaBatchMapper.selectById(rbId);
        if (retaBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "零售商产品批号不存在");
        }

        // 生成溯源码
        String traceCode = generateStandardTraceCode(retaBatch);

        // 使用 UpdateWrapper 和 Lambda 表达式进行更新
        LambdaUpdateWrapper<RetaBatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(RetaBatch::getRbId, rbId) // 指定更新条件
                .set(RetaBatch::getSourceId, traceCode); // 设置要更新的字段

        int result = retaBatchMapper.update(null, updateWrapper);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "溯源码保存失败");
        }

        return traceCode;
    }

    @Override
    @Cacheable(key = "'trace:info:' + #sourceId", expire = 3600) // 缓存1小时
    public TraceInfoDto getTraceInfoBySourceId(String sourceId) {
        // 查询零售商产品批号信息
        LambdaQueryWrapper<RetaBatch> retaWrapper = new LambdaQueryWrapper<>();
        retaWrapper.eq(RetaBatch::getSourceId, sourceId);
        RetaBatch retaBatch = retaBatchMapper.selectOne(retaWrapper);

        if (retaBatch == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到对应的溯源信息");
        }

        TraceInfoDto traceInfo = new TraceInfoDto();

        // 设置零售商信息
        setRetailerInfo(traceInfo, retaBatch);

        // 设置批发商信息
        setWholesalerInfo(traceInfo, retaBatch.getWbId());

        // 设置屠宰企业信息
        setSlaughterhouseInfo(traceInfo, retaBatch.getWbId());

        // 设置养殖企业信息
        setFarmInfo(traceInfo, retaBatch.getWbId());

        return traceInfo;
    }

    private void setRetailerInfo(TraceInfoDto traceInfo, RetaBatch retaBatch) {
        NodeInfo retailer = nodeInfoMapper.selectById(retaBatch.getRetaId());
        if (retailer != null) {
            traceInfo.setRetaName(retailer.getName());
            traceInfo.setRetaProvince(getProvinceName(retailer.getProvId()));
            traceInfo.setRetaCity(getCityName(retailer.getCityId()));
            traceInfo.setRetaBatchId(retaBatch.getBatchId());
            traceInfo.setRetaType(retaBatch.getType());
            traceInfo.setRetaBatchDate(retaBatch.getBatchDate());
        }
    }

    private void setWholesalerInfo(TraceInfoDto traceInfo, Integer wbId) {
        WholBatch wholBatch = wholBatchMapper.selectById(wbId);
        if (wholBatch != null) {
            NodeInfo wholesaler = nodeInfoMapper.selectById(wholBatch.getWholId());
            if (wholesaler != null) {
                traceInfo.setWholName(wholesaler.getName());
                traceInfo.setWholProvince(getProvinceName(wholesaler.getProvId()));
                traceInfo.setWholCity(getCityName(wholesaler.getCityId()));
                traceInfo.setWholBatchId(wholBatch.getBatchId());
                traceInfo.setWholType(wholBatch.getType());
                traceInfo.setWholBatchDate(wholBatch.getBatchDate());
            }
        }
    }

    private void setSlaughterhouseInfo(TraceInfoDto traceInfo, Integer wbId) {
        WholBatch wholBatch = wholBatchMapper.selectById(wbId);
        if (wholBatch != null) {
            SlauBatch slauBatch = slauBatchMapper.selectById(wholBatch.getSbId());
            if (slauBatch != null) {
                NodeInfo slaughterhouse = nodeInfoMapper.selectById(slauBatch.getSlauId());
                if (slaughterhouse != null) {
                    traceInfo.setSlauName(slaughterhouse.getName());
                    traceInfo.setSlauProvince(getProvinceName(slaughterhouse.getProvId()));
                    traceInfo.setSlauCity(getCityName(slaughterhouse.getCityId()));
                    traceInfo.setSlauBatchId(slauBatch.getBatchId());
                    traceInfo.setSlauType(slauBatch.getType());
                    traceInfo.setSlauQuaId(slauBatch.getQuaId());
                    traceInfo.setSlauTestName(slauBatch.getTestName());
                    traceInfo.setSlauBatchDate(slauBatch.getBatchDate());
                }
            }
        }
    }

    private void setFarmInfo(TraceInfoDto traceInfo, Integer wbId) {
        WholBatch wholBatch = wholBatchMapper.selectById(wbId);
        if (wholBatch != null) {
            SlauBatch slauBatch = slauBatchMapper.selectById(wholBatch.getSbId());
            if (slauBatch != null) {
                FarmBatch farmBatch = farmBatchMapper.selectById(slauBatch.getFbId());
                if (farmBatch != null) {
                    NodeInfo farm = nodeInfoMapper.selectById(farmBatch.getFarmId());
                    if (farm != null) {
                        traceInfo.setFarmName(farm.getName());
                        traceInfo.setFarmProvince(getProvinceName(farm.getProvId()));
                        traceInfo.setFarmCity(getCityName(farm.getCityId()));
                        traceInfo.setFarmBatchId(farmBatch.getBatchId());
                        traceInfo.setFarmType(farmBatch.getType());
                        traceInfo.setFarmAgcId(farmBatch.getAgcId());
                        traceInfo.setFarmTestName(farmBatch.getTestName());
                        traceInfo.setFarmBatchDate(farmBatch.getBatchDate());
                    }
                }
            }
        }
    }

//    /**
//     * 将日期时间格式化为年月日格式（YYYY-MM-DD）
//     */
//    private String formatDateToYMD(LocalDateTime dateTime) {
//        if (dateTime == null) return "";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        return dateTime.format(formatter);
//    }


    private String getProvinceName(Integer provId) {
        if (provId == null) return "";
        Province province = provinceMapper.selectById(provId);
        return province != null ? province.getProvName() : "";
    }

    private String getCityName(Integer cityId) {
        if (cityId == null) return "";
        City city = cityMapper.selectById(cityId);
        return city != null ? city.getCityName() : "";
    }

    /**
     * 生成正规的肉类食品溯源码
     * 格式：TR[企业类型][企业ID4位][日期6位][时间4位][序列4位][校验1位]
     * 示例：TR4102308151234567
     * - TR: 追溯标识
     * - 4: 企业类型(4=零售商)
     * - 1023: 企业ID后4位
     * - 081512: 日期时间(月日时分)
     * - 3456: 随机序列
     * - 7: 校验位
     */
    private String generateStandardTraceCode(RetaBatch retaBatch) {
        LocalDateTime now = LocalDateTime.now();

        // 1. 基础信息部分
        String companyType = "4"; // 零售商类型
        String companyId = String.format("%04d", retaBatch.getRetaId() % 10000); // 企业ID后4位
        String dateTime = now.format(DateTimeFormatter.ofPattern("MMddHHmm")); // 月日时分

        // 2. 随机序列部分
        String randomSeq = generateRandomSequence(4);

        // 3. 组合基础码
        String baseCode = "TR" + companyType + companyId + dateTime + randomSeq;

        // 4. 计算校验位
        char checksum = calculateChecksum(baseCode);

        return baseCode + checksum;
    }

    /**
     * 生成数字随机序列
     */
    private String generateRandomSequence(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 计算校验位（Luhn算法改进版）
     */
    private char calculateChecksum(String code) {
        int sum = 0;
        for (int i = 0; i < code.length(); i++) {
            int digit = Character.digit(code.charAt(i), 36); // 支持数字和字母
            // 奇数位×2，偶数位×1
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit = digit - 9;
                }
            }
            sum += digit;
        }
        return (char) ('0' + ((10 - (sum % 10)) % 10));
    }

    /**
     * 验证溯源码格式和校验位
     */
    public boolean validateTraceCode(String traceCode) {
        if (traceCode == null || traceCode.length() != 18) {
            return false;
        }

        // 验证前缀
        if (!traceCode.startsWith("TR")) {
            return false;
        }

        // 验证企业类型
        char typeChar = traceCode.charAt(2);
        if (typeChar < '1' || typeChar > '4') {
            return false;
        }

        // 验证日期时间部分是否合理
        if (!isValidDateTime(traceCode.substring(3, 11))) {
            return false;
        }

        // 验证校验位
        String baseCode = traceCode.substring(0, traceCode.length() - 1);
        char expectedChecksum = calculateChecksum(baseCode);
        return traceCode.charAt(traceCode.length() - 1) == expectedChecksum;
    }

    /**
     * 验证日期时间格式
     */
    private boolean isValidDateTime(String dateTimeStr) {
        try {
            int month = Integer.parseInt(dateTimeStr.substring(0, 2));
            int day = Integer.parseInt(dateTimeStr.substring(2, 4));
            int hour = Integer.parseInt(dateTimeStr.substring(4, 6));
            int minute = Integer.parseInt(dateTimeStr.substring(6, 8));

            return month >= 1 && month <= 12 &&
                    day >= 1 && day <= 31 &&
                    hour >= 0 && hour <= 23 &&
                    minute >= 0 && minute <= 59;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}