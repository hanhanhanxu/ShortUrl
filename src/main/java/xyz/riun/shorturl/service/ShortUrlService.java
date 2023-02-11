package xyz.riun.shorturl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.riun.shorturl.entity.ShortUrl;
import xyz.riun.shorturl.mapper.ShortUrlMapper;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ShortUrlService {
    @Resource
    private ShortUrlMapper shortUrlMapper;

    public void insert(ShortUrl shortUrl) {
        int insert = shortUrlMapper.insert(shortUrl);
        if (insert != 1) {
            throw new RuntimeException("插入失败！");
        }
    }

    public ShortUrl findByShortUrl(String shortUrl) {
        HashMap<String, Object> columnMap = new HashMap<>();
        columnMap.put("short_url", shortUrl);
        List<ShortUrl> resultList = shortUrlMapper.selectByMap(columnMap);
        if (resultList == null || resultList.size() != 1) {
            log.error("查询长链异常：shortUrl:{}, resultList:{}", shortUrl, resultList);
            return null;
        }
        return resultList.get(0);
    }
}
