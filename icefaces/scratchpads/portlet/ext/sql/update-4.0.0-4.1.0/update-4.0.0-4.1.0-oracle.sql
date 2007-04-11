alter table Contact_ add jabberSn varchar2(75) null;

alter table Group_ add type_ varchar2(75) null;

create table Groups_Orgs (
	groupId varchar2(75) not null,
	organizationId varchar2(75) not null,
	primary key (groupId, organizationId)
);

create table Groups_UserGroups (
	groupId varchar2(75) not null,
	userGroupId varchar2(75) not null,
	primary key (groupId, userGroupId)
);

create table MBStatsUser (
	groupId varchar2(75) not null,
	userId varchar2(75) not null,
	messageCount number(30,0),
	lastPostDate timestamp null,
	primary key (groupId, userId)
);

alter table MBCategory add lastPostDate timestamp null;

alter table MBMessage add categoryId varchar2(75) null;

alter table MBThread add categoryId varchar2(75) null;
alter table MBThread add viewCount number(30,0);
alter table MBThread add lastPostByUserId varchar2(75) null;

drop table Properties;

alter table Role_ add description varchar2(75) null;

create table UserGroup (
	userGroupId varchar2(75) not null primary key,
	companyId varchar2(75) not null,
	parentUserGroupId varchar2(75) null,
	name varchar2(75) null,
	description varchar2(4000) null
);

create table Users_UserGroups (
	userId varchar2(75) not null,
	userGroupId varchar2(75) not null,
	primary key (userId, userGroupId)
);
