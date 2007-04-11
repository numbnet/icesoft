create table BlogsCategory (
	categoryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate date null,
	modifiedDate date null,
	name varchar(100) null,
	lastPostDate date null
);

create table BlogsComments (
	commentsId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	entryId varchar(100) null,
	comments long varchar null
);

create table BlogsEntry (
	entryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate date null,
	modifiedDate date null,
	categoryId varchar(100) null,
	sharing boolean,
	commentable boolean,
	content long varchar null
);

create table BlogsLink (
	linkId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate date null,
	modifiedDate date null,
	name varchar(100) null,
	url varchar(100) null
);

create table BlogsProps (
	propsId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	entryId varchar(100) null,
	quantity integer
);

create table BlogsReferer (
	entryId varchar(100) not null,
	url varchar(100) not null,
	type_ varchar(100) not null,
	quantity integer,
	primary key (entryId, url, type_)
);

drop table DLFileProfile;
create table DLFileProfile (
	companyId varchar(100) not null,
	repositoryId varchar(100) not null,
	fileName varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	versionUserId varchar(100) not null,
	versionUserName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	readRoles varchar(100) null,
	writeRoles varchar(100) null,
	description long varchar null,
	version double,
	size_ integer,
	primary key (companyId, repositoryId, fileName)
);

drop table DLFileVersion;
create table DLFileVersion (
	companyId varchar(100) not null,
	repositoryId varchar(100) not null,
	fileName varchar(100) not null,
	version double not null, 
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	size_ integer,
	primary key (companyId, repositoryId, fileName, version)
);

create table DLRepository (
	repositoryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	readRoles varchar(100) null,
	writeRoles varchar(100) null,
	name varchar(100) null,
	description long varchar null,
	lastPostDate date null
);

alter table Layer add href varchar(100) null;
alter table Layer add hrefHover varchar(100) null;

alter table MBMessage add attachments boolean;
alter table MBMessage add anonymous boolean;

create table Note (
	noteId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	className varchar(100) null,
	classPK varchar(100) null,
	content long varchar null
);

drop table Portlet;
create table Portlet (
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	narrow boolean,
	defaultPreference long varchar null,
	roles long varchar null,
	active_ boolean,
	primary key (portletId, groupId, companyId)
);

create table ProjFirm (
	firmId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	name varchar(100) null,
	description long varchar null,
	url varchar(100) null
);

create table ProjProject (
	projectId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	firmId varchar(100) null,
	code varchar(100) null,
	name varchar(100) null,
	description long varchar null
);

create table ProjTask (
	taskId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	projectId varchar(100) null,
	name varchar(100) null,
	description long varchar null,
	comments long varchar null,
	estimatedDuration integer,
	estimatedEndDate date null,
	actualDuration integer,
	actualEndDate date null,
	status integer
);

create table ProjTime (
	timeId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	projectId varchar(100) null,
	taskId varchar(100) null,
	description long varchar null,
	startDate date null,
	endDate date null
);

alter table User_ add dottedSkins boolean;

create table Users_ProjProjects (
	userId varchar(100) not null,
	projectId varchar(100) not null
);

create table Users_ProjTasks (
	userId varchar(100) not null,
	taskId varchar(100) not null
);
