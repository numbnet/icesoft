create table BlogsCategory (
	categoryId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(100) null,
	lastPostDate timestamp null
);

create table BlogsComments (
	commentsId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	entryId varchar2(100) null,
	comments clob null
);

create table BlogsEntry (
	entryId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	categoryId varchar2(100) null,
	sharing number(1, 0),
	commentable number(1, 0),
	content clob null
);

create table BlogsLink (
	linkId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(100) null,
	url varchar2(100) null
);

create table BlogsProps (
	propsId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	entryId varchar2(100) null,
	quantity number(30,0)
);

create table BlogsReferer (
	entryId varchar2(100) not null,
	url varchar2(100) not null,
	type_ varchar2(100) not null,
	quantity number(30,0),
	primary key (entryId, url, type_)
);

drop table DLFileProfile;
create table DLFileProfile (
	companyId varchar2(100) not null,
	repositoryId varchar2(100) not null,
	fileName varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	versionUserId varchar2(100) not null,
	versionUserName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	readRoles varchar2(100) null,
	writeRoles varchar2(100) null,
	description clob null,
	version number(30,20),
	size_ number(30,0),
	primary key (companyId, repositoryId, fileName)
);

drop table DLFileVersion;
create table DLFileVersion (
	companyId varchar2(100) not null,
	repositoryId varchar2(100) not null,
	fileName varchar2(100) not null,
	version number(30,20) not null, 
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	size_ number(30,0),
	primary key (companyId, repositoryId, fileName, version)
);

create table DLRepository (
	repositoryId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	readRoles varchar2(100) null,
	writeRoles varchar2(100) null,
	name varchar2(100) null,
	description varchar2(4000) null,
	lastPostDate timestamp null
);

alter table Layer add href varchar2(100) null;
alter table Layer add hrefHover varchar2(100) null;

alter table MBMessage add attachments number(1, 0);
alter table MBMessage add anonymous number(1, 0);

create table Note (
	noteId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar2(100) null,
	classPK varchar2(100) null,
	content clob null
);

drop table Portlet;
create table Portlet (
	portletId varchar2(100) not null,
	groupId varchar2(100) not null,
	companyId varchar2(100) not null,
	narrow number(1, 0),
	defaultPreference clob null,
	roles varchar2(4000) null,
	active_ number(1, 0),
	primary key (portletId, groupId, companyId)
);

create table ProjFirm (
	firmId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(100) null,
	description varchar2(4000) null,
	url varchar2(100) null
);

create table ProjProject (
	projectId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	firmId varchar2(100) null,
	code varchar2(100) null,
	name varchar2(100) null,
	description varchar2(4000) null
);

create table ProjTask (
	taskId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	projectId varchar2(100) null,
	name varchar2(100) null,
	description varchar2(4000) null,
	comments clob null,
	estimatedDuration number(30,0),
	estimatedEndDate timestamp null,
	actualDuration number(30,0),
	actualEndDate timestamp null,
	status number(30,0)
);

create table ProjTime (
	timeId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	projectId varchar2(100) null,
	taskId varchar2(100) null,
	description varchar2(4000) null,
	startDate timestamp null,
	endDate timestamp null
);

alter table User_ add dottedSkins number(1, 0);

create table Users_ProjProjects (
	userId varchar2(100) not null,
	projectId varchar2(100) not null
);

create table Users_ProjTasks (
	userId varchar2(100) not null,
	taskId varchar2(100) not null
);
