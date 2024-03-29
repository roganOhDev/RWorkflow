package com.source.rworkflow.workflow.type;

import com.source.rworkflow.common.exception.IllegalEnumStringException;

public enum WorkflowRequestType {
    ACCESS_CONTROL,
    SQL_EXECUTION,
    DATA_EXPORT;

    public static WorkflowRequestType of(String name){
        try {
            return WorkflowRequestType.valueOf(name);
        } catch (Exception e){
            throw new IllegalEnumStringException(name);
        }
    }

    public boolean isNotAutoExecution() {
        return this != WorkflowRequestType.ACCESS_CONTROL;
    }
}
