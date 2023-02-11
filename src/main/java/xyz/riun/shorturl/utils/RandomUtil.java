package xyz.riun.shorturl.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class RandomUtil {
    private static final Random random = new Random();
    public static int nextInt(int max) {
        return random.nextInt(max);
    }

    public static void main(String[] args) {
        System.out.println(nextInt(2));
    }
}
