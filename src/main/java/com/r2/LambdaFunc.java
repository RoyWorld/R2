package com.r2;

/**
 * 函数对象，用于函数定义
 * Created by RoyChan on 2018/3/26.
 */
public class LambdaFunc {

    //形参变量
    private char var;

    //函数调用表达式
    private String exp;

    //当前函数所属的环境，对应于某一个checkpoints
    private int envIndex;

    public LambdaFunc(char var, String exp, int envIndex) {
        this.var = var;
        this.exp = exp;
        this.envIndex = envIndex;
    }

    public char getVar() {
        return var;
    }

    public String getExp() {
        return exp;
    }

    public int getEnvIndex() {
        return envIndex;
    }
}
