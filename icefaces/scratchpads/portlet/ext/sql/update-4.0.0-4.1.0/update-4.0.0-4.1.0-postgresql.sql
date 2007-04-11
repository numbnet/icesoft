alter table Contact_ add jabberSn varchar(75) null;

alter table Group_ add type_ varchar(75) null;

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
	lastPostDate timestamp null,
	primary key (groupId, userId)
);

alter table MBCategory add lastPostDate timestamp null;

alter table MBMessage add categoryId varchar(75) null;

alter table MBThread add categoryId varchar(75) null;
alter table MBThread add viewCount integer;
alter table MBThread add lastPostByUserId varchar(75) null;

drop table Properties;

alter table Role_ add description varchar(75) null;

create table UserGroup (
	userGroupId varchar(75) not null primary key,
	companyId varchar(75) not null,
	parentUserGroupId varchar(75) null,
	name varchar(75) null,
	description text null
);

create table Users_UserGroups (
	userId varchar(75) not null,
	userGroupId varchar(75) not null,
	primary key (userId, userGroupId)
);
