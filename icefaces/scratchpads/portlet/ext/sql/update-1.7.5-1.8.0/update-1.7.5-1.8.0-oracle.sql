alter table Address add country varchar2(100) null;

alter table BJEntry add name varchar2(100) null;
alter table BJEntry add versesInput varchar2(4000) null;

alter table BJTopic add createDate timestamp null;
alter table BJTopic add modifiedDate timestamp null;
alter table BJTopic add description clob null;

alter table BJVerse add name varchar2(100) null;

alter table Cache rename to Cache_;
delete from Cache_;

alter table CalEvent add remindBy varchar2(100) null;
alter table CalEvent add firstReminder number(30,0);
alter table CalEvent add secondReminder number(30,0);

alter table FileProfile rename to DLFileProfile;
alter table DLFileProfile add modifiedDate timestamp null;

alter table VersionedFile rename to DLFileVersion;

alter table ExtranetLink add userName varchar2(100) null;

alter table Group_ add wikiNodeIds varchar2(100) null;

alter table MBTopic add lastPostDate timestamp null;

create table NetworkAddress (
	addressId varchar2(100) not null primary key,
	userId varchar2(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(100) null,
	url varchar2(100) null,
	comments varchar2(4000) null,
	content clob null,
	status number(30,0),
	lastUpdated timestamp null,
	notifyBy varchar2(100) null,
	interval_ number(30,0),
	active_ number(1, 0)
);

alter table PollsQuestion add lastVoteDate timestamp null;

alter table Portlet add active_ number(1, 0);

delete from PortletPreference where portletId = '4';

alter table SSEntry add createDate timestamp null;
alter table SSEntry add modifiedDate timestamp null;

alter table User_ add loginDate timestamp null;
alter table User_ add loginIP varchar2(100) null;
alter table User_ add active_ number(1, 0);

create table UserTracker (
	userTrackerId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	modifiedDate timestamp null,
	remoteAddr varchar2(100) null,
	remoteHost varchar2(100) null,
	userAgent varchar2(100) null
);

create table UserTrackerPath (
	userTrackerPathId varchar2(100) not null primary key,
	userTrackerId varchar2(100) not null,
	path varchar2(100) not null,
	pathDate timestamp not null
);

create table WikiNode (
	nodeId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	readRoles varchar2(100) null,
	writeRoles varchar2(100) null,
	name varchar2(100) null,
	description varchar2(4000) null,
	sharing number(1, 0),
	lastPostDate timestamp null
);

create table WikiPage (
	nodeId varchar2(100) not null,
	title varchar2(100) not null,
	version number(30,20) not null,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	content clob null,
	format varchar2(100) null,
	head number(1, 0),
	primary key (nodeId, title, version)
);
