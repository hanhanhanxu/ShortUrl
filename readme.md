短链服务:

短链请求，收到后，解析出长链，跳转。

长链作为参数，请求转为短链，返回短链

启动依赖：

- redis
- mysql



### 短链服务

我们经常会收到许多短信，里面是各种推广什么的，然后会附上一个很短的链接让你点进去。

为什么这里的URL都是短的呢？有什么好处呢？怎么做到的？



短URL的好处：

1、平台（短信或者微博等）有字数限制，太长的链接放进去，没办法写正文了

2、好看，简洁友好

3、安全，不暴露访问参数

4、方便做统计





### 服务设计思路：

1、mysql储存短链（ short_key ）与长链（ original_url ），短链字段唯一。

2、短链如何生成？可以使用分布式唯一id，但是这个容易被人观察出规律，写个脚本一下遍历出来，不安全。

3、如何保证安全？id是10进制的，转换成62进制即可（10个数字，26个大写、26个小写字母）



用户点击短链，请求到我们的解析系统中，我们根据key到数据库中找到原来的长链，做个302跳转即可。



前面的10进制转换成62进制，在行家眼里是一样的，我们还要做的更安全些。向原本的id的2进制中添加一写扰乱，例如：每隔5位添加一个0或1，直到前面都是0为止。这样两个相邻的数字生成的key就完全没有规律了。

（要还原，只需每隔5位丢弃掉一个位置上的数字即可。但是短链场景是不用考虑还原的，我们的db中有短链和长链的映射。）



另外，id生成不应该从1开始，而是从一个中等数值开始，例如100w。



最后注意：mysql默认大小写不敏感，3rtX 和 3Rtx 被认为是相同的。

解决办法如下，设置列为 utf8_bin：alter table `xxx` modify `short_key` char(10) character set utf8 collate utf8_bin;



性能分析：

这个系统性能瓶颈都在数据库中，缓存只能适当提高性能。如果预估未来需要存放50~100亿条记录，按照单表1000w数据来设计，那么需要500~1000张表，那么可以定512张表。使用key来做分表键。



参考：https://www.jianshu.com/p/44b50d295e3a







### 实践

#### 表：

```sql
create table short_url (
	id bigint UNSIGNED not null auto_increment primary key,
    short_url varchar(16) not null unique comment "短链",
    original_url varchar(255) not null comment "长链",
    biz_flag varchar(16) comment "业务标识",
    expire_time datetime comment "过期时间",
    create_time datetime not null default CURRENT_TIMESTAMP comment "创建时间"
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin comment "短链服务数据";
```

注意：`DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin`是必须的，它设置了大小写敏感。如果不设置，MySQL默认大小写不敏感，那么短链CwjW和Cwjw将被认为是一样的。



#### 代码：

https://github.com/hanhanhanxu/ShortUrl

![](https://minio.riun.xyz/riun1/2023-02-11_2SrS7Djr3XWMpxfpsq.jpg)

#### 扰动算法：

```java
    /**
     * 扰动，二进制上每隔5位添加一个0、1随机数
     * @param num
     * @return
     */
    public static long disturbance(long num) {
        long result = num;
        long leftNum = num;
        for (int i = 0; i < 10; i++) {
            //leftNum不断右移，当右移成0了，说明前面都是0
            if (leftNum == 0) {
                break;
            }
            int pos = 5 + 5 * i + i; // 5
            long rightNum = (long) Math.pow(2, pos) - 1; // 31:11111
            leftNum = result >> pos; // 11
            int randomNum = RandomUtil.nextInt(2);
            // 110 00000 | 00 11011
            result = ((leftNum << 1 | randomNum) << pos) | (result & rightNum);
        }
        return result;
    }
```





### 性能

#### 长链转短链接口：

单redis单MySQL，QPS在4000左右，平均响应时间在30ms左右：

![](https://minio.riun.xyz/riun1/2023-02-11_2SoZZ5GY9QPIFE6oEB.jpg)



#### 访问短链接口：

jmeter里只请求同一个短链，结果肯定是不太准的，QPS能达到1w左右。

![](https://minio.riun.xyz/riun1/2023-02-11_2Sq5bMkFKNj6prvkx0.jpg)