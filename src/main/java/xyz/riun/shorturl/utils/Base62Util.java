package xyz.riun.shorturl.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

@Slf4j
public class Base62Util {

    private static final char[] BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final int BASE_LEN = BASE.length;

    /**
     * 10进制转62进制
     * @param num
     * @return
     */
    public static String toBase62(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int i = (int) (num % BASE_LEN);
            sb.append(BASE[i]);
            num /= BASE_LEN;
        }
        // 耗时？写一个栈，然后重写toString()?
        return sb.reverse().toString();
    }

    /**
     * 自定义栈版本的，性能没差别
     * @param num
     * @return
     */
    /*public static String toBase62(long num) {
        MyStack<Character> myStack = new MyStack<>(10);
        while (num > 0) {
            int i = (int) (num % BASE_LEN);
            myStack.push(BASE[i]);
            num /= BASE_LEN;
        }
        return myStack.toString();
    }*/

    public static void main(String[] args) {
        System.out.println(toBase62(123));

        System.out.println(1%62);
        Stack<Object> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        System.out.println(stack.peek());
        System.out.println(stack);
    }
}
