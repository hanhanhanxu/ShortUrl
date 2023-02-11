package xyz.riun.shorturl;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@Slf4j
@MapperScan("xyz.riun.shorturl.mapper")
@SpringBootApplication
public class ShortUrlApplication {

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext application = SpringApplication.run(ShortUrlApplication.class, args);
            Environment env = application.getEnvironment();
            String[] activeProfiles = env.getActiveProfiles();
            String port = env.getProperty("server.port");
            String datasourceUrl = env.getProperty("spring.datasource.url");
            String redisHost = env.getProperty("spring.redis.host");
            String redisPort = env.getProperty("spring.redis.port");
            String redisDatabase = env.getProperty("spring.redis.database");
            String path = env.getProperty("server.servlet.context-path");
            if (path == null) {
                path = "";
            }
            log.info("\n----------------------------------------------------------\n\t" +
                    "Application is running! Access URLs:\n\t\n\t" +
                    "已指定的配置文件: \t\t" + ((activeProfiles.length == 0) ? "默认配置" : activeProfiles[0].toString()) + "\n\t\n\t" +
                    "ok健康检查: \t\t\thttp://localhost" + ":" + port + path + "/hs\n\t\n\t" +
                    "数据库链接: \t\t\t" + datasourceUrl + "\n\t\n\t" +
                    "redis链接: \t\t\t" + redisHost + ":" + redisPort + "/" + redisDatabase + "\n\t\n\t" +
                    "----------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //解决重定向时，控制台输出A cookie header was received [Hm_lvt_eaa57ca47dacb4ad4f5a257001a3457c=1656209978,1657770127,1657812148,1657850976;] that contained an invalid cookie. That cookie will be ignored.
    //Note: further occurrences of this error will be logged at DEBUG level.
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return tomcatServletWebServerFactory -> tomcatServletWebServerFactory.addContextCustomizers((TomcatContextCustomizer) context -> {
            context.setCookieProcessor(new LegacyCookieProcessor());
        });
    }


}
