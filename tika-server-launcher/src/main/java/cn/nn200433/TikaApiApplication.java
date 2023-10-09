package cn.nn200433;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


/**
 * 应用启动程序
 *
 * @author song_jx
 * @date 2023-10-09 04:01:11
 */
@SpringBootApplication
@Import(cn.hutool.extra.spring.SpringUtil.class)
public class TikaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TikaApiApplication.class, args);
    }

}
