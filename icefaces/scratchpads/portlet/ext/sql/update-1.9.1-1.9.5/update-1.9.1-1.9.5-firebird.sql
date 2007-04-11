create table BlogsCategory (
	categoryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(100),
	lastPostDate timestamp
);

create table BlogsComments (
	commentsId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	entryId varchar(100),
	comments blob
);

create table BlogsEntry (
	entryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate timestamp,
	modifiedDate timestamp,
	categoryId varchar(100),
	sharing smallint,
	commentable smallint,
	content blob
);

create table BlogsLink (
	linkId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(100),
	url varchar(100)
);

create table BlogsProps (
	propsId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	entryId varchar(100),
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
	userName varchar(100),
	versionUserId varchar(100) not null,
	versionUserName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	readRoles varchar(100),
	writeRoles varchar(100),
	description blob,
	version double precision,
	size_ integer,
	primary key (companyId, repositoryId, fileName)
);

drop table DLFileVersion;
create table DLFileVersion (
	companyId varchar(100) not null,
	repositoryId varchar(100) not null,
	fileName varchar(100) not null,
	version double precision not null, 
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	size_ integer,
	primary key (companyId, repositoryId, fileName, version)
);

create table DLRepository (
	repositoryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	readRoles varchar(100),
	writeRoles varchar(100),
	name varchar(100),
	description varchar(4000),
	lastPostDate timestamp
);

alter table Layer add href varchar(100);
alter table Layer add hrefHover varchar(100);

alter table MBMessage add attachments smallint;
alter table MBMessage add anonymous smallint;

create table Note (
	noteId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(100),
	classPK varchar(100),
	content blob
);

drop table Portlet;
create table Portlet (
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	narrow smallint,
	defaultPreference blob,
	roles varchar(4000),
	active_ smallint,
	primary key (portletId, groupId, companyId)
);

create table ProjFirm (
	firmId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(100),
	description varchar(4000),
	url varchar(100)
);

create table ProjProject (
	projectId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	firmId varchar(100),
	code varchar(100),
	name varchar(100),
	description varchar(4000)
);

create table ProjTask (
	taskId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	projectId varchar(100),
	name varchar(100),
	description varchar(4000),
	comments blob,
	estimatedDuration integer,
	estimatedEndDate timestamp,
	actualDuration integer,
	actualEndDate timestamp,
	status integer
);

create table ProjTime (
	timeId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	projectId varchar(100),
	taskId varchar(100),
	description varchar(4000),
	startDate timestamp,
	endDate timestamp
);

alter table User_ add dottedSkins smallint;

create table Users_ProjProjects (
	userId varchar(100) not null,
	projectId varchar(100) not null
);

create table Users_ProjTasks (
	userId varchar(100) not null,
	taskId varchar(100) not null
);
