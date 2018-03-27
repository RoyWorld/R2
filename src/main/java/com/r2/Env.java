package com.r2;

import java.util.ListIterator;
import java.util.Stack;

/**
 * 环境
 * Created by RoyChan on 2018/3/23.
 */
public class Env {

    //保存变量对应的值
    private static Stack<KVPair> stack = new Stack<>();

    /**
     * 对环境进行扩展
     * @param ch
     * @param val
     */
    public static void extEnv(char ch, Object val){
        KVPair kvPair = new KVPair(ch, val);
        stack.push(kvPair);
    }

    /**
     * 在环境中查找变量对应的值
     * @param ch
     * @return
     */
    public static Object lookup(char ch){
        ListIterator<KVPair> iterator = stack.listIterator(stack.size());
        while (iterator.hasPrevious()){
            KVPair kvPair = iterator.previous();
            if (kvPair.key == ch){
                return kvPair.value;
            }
        }
        return 0;
    }

    /**
     * 从checkPoints开始找变量对应的值
     * 若没有，则全局查询
     * @param ch
     * @param index
     * @return
     */
    public static Object lookupFromIndex(char ch, int index){
        ListIterator<KVPair> iterator = stack.listIterator(index);
        while (iterator.hasPrevious()){
            KVPair kvPair = iterator.previous();
            if (kvPair.key == ch){
                return kvPair.value;
            }
        }
        return lookup(ch);
    }

    /**
     * 顶层元素弹出
     * @return
     */
    public static KVPair pop(){
        return stack.pop();
    }

    /**
     * 清除环境
     */
    public static void clear(){
        stack.clear();
    }

    /**
     * 返回当前的checkpoints
     * @return
     */
    public static int index(){
        return stack.size();
    }

    /**
     * 打印输出环境信息
     */
    public static void print(){
        ListIterator<KVPair> iterator = stack.listIterator(stack.size());
        while (iterator.hasPrevious()){
            KVPair kvPair = iterator.previous();
            System.out.println(kvPair.toString());
        }
    }
}
