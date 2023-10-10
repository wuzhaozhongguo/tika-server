package cn.nn200433.tika.config;

import cn.nn200433.tika.entity.ThreadProperties;
import cn.nn200433.tika.entity.ToolsPathProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * tika属性
 *
 * @author song_jx
 * @date 2023-08-26 12:53:49
 */
@Setter
@Getter
@ConfigurationProperties(prefix = TikaProperties.TIKA_PREFIX)
public class TikaProperties {

    public static final String TIKA_PREFIX = "tika";

    /** 允许的扩展名 */
    private String              allowExtName;
    /** 工具路径 */
    private ToolsPathProperties toolsPath;
    /** 线程池配置 */
    private ThreadProperties    thread;

}
