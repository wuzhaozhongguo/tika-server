package cn.nn200433.tika.config;

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

    /** tesseract-ocr 可执行文件目录 */
    private String tessOcrPath;
    /** imageMagick 可执行文件目录 */
    private String imageMagickPath;
    /** ffmpeg 可执行文件目录 */
    private String ffmpegPath;
    /** 允许的扩展名 */
    private String allowExtName;

}
