package com.finance.anubis.repository.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yezhaoyang
 * @Date 2023/03/07 22:50
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OffLineActionMqBody {

    private String bizKey;

    private String configKey;

}
