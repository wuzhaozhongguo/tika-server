package cn.nn200433.tika.entity;

import lombok.Data;

/**
 * 线程池配置
 *
 * @author song_jx
 * @date 2023-10-10 09:59:25
 */
@Data
public class ThreadProperties {
    /** 核心池大小 */
    private int    corePoolSize     = 5;
    /** 最大池大小 */
    private int    maxPoolSize      = 10;
    /** 队列容量 */
    private int    queueCapacity    = 20;
    /** 维持几秒钟 */
    private int    keepAliveSeconds = 60;
    /** 线程名 */
    private String name             = "tika";
}
