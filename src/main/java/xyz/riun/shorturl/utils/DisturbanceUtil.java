package xyz.riun.shorturl.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DisturbanceUtil {

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

    // TODO: 2023/2/10 如何还原
    public static long revivification(long num) {
        long result = num;
        long leftNum = num;
        for (int i = 0; i < 10; i++) {
            if (leftNum == 0) {
                break;
            }
            int pos = 5 + 5 * i + i;
            long rightNum = (long) Math.pow(2, pos) - 1;
            leftNum = result >> (pos + 1);
            result = (leftNum << pos) | (result & rightNum);
        }
        return 0;
    }

    public static void main(String[] args) {
        long num = 12345;
        System.out.println(num);
        System.out.println(Long.toBinaryString(num));
        long disturbance = disturbance(num);
        System.out.println(disturbance);
        System.out.println(Long.toBinaryString(disturbance));;

        long revivification = revivification(disturbance);
        System.out.println(revivification);
        System.out.println(Long.toBinaryString(revivification));
    }
}
/*
num=12345
01100   00001   11001
01100 0 00001 1 11001

num=123456
  00011   11000   10010   00000
1 00011 1 11000 0 10010 1 00000

 */