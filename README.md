# RWorkflow
# Getting Started

## Docker Config (MetaData Database)

#### install docker image
```
docker pull mysql:8.0.31
```

#### MySQL server start
```
docker run -d -p 13306:3306 -e MYSQL_ROOT_PASSWORD=password -v /etc/timezone:/etc/timezone -v /etc/localtime:/etc/localtime --name RWorkflow {MYSQL_IMAGE_ID} --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

###### MySQL start/stop/restart

```
# container stop
docker stop RWorkspace

# container start
docker start RWorkspace

#container restart
docker restart RWorkspace
```

#### Access in MySQL Docker Container & Create DB
```
docker exec -it RWorkflow bash
mysql -u root -p
password
create database RWorkflow;
```

## If Test Doesn't Work, Do This
```
Intelij Preferences -> Build, Execution, Deployment -> BuildTools -> Gradle -> Run tests using
change to InteliJ IDEA
```