package com.source.rworkflow.common.exception;

public class ExceptionCode {
    public static class Api{
        final public static String UNKNOWN_EXCEPTION = "Unknown Exception";
    }

    public static class UserToken{
        final public static String USER_TOKEN_NOT_FOUND_EXCEPTION = "User Token Not Found";
    }

    public static class WorkflowRule{
        final public static String APPROVAL_ASSIGNEE_CAN_NOT_BE_CREATED_WHEN_URGENT = "You Can't Assign Assignees When Urgent";
        final public static String CANNOT_DUPLICATE_ASSIGNEE = "Assignee Can Not Be Duplicated Even If It Is ROLE";
        final public static String ORDER_VALUE_EXCEPTION = "Only 0 < {value} < 4 Can Be Order Value";
        final public static String ASSIGNEE_CAN_NOT_BE_EMPTY = "You Must Assign At Least 1 Assignee";
        final public static String CANNOT_DUPLICATE_ORDER = "Order Can Not Be Duplicated";
    }
}
