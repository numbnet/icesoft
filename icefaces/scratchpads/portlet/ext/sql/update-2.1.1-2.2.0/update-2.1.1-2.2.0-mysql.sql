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
alter table ABContact add hoursOfOperation longtext null;
alter table ABContact add timeZoneId varchar(100) null;
alter table ABContact add comments longtext null;

alter table BlogsComments add content longtext null;

alter table BlogsEntry add title varchar(100) null;
alter table BlogsEntry add displayDate datetime null;
alter table BlogsEntry add propsCount integer;
alter table BlogsEntry add commentsCount integer;

create table BlogsUser (
	userId varchar(100) not null primary key,
	companyId varchar(100) not null,
	entryId varchar(100) not null,
	lastPostDate datetime null
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
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(100) null,
	description longtext null,
	xsd longtext null
);

create table JournalTemplate (
	templateId varchar(100) not null primary key,
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	userName varchar(100) null,
	createDate datetime null,
	modifiedDate datetime null,
	structureId varchar(100) null,
	name varchar(100) null,
	description longtext null,
	xsl longtext null,
	smallImage tinyint,
	smallImageURL varchar(100) null
);

create table MailReceipt (
	receiptId varchar(100) not null primary key,
	companyId varchar(100) not null,
	userId varchar(100) not null,
	createDate datetime null,
	modifiedDate datetime null,
	recipientName varchar(100) null,
	recipientAddress varchar(100) null,
	subject varchar(100) null,
	sentDate datetime null,
	readCount integer,
	firstReadDate datetime null,
	lastReadDate datetime null
);

alter table MBThread add messageCount integer;
alter table MBThread add lastPostDate datetime null;

alter table ShoppingCart add couponIds longtext null;

create table ShoppingCoupon (
	couponId varchar(100) not null primary key,
	companyId varchar(100) not null,
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(100) null,
	description longtext null,
	startDate datetime null,
	endDate datetime null,
	active_ tinyint,
	limitCategories longtext null,
	limitSkus longtext null,
	minOrder double,
	discount double,
	discountType varchar(100) null
);

alter table ShoppingOrder add couponIds longtext null;
alter table ShoppingOrder add couponDiscount double;
alter table ShoppingOrder add comments longtext null;

alter table User_ add nickName varchar(100) null;
