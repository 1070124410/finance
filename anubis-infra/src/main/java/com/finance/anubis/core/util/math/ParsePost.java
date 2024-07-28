package com.finance.anubis.core.util.math;

import com.guming.api.pojo.Status;
import com.guming.common.exception.StatusCodeException;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.groovy.syntax.Numbers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayDeque;

public class ParsePost {

    private ArrayDeque<Object> stack;

    private String input;

    private final String SPLIT = " ";

    public ParsePost(String input) {
        this.input = input;
    }

    /**
     * 解析后缀表达式计算结果
     * @return
     */
    public BigDecimal doParse() {
        stack = new ArrayDeque<>();
        BigDecimal num1, num2;
        char c;
        BigDecimal interAns;
        String[] strs = input.trim().split(SPLIT);
        for (String str : strs) {
            c = str.charAt(0);
            if (NumberUtils.isParsable(str)) {
                stack.push(str);
            } else {
                if (stack.size() < 2) {
                    throw new StatusCodeException(Status.error("总账计算公式错误"));
                }
                num2 = getBigDecimal(stack.pop());
                num1 = getBigDecimal(stack.pop());
                if (num1 == null || num2 == null) {
                    throw new StatusCodeException(Status.error("总账计算公式错误"));
                }
                //todo 使用时注意除法无法整除无限循环问题,后续对齐如何计算
                switch (c) {
                    case '+':
                        interAns = num1.add(num2);
                        break;
                    case '-':
                        interAns = num1.subtract(num2);
                        break;
                    case '*':
                        interAns = num1.multiply(num2);
                        break;
                    case '/':
                        interAns = num1.divide(num2);
                        break;
                    default:
                        interAns = BigDecimal.valueOf(0);
                        break;
                }
                stack.push(interAns);
            }
        }
        interAns = getBigDecimal(stack.pop());
        return interAns;
    }

    public static BigDecimal getBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = BigDecimal.valueOf(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }


}
