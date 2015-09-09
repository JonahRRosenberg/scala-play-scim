
# Users schema

# --- !Ups

CREATE TABLE users(
  ID BIGINT(20) NOT NULL AUTO_INCREMENT,
  userName VARCHAR(255) NOT NULL,
  displayName VARCHAR(255) NOT NULL,
  created DATETIME NOT NULL,
  lastModified DATETIME NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE groups(
  ID BIGINT(20) NOT NULL AUTO_INCREMENT,
  displayName VARCHAR(255) NOT NULL,
  created DATETIME NOT NULL,
  lastModified DATETIME NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE users;
DROP TABLE groups;

