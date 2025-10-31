package com.gala.farmsearchbackend.constants;

/**
 * 产品批号状态常量类
 */
public class BatchStateConstants {

    // 养殖企业状态
    public static class Farm {
        public static final int PENDING = 1;     // 待发布
        public static final int PUBLISHED = 2;   // 已发布
        public static final int OFFLINE = 3;     // 已下架
    }

    // 屠宰企业、批发商、零售商状态
    public static class Other {
        public static final int NEW = 1;         // 新建
        public static final int PENDING_CONFIRM = 2; // 待确认
        public static final int CONFIRMED = 3;   // 已确认
        public static final int OFFLINE = 4;     // 已下架
    }
}