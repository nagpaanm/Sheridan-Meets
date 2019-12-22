DROP DATABASE IF EXISTS assignment4;
CREATE DATABASE assignment4;
USE assignment4;


create table SEC_USER
(
  userId           BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  userName         VARCHAR(36) NOT NULL UNIQUE,
  firstName		   VARCHAR(36) NOT NULL,
  encryptedPassword VARCHAR(128) NOT NULL,
  age			   INT NOT NULL,
  gender		   VARCHAR(32) NOT NULL,
  ethnicity		   VARCHAR(64) NOT NULL,
  campus			VARCHAR(32) NOT NULL,
  program			VARCHAR(96) NOT NULL,
  bio				VARCHAR(200) NOT NULL,
  interestedIn		VARCHAR(32) NOT NULL,
  showMe			VARCHAR(32) NOT NULL,
  picBytes			LONGBLOB,
  likes				BIGINT NOT NULL,
  matches			BIGINT NOT NULL,
  ENABLED           BIT NOT NULL 
);

create table SEC_ROLE
(
  roleId   BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  roleName VARCHAR(30) NOT NULL UNIQUE
) ;

create table USER_ROLE
(
  ID      BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  userId BIGINT NOT NULL,
  roleId BIGINT NOT NULL
);

alter table USER_ROLE
  add constraint USER_ROLE_UK unique (userId, roleId);

alter table USER_ROLE
  add constraint USER_ROLE_FK1 foreign key (userId)
  references SEC_USER (userId);
 
alter table USER_ROLE
  add constraint USER_ROLE_FK2 foreign key (roleId)
  references SEC_ROLE (roleId);

insert into SEC_USER (userName, firstName, encryptedPassword, age, gender, ethnicity, campus, program, bio, interestedIn, showMe, likes, matches, ENABLED)
values ('smadmin', 'smadmin', '$2a$10$qPtWFeK7rNh8PRK5HlD8e.AqaR0O5gI9wqXcRRsd4z/Icj7VQnDkK', 0, '-', '-', '-', '-', '-', '-', '-', 0, 0, 1);

insert into sec_role (roleName)
values ('ROLE_ADMIN');
 
insert into sec_role (roleName)
values ('ROLE_USER');

insert into user_role (userId, roleId)
values (1, 1);
 
create table USER_LIKE
(
	userId BIGINT NOT NULL,
	userMatch VARCHAR(36)
);

create table USER_DISLIKE
(
	userId BIGINT NOT NULL,
	userMatch VARCHAR(36)
);

create table USER_MATCH
(
	userName1 VARCHAR(36) NOT NULL,
	userName2 VARCHAR(36) NOT NULL
);

create table USER_MESSAGES
(
	message_id bigint primary key auto_increment,
	from_ VARCHAR(36) not null,
	to_ VARCHAR(36) not null,
	msg VARCHAR(105) not null
);

create table user_filter
(
	userName VARCHAR(36) NOT NULL UNIQUE,
	campus VARCHAR(32) NOT NULL,
	program VARCHAR(96) NOT NULL,
	ethnicity VARCHAR(64) NOT NULL,
	interestedIn	VARCHAR(32) NOT NULL,
	showMe		VARCHAR(32) NOT NULL
);

COMMIT;
