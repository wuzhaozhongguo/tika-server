package cn.nn200433.tika.config;

import cn.hutool.core.util.StrUtil;
import cn.nn200433.tika.entity.ThreadProperties;
import cn.nn200433.tika.service.DefineParser;
import cn.nn200433.tika.service.impl.DefaultDefineParserImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * tika自动配置
 *
 * @author song_jx
 * @date 2023-10-09 11:22:04
 */
@EnableConfigurationProperties(value = TikaProperties.class)
public class TikaAutoConfig {

    /**
     * 默认定义解析器实现
     *
     * @param properties 配置
     * @return {@link DefineParser }
     * @author song_jx
     */
    @Bean
    @ConditionalOnMissingBean
    public DefineParser defaultDefineParser(TikaProperties properties) {
        return new DefaultDefineParserImpl(properties);
    }

    /**
     * 任务执行器
     *
     * @param properties 特性
     * @return {@link ThreadPoolTaskExecutor }
     * @author song_jx
     */
    @Bean("taskPool")
    public TaskExecutor taskExecutor(TikaProperties properties) {
        int              corePoolSize     = 5;
        int              maxPoolSize      = 10;
        int              queueCapacity    = 20;
        int              keepAliveSeconds = 60;
        String           threadName       = "tika";
        ThreadProperties thread           = properties.getThread();
        if (null != thread) {
            corePoolSize     = thread.getCorePoolSize();
            maxPoolSize      = thread.getMaxPoolSize();
            queueCapacity    = thread.getQueueCapacity();
            keepAliveSeconds = thread.getKeepAliveSeconds();
            threadName       = thread.getName();
        }
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 设置队列容量
        executor.setQueueCapacity(queueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 设置默认线程名称
        executor.setThreadNamePrefix(StrUtil.addSuffixIfNot(threadName, StrUtil.DASHED));
        // 设置拒绝策略
        // ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
        // ThreadPoolExecutor.DiscardPolicy(默认策略):也是丢弃任务，但是不抛出异常。
        // ThreadPoolExecutor.DiscardOldestPolicy:丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程)
        // ThreadPoolExecutor.CallerRunsPolicy:由调用线程处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

}
