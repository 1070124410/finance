package com.finance.anubis.core.task.model;

import com.finance.anubis.core.constants.enums.ActionResult;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskActivityResult extends BaseModel {

    private Long taskActivityId;

    private String bizKey;

    private Integer times;

    private Map<String, Object> compareSourceData;
    private Map<String, Object> compareTargetData;
    private List<String> compareKeys;

    private List<String> varianceKeys;

    private ActionResult actionResult;

}
