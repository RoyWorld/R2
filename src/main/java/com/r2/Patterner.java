package com.r2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表达式匹配类，用于表达式的模式匹配
 * Created by RoyChan on 2018/3/23.
 */
public class Patterner {

    private static final String removeSquareBrackets = "(?<=\\[).*(?=\\])";

    private static final String desuger = "(?<=\\().*(?=\\))";

    public static final String calcFind = "^\\([*+-/].+\\)$";
    public static final String calcMatch = "(?<=\\([*+-/]\\s).+(?=\\))";

    private static final String lambdaFind = "^\\(lambda\\s\\(.+\\)$";
    private static final String lambdaMatch = "(?<=\\(lambda\\s).+(?=\\))";

    private static final String letFind = "^\\(let\\s\\(.+\\)\\s.+\\)$";
    private static final String letMatch = "(?<=\\(let\\s).+(?=\\))";


    /**
     * 返回去掉两边方括号的表达式
     * [e] -> e
     * @param exp
     * @return
     */
    public static String removeSquareBrackets(String exp){
        return match(exp, removeSquareBrackets);
    }

    /**
     * 判断是否表达式
     * @param exp
     * @return
     */
    public static boolean expFind(String exp){
        return find(exp, desuger);
    }

    /**
     * 返回去掉两边括号的表达式
     * (e) -> e
     * @param exp
     * @return
     */
    public static String expDesuger(String exp){
        return match(exp, desuger);
    }


    /**
     * 判断是否算术表达式
     * @param exp
     * @return
     */
    public static boolean expCalcFind(String exp){
        return find(exp, calcFind);
    }

    /**
     * 解析算术表达式
     * @param exp
     * @return
     */
    public static String expCalcMatch(String exp){
        return match(exp, calcMatch);
    }

    /**
     * 判断是否lambda函数表达式
     * @param exp
     * @return
     */
    public static boolean expLambdaFind(String exp){
        return find(exp, lambdaFind);
    }

    /**
     * 解析lambda函数表达式
     * @param exp
     * @return
     */
    public static String expLambdaMatch(String exp){
        return match(exp, lambdaMatch);
    }


    /**
     * 判断是否let函数表达式
     * @param exp
     * @return
     */
    public static boolean expLetFind(String exp){
        return find(exp, letFind);
    }

    /**
     * 解析let函数表达式
     * @param exp
     * @return
     */
    public static String expLetMatch(String exp){
        return match(exp, letMatch);
    }


    /**
     * match的基础方法
     * @param exp
     * @param pattern
     * @return
     */
    private static String match(String exp, String pattern){
        Pattern r = Pattern.compile(pattern);
        Matcher m =  r.matcher(exp);
        m.find();
        return m.group();
    }

    /**
     * find的基础方法
     * @param exp
     * @param pattern
     * @return
     */
    private static boolean find(String exp, String pattern){
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
        Matcher m =  r.matcher(exp);
        return m.find();
    }
}
