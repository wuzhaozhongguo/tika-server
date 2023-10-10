package cn.nn200433.tika.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemPropsKeys;
import cn.hutool.system.SystemUtil;
import cn.nn200433.tika.config.TikaProperties;
import cn.nn200433.tika.enums.ResponseEnum;
import cn.nn200433.tika.exception.ApiException;
import cn.nn200433.tika.service.AttachmentService;
import cn.nn200433.tika.utils.TikaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 附件服务实现
 *
 * @author song_jx
 * @date 2023-10-09 03:33:49
 */
@Slf4j
@RequiredArgsConstructor
@Service(value = "attachmentService")
public class AttachmentServiceImpl implements AttachmentService {

    private static final String         FILE_TMP_DIR     = SystemUtil.get(SystemPropsKeys.TMPDIR) + File.separator + "tika" + File.separator + "upload" + File.separator;
    private static final File           FILE_TMP_DIR_OBJ = new File(FILE_TMP_DIR);
    private final        TikaProperties properties;
    private final        TaskExecutor   taskPool;

    @Override
    public Map<String, TikaUtil.FileInfo> uploadMultiFileMap(MultipartHttpServletRequest multipartRequest) {
        return uploadMultiFile(multipartRequest).stream()
                .collect(Collectors.groupingBy(TikaUtil.FileInfo::getFileName, Collectors.collectingAndThen(Collectors.toList(), CollUtil::getFirst)));
    }

    @Override
    public List<TikaUtil.FileInfo> uploadMultiFile(MultipartHttpServletRequest multipartRequest) {
        final CompletionService<TikaUtil.FileInfo> threadExecutor = new ExecutorCompletionService<TikaUtil.FileInfo>(taskPool);
        final MultiValueMap<String, MultipartFile> multiFileMap   = multipartRequest.getMultiFileMap();
        AtomicInteger                              atomicCount    = new AtomicInteger(0);
        multiFileMap.forEach((k, v) -> v.forEach(f -> {
            // 将操作加入线程
            threadExecutor.submit(() -> uploadFile(f, Boolean.TRUE));
            atomicCount.incrementAndGet();
        }));
        // 开始解析
        final int               totalFile    = atomicCount.get();
        List<TikaUtil.FileInfo> fileInfoList = new ArrayList<TikaUtil.FileInfo>(totalFile);
        for (int i = 0; i < totalFile; i++) {
            try {
                final Future<TikaUtil.FileInfo> take = threadExecutor.take();
                fileInfoList.add(take.get(3, TimeUnit.MINUTES));
            } catch (Exception e) {
                log.error("---> 文件解析异常...", e);
            }
        }
        return fileInfoList;
    }

    @Override
    public TikaUtil.FileInfo uploadFile(MultipartFile file, Boolean isCheckType) {
        if (isCheckType) {
            checkFile(file);
        }
        final String      originalFilename = file.getOriginalFilename();
        final String      extName          = StrUtil.DOT + FileUtil.extName(originalFilename);
        final String      namePrefix       = DateUtil.date().toString(DatePattern.PURE_DATETIME_FORMAT) + StrUtil.DASHED;
        TikaUtil.FileInfo fileInfo         = new TikaUtil.FileInfo();
        File tempFile = null;
        try {
            // 将数据塞了临时文件
            tempFile = FileUtil.createTempFile(namePrefix, extName, FILE_TMP_DIR_OBJ, Boolean.TRUE);
            file.transferTo(tempFile);
            log.debug("---> 上文文件路径：{}", tempFile.getAbsolutePath());
            // 开始解析
            fileInfo = TikaUtil.parseFile(tempFile).fileName(originalFilename);
        } catch (Exception e) {
            log.error("---> 文件上传或解析异常", e);
        } finally {
            // 删除文件
            FileUtil.del(tempFile);
        }
        return fileInfo;
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
