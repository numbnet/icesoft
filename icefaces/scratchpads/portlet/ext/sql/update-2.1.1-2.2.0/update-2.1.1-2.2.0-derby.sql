alter table ABContact add middleName varchar(100);
alter table ABContact add nickName varchar(100);
alter table ABContact add homePager varchar(100);
alter table ABContact add homeTollFree varchar(100);
alter table ABContact add homeEmailAddress varchar(100);
alter table ABContact add businessPager varchar(100);
alter table ABContact add businessTollFree varchar(100);
alter table ABContact add businessEmailAddress varchar(100);
alter table ABContact add employeeNumber varchar(100);
alter table ABContact add jobTitle varchar(100);
alter table ABContact add jobClass varchar(100);
alter table ABContact add hoursOfOperation long varchar;
alter table ABContact add timeZoneId varchar(100);
alter table ABContact add comments long varchar;

alter table BlogsComments add content clob;

alter table BlogsEntry add title varchar(100);
alter table BlogsEntry add displayDate timestamp;
alter table BlogsEntry add propsCount integer;
alter table BlogsEntry add commentsCount integer;

create table BlogsUser (
	userId varchar(100) not null primary key,
	companyId varchar(100) not null,
	entryId varchar(100) not null,
	lastPostDate timestamp
);

alter table JournalArticle add version double not null default '';
alter table JournalArticle add type_ varchar(100);
alter table JournalArticle add structureId varchar(100);
alter table JournalArticle add templateId varchar(100);
alter table JournalArticle drop primary key;
alter table JournalArticle add primary key (articleId, version);

create table JournalStructure (
	structureId varchar(100) not null primary key,
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(100),
	description long varchar,
	xsd clob
);

create table JournalTemplate (
	templateId varchar(100) not null primary key,
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100),
	createDate timestamp,
	modifiedDate timestamp,
	structureId varchar(100),
	name varchar(100),
	description long varchar,
	xsl clob,
	smallImage smallint,
	smallImageURL varchar(100)
);

create table MailReceipt (
	receiptId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate timestamp,
	modifiedDate timestamp,
	recipientName varchar(100),
	recipientAddress varchar(100),
	subject varchar(100),
	sentDate timestamp,
	readCount integer,
	firstReadDate timestamp,
	lastReadDate timestamp
);

alter table MBThread add messageCount integer;
alter table MBThread add lastPostDate timestamp;

alter table ShoppingCart add couponIds long varchar;

create table ShoppingCoupon (
	couponId varchar(100) not null primary key,
	companyId varchar(100) not null,
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(100),
	description long varchar,
	startDate timestamp,
	endDate timestamp,
	active_ smallint,
	limitCategories long varchar,
	limitSkus long varchar,
	minOrder double,
	discount double,
	discountType varchar(100)
);

alter table ShoppingOrder add couponIds long varchar;
alter table ShoppingOrder add couponDiscount double;
alter table ShoppingOrder add comments clob;

alter table User_ add nickName varchar(100);
