package com.finance.anubis.request;

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
public class TaskActivityReq implements Serializable {
    private Long id;

    private String action;

}
