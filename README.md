# Initial Scim Protocol API using Scala and the Play Framework
http://www.simplecloud.info/

Supports the following api requests:
```
GET     ${HOST}/scim/v1/Users (with optional filter parameter)
GET     ${HOST}/scim/v1/Users/:uid
POST    ${HOST}/scim/v1/Users
PUT     ${HOST}/scim/v1/Users/:uid
DELETE  ${HOST}/scim/v1/Users/:uid
GET     ${HOST}/scim/v1/Groups
```
