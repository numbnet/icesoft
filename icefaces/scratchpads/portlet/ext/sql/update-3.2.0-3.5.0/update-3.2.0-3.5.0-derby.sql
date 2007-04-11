create table Account (
	accountId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	parentAccountId varchar(100),
	name varchar(100),
	legalName varchar(100),
	legalId varchar(100),
	legalType varchar(100),
	sicCode varchar(100),
	tickerSymbol varchar(100),
	industry varchar(100),
	type_ varchar(100),
	size_ varchar(100),
	website varchar(100),
	emailAddress1 varchar(100),
	emailAddress2 varchar(100)
);

alter table Address add pager varchar(100);
alter table Address add tollFree varchar(100);

drop table BlogsReferer;
create table BlogsReferer (
	refererId varchar(100) not null primary key,
	entryId varchar(100),
	url long varchar,
	type_ varchar(100),
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
	settings_ clob
);

create table Contact (
	contactId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	parentContactId varchar(100),
	firstName varchar(100),
	middleName varchar(100),
	lastName varchar(100),
	nickName varchar(100),
	emailAddress1 varchar(100),
	emailAddress2 varchar(100),
	smsId varchar(100),
	aimId varchar(100),
	icqId varchar(100),
	msnId varchar(100),
	skypeId varchar(100),
	ymId varchar(100),
	website varchar(100),
	male smallint,
	birthday timestamp,
	timeZoneId varchar(100),
	employeeNumber varchar(100),
	jobTitle varchar(100),
	jobClass varchar(100),
	hoursOfOperation long varchar
);

alter table Group_ add friendlyURL varchar(100);
alter table Group_ add themeId varchar(100);
alter table Group_ add colorSchemeId varchar(100);
update Group_ set themeId = 'classic';
update Group_ set colorSchemeId = '01';

update JournalArticle set displayDate = current timestamp where displayDate is NULL;

alter table Image add modifiedDate timestamp;
update Image set modifiedDate = current timestamp;

alter table Layout add companyId varchar(100);
alter table Layout add parentLayoutId varchar(100);
alter table Layout add type_ long varchar;
alter table Layout add typeSettings long varchar;
alter table Layout add friendlyURL varchar(100);
alter table Layout add priority integer;
update Layout set parentLayoutId = '-1' where parentLayoutId is NULL;
update Layout set type_ = 'portlet' where type_ is NULL;

drop table Portlet;
create table Portlet (
	portletId varchar(100) not null,
	companyId varchar(100) not null,
	narrow smallint,
	roles long varchar,
	active_ smallint,
	primary key (portletId, companyId)
);

create table Properties (
	companyId varchar(100) not null,
	type_ varchar(100) not null,
	properties clob,
	primary key (companyId, type_)
);

alter table ShoppingCart add insure smallint;

alter table ShoppingOrder add insure smallint;

alter table User_ add themeId varchar(100);
alter table User_ add colorSchemeId varchar(100);
update User_ set themeId = 'classic';
update User_ set colorSchemeId = '01';
