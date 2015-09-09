
# Users schema

# --- !Ups

CREATE TABLE users(
  ID BIGINT(20) NOT NULL AUTO_INCREMENT,
  userName VARCHAR(255) NOT NULL,
  displayName VARCHAR(255) NOT NULL,
  created DATETIME NOT NULL,
  lastModified DATETIME NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY userName (userName)
);

CREATE TABLE groups(
  ID BIGINT(20) NOT NULL AUTO_INCREMENT,
  displayName VARCHAR(255) NOT NULL,
  created DATETIME NOT NULL,
  lastModified DATETIME NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY displayName (displayName)
);

CREATE TABLE users_groups_mapping(
  userid BIGINT(20) NOT NULL,
  groupid BIGINT(20) NOT NULL,
  FOREIGN KEY(userid) REFERENCES users(ID),
  FOREIGN KEY(groupid) REFERENCES groups(ID)
)

# --- !Downs

DROP TABLE users;
DROP TABLE groups;

