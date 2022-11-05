alter table workflow_request_detail_access_controls add expiration_date datetime(6);

update workflow_request_detail_access_controls
set expiration_date = DATE_ADD(NOW(), INTERVAL 1 DAY);