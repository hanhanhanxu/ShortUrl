package xyz.riun.shorturl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortUrl {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String shortUrl;
    private String originalUrl;
    private String bizFlag;
    private Date expireTime;
    private Date createTime;

    public ShortUrl(String shortUrl, String originalUrl) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
    }
}
