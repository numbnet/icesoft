alter table Address add country varchar(100) null;

alter table BJEntry add name varchar(100) null;
alter table BJEntry add versesInput longtext null;

alter table BJTopic add createDate datetime null;
alter table BJTopic add modifiedDate datetime null;
alter table BJTopic add description longtext null;

alter table BJVerse add name varchar(100) null;

alter table Cache rename to Cache_;
delete from Cache_;

alter table CalEvent add remindBy varchar(100) null;
alter table CalEvent add firstReminder integer;
alter table CalEvent add secondReminder integer;

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
	comments longtext null,
	content longtext null,
	status integer,
	lastUpdated datetime null,
	notifyBy varchar(100) null,
	interval_ integer,
	active_ tinyint
);

alter table PollsQuestion add lastVoteDate datetime null;

alter table Portlet add active_ tinyint;

delete from PortletPreference where portletId = '4';

alter table SSEntry add createDate datetime null;
alter table SSEntry add modifiedDate datetime null;

alter table User_ add loginDate datetime null;
alter table User_ add loginIP varchar(100) null;
alter table User_ add active_ tinyint;

create table UserTracker (
	userTrackerId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	modifiedDate datetime null,
	remoteAddr varchar(100) null,
	remoteHost varchar(100) null,
	userAgent varchar(100) null
);

create table UserTrackerPath (
	userTrackerPathId varchar(100) not null primary key,
	userTrackerId varchar(100) not null,
	path varchar(100) not null,
	pathDate datetime not null
);

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
	description longtext null,
	sharing tinyint,
	lastPostDate datetime null
);

create table WikiPage (
	nodeId varchar(100) not null,
	title varchar(100) not null,
	version double not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	content longtext null,
	format varchar(100) null,
	head tinyint,
	primary key (nodeId, title, version)
);
