package com.finance.anubis.utils.math

import com.finance.anubis.core.util.math.InToPost
import com.finance.anubis.core.util.math.ParsePost
import com.finance.anubis.report.common.BaseSpecification

/**
 * @Author yezhaoyang
 * @Date 2023/03/13 22:28
 * @Description
 * */
class ParsePostSpec extends BaseSpecification {

    void "testSingleValue"() {
        given:
        def s = "3"
        def inToPost = new InToPost(s)
        when:
        def trans = inToPost.doTrans()
        def parsePost = new ParsePost(trans)
        def output = parsePost.doParse()
        then:
        output.equals(new BigDecimal(3))
    }

    void "testSingleValueWithKey"() {
        given:
        def s = "key"
        def inToPost = new InToPost(s)
        def map = new HashMap<>()
        map.put("key", new BigDecimal(-3))
        def trans = inToPost.doTransWithReplaceKey(map)
        def parsePost = new ParsePost(trans)
        when:
        def output = parsePost.doParse()
        then:
        output.equals(new BigDecimal(3))
    }

    void "testGetBigDecimal"() {
        given:
        Object a = "123"
        def decimal = ParsePost.getBigDecimal(a)
        when:
        Object b = 123.4
        then:
        print(ParsePost.getBigDecimal(b))
    }

    void "testValue"() {
        given:
        String s = "( 3 + 2 ) * 5"
        InToPost inToPost = new InToPost(s)
        String trans = inToPost.doTrans()
        ParsePost parsePost = new ParsePost(trans)
        when:
        BigDecimal output = parsePost.doParse()
        then:
        output.equals(new BigDecimal(25))
    }

    void "testValueWithKey"() {
        given:
        def s = "( key1 + key2 ) * key3"
        def inToPost = new InToPost(s)
        def map = new HashMap<>()
        map.put("key1", new BigDecimal(3))
        map.put("key2", new BigDecimal(2))
        map.put("key3", new BigDecimal(5))
        def trans = inToPost.doTransWithReplaceKey(map)
        def parsePost = new ParsePost(trans)
        when:
        def output = parsePost.doParse()
        then:
        output.equals(new BigDecimal(25))
    }


}
