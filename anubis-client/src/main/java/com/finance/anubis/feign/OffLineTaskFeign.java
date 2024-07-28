package com.finance.anubis.feign;

import com.finance.anubis.constant.AnubisServiceConstant;
import com.finance.anubis.dto.OffLineDataReadyDTO;
import com.guming.api.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author yezhaoyang
 * @Date 2023/03/07 19:06
 * @Description
 **/
@FeignClient(name = AnubisServiceConstant.APPLICATION_NAME,
        decode404 = true,
        contextId = "offLineTaskFeign")
public interface OffLineTaskFeign {

    String PREFIX_PATH = "offLineTask";

    /**
     * 离线对账数据就绪接口
     *
     * @param dto
     * @return
     */
    @PostMapping(PREFIX_PATH + "/OffLineDataReady")
    Result<String> OffLineDataReady(@RequestBody OffLineDataReadyDTO dto);


}
