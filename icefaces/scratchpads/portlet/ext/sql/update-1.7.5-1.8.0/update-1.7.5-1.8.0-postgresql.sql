alter table Address add country varchar(100) null;

alter table BJEntry add name varchar(100) null;
alter table BJEntry add versesInput text null;

alter table BJTopic add createDate timestamp null;
alter table BJTopic add modifiedDate timestamp null;
alter table BJTopic add description text null;

alter table BJVerse add name varchar(100) null;

alter table Cache rename to Cache_;
delete from Cache_;

alter table CalEvent add remindBy varchar(100) null;
alter table CalEvent add firstReminder integer;
alter table CalEvent add secondReminder integer;

alter table FileProfile rename to DLFileProfile;
alter table DLFileProfile add modifiedDate timestamp null;

alter table VersionedFile rename to DLFileVersion;

alter table ExtranetLink add userName varchar(100) null;

alter table Group_ add wikiNodeIds varchar(100) null;

alter table MBTopic add lastPostDate timestamp null;

create table NetworkAddress (
	addressId varchar(100) not null primary key,
	userId varchar(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(100) null,
	url varchar(100) null,
	comments text null,
	content text null,
	status integer,
	lastUpdated timestamp null,
	notifyBy varchar(100) null,
	interval_ integer,
	active_ bool
);

alter table PollsQuestion add lastVoteDate timestamp null;

alter table Portlet add active_ bool;

delete from PortletPreference where portletId = '4';

alter table SSEntry add createDate timestamp null;
alter table SSEntry add modifiedDate timestamp null;

alter table User_ add loginDate timestamp null;
alter table User_ add loginIP varchar(100) null;
alter table User_ add active_ bool;

create table UserTracker (
	userTrackerId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	modifiedDate timestamp null,
	remoteAddr varchar(100) null,
	remoteHost varchar(100) null,
	userAgent varchar(100) null
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
	userName varchar(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	readRoles varchar(100) null,
	writeRoles varchar(100) null,
	name varchar(100) null,
	description text null,
	sharing bool,
	lastPostDate timestamp null
);

create table WikiPage (
	nodeId varchar(100) not null,
	title varchar(100) not null,
	version double precision not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate timestamp null,
	content text null,
	format varchar(100) null,
	head bool,
	primary key (nodeId, title, version)
);
