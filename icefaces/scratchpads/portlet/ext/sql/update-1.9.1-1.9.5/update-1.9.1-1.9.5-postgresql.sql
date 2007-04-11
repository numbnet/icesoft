create table BlogsCategory (
	categoryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(100) null,
	lastPostDate timestamp null
);

create table BlogsComments (
	commentsId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	entryId varchar(100) null,
	comments text null
);

create table BlogsEntry (
	entryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	categoryId varchar(100) null,
	sharing bool,
	commentable bool,
	content text null
);

create table BlogsLink (
	linkId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(100) null,
	url varchar(100) null
);

create table BlogsProps (
	propsId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
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
	createDate timestamp null,
	modifiedDate timestamp null,
	readRoles varchar(100) null,
	writeRoles varchar(100) null,
	description text null,
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
	userName varchar(100) null,
	createDate timestamp null,
	size_ integer,
	primary key (companyId, repositoryId, fileName, version)
);

create table DLRepository (
	repositoryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	readRoles varchar(100) null,
	writeRoles varchar(100) null,
	name varchar(100) null,
	description text null,
	lastPostDate timestamp null
);

alter table Layer add href varchar(100) null;
alter table Layer add hrefHover varchar(100) null;

alter table MBMessage add attachments bool;
alter table MBMessage add anonymous bool;

create table Note (
	noteId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar(100) null,
	classPK varchar(100) null,
	content text null
);

drop table Portlet;
create table Portlet (
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	narrow bool,
	defaultPreference text null,
	roles text null,
	active_ bool,
	primary key (portletId, groupId, companyId)
);

create table ProjFirm (
	firmId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(100) null,
	description text null,
	url varchar(100) null
);

create table ProjProject (
	projectId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	firmId varchar(100) null,
	code varchar(100) null,
	name varchar(100) null,
	description text null
);

create table ProjTask (
	taskId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	projectId varchar(100) null,
	name varchar(100) null,
	description text null,
	comments text null,
	estimatedDuration integer,
	estimatedEndDate timestamp null,
	actualDuration integer,
	actualEndDate timestamp null,
	status integer
);

create table ProjTime (
	timeId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	projectId varchar(100) null,
	taskId varchar(100) null,
	description text null,
	startDate timestamp null,
	endDate timestamp null
);

alter table User_ add dottedSkins bool;

create table Users_ProjProjects (
	userId varchar(100) not null,
	projectId varchar(100) not null
);

create table Users_ProjTasks (
	userId varchar(100) not null,
	taskId varchar(100) not null
);
