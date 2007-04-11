create table Account (
	accountId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	parentAccountId varchar(100) null,
	name varchar(100) null,
	legalName varchar(100) null,
	legalId varchar(100) null,
	legalType varchar(100) null,
	sicCode varchar(100) null,
	tickerSymbol varchar(100) null,
	industry varchar(100) null,
	type_ varchar(100) null,
	size_ varchar(100) null,
	website varchar(100) null,
	emailAddress1 varchar(100) null,
	emailAddress2 varchar(100) null
);

alter table Address add pager varchar(100) null;
alter table Address add tollFree varchar(100) null;

drop table BlogsReferer;
create table BlogsReferer (
	refererId varchar(100) not null primary key,
	entryId varchar(100) null,
	url longtext null,
	type_ varchar(100) null,
	quantity integer
);

alter table BookmarksEntry add groupId varchar(100) not null default '';
alter table BookmarksEntry add companyId varchar(100) not null default '';
update BookmarksEntry set groupId = '-1';
update BookmarksEntry set companyId = 'liferay.com';

alter table BookmarksFolder add groupId varchar(100) not null default '';
alter table BookmarksFolder add companyId varchar(100) not null default '';
update BookmarksFolder set groupId = '-1';
update BookmarksFolder set companyId = 'liferay.com';

create table ColorScheme (
	colorSchemeId varchar(100) not null primary key,
	settings_ longtext null
);

create table Contact (
	contactId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	parentContactId varchar(100) null,
	firstName varchar(100) null,
	middleName varchar(100) null,
	lastName varchar(100) null,
	nickName varchar(100) null,
	emailAddress1 varchar(100) null,
	emailAddress2 varchar(100) null,
	smsId varchar(100) null,
	aimId varchar(100) null,
	icqId varchar(100) null,
	msnId varchar(100) null,
	skypeId varchar(100) null,
	ymId varchar(100) null,
	website varchar(100) null,
	male tinyint,
	birthday datetime null,
	timeZoneId varchar(100) null,
	employeeNumber varchar(100) null,
	jobTitle varchar(100) null,
	jobClass varchar(100) null,
	hoursOfOperation longtext null
);

alter table Group_ add friendlyURL varchar(100) null;
alter table Group_ add themeId varchar(100) null;
alter table Group_ add colorSchemeId varchar(100) null;
update Group_ set themeId = 'classic';
update Group_ set colorSchemeId = '01';

update JournalArticle set displayDate = now() where displayDate is NULL;

alter table Image add modifiedDate datetime null;
update Image set modifiedDate = now();

alter table Layout add companyId varchar(100) null;
alter table Layout add parentLayoutId varchar(100) null;
alter table Layout add type_ longtext null;
alter table Layout add typeSettings longtext null;
alter table Layout add friendlyURL varchar(100) null;
alter table Layout add priority integer null;
update Layout set parentLayoutId = '-1' where parentLayoutId is NULL;
update Layout set type_ = 'portlet' where type_ is NULL;

drop table Portlet;
create table Portlet (
	portletId varchar(100) not null,
	companyId varchar(100) not null,
	narrow tinyint,
	roles longtext null,
	active_ tinyint,
	primary key (portletId, companyId)
);

create table Properties (
	companyId varchar(100) not null,
	type_ varchar(100) not null,
	properties longtext null,
	primary key (companyId, type_)
);

alter table ShoppingCart add insure tinyint null;

alter table ShoppingOrder add insure tinyint null;

alter table User_ add themeId varchar(100) null;
alter table User_ add colorSchemeId varchar(100) null;
update User_ set themeId = 'classic';
update User_ set colorSchemeId = '01';
