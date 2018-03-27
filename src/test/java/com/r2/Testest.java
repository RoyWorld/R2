package com.r2;

import org.junit.Test;

import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by RoyChan on 2018/3/22.
 */
public class Testest {

    @Test
    public void test1(){
        String exp = "(1 (1 2))";
        String pattern = "(?<=\\().*(?=\\))";
        Pattern r = Pattern.compile(pattern);
        Matcher m =  r.matcher(exp);
        if (m.find()){
            String s = m.group();
            System.out.println(s);
        }

        String a = "a b c";
        String[] s = a.split("\\s");
        System.out.println(s.length);
    }

    @Test
    public void test2(){
        String exp0 = "(a b)";
        String exp1 = "(a (a b))";
        String exp2 = "((a b) a)";
        String exp3 = "((a b) (a b))";

        String exp4 = "a b";
        String exp5 = "a (a b)";
        String exp6 = "(a b) a";
        String exp7 = "(a b) (a b)";

        String special = "a (a (a b))";

        find(exp0);
        find(exp1);
        find(exp2);
        find(exp3);


        String a = "a b c";
        String[] s = a.split("\\s");
        System.out.println(s.length);
    }

    private int treeSum(String exp){
        if (find(exp)){
            String s = matchExp(exp);
            String[] subExps = spilt1(s, '(', ')');
            return treeSum(subExps[0]) + treeSum(subExps[1]);
        }else {
            return Integer.valueOf(exp);
        }
    }

    private String matchExp(String exp){
        String pattern = "(?<=\\().*(?=\\))";
        Pattern r = Pattern.compile(pattern);
        Matcher m =  r.matcher(exp);
        m.find();
        return m.group();
    }


    private boolean find(String exp){
        String pattern = "(?<=\\().*(?=\\))";
        Pattern r = Pattern.compile(pattern);
        Matcher m =  r.matcher(exp);
        return m.find();
    }

    private String[] spilt1(String s, char openSymbol, char closeSymbol){
        Stack<Character> stack = new Stack<>();
        String s1 = "";
        int index = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            s1 = s1 + ch;
            if (ch == openSymbol){
                stack.push(ch);
            }else if (ch == closeSymbol){
                stack.pop();
            }

            if (stack.empty()){
                index = i + 1;
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

    @Test
    public void test3(){
        String exp4 = "a b";
        String exp5 = "a (a b)";
        String exp6 = "(a b) a";
        String exp7 = "(a b) (a b)";

        String special = "a (a (a b))";

        spilt1(exp4, '(', ')');
        spilt1(exp5, '(', ')');
        spilt1(exp6, '(', ')');
        spilt1(exp7, '(', ')');

        spilt1(special, '(', ')');
    }

    private String matchLambdaExp(String exp){
        String pattern = "\\(lambda\\s\\(.+\\)\\s.+\\)";
        Pattern r = Pattern.compile(pattern);
        Matcher m =  r.matcher(exp);
        m.find();
        return m.group();
    }

    private String[] parserLambdaExp(String exp){
        String exp1 = matchExp(exp);
        String s0 = String.valueOf(exp1.charAt(8));
        exp = exp.substring(11);
        return new String[]{s0, exp};
    }

    @Test
    public void test4(){
        String s0 = "(lambda (x) e)";
        String s1 = "(lambda (x) (lambda (x) e))";
        String str = matchLambdaExp(s0);
        String str1 = matchLambdaExp(s1);
        System.out.println(str1);
        System.out.println(Arrays.toString(parserLambdaExp(str1)));
        System.out.println(str.charAt(8));
        System.out.println(str1.substring(11));
    }

    private String matchExp1(String exp){
        String pattern = "(?<=\\[).*(?=\\])";
        Pattern r = Pattern.compile(pattern);
        Matcher m =  r.matcher(exp);
        m.find();
        return m.group();
    }

    private String matchLetExp(String exp){
        String pattern = "\\(let\\s\\(.+\\)\\s.+\\)";
        Pattern r = Pattern.compile(pattern);
        Matcher m =  r.matcher(exp);
        m.find();
        return m.group();
    }

    private String[] parserLetExp(String exp){
        String exp1 = matchExp(exp);

        String pattern = "(?<=\\(let\\s).+(?=\\))";
        Pattern r = Pattern.compile(pattern);
        Matcher m =  r.matcher(exp);
        m.find();
        exp1 = m.group();

        String[] exps = spilt1(exp1, '(', ')');
        String e1 = exps[0];
        String e2 = exps[1];
        String[] exps2 = spilt1(matchExp1(e1), '(', ')');
        return new String[]{exps2[0], exps2[1], e2};
    }


    @Test
    public void test5(){
        String s0 = "(let ([x e1]) e2)";
        String s1 = "(let ([x e1]) (let ([x e1]) e2))";
        String str = matchLetExp(s0);
        String str1 = matchLetExp(s1);
        System.out.println(str1);
        System.out.println(Arrays.toString(parserLetExp(str1)));
    }

    @Test
    public void test6(){

        Env.extEnv('u', 2);
        Env.extEnv('x', 1);
        Env.extEnv('y', 3);
        Env.extEnv('x', 4);
        Env.extEnv('v', 8);

        Env.print();

        System.out.println(Env.pop());

        System.out.println(Env.lookupFromIndex('x', 2));
        Env.print();
    }

    @Test
    public void test7(){
        R2 r2 = new R2();
        String s0;

        s0 = "(+ 1 (+ 1 2))";
        System.out.println(";; => " + r2.interp(s0));

        s0 = "(* (+ 1 2) (+ 3 4))";
        System.out.println(";; => " + r2.interp(s0));

        s0 = "((lambda (x) (* 2 x)) 3)";
        System.out.println(";; => " + r2.interp(s0));

        s0 = "(let ([x 2]) (let ([f (lambda (y) (* x y))]) (f 3)))";
        System.out.println(";; => " + r2.interp(s0));

        s0 = "(let ([x 2]) (let ([f (lambda (y) (* x y))]) (let ([x 4]) (f 3))))";
        System.out.println(";; => " + r2.interp(s0));

        s0 = "(+ (let ([x 2]) (let ([f (lambda (y) (* x y))]) (f 3))) 3)";
        System.out.println(";; => " + r2.interp(s0));

        s0 = "(let ([x (let ([x 2]) (let ([f, (lambda (y) (+ x y))]) (f 3)))]) (let ([f (lambda (y) (* x y))]) (f 3)))";
        System.out.println(";; => " + r2.interp(s0));

    }

}
