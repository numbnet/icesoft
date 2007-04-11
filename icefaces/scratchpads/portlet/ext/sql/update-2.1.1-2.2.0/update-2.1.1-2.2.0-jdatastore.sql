alter table ABContact add middleName varchar(100) null;
alter table ABContact add nickName varchar(100) null;
alter table ABContact add homePager varchar(100) null;
alter table ABContact add homeTollFree varchar(100) null;
alter table ABContact add homeEmailAddress varchar(100) null;
alter table ABContact add businessPager varchar(100) null;
alter table ABContact add businessTollFree varchar(100) null;
alter table ABContact add businessEmailAddress varchar(100) null;
alter table ABContact add employeeNumber varchar(100) null;
alter table ABContact add jobTitle varchar(100) null;
alter table ABContact add jobClass varchar(100) null;
alter table ABContact add hoursOfOperation long varchar null;
alter table ABContact add timeZoneId varchar(100) null;
alter table ABContact add comments long varchar null;

alter table BlogsComments add content long varchar null;

alter table BlogsEntry add title varchar(100) null;
alter table BlogsEntry add displayDate date null;
alter table BlogsEntry add propsCount integer;
alter table BlogsEntry add commentsCount integer;

create table BlogsUser (
	userId varchar(100) not null primary key,
	companyId varchar(100) not null,
	entryId varchar(100) not null,
	lastPostDate date null
);

alter table JournalArticle add version double not null default '';
alter table JournalArticle add type_ varchar(100) null;
alter table JournalArticle add structureId varchar(100) null;
alter table JournalArticle add templateId varchar(100) null;
alter table JournalArticle drop primary key;
alter table JournalArticle add primary key (articleId, version);

create table JournalStructure (
	structureId varchar(100) not null primary key,
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	name varchar(100) null,
	description long varchar null,
	xsd long varchar null
);

create table JournalTemplate (
	templateId varchar(100) not null primary key,
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate date null,
	modifiedDate date null,
	structureId varchar(100) null,
	name varchar(100) null,
	description long varchar null,
	xsl long varchar null,
	smallImage boolean,
	smallImageURL varchar(100) null
);

create table MailReceipt (
	receiptId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate date null,
	modifiedDate date null,
	recipientName varchar(100) null,
	recipientAddress varchar(100) null,
	subject varchar(100) null,
	sentDate date null,
	readCount integer,
	firstReadDate date null,
	lastReadDate date null
);

alter table MBThread add messageCount integer;
alter table MBThread add lastPostDate date null;

alter table ShoppingCart add couponIds long varchar null;

create table ShoppingCoupon (
	couponId varchar(100) not null primary key,
	companyId varchar(100) not null,
	createDate date null,
	modifiedDate date null,
	name varchar(100) null,
	description long varchar null,
	startDate date null,
	endDate date null,
	active_ boolean,
	limitCategories long varchar null,
	limitSkus long varchar null,
	minOrder double,
	discount double,
	discountType varchar(100) null
);

alter table ShoppingOrder add couponIds long varchar null;
alter table ShoppingOrder add couponDiscount double;
alter table ShoppingOrder add comments long varchar null;

alter table User_ add nickName varchar(100) null;
