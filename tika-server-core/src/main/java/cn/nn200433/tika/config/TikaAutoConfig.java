package cn.nn200433.tika.config;

import cn.nn200433.tika.service.DefineParser;
import cn.nn200433.tika.service.impl.DefaultDefineParserImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

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

}
