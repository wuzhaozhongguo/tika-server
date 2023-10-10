package cn.nn200433.tika.listener;

import cn.nn200433.tika.utils.TikaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 初始化监听
 *
 * @author song_jx
 * @date 2023-10-10 04:57:57
 */
@Slf4j
@Component
public class InitListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("---> 项目启动完毕， 准备初始化...");
        TikaUtil.initParser();
    }

}
