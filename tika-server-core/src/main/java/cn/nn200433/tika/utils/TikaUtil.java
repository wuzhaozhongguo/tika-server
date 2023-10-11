package cn.nn200433.tika.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import cn.nn200433.tika.service.DefineParser;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.j256.simplemagic.ContentInfoUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.langdetect.tika.LanguageIdentifier;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.ocr.TesseractOCRParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * tika 工具类
 *
 * @author song_jx
 * @date 2023-10-09 10:59:34
 */
@Slf4j
public class TikaUtil {

    private static final String                TIKA_KEY               = "tika.key";
    private static final String                TIKA_PARSER_KEY        = "tika.parser.key";
    private static final String                TIKA_LOADED_PARSER_KEY = "tika.parser.loaded.key";
    private static final Cache<String, Object> CACHE_MAP              = Caffeine.newBuilder()
            .initialCapacity(5)
            .maximumSize(10)
            .expireAfterWrite(Integer.MAX_VALUE, TimeUnit.DAYS)
            .build();

    /**
     * 解析文件
     *
     * @param filePath 文件路径
     * @return {@link String }
     * @author song_jx
     */
    public static FileInfo parseFile(String filePath) {
        return parseFile(new File(filePath));
    }

    /**
     * 解析文件
     *
     * @param filePath 文件路径
     * @return {@link String }
     * @author song_jx
     */
    public static FileInfo parseFile(File filePath) {
        FileInfo fileInfo = new FileInfo();
        try {
            // final byte[] bytes = FileUtil.readBytes(filePath);
            fileInfo = parseStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            log.error("---> 文档信息抽取异常", e);
        }
        return fileInfo;
    }

    /**
     * 解析文件地址
     *
     * @param url 文件地址
     * @return {@link String }
     * @author song_jx
     */
    public static FileInfo parseUrl(String url) {
        FileInfo fileInfo = new FileInfo();
        try {
            final byte[] fileBytes = HttpUtil.downloadBytes(url);
            fileInfo = parseStream(IoUtil.toStream(fileBytes));
        } catch (Exception e) {
            log.error("---> 文档信息抽取异常", e);
        }
        return fileInfo;
    }

    /**
     * 解析文件流
     *
     * @param stream 数据流
     * @return {@link FileInfo }
     * @author song_jx
     */
    public static FileInfo parseStream(InputStream stream) {
        return parseStream(stream, null, Boolean.TRUE);
    }

    /**
     * 解析文件流
     *
     * @param stream       数据流
     * @param parseOptions 解析选项
     * @param clean        是否清理无效字符
     * @return {@link FileInfo }
     * @author song_jx
     */
    public static FileInfo parseStream(InputStream stream, Map<Class, Object> parseOptions, Boolean clean) {
        return parseBytes(IoUtil.readBytes(stream), parseOptions, clean);
    }

    /**
     * 解析文件流
     *
     * @param bytes        字节数据
     * @param parseOptions 解析选项
     * @param clean        是否清理无效字符
     * @return {@link FileInfo }
     * @author song_jx
     */
    public static FileInfo parseBytes(byte[] bytes, Map<Class, Object> parseOptions, Boolean clean) {
        String                 parseResult      = StrUtil.EMPTY;
        String                 mediaType        = null;
        String                 language         = StrUtil.EMPTY;
        final AutoDetectParser autoDetectParser = getParser();
        BodyContentHandler     handler          = new BodyContentHandler(Integer.MAX_VALUE);
        Metadata               metadata         = new Metadata();
        ParseContext           parseContext     = new ParseContext();
        fillConfig(parseContext, parseOptions);
        try (InputStream is = new BufferedInputStream(new ByteArrayInputStream(bytes))) {
            autoDetectParser.parse(is, handler, metadata, parseContext);
            final String content = handler.toString();
            parseResult = clean ? StrUtil.cleanBlank(content) : content;
            language    = new LanguageIdentifier(parseResult).getLanguage();
            mediaType   = new ContentInfoUtil().findMatch(bytes).getMimeType();
        } catch (Exception e) {
            log.error("---> 文档信息抽取异常", e);
        }
        return new FileInfo(mediaType, language, parseResult);
    }

