create table workflow_requests (
                                   id int,
                                   title varchar(255),
                                   type enum ('ACCESS_CONTROL', 'SQL_EXECUTION', 'DATA_EXPORT'),
                                   rule_id int,
                                   urgent tinyint,
                                   comment text,
                                   approval_status enum ('PENDING', 'IN_PROGRESS', 'APPROVED', 'REJECTED'),
                                   execution_status enum ('NONE', 'PENDING', 'EXECUTED', 'SUCCEEDED', 'FAILED'),
                                   review_status enum ('NONE', 'PENDING', 'CONFIRMED'),
                                   requested_at datetime(6),
                                   requested_by int,
                                   expired tinyint,
                                   canceled tinyint,
                                   canceled_at datetime(6),
                                   canceled_by int,
                                   created_at datetime(6),
                                   created_by int,
                                   updated_at datetime(6),
                                   updated_by int,
                                   primary key (id)
);

create table workflow_request_approvals (
                                            id int,
                                            request_id int,
                                            `order` int,
                                            approve_type enum ('ANY', 'ALL'),
                                            status enum ('PENDING', 'IN_PROGRESS', 'APPROVED', 'REJECTED'),
                                            primary key (id),
                                            foreign key (request_id) references workflow_requests (id)
);

create table workflow_request_approval_assignees (
                                                     id int,
                                                     request_id int,
                                                     request_approval_id int,
                                                     assignee_id int,
                                                     action_at datetime(6),
                                                     action_by int,
                                                     status enum ('PENDING', 'APPROVED', 'REJECTED', 'CONFIRMED'),
                                                     primary key (id),
                                                     #     foreign key (request_id) references workflow_requests (id),
                                                     foreign key (request_approval_id) references workflow_request_approvals (id)
);

create table workflow_request_execution_assignees (
                                                      id int,
                                                      request_id int,
                                                      assignee_id int,
                                                      action_at datetime(6),
                                                      action_by int,
                                                      finished_at datetime(6),
                                                      status enum ('PENDING', 'EXECUTED', 'SUCCEEDED', 'FAILED'),
                                                      primary key (id),
                                                      foreign key (request_id) references workflow_requests (id)
);

create table workflow_request_review_assignees (
                                                   id int,
                                                   request_id int,
                                                   assignee_id int,
                                                   action_at datetime(6),
                                                   action_by int,
                                                   status enum ('PENDING', 'CONFIRMED'),
                                                   primary key (id),
                                                   foreign key (request_id) references workflow_requests (id)
);

create table workflow_request_detail_access_controls (
                                                         id int,
                                                         request_id int,
                                                         expiration_date datetime(6),
                                                         primary key (id),
                                                         foreign key (request_id) references workflow_requests (id)
);

create table workflow_request_detail_access_control_connections (
                                                                    id int,
                                                                    detail_access_control_id int,
                                                                    connection_id int,
                                                                    privilege_id int,
                                                                    granted tinyint default 0,
                                                                    primary key (id),
                                                                    foreign key (detail_access_control_id) references workflow_request_detail_access_controls (id)
);

create table workflow_request_detail_sql_executions (
                                                        id int,
                                                        request_id int,
                                                        connection_id int,
                                                        `database` varchar(255),
                                                        content_type enum ('TEXT', 'FILE'),
                                                        content_value text,
                                                        execution_expiry_at datetime(6),
                                                        request_expiry_at datetime(6),
                                                        primary key (id),
                                                        foreign key (request_id) references workflow_requests (id)
);

create table workflow_request_detail_data_exports (
                                                      id int,
                                                      request_id int,
                                                      connection_id int,
                                                      `database` varchar(255),
                                                      content_type enum ('QUERY', 'TABLE'),
                                                      content_value text,
                                                      execution_expiry_at datetime(6),
                                                      request_expiry_at datetime(6),
                                                      primary key (id),
                                                      foreign key (request_id) references workflow_requests (id)
);

create table workflow_rules (
                                id int,
                                name varchar(255),
                                request_type enum ('ACCESS_CONTROL', 'SQL_EXECUTION', 'DATA_EXPORT'),
                                urgent tinyint,
                                deleted tinyint,
                                created_at datetime(6),
                                created_by int,
                                updated_at datetime(6),
                                updated_by int,
                                primary key (id)
);

create table workflow_rule_approvals (
                                         id int,
                                         rule_id int,
                                         `order` int,
                                         approve_type enum ('ANY', 'ALL'),
                                         primary key (id),
                                         foreign key (rule_id) references workflow_rules (id)
);

create table workflow_rule_approval_assignees (
                                                  id int,
                                                  rule_approval_id int,
                                                  assignee_type enum ('USER', 'ROLE'),
                                                  assignee_value int,
                                                  primary key (id),
                                                  foreign key (rule_approval_id) references workflow_rule_approvals (id)
);

create table workflow_rule_execution_assignees (
                                                   id int,
                                                   rule_id int,
                                                   assignee_type enum ('USER', 'ROLE'),
                                                   assignee_value int,
                                                   primary key (id),
                                                   foreign key (rule_id) references workflow_rules (id)
);

create table workflow_rule_review_assignees (
                                                id int,
                                                rule_id int,
                                                assignee_type enum ('USER', 'ROLE'),
                                                assignee_value int,
                                                primary key (id),
                                                foreign key (rule_id) references workflow_rules (id)
);

create table users (
                       id int,
                       username varchar(64),
                       password varchar(64),
                       name varchar(64),
                       email varchar(64),
                       deleted tinyint,
                       primary key (id)
);

create table roles (
                       id int,
                       name varchar(64),
                       deleted tinyint,
                       primary key (id)
);

create table user_roles (
                            id int,
                            user_id int,
                            role_id int,
                            deleted tinyint,
                            primary key (id),
                            foreign key (user_id) references users (id),
                            foreign key (role_id) references roles (id)
);

create table connection (
                            id int,
                            name varchar(64),
                            database_type enum ('MYSQL', 'POSTGRESQL'),
                            host varchar(512),
                            port int,
                            username varchar(64),
                            password varchar(64),
                            deleted tinyint,
                            primary key (id)
);

create table privileges (
                            id int,
                            name varchar(64),
                            permissions text comment 'SELECT,INSERT,UPDATE,DELETE',
                            deleted tinyint,
                            primary key (id)
);

create table user_access_controls (
                                      id int,
                                      user_id int,
                                      connection_id int,
                                      privilege_id int,
                                      expired tinyint,
                                      expiry_at datetime(6),
                                      deleted tinyint,
                                      created_at datetime(6),
                                      created_by int,
                                      updated_at datetime(6),
                                      updated_by int,
                                      primary key (id),
                                      foreign key (user_id) references users (id),
                                      foreign key (connection_id) references connection (id),
                                      foreign key (privilege_id) references privileges (id)
);