create table Account (
	accountId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentAccountId varchar2(100) null,
	name varchar2(100) null,
	legalName varchar2(100) null,
	legalId varchar2(100) null,
	legalType varchar2(100) null,
	sicCode varchar2(100) null,
	tickerSymbol varchar2(100) null,
	industry varchar2(100) null,
	type_ varchar2(100) null,
	size_ varchar2(100) null,
	website varchar2(100) null,
	emailAddress1 varchar2(100) null,
	emailAddress2 varchar2(100) null
);

alter table Address add pager varchar2(100) null;
alter table Address add tollFree varchar2(100) null;

drop table BlogsReferer;
create table BlogsReferer (
	refererId varchar2(100) not null primary key,
	entryId varchar2(100) null,
	url varchar2(4000) null,
	type_ varchar2(100) null,
	quantity number(30,0)
);

alter table BookmarksEntry add groupId varchar2(100) not null default '';
alter table BookmarksEntry add companyId varchar2(100) not null default '';
update BookmarksEntry set groupId = '-1';
update BookmarksEntry set companyId = 'liferay.com';

alter table BookmarksFolder add groupId varchar2(100) not null default '';
alter table BookmarksFolder add companyId varchar2(100) not null default '';
update BookmarksFolder set groupId = '-1';
update BookmarksFolder set companyId = 'liferay.com';

create table ColorScheme (
	colorSchemeId varchar2(100) not null primary key,
	settings_ clob null
);

create table Contact (
	contactId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentContactId varchar2(100) null,
	firstName varchar2(100) null,
	middleName varchar2(100) null,
	lastName varchar2(100) null,
	nickName varchar2(100) null,
	emailAddress1 varchar2(100) null,
	emailAddress2 varchar2(100) null,
	smsId varchar2(100) null,
	aimId varchar2(100) null,
	icqId varchar2(100) null,
	msnId varchar2(100) null,
	skypeId varchar2(100) null,
	ymId varchar2(100) null,
	website varchar2(100) null,
	male number(1, 0),
	birthday timestamp null,
	timeZoneId varchar2(100) null,
	employeeNumber varchar2(100) null,
	jobTitle varchar2(100) null,
	jobClass varchar2(100) null,
	hoursOfOperation varchar2(4000) null
);

alter table Group_ add friendlyURL varchar2(100) null;
alter table Group_ add themeId varchar2(100) null;
alter table Group_ add colorSchemeId varchar2(100) null;
update Group_ set themeId = 'classic';
update Group_ set colorSchemeId = '01';

update JournalArticle set displayDate = sysdate where displayDate is NULL;

alter table Image add modifiedDate timestamp null;
update Image set modifiedDate = sysdate;

alter table Layout add companyId varchar2(100) null;
alter table Layout add parentLayoutId varchar2(100) null;
alter table Layout add type_ varchar2(4000) null;
alter table Layout add typeSettings varchar2(4000) null;
alter table Layout add friendlyURL varchar2(100) null;
alter table Layout add priority number(30,0) null;
update Layout set parentLayoutId = '-1' where parentLayoutId is NULL;
update Layout set type_ = 'portlet' where type_ is NULL;

drop table Portlet;
create table Portlet (
	portletId varchar2(100) not null,
	companyId varchar2(100) not null,
	narrow number(1, 0),
	roles varchar2(4000) null,
	active_ number(1, 0),
	primary key (portletId, companyId)
);

create table Properties (
	companyId varchar2(100) not null,
	type_ varchar2(100) not null,
	properties clob null,
	primary key (companyId, type_)
);

alter table ShoppingCart add insure number(1, 0) null;

alter table ShoppingOrder add insure number(1, 0) null;

alter table User_ add themeId varchar2(100) null;
alter table User_ add colorSchemeId varchar2(100) null;
update User_ set themeId = 'classic';
update User_ set colorSchemeId = '01';
