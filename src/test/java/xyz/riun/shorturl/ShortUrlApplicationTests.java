package xyz.riun.shorturl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import xyz.riun.shorturl.entity.ShortUrl;
import xyz.riun.shorturl.mapper.ShortUrlMapper;

import java.util.List;

@SpringBootTest
class ShortUrlApplicationTests {

    @Autowired
    private ShortUrlMapper shortUrlMapper;

    @Test
    void contextLoads() {
        List<ShortUrl> shortUrls = shortUrlMapper.selectList(null);
        Assert.notEmpty(shortUrls, "空");
        shortUrls.forEach(System.out::println);
        System.out.println("over");
    }

}
