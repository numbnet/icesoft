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
alter table ABContact add hoursOfOperation text null;
alter table ABContact add timeZoneId varchar(100) null;
alter table ABContact add comments text null;

alter table BlogsComments add content text null;

alter table BlogsEntry add title varchar(100) null;
alter table BlogsEntry add displayDate timestamp null;
alter table BlogsEntry add propsCount integer;
alter table BlogsEntry add commentsCount integer;

create table BlogsUser (
	userId varchar(100) not null primary key,
	companyId varchar(100) not null,
	entryId varchar(100) not null,
	lastPostDate timestamp null
);

alter table JournalArticle add version double precision not null default '';
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
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(100) null,
	description text null,
	xsd text null
);

create table JournalTemplate (
	templateId varchar(100) not null primary key,
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	structureId varchar(100) null,
	name varchar(100) null,
	description text null,
	xsl text null,
	smallImage bool,
	smallImageURL varchar(100) null
);

create table MailReceipt (
	receiptId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	recipientName varchar(100) null,
	recipientAddress varchar(100) null,
	subject varchar(100) null,
	sentDate timestamp null,
	readCount integer,
	firstReadDate timestamp null,
	lastReadDate timestamp null
);

alter table MBThread add messageCount integer;
alter table MBThread add lastPostDate timestamp null;

alter table ShoppingCart add couponIds text null;

create table ShoppingCoupon (
	couponId varchar(100) not null primary key,
	companyId varchar(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(100) null,
	description text null,
	startDate timestamp null,
	endDate timestamp null,
	active_ bool,
	limitCategories text null,
	limitSkus text null,
	minOrder double precision,
	discount double precision,
	discountType varchar(100) null
);

alter table ShoppingOrder add couponIds text null;
alter table ShoppingOrder add couponDiscount double precision;
alter table ShoppingOrder add comments text null;

alter table User_ add nickName varchar(100) null;
