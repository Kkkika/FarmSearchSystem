package com.gala.farmsearchbackend.utils;

import com.gala.farmsearchbackend.constants.BatchStateConstants;

/**
 * 产品批号状态工具类
 */
public class BatchStateUtils {

    /**
     * 检查状态是否需要显示进场批号
     * @param state 状态
     * @return 是否需要显示进场批号
     */
    public static boolean shouldShowEntryBatch(Integer state) {
        if (state == null) {
            return false;
        }
        return state == BatchStateConstants.Other.PENDING_CONFIRM ||
                state == BatchStateConstants.Other.CONFIRMED;
    }

    /**
     * 检查状态是否为有效状态（非下架状态）
     * @param state 状态
     * @return 是否为有效状态
     */
    public static boolean isValidState(Integer state) {
        if (state == null) {
            return true;
        }
        return state != BatchStateConstants.Farm.OFFLINE &&
                state != BatchStateConstants.Other.OFFLINE;
    }

    /**
     * 检查养殖企业状态是否有效
     * @param state 状态
     * @return 是否有效
     */
    public static boolean isValidFarmState(Integer state) {
        if (state == null) {
            return true;
        }
        return state == BatchStateConstants.Farm.PENDING ||
                state == BatchStateConstants.Farm.PUBLISHED;
    }

    /**
     * 检查其他企业状态是否有效
     * @param state 状态
     * @return 是否有效
     */
    public static boolean isValidOtherState(Integer state) {
        if (state == null) {
            return true;
        }
        return state == BatchStateConstants.Other.NEW ||
                state == BatchStateConstants.Other.PENDING_CONFIRM ||
                state == BatchStateConstants.Other.CONFIRMED;
    }
}