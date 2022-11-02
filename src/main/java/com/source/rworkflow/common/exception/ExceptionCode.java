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
        final public static String CANNOT_DUPLICATE_ASSIGNEE = "Assignee Is Duplicated Even If It Is ROLE";
    }
}
