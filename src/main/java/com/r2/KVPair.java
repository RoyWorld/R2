package com.r2;

/**
 * 键值对，用于变量和对应值的保存
 * Created by RoyChan on 2018/3/23.
 */
public class KVPair {

    public char key;
    public Object value;

    public KVPair(char key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "KVPair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
