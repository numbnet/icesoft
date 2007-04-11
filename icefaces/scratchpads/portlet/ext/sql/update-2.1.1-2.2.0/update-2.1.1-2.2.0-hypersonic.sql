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
alter table ABContact add hoursOfOperation longvarchar null;
alter table ABContact add timeZoneId varchar(100) null;
alter table ABContact add comments longvarchar null;

alter table BlogsComments add content longvarchar null;

alter table BlogsEntry add title varchar(100) null;
alter table BlogsEntry add displayDate timestamp null;
alter table BlogsEntry add propsCount int;
alter table BlogsEntry add commentsCount int;

create table BlogsUser (
	userId varchar(100) not null primary key,
	companyId varchar(100) not null,
	entryId varchar(100) not null,
	lastPostDate timestamp null
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
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(100) null,
	description longvarchar null,
	xsd longvarchar null
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
	description longvarchar null,
	xsl longvarchar null,
	smallImage bit,
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
	readCount int,
	firstReadDate timestamp null,
	lastReadDate timestamp null
);

alter table MBThread add messageCount int;
alter table MBThread add lastPostDate timestamp null;

alter table ShoppingCart add couponIds longvarchar null;

create table ShoppingCoupon (
	couponId varchar(100) not null primary key,
	companyId varchar(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(100) null,
	description longvarchar null,
	startDate timestamp null,
	endDate timestamp null,
	active_ bit,
	limitCategories longvarchar null,
	limitSkus longvarchar null,
	minOrder double,
	discount double,
	discountType varchar(100) null
);

alter table ShoppingOrder add couponIds longvarchar null;
alter table ShoppingOrder add couponDiscount double;
alter table ShoppingOrder add comments longvarchar null;

alter table User_ add nickName varchar(100) null;
