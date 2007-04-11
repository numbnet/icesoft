alter table Address add country varchar(100) null;

alter table BJEntry add name varchar(100) null;
alter table BJEntry add versesInput varchar null;

alter table BJTopic add createDate timestamp null;
alter table BJTopic add modifiedDate timestamp null;
alter table BJTopic add description varchar null;

alter table BJVerse add name varchar(100) null;

alter table Cache rename to Cache_;
delete from Cache_;

alter table CalEvent add remindBy varchar(100) null;
alter table CalEvent add firstReminder int;
alter table CalEvent add secondReminder int;

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
	comments varchar null,
	content varchar null,
	status int,
	lastUpdated timestamp null,
	notifyBy varchar(100) null,
	interval_ int,
	active_ boolean
);

alter table PollsQuestion add lastVoteDate timestamp null;

alter table Portlet add active_ boolean;

delete from PortletPreference where portletId = '4';

alter table SSEntry add createDate timestamp null;
alter table SSEntry add modifiedDate timestamp null;

alter table User_ add loginDate timestamp null;
alter table User_ add loginIP varchar(100) null;
alter table User_ add active_ boolean;

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
	description varchar null,
	sharing boolean,
	lastPostDate timestamp null
);

create table WikiPage (
	nodeId varchar(100) not null,
	title varchar(100) not null,
	version float not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate timestamp null,
	content varchar null,
	format varchar(100) null,
	head boolean,
	primary key (nodeId, title, version)
);
