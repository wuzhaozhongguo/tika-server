package cn.nn200433.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.image.ImageParser;
import org.apache.tika.parser.microsoft.OfficeParser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.ocr.TesseractOCRParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
public class Demo {

    /**
     * Tika AutoDetectParser类来识别和抽取内容
     *
     * @param path 路径
     */
    public static void getTextFronPDF(String path) throws Exception {
        Tika               tika      = new Tika();
        TesseractOCRConfig ocrConfig = new TesseractOCRConfig();
        ocrConfig.setEnableImagePreprocessing(Boolean.TRUE);
        ocrConfig.setLanguage("chi_sim+eng");
        ocrConfig.setPageSegMode("3");
        PDFParserConfig pdfConfig = new PDFParserConfig();
        pdfConfig.setExtractInlineImages(Boolean.TRUE);
        pdfConfig.setExtractUniqueInlineImagesOnly(Boolean.FALSE);

        PDFParser    pdfParser    = new PDFParser();
        OfficeParser officeParser = new OfficeParser();
        TXTParser    txtParser    = new TXTParser();
        ImageParser  imageParser  = new ImageParser();
        // AudioParser        audioParser        = new AudioParser();
        // MidiParser         midiParser         = new MidiParser();
        TesseractOCRParser tesseractOCRParser = new TesseractOCRParser();
        tesseractOCRParser.setPreloadLangs(Boolean.TRUE);
        tesseractOCRParser.setTesseractPath("E:\\Program Files\\Tesseract-OCR");
        tesseractOCRParser.setImageMagickPath("E:\\Program Files\\ImageMagick-7.1.1-Q16-HDRI");
        tesseractOCRParser.initialize(null);
        Parser             parser       = new AutoDetectParser(txtParser, pdfParser, officeParser, imageParser);
        BodyContentHandler handler      = new BodyContentHandler(Integer.MAX_VALUE);
        ParseContext       parseContext = new ParseContext();
        parseContext.set(TesseractOCRConfig.class, ocrConfig);
        parseContext.set(PDFParserConfig.class, pdfConfig);
        parseContext.set(Parser.class, parser);
        Metadata metadata = new Metadata();
        try (InputStream input = new FileInputStream(new File(path));) {
            // // 方式一（会导致读到图片时才解析）
            // Reader parse = new ParsingReader(parser, input, metadata, parseContext);
            // 方式二
            parser.parse(input, handler, metadata, parseContext);

            System.out.println("--- 开始获取文档类型");
            System.out.println("    文档类型 = " + tika.detect(input, metadata));
            System.out.println("--- 开始获取文档元数据");
            Arrays.stream(metadata.names()).forEach(n -> System.out.println("    " + n + " = " + metadata.get(n)));
            System.out.println("--- 开始获取文档内容");
            System.out.println(handler);
            // // 方式一的操作
            // BufferedReader bufferedReader = new BufferedReader(parse);
            // String         line           = null;
            // while ((line = bufferedReader.readLine()) != null) {
            //     line = line.trim();
            //     if (null == line || "".equals(line)) {
            //         continue;
            //     }
            //     System.out.println(line);
            // }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        try {
            // getTextFronPDF("E:\\下载\\可视化设计管理系统-产品手册.docx");
            // System.out.println("=============================");
            // getTextFronPDF("E:\\下载\\监测接口文档V1.6.doc");
            // System.out.println("=============================");
            getTextFronPDF("E:\\下载\\OnlyOffice V7.0.1.38 下划线交接.pdf");
            // System.out.println("=============================");
        } catch (Exception e) {

        }
    }

}

