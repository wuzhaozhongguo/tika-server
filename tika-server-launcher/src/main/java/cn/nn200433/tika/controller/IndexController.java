package cn.nn200433.tika.controller;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.nn200433.tika.common.Result;
import cn.nn200433.tika.config.TikaProperties;
import cn.nn200433.tika.enums.ResponseEnum;
import cn.nn200433.tika.exception.ApiException;
import cn.nn200433.tika.service.AttachmentService;
import cn.nn200433.tika.utils.TikaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;


/**
 * 入口控制器
 *
 * @author song_jx
 * @date 2023-10-09 03:57:41
 */
@Slf4j
@RestController
@RequestMapping(value = "/")
@RequiredArgsConstructor
public class IndexController {

    private final AttachmentService attachmentService;
    private final TikaProperties    properties;

    /**
     * 首页
     *
     * @return {@link String }
     * @author song_jx
     */
    @ResponseBody
    @GetMapping("/")
    public String index() {
        return "启动成功";
    }

    /**
     * 文件上传
     *
     * @param request 请求
     * @return {@link Result }<{@link List }<{@link TikaUtil.FileInfo }>>
     * @author song_jx
     */
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public Result<List<TikaUtil.FileInfo>> upload(HttpServletRequest request) {
        ServletContext           servletContext    = request.getSession().getServletContext();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);
        // List<FileInfo>           fileInfoList      = Lists.newArrayList();
        // 判断request是否有文件上传
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            multiRequest.getMultiFileMap().forEach((k, v) -> {
                v.forEach(file -> {
                    checkFile(file);
                    TikaUtil.FileInfo fileInfo = attachmentService.uploadFile(file);
                });
            });
        }
        return Result.ofSuccess("上传成功", null);
    }

    /**
     * 检查文件类型
     *
     * @param file 文件
     * @author song_jx
     */
    private void checkFile(MultipartFile file) {
        final String allowExt = properties.getAllowExtName();
        if (StrUtil.isBlank(allowExt)) {
            return;
        }
        try (InputStream is = new ByteArrayInputStream(file.getBytes())) {
            final String fileType = StrUtil.blankToDefault(FileTypeUtil.getType(is), FileUtil.extName(file.getOriginalFilename()));
            Assert.isTrue(StrUtil.containsIgnoreCase(allowExt, fileType), "不支持该格式上传");
        } catch (Exception e) {
            throw new ApiException(ResponseEnum.UPLOAD_ERROR, e.getMessage());
        }
    }

}
