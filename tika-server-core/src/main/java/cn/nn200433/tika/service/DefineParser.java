package cn.nn200433.tika.service;

import org.apache.tika.parser.AutoDetectParser;

/**
 * 定义解析器
 *
 * @author song_jx
 * @date 2023-10-09 11:12:12
 */
public interface DefineParser {

    /**
     * 构建
     *
     * @return {@link AutoDetectParser }
     * @author song_jx
     */
    public AutoDetectParser build();

}
