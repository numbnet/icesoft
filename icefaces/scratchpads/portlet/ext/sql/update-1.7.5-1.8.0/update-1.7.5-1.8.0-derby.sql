alter table Address add country varchar(100);

alter table BJEntry add name varchar(100);
alter table BJEntry add versesInput long varchar;

alter table BJTopic add createDate timestamp;
alter table BJTopic add modifiedDate timestamp;
alter table BJTopic add description clob;

alter table BJVerse add name varchar(100);

alter table Cache rename to Cache_;
delete from Cache_;

alter table CalEvent add remindBy varchar(100);
alter table CalEvent add firstReminder integer;
alter table CalEvent add secondReminder integer;

alter table FileProfile rename to DLFileProfile;
alter table DLFileProfile add modifiedDate timestamp;

alter table VersionedFile rename to DLFileVersion;

alter table ExtranetLink add userName varchar(100);

alter table Group_ add wikiNodeIds varchar(100);

alter table MBTopic add lastPostDate timestamp;

create table NetworkAddress (
	addressId varchar(100) not null primary key,
	userId varchar(100) not null,
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(100),
	url varchar(100),
	comments long varchar,
	content clob,
	status integer,
	lastUpdated timestamp,
	notifyBy varchar(100),
	interval_ integer,
	active_ smallint
);

alter table PollsQuestion add lastVoteDate timestamp;

alter table Portlet add active_ smallint;

delete from PortletPreference where portletId = '4';

alter table SSEntry add createDate timestamp;
alter table SSEntry add modifiedDate timestamp;

alter table User_ add loginDate timestamp;
alter table User_ add loginIP varchar(100);
alter table User_ add active_ smallint;

create table UserTracker (
	userTrackerId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	modifiedDate timestamp,
	remoteAddr varchar(100),
	remoteHost varchar(100),
	userAgent varchar(100)
);

create table UserTrackerPath (
	userTrackerPathId varchar(100) not null primary key,
	userTrackerId varchar(100) not null,
	path varchar(100) not null,
	pathDate timestamp not null
);

create table WikiNode (
	nodeId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	readRoles varchar(100),
	writeRoles varchar(100),
	name varchar(100),
	description long varchar,
	sharing smallint,
	lastPostDate timestamp
);

create table WikiPage (
	nodeId varchar(100) not null,
	title varchar(100) not null,
	version double not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	content clob,
	format varchar(100),
	head smallint,
	primary key (nodeId, title, version)
);
