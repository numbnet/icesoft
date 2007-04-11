create table BlogsCategory (
	categoryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(100) null,
	lastPostDate datetime null
)
go

create table BlogsComments (
	commentsId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	entryId varchar(100) null,
	comments text null
)
go

create table BlogsEntry (
	entryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate datetime null,
	modifiedDate datetime null,
	categoryId varchar(100) null,
	sharing int,
	commentable int,
	content text null
)
go

create table BlogsLink (
	linkId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(100) null,
	url varchar(100) null
)
go

create table BlogsProps (
	propsId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	entryId varchar(100) null,
	quantity int
)
go

create table BlogsReferer (
	entryId varchar(100) not null,
	url varchar(100) not null,
	type_ varchar(100) not null,
	quantity int,
	primary key (entryId, url, type_)
)
go

drop table DLFileProfile;
create table DLFileProfile (
	companyId varchar(100) not null,
	repositoryId varchar(100) not null,
	fileName varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	versionUserId varchar(100) not null,
	versionUserName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	readRoles varchar(100) null,
	writeRoles varchar(100) null,
	description text null,
	version float,
	size_ int,
	primary key (companyId, repositoryId, fileName)
)
go

drop table DLFileVersion;
create table DLFileVersion (
	companyId varchar(100) not null,
	repositoryId varchar(100) not null,
	fileName varchar(100) not null,
	version float not null, 
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	size_ int,
	primary key (companyId, repositoryId, fileName, version)
)
go

create table DLRepository (
	repositoryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	readRoles varchar(100) null,
	writeRoles varchar(100) null,
	name varchar(100) null,
	description varchar(1000) null,
	lastPostDate datetime null
)
go

alter table Layer add href varchar(100) null;
alter table Layer add hrefHover varchar(100) null;

alter table MBMessage add attachments int;
alter table MBMessage add anonymous int;

create table Note (
	noteId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	className varchar(100) null,
	classPK varchar(100) null,
	content text null
)
go

drop table Portlet;
create table Portlet (
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	narrow int,
	defaultPreference text null,
	roles varchar(1000) null,
	active_ int,
	primary key (portletId, groupId, companyId)
)
go

create table ProjFirm (
	firmId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(100) null,
	description varchar(1000) null,
	url varchar(100) null
)
go

create table ProjProject (
	projectId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	firmId varchar(100) null,
	code varchar(100) null,
	name varchar(100) null,
	description varchar(1000) null
)
go

create table ProjTask (
	taskId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	projectId varchar(100) null,
	name varchar(100) null,
	description varchar(1000) null,
	comments text null,
	estimatedDuration int,
	estimatedEndDate datetime null,
	actualDuration int,
	actualEndDate datetime null,
	status int
)
go

create table ProjTime (
	timeId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	projectId varchar(100) null,
	taskId varchar(100) null,
	description varchar(1000) null,
	startDate datetime null,
	endDate datetime null
)
go

alter table User_ add dottedSkins int;

create table Users_ProjProjects (
	userId varchar(100) not null,
	projectId varchar(100) not null
)
go

create table Users_ProjTasks (
	userId varchar(100) not null,
	taskId varchar(100) not null
)
go
