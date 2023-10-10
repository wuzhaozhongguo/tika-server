package cn.nn200433.tika.controller;

import cn.nn200433.tika.common.Result;
import cn.nn200433.tika.service.AttachmentService;
import cn.nn200433.tika.utils.TikaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 入口控制器
 *
 * @author song_jx
 * @date 2023-10-09 03:57:41
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class IndexController {

    private final AttachmentService attachmentService;

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
    public Result<Map<String, TikaUtil.FileInfo>> upload(HttpServletRequest request) {
        ServletContext                 servletContext    = request.getSession().getServletContext();
        CommonsMultipartResolver       multipartResolver = new CommonsMultipartResolver(servletContext);
        Map<String, TikaUtil.FileInfo> fileInfoMap       = new HashMap<String, TikaUtil.FileInfo>(0);
        // 判断request是否有文件上传
        if (multipartResolver.isMultipart(request)) {
            fileInfoMap = attachmentService.uploadMultiFileMap((MultipartHttpServletRequest) request);
        }
        return Result.ofSuccess("上传解析成功", fileInfoMap);
    }


}
