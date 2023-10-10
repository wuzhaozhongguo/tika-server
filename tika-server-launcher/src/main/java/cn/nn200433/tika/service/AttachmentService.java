package cn.nn200433.tika.service;

import cn.nn200433.tika.utils.TikaUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * 附件服务
 *
 * @author song_jx
 * @date 2023-10-09 03:31:47
 */
public interface AttachmentService {

    /**
     * 上传多文件
     *
     * @param multipartRequest 请求
     * @return {@link Map }<{@link String }, {@link TikaUtil.FileInfo }>
     * @author song_jx
     */
    public Map<String, TikaUtil.FileInfo> uploadMultiFileMap(MultipartHttpServletRequest multipartRequest);

    /**
     * 上传多文件
     *
     * @param multipartRequest 请求
     * @return {@link List }<{@link TikaUtil.FileInfo }>
     * @author song_jx
     */
    public List<TikaUtil.FileInfo> uploadMultiFile(MultipartHttpServletRequest multipartRequest);

    /**
     * 上传文件
     *
     * @param file        文件
     * @param isCheckType 是否检查文件类型
     * @return {@link TikaUtil.FileInfo }
     * @author song_jx
     */
    public TikaUtil.FileInfo uploadFile(MultipartFile file, Boolean isCheckType);

}
