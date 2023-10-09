package cn.nn200433.tika.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.system.SystemPropsKeys;
import cn.hutool.system.SystemUtil;
import cn.nn200433.tika.service.AttachmentService;
import cn.nn200433.tika.utils.TikaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 附件服务实现
 *
 * @author song_jx
 * @date 2023-10-09 03:33:49
 */
@Slf4j
@Service(value = "attachmentService")
public class AttachmentServiceImpl implements AttachmentService {

    private static final String FILE_TMP_DIR     = SystemUtil.get(SystemPropsKeys.TMPDIR) + File.separator + "tika" + File.separator + "download" + File.separator;
    private static final File   FILE_TMP_DIR_OBJ = new File(FILE_TMP_DIR);

    @Override
    public TikaUtil.FileInfo uploadFile(MultipartFile file) {
        final String      extName     = FileUtil.extName(file.getOriginalFilename());
        final String      newFileName = IdUtil.fastSimpleUUID();
        TikaUtil.FileInfo fileInfo    = new TikaUtil.FileInfo();
        try {
            // 将数据塞了临时文件
            File tempFile = FileUtil.createTempFile(newFileName, extName, FILE_TMP_DIR_OBJ, Boolean.TRUE);
            file.transferTo(tempFile);
            // 开始解析
            fileInfo = TikaUtil.parseFile(tempFile);
        } catch (Exception e) {
            log.error("---> 文件上传或解析异常", e);
        }
        return fileInfo;
    }

}
