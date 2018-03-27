package com.r2;

import java.util.Arrays;
import java.util.Stack;

import static com.r2.Patterner.*;

/**
 * Created by RoyChan on 2018/3/22.
 */
public class R2 {


    private int treeCal(String exp){
        if (expFind(exp)){
            String[] subExps = parserCalc(exp);
            return cal(subExps[0], treeCal(subExps[1]), treeCal(subExps[2]));
        }else {
            return Integer.valueOf(exp);
        }
    }


    /**
     * 匹配四则运算
     * @param op
     * @param a
     * @param b
     * @return
     */
    private int cal(String op, int a, int b){
        switch (op){
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
        }
        return 0;
    }


    /**
     * 解析表达式
     * (a) (b)
     * @param s
     * @return {(a), (b)}
     */
    private String[] parserExp(String s){
        //用栈保存当前未匹配的左括号
        Stack<Character> stack = new Stack<>();
        String s1 = "";
        int index = 0;
        //遍历整个字符串
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            s1 = s1 + ch;
            if (ch == '('){//当出现左括号时，将左括号入栈
                stack.push(ch);
            }else if (ch == ')'){//当出现右括号时，将栈顶元素弹出
                stack.pop();
            }

            //通过栈是否为空，判断括号是否已匹配完成
            if (stack.empty()){
                //赋值e2的起始位置
                index = i + 1;
                //若i+1为空，则index++
                if (s.charAt(i+1) == ' '){
                    index++;
                }
                break;
            }
        }
        String s2 = s.substring(index);
        String[] sArray = new String[]{s1, s2};
        System.out.println(s + ": " + Arrays.toString(sArray));
        return new String[]{s1, s2};
    }

    /**
     * 解析算术表达式
     * (· e1 e2)
     * @param s
     * @return {·, e1, e2}
     */
    private String[] parserCalc(String s){
        String desuger = expDesuger(s);//· e1 e2
        String s0 = String.valueOf(desuger.charAt(0));//·
        s = expCalcMatch(s);//e1 e2
        String[] sArray = parserExp(s);//{e1, e2}
        System.out.println(s + ": " + Arrays.toString(sArray));
        return new String[]{s0, sArray[0], sArray[1]};
    }

    /**
     * 解析lambda表达式
     * (lambda (x) e)
     * @param s
     * @return {x, e}
     */
    private String[] parserLambda(String s){
        String exp = expLambdaMatch(s);//(x) e
        String[] sArray = parserExp(exp);//{x, e}
        System.out.println(s + ": " + Arrays.toString(sArray));
        return new String[]{expDesuger(sArray[0]), sArray[1]};
    }

    /**
     * 解析let表达式
     * (let ([x e1]) e2)
     * @param s
     * @return {x, e1, e2}
     */
    private String[] parserLet(String s){
        String exp = expLetMatch(s);//([x e1]) e2
        String[] sArray = parserExp(exp);//{([x e1]), e2}
        String[] sArray1 = parserExp(removeSquareBrackets(expDesuger(sArray[0])));//{x, e1}
        System.out.println(s + ": " + Arrays.toString(sArray));
        return new String[]{sArray1[0], sArray1[1], sArray[1]};
    }

    /**
     * 解析调用表达式
     * (e1 e2)
     * @param s
     * @return {e1, e2}
     */
    private String[] parserCall(String s){
        String desuger = expDesuger(s);//e1 e2
        String[] sArray = parserExp(desuger);//{e1, e2}
        System.out.println(s + ": " + Arrays.toString(sArray));
        return new String[]{sArray[0], sArray[1]};
    }






    /**
     * 数字执行
     * @param s
     * @return
     */
    private int execNumber(String s){
        return Integer.valueOf(s);
    }

    /**
     * 变量x执行
     * @param s
     * @return
     */
    private Object execX(String s, int index){
        //从特定的环境中找到x对应的值
        Object object = Env.lookupFromIndex(s.charAt(0), index);
        if (object instanceof String){
            return Integer.valueOf((String) object);
        }else {
            return object;
        }
    }

    /**
     * 算式执行
     * @param s
     * @param index
     * @return
     */
    private int execCalc(String s, int index){
        String[] subExps = parserCalc(s);//解析算式表达式
        return cal(subExps[0], (int)exec(subExps[1], index), (int)exec(subExps[2], index));//返回四则计算结果
    }

    /**
     * lambda语句执行
     * @param s
     * @return
     */
    private LambdaFunc execLambda(String s){
        String[] subExps = parserLambda(s);//解析lambda表达式
        return new LambdaFunc(subExps[0].charAt(0), subExps[1], Env.index());//返回lambda函数对象
    }

    /**
     * let执行
     * (let ([x e1]) e2)
     * @param s
     * @return
     */
    private int execLet(String s){
        String[] subExps = parserLet(s);//解析let表达式
        char var = subExps[0].charAt(0);
        String e1 = subExps[1];
        String e2 = subExps[2];
        Object result = exec(e1, Env.index());//求出e1的值v1
        Env.extEnv(var, result);//将(x.v1)扩充到环境中
        return (int) exec(e2, Env.index());//返回e2的执行结果
    }

    /**
     * func调用执行
     * (f e)
     * @param s
     * @return
     */
    private int execCall(String s){
        String[] subExps = parserCall(s);
        String e1 = subExps[1];//exp
        LambdaFunc func = (LambdaFunc) exec(subExps[0], Env.index());//在环境中找到变量f所代表的函数
        Env.extEnv(func.getVar(), e1);//将函数的形参x和入参e，扩充到环境中
        return (int) exec(func.getExp(), func.getEnvIndex());//返回函数主体表达式的执行结果
    }

    /**
     * 表达式解析执行
     * @param exp
     * @param index 用于找到特定的环境
     * @return
     */
    public Object exec(String exp, int index){
        if (exp.length() == 1){//通过表达式的长度判断是否变量或数字
            if (Character.isDigit(exp.charAt(0))){//数字
                return execNumber(exp);
            }else if (Character.isLetter(exp.charAt(0))){//变量
                return execX(exp, index);
            }
        }else if (expLetFind(exp)){//let表达式
            return execLet(exp);
        }else if (expLambdaFind(exp)){//lambda表达式
            return execLambda(exp);
        }else if (expCalcFind(exp)){//算术表达式
            return execCalc(exp, index);
        }else {//调用表达式
            return execCall(exp);
        }
        return 0;
    }


    /**
     * 客户端函数
     * 编译生成结果
     * @param s
     * @return
     */
    public int interp(String s){
        try{
            return (int) exec(s, Env.index());
        }finally {
            //清空环境
            Env.clear();
        }
    }


    public static void main(String[] args) {
        R2 r2 = new R2();
        int a = (int) r2.exec("(+ 1 (+ 1 2))", Env.index());
        System.out.println(a);
    }
}
