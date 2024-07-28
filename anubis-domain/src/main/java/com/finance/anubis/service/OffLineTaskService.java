package com.finance.anubis.service;

import com.finance.anubis.dto.OffLineDataReadyDTO;

/**
 * @Author yezhaoyang
 * @Date 2023/03/07 20:01
 * @Description
 **/
public interface OffLineTaskService {


    /**
     * 处理对账数据就绪事件
     * @param dto
     */
    Boolean dataReady(OffLineDataReadyDTO dto);

}
