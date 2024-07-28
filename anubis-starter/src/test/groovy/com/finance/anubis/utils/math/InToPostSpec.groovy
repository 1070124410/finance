package com.finance.anubis.utils.math

import com.finance.anubis.core.util.math.InToPost
import com.finance.anubis.report.common.BaseSpecification

/**
 * @Author yezhaoyang
 * @Date 2023/03/15 09:30
 * @Description
 * */
class InToPostSpec extends BaseSpecification {

    void "testDoTrans"() {
        given:
        def infixExp = "( 2.4 + 1.2 ) * 2.2 / 4.1"
        def inToPost = new InToPost(infixExp)
        when:
        def trans = inToPost.doTrans()
        then:
        trans.equals("2.4 1.2 + 2.2 * 4.1 /")
    }

    void "testDoTransWithReplaceKey"() {
        given:
        def map = new HashMap<>();
        map.put("key1", 2.4);
        map.put("key2", 1.2);
        map.put("key3", 2.2);
        map.put("key4", 4.1);
        def infixExp = "( key1 + key2 ) * key3 / key4"
        def inToPost = new InToPost(infixExp);
        when:
        def res = inToPost.doTransWithReplaceKey(map)
        then:
        res.equals("2.4 1.2 + 2.2 * 4.1 /");
    }
}
