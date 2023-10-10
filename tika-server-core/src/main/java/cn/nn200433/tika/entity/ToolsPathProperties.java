package cn.nn200433.tika.entity;

import lombok.Data;

/**
 * 第三方调用工具路径配置
 *
 * @author song_jx
 * @date 2023-10-10 09:59:25
 */
@Data
public class ToolsPathProperties {
    /** tesseract-ocr 可执行文件目录 */
    private String tessOcr;
    /** imageMagick 可执行文件目录 */
    private String imageMagick;
    /** ffmpeg 可执行文件目录 */
    private String ffmpeg;
}
