package com.finance.anubis.core.util.math;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Map;

public class InToPost {

    private ArrayDeque<Object> stack;
    private String input;
    private String output = "";

    /**
     * 表达式分隔符
     */
    private String SPLIT = " ";

    public InToPost(String input) {
        this.stack = new ArrayDeque();
        this.input = input;
    }

    /**
     * 生成后缀表达式
     * @return
     */
    public String doTrans() {
        String[] ins = input.split(SPLIT);
        for (String str : ins) {
            switch (str.charAt(0)) {
                case '+':
                case '-':
                    getOper(str.charAt(0), 1);
                    break;
                case '*':
                case '/':
                    getOper(str.charAt(0), 2);
                    break;
                case '(':
                    stack.push(str.charAt(0));
                    break;
                case ')':
                    //遇到右括号，把括号中的操作符，添加到后缀表达式字符串中。
                    getParen();
                    break;
                default:
                    output = output + SPLIT + str;
                    break;
            }
        }
        while (!stack.isEmpty()) {
            output = output + SPLIT + stack.pop();
        }

        return output.trim();
    }

    /**
     * 从input获得操作符
     *
     * @param opThis
     * @param currentPriority 操作符的优先级
     */
    private void getOper(char opThis, int currentPriority) {
        while (!stack.isEmpty()) {
            char opTop = String.valueOf(stack.pop()).charAt(0);
            //括号有较高优先级重新压入栈中
            if (opTop == '(') {
                stack.push(opTop);
                break;
            } else {
                int stackTopPriority;
                //+ ，-优先级都是1
                if (opTop == '+' || opTop == '-') {
                    stackTopPriority = 1;
                } else {
                    stackTopPriority = 2;
                }
                //如果当前优先级大于栈顶部的优先级，重新压入栈中，否则出栈加入到后缀表达式字符串中
                if (stackTopPriority < currentPriority) {
                    stack.push(opTop);
                    break;
                } else {
                    output = output + SPLIT + opTop;
                }
            }
        }
        stack.push(opThis);
    }

    public void getParen() {
        while (!stack.isEmpty()) {
            char chx = String.valueOf(stack.pop()).charAt(0);
            //如果是'('直接返回，其他操作符直接拼接到后缀表达式中。
            if (chx == '(') {
                break;
            } else {
                output = output + SPLIT + chx;
            }
        }
    }

    /**
     * 针对采用Key配置的表达式进行值替换
     * @param map
     * @return
     */
    public String doTransWithReplaceKey(Map<String, BigDecimal> map){
        String[] ins = input.split(SPLIT);
        for (String str : ins) {
            switch (str.charAt(0)) {
                case '+':
                case '-':
                    getOper(str.charAt(0), 1);
                    break;
                case '*':
                case '/':
                    getOper(str.charAt(0), 2);
                    break;
                case '(':
                    stack.push(str.charAt(0));
                    break;
                case ')':
                    //遇到右括号，把括号中的操作符，添加到后缀表达式字符串中。
                    getParen();
                    break;
                default:
                    output = output + SPLIT + map.get(str);
                    break;
            }
        }
        while (!stack.isEmpty()) {
            output = output + SPLIT + stack.pop();
        }

        return output.trim();
    }
}

