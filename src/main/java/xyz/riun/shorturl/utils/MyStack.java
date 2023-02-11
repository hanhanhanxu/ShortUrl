package xyz.riun.shorturl.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyStack<T> {
    // 指向栈底
    private int tail;
    // 指向当前栈顶元素的上面
    private int p;
    private int capacity;
    private Object[] table;

    public MyStack(int capacity) {
        this.capacity = capacity;
        this.tail = this.p = 0;
        this.table = new Object[capacity];
    }

    public void push(T element) {
        if (p >= capacity) {
            throw new RuntimeException("栈满");
        }
        table[p++] = element;
    }

    public T pop() {
        if (p == tail) {
            throw new RuntimeException("栈空");
        }
        T o = (T) table[p-1];
        p -= 1;
        return o;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = p - 1; i >= 0; i--) {
            sb.append(table[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        MyStack<Character> myStack = new MyStack<Character>(5);
        myStack.push('1');
        myStack.push('2');
        myStack.push('3');
        myStack.push('4');
        myStack.push('5');
        System.out.println(myStack.pop());
        System.out.println(myStack);
    }
}
