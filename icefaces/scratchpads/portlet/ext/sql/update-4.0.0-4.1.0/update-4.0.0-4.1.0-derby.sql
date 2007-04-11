alter table Contact_ add jabberSn varchar(75);

alter table Group_ add type_ varchar(75);

create table Groups_Orgs (
	groupId varchar(75) not null,
	organizationId varchar(75) not null,
	primary key (groupId, organizationId)
);

create table Groups_UserGroups (
	groupId varchar(75) not null,
	userGroupId varchar(75) not null,
	primary key (groupId, userGroupId)
);

create table MBStatsUser (
	groupId varchar(75) not null,
	userId varchar(75) not null,
	messageCount integer,
	lastPostDate timestamp,
	primary key (groupId, userId)
);

alter table MBCategory add lastPostDate timestamp;

alter table MBMessage add categoryId varchar(75);

alter table MBThread add categoryId varchar(75);
alter table MBThread add viewCount integer;
alter table MBThread add lastPostByUserId varchar(75);

drop table Properties;

alter table Role_ add description varchar(75);

create table UserGroup (
	userGroupId varchar(75) not null primary key,
	companyId varchar(75) not null,
	parentUserGroupId varchar(75),
	name varchar(75),
	description long varchar
);

create table Users_UserGroups (
	userId varchar(75) not null,
	userGroupId varchar(75) not null,
	primary key (userId, userGroupId)
);
