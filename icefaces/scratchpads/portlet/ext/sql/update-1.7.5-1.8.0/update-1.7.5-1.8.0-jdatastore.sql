alter table Address add country varchar(100) null;

alter table BJEntry add name varchar(100) null;
alter table BJEntry add versesInput long varchar null;

alter table BJTopic add createDate date null;
alter table BJTopic add modifiedDate date null;
alter table BJTopic add description long varchar null;

alter table BJVerse add name varchar(100) null;

alter table Cache rename to Cache_;
delete from Cache_;

alter table CalEvent add remindBy varchar(100) null;
alter table CalEvent add firstReminder integer;
alter table CalEvent add secondReminder integer;

alter table FileProfile rename to DLFileProfile;
alter table DLFileProfile add modifiedDate date null;

alter table VersionedFile rename to DLFileVersion;

alter table ExtranetLink add userName varchar(100) null;

alter table Group_ add wikiNodeIds varchar(100) null;

alter table MBTopic add lastPostDate date null;

create table NetworkAddress (
	addressId varchar(100) not null primary key,
	userId varchar(100) not null,
	createDate date null,
	modifiedDate date null,
	name varchar(100) null,
	url varchar(100) null,
	comments long varchar null,
	content long varchar null,
	status integer,
	lastUpdated date null,
	notifyBy varchar(100) null,
	interval_ integer,
	active_ boolean
);

alter table PollsQuestion add lastVoteDate date null;

alter table Portlet add active_ boolean;

delete from PortletPreference where portletId = '4';

alter table SSEntry add createDate date null;
alter table SSEntry add modifiedDate date null;

alter table User_ add loginDate date null;
alter table User_ add loginIP varchar(100) null;
alter table User_ add active_ boolean;

create table UserTracker (
	userTrackerId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	modifiedDate date null,
	remoteAddr varchar(100) null,
	remoteHost varchar(100) null,
	userAgent varchar(100) null
);

create table UserTrackerPath (
	userTrackerPathId varchar(100) not null primary key,
	userTrackerId varchar(100) not null,
	path varchar(100) not null,
	pathDate date not null
);

create table WikiNode (
	nodeId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	readRoles varchar(100) null,
	writeRoles varchar(100) null,
	name varchar(100) null,
	description long varchar null,
	sharing boolean,
	lastPostDate date null
);

create table WikiPage (
	nodeId varchar(100) not null,
	title varchar(100) not null,
	version double not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	content long varchar null,
	format varchar(100) null,
	head boolean,
	primary key (nodeId, title, version)
);
