alter table Address add country varchar(100) null;

alter table BJEntry add name varchar(100) null;
alter table BJEntry add versesInput varchar(1000) null;

alter table BJTopic add createDate datetime null;
alter table BJTopic add modifiedDate datetime null;
alter table BJTopic add description text null;

alter table BJVerse add name varchar(100) null;

alter table Cache rename to Cache_;
delete from Cache_;

alter table CalEvent add remindBy varchar(100) null;
alter table CalEvent add firstReminder int;
alter table CalEvent add secondReminder int;

alter table FileProfile rename to DLFileProfile;
alter table DLFileProfile add modifiedDate datetime null;

alter table VersionedFile rename to DLFileVersion;

alter table ExtranetLink add userName varchar(100) null;

alter table Group_ add wikiNodeIds varchar(100) null;

alter table MBTopic add lastPostDate datetime null;

create table NetworkAddress (
	addressId varchar(100) not null primary key,
	userId varchar(100) not null,
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(100) null,
	url varchar(100) null,
	comments varchar(1000) null,
	content text null,
	status int,
	lastUpdated datetime null,
	notifyBy varchar(100) null,
	interval_ int,
	active_ int
)
go

alter table PollsQuestion add lastVoteDate datetime null;

alter table Portlet add active_ int;

delete from PortletPreference where portletId = '4';

alter table SSEntry add createDate datetime null;
alter table SSEntry add modifiedDate datetime null;

alter table User_ add loginDate datetime null;
alter table User_ add loginIP varchar(100) null;
alter table User_ add active_ int;

create table UserTracker (
	userTrackerId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	modifiedDate datetime null,
	remoteAddr varchar(100) null,
	remoteHost varchar(100) null,
	userAgent varchar(100) null
)
go

create table UserTrackerPath (
	userTrackerPathId varchar(100) not null primary key,
	userTrackerId varchar(100) not null,
	path varchar(100) not null,
	pathDate datetime not null
)
go

create table WikiNode (
	nodeId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	readRoles varchar(100) null,
	writeRoles varchar(100) null,
	name varchar(100) null,
	description varchar(1000) null,
	sharing int,
	lastPostDate datetime null
)
go

create table WikiPage (
	nodeId varchar(100) not null,
	title varchar(100) not null,
	version float not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	content text null,
	format varchar(100) null,
	head int,
	primary key (nodeId, title, version)
)
go
