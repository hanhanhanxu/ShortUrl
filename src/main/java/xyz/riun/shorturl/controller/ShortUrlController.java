package xyz.riun.shorturl.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.riun.shorturl.entity.ShortUrl;
import xyz.riun.shorturl.service.RedisService;
import xyz.riun.shorturl.service.ShortUrlService;
import xyz.riun.shorturl.utils.Base62Util;
import xyz.riun.shorturl.utils.DisturbanceUtil;

@Slf4j
@Controller
public class ShortUrlController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ShortUrlService shortUrlService;

    @Value("${base.url}")
    private String baseUrl;


    //直接写String longUrl是表单 from-data
    //@RequestBody String longUrl 是 raw ，并且是json格式的 {}
    @ResponseBody
    @RequestMapping("toshorturl")
    public String toShortUrl(String longUrl) {
        if (longUrl == null || "".equals(longUrl)) {
            return "error";
        }
        // 从redis读出下一个自增id
        Long num = redisService.getIncrementId();
        // 将数字id转化为短链
        long disturbanceNum = DisturbanceUtil.disturbance(num);
        String base62Str = Base62Util.toBase62(disturbanceNum);
        log.info("longUrl:{}, num:{}, disturbanceNum:{}, base62Str:{}", longUrl, num, disturbanceNum, base62Str);
        // 存入MySQL
        ShortUrl shortUrl = new ShortUrl(base62Str, longUrl);
        shortUrlService.insert(shortUrl);
        return baseUrl + "/" + shortUrl.getShortUrl();
    }

    public static void main(String[] args) {
        for (long i = 1000000; i < 1000010; i++) {
            long disturbanceNum = DisturbanceUtil.disturbance(i);
            String toBase62 = Base62Util.toBase62(disturbanceNum);
            System.out.println(toBase62);
        }
    }

    @RequestMapping("/{shortUrl}")
    public String toLongUrl(@PathVariable("shortUrl") String shortUrl) {
        log.info("shortUrl:{}", shortUrl);
        // TODO: 2023/2/11 增加缓存层？
        // 从mysql查出长链
        ShortUrl shortUrlBean = shortUrlService.findByShortUrl(shortUrl);
        // 没有找到长链，跳转到固定页面
        if (shortUrlBean == null) {
            return "/error/404.html";
        }
        String originalUrl = shortUrlBean.getOriginalUrl();
        // 302跳转originalUrl
        return "redirect:" + originalUrl;
    }
}
