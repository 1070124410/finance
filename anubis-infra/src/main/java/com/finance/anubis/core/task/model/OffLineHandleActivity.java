package com.finance.anubis.core.task.model;

import com.finance.anubis.core.constants.enums.OffLineAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yezhaoyang
 * @Date 2023/03/08 19:59
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OffLineHandleActivity {

    private String taskName;

    private String configKey;

    private OffLineAction action;

}
