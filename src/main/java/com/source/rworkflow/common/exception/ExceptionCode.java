package com.source.rworkflow.common.exception;

public class ExceptionCode {
    public static class Api{
        final public static String UNKNOWN_EXCEPTION = "Unknown Exception";
        final public static String ILLEGAL_ENUM_STRING_EXCEPTION = "Illegal Enum Value";
    }

    public static class UserToken{
        final public static String USER_TOKEN_NOT_FOUND_EXCEPTION = "User Token Not Found";
    }

    public static class User{
        final public static String USER_NOT_FOUND_EXCEPTION = "User Not Found";
        final public static String SYSTEM_USER_CAN_NOT_DO_THAT = "System User Can Not Do That";
    }

    public static class Role {
        final public static String ROLE_NOT_FOUND_EXCEPTION = "Rule Not Found";
    }

    public static class WorkflowRule{
        final public static String WORKFLOW_RULE_NOT_FOUND = "Workflow Rule Not Found";
        final public static String APPROVAL_ASSIGNEE_CAN_NOT_BE_CREATED_WHEN_URGENT = "You Can't Assign Assignees When Urgent";
        final public static String CANNOT_DUPLICATE_ASSIGNEE = "Assignee Can Not Be Duplicated Even If It Is ROLE";
        final public static String ORDER_VALUE_EXCEPTION = "Only 0 < {value} < 4 Can Be Order Value";
        final public static String ASSIGNEE_CAN_NOT_BE_NULL = "Assignee Field Cannot Be Null";
        final public static String CANNOT_DUPLICATE_ORDER = "Order Can Not Be Duplicated";
        final public static String CANNOT_DELETE = "Can Not Delete";
    }

    public static class WorkflowRequest{
        final public static String WORKFLOW_REQUEST_NOT_FOUND = "Workflow Request Not Found";
        final public static String WORKFLOW_REQUEST_ORDER_MUST_MATCH_WITH_ORDER_OF_RULE = "Order Must Match With Rule's Order";
        final public static String EXECUTION_EXPIRATION_DATE_MUST_BE_AFTER_REQUEST_EXPIRATION_DATE = "Execution Expiration Date Must Be After Request Expiration Date";
        final public static String REQUEST_DETAIL_NULL_EXCEPTION = "Request Doesn't Have Suitable Detail Request";
        final public static String EXPIRATION_DATE_IS_BEFORE_NOW = "Expiration Date Is Before Than Now";
        final public static String SELF_APPROVE_EXCEPTION = "Cannot Opperate Self Approve";
        final public static String URGENT_VALUE_NOT_MATCH_EXCEPTION = "Urgent Value Must Match With Rule's";
        final public static String TYPE_NOT_MATCH_EXCEPTION = "Type Must Match With Rule's";
        final public static String ASSIGNEE_CAN_NOT_BE_EMPTY = "Assignee Is Empty. Please Fill It";
        final public static String CAN_NOT_DUPLICATE_REQUEST_ACCESS_CONTROL_CONNECTION_EXCEPTION = "Access Control Connection Is Duplicated In Request";
        final public static String APPROVAL_NOT_FOUND_BY_ORDER_EXCEPTION = "Approval Not Found By Order And Request Id";
        final public static String CAN_NOT_FOUND_APPROVAL_ASSIGNEE = "Approval Assignee Not Found";
        final public static String ORDERS_MUST_BE_INCREMENT = "Orders Must Be Increment";
        final public static String APPROVAL_HAVE_TO_FOLLOW_ITS_TURN_EXCEPTION = "Approval Have To Follow Its Turn Exception";
        final public static String CAN_NOT_APPROVE_BY_URGENT_EXCEPTION = "You Cannot Approve This Request BeCause Urgent. Move To Execute";
        final public static String WORKFLOW_IS_CANCELED = "Workflow Is Canceled";
    }
}
