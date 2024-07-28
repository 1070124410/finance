package com.finance.anubis.req;

import com.guming.api.pojo.page.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 14:15
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskActivityReq extends PageRequest implements Serializable {
    private Long id;

    private String action;

}