    /**
     * 初始化解析器
     *
     * @author song_jx
     */
    public static void initParser() {
        final AutoDetectParser parser = SpringUtil.getBean(DefineParser.class).build();
        final Set<String> loadedClassSet = parser.getParsers()
                .values()
                .stream()
                .map(c -> c.getClass().getName())
                .collect(Collectors.toSet());
        CACHE_MAP.put(TIKA_PARSER_KEY, parser);
        CACHE_MAP.put(TIKA_LOADED_PARSER_KEY, loadedClassSet);
        // CACHE_MAP.put(TIKA_KEY, new Tika());
    }

    /**
     * 获取解析器
     *
     * @return {@link AutoDetectParser }
     * @author song_jx
     */
    public static Tika getTika() {
        return (Tika) CACHE_MAP.get(TIKA_KEY, k -> new Tika());
    }

    /**
     * 获取解析器
     *
     * @return {@link AutoDetectParser }
     * @author song_jx
     */
    public static AutoDetectParser getParser() {
        return (AutoDetectParser) CACHE_MAP.get(TIKA_PARSER_KEY, k -> SpringUtil.getBean(DefineParser.class).build());
    }

    /**
     * 填充配置
     * <p>
     * 解析选项可覆盖内置选项
     * </p>
     *
     * @param parseContext 解析上下文
     * @param parseOptions 解析选项
     * @author song_jx
     */
    private static void fillConfig(ParseContext parseContext, Map<Class, Object> parseOptions) {
        final Set<String> loadedClassSet = (Set<String>) CACHE_MAP.get(TIKA_LOADED_PARSER_KEY, k -> getParser().getParsers()
                .values()
                .stream()
                .map(c -> c.getClass().getName())
                .collect(Collectors.toSet()));
        // 如果存在ocr解析，添加一些参数
        if (loadedClassSet.contains(TesseractOCRParser.class.getName())) {
            TesseractOCRConfig ocrConfig = new TesseractOCRConfig();
            ocrConfig.setEnableImagePreprocessing(Boolean.TRUE);
            // chi_sim.traineddata（简体，仅对宋体而言，像素 >= 300 dpi：识别率高达100%，同时对英文及阿拉伯数字识别率高达90%以上）
            // chi_sim_vert.traineddata（简体，竖排）
            // chi_tra.traineddata（繁体）
            // chi_tra_vert.traineddata（繁体，竖排）
            // eng 英文
            ocrConfig.setLanguage("chi_sim+chi_tra+chi_sim_vert+chi_tra_vert+eng");
            ocrConfig.setPageSegMode("3");
            parseContext.set(TesseractOCRConfig.class, ocrConfig);
        }
        // 如果存在pdf解析，添加一些参数
        if (loadedClassSet.contains(PDFParser.class.getName())) {
            PDFParserConfig pdfConfig = new PDFParserConfig();
            // 如果为true，则提取文本内联嵌入的OBXImages。
            pdfConfig.setExtractInlineImages(Boolean.TRUE);
            // PDF文件中的多个页面可能引用相同的底层图像。如果extractUniqueInlineImagesOnly设置为false，则每当图像出现在页面上时，解析器都会调用EmbeddedExtractor。
            // 这可能是某些用例所需要的。但是，为了避免重复提取的图像，请将其设置为true。默认值为true。
            pdfConfig.setExtractUniqueInlineImagesOnly(Boolean.FALSE);
            parseContext.set(PDFParserConfig.class, pdfConfig);
        }
        if (MapUtil.isNotEmpty(parseOptions)) {
            parseOptions.forEach((c, v) -> {
                if (ClassUtil.isAssignable(c, v.getClass())) {
                    parseContext.set(c, v);
                }
            });
        }
    }

    /**
     * 文件信息
     *
     * @author song_jx
     * @date 2023-10-09 03:04:14
     */
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        /** 文件名 */
        private String fileName;
        /** 介质类型 */
        private String mediaType;
        /** 语言 */
        private String language;
        /** 内容 */
        private String content;

        public FileInfo(String mediaType, String language, String content) {
            this.mediaType = mediaType;
            this.language  = language;
            this.content   = content;
        }

        /**
         * 新文件名
         *
         * @param fileName 文件名
         * @return {@link FileInfo }
         * @author song_jx
         */
        public FileInfo fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

    }

}
