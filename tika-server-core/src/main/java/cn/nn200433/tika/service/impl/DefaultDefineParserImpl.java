package cn.nn200433.tika.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.nn200433.tika.config.TikaProperties;
import cn.nn200433.tika.entity.ToolsPathProperties;
import cn.nn200433.tika.service.DefineParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaConfigException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.parser.image.ImageParser;
import org.apache.tika.parser.microsoft.OfficeParser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.parser.ocr.TesseractOCRParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.parser.video.FLVParser;

/**
 * 默认定义解析器
 *
 * @author song_jx
 * @date 2023-10-09 11:12:59
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultDefineParserImpl implements DefineParser {

    private final TikaProperties properties;

    @Override
    public AutoDetectParser build() {
        AutoDetectParser parser = null;
        try {
            final ToolsPathProperties toolsPath          = properties.getToolsPath();
            PDFParser                 pdfParser          = new PDFParser();
            OfficeParser              officeParser       = new OfficeParser();
            TXTParser                 txtParser          = new TXTParser();
            ImageParser               imageParser        = new ImageParser();
            AudioParser               audioParser        = new AudioParser();
            Mp3Parser                 mp3Parser          = new Mp3Parser();
            MP4Parser                 mp4Parser          = new MP4Parser();
            FLVParser                 flvParser          = new FLVParser();
            TesseractOCRParser        tesseractOCRParser = new TesseractOCRParser();
            String                    tessOcrPath        = "";
            String                    imageMagickPath    = "";
            if (null != toolsPath) {
                tessOcrPath     = toolsPath.getTessOcr();
                imageMagickPath = toolsPath.getImageMagick();
            }
            if (StrUtil.isNotBlank(tessOcrPath)) {
                tesseractOCRParser.setTesseractPath(tessOcrPath);
            }
            if (StrUtil.isNotBlank(imageMagickPath)) {
                tesseractOCRParser.setImageMagickPath(imageMagickPath);
            }
            // tesseractOCRParser.setPreloadLangs(Boolean.TRUE);
            tesseractOCRParser.initialize(null);
            parser = new AutoDetectParser(txtParser, pdfParser, officeParser, imageParser, tesseractOCRParser, audioParser, mp3Parser, mp4Parser, flvParser);
        } catch (TikaConfigException e) {
            parser = new AutoDetectParser();
            log.error("---> AutoDetectParser 构建异常，将使用 new AutoDetectParser() ");
        }
        return parser;
    }

}
