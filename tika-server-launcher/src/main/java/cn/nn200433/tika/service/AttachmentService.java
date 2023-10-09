package cn.nn200433.tika.service;

import cn.nn200433.tika.utils.TikaUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * 附件服务
 *
 * @author song_jx
 * @date 2023-10-09 03:31:47
 */
public interface AttachmentService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return {@link TikaUtil.FileInfo }
     * @author song_jx
     */
    public TikaUtil.FileInfo uploadFile(MultipartFile file);

}
