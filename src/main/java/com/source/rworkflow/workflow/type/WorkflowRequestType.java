package com.source.rworkflow.workflow.type;

import com.source.rworkflow.misc.user.exception.IllegalEnumStringException;

public enum WorkflowRequestType {
    ACCESS_CONTROL,
    SQl_EXECUTION,
    DATA_EXPORT;

    public static WorkflowRequestType of(String name){
        try {
            return WorkflowRequestType.valueOf(name);
        } catch (Exception e){
            throw new IllegalEnumStringException(name);
        }
    }
}
