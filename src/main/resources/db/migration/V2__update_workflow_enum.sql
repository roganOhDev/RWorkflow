alter table
    workflow_requests
    modify column
    approval_status enum(
    'PENDING',
    'IN_PROGRESS',
    'APPROVED',
    'REJECTED',
    'EXPIRED'
    );

alter table
    workflow_requests
    modify column
    execution_status enum(
    'NONE',
    'PENDING',
    'IN_PROGRESS',
    'SUCCEEDED',
    'FAILED',
    'EXPIRED'
    );

alter table
    workflow_requests
    modify column
    review_status enum(
    'NONE',
    'PENDING',
    'CONFIRMED',
    'EXPIRED'
    );

alter table
    workflow_request_approvals
    modify column
    `status` enum(
    'NONE',
    'PENDING',
    'IN_PROGRESS',
    'APPROVED',
    'REJECTED',
    'EXPIRED'
    );

alter table
    workflow_request_approval_assignees
    modify column
    `status` enum(
    'None',
    'PENDING',
    'APPROVED',
    'REJECTED',
    'IN_PROGRESS',
    'SUCCEEDED',
    'FAILED',
    'AUTO_CONFIRMED',
    'EXPIRED'
    );

alter table
    workflow_request_execution_assignees
    modify column
    `status` enum(
    'None',
    'PENDING',
    'APPROVED',
    'REJECTED',
    'IN_PROGRESS',
    'SUCCEEDED',
    'FAILED',
    'AUTO_CONFIRMED',
    'EXPIRED'
    );

alter table
    workflow_request_review_assignees
    modify column
    `status` enum(
    'None',
    'PENDING',
    'APPROVED',
    'REJECTED',
    'IN_PROGRESS',
    'SUCCEEDED',
    'FAILED',
    'AUTO_CONFIRMED',
    'EXPIRED'
    );
