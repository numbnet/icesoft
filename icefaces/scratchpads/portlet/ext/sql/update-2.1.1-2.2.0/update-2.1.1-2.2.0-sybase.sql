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
alter table ABContact add hoursOfOperation varchar(1000) null;
alter table ABContact add timeZoneId varchar(100) null;
alter table ABContact add comments varchar(1000) null;

alter table BlogsComments add content text null;

alter table BlogsEntry add title varchar(100) null;
alter table BlogsEntry add displayDate datetime null;
alter table BlogsEntry add propsCount int;
alter table BlogsEntry add commentsCount int;

create table BlogsUser (
	userId varchar(100) not null primary key,
	companyId varchar(100) not null,
	entryId varchar(100) not null,
	lastPostDate datetime null
)
go

alter table JournalArticle add version float not null default '';
alter table JournalArticle add type_ varchar(100) null;
alter table JournalArticle add structureId varchar(100) null;
alter table JournalArticle add templateId varchar(100) null;
alter table JournalArticle drop primary key;
alter table JournalArticle add primary key (articleId, version)
go

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
	description varchar(1000) null,
	xsd text null
)
go

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
	description varchar(1000) null,
	xsl text null,
	smallImage int,
	smallImageURL varchar(100) null
)
go

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
	readCount int,
	firstReadDate datetime null,
	lastReadDate datetime null
)
go

alter table MBThread add messageCount int;
alter table MBThread add lastPostDate datetime null;

alter table ShoppingCart add couponIds varchar(1000) null;

create table ShoppingCoupon (
	couponId varchar(100) not null primary key,
	companyId varchar(100) not null,
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(100) null,
	description varchar(1000) null,
	startDate datetime null,
	endDate datetime null,
	active_ int,
	limitCategories varchar(1000) null,
	limitSkus varchar(1000) null,
	minOrder float,
	discount float,
	discountType varchar(100) null
)
go

alter table ShoppingOrder add couponIds varchar(1000) null;
alter table ShoppingOrder add couponDiscount float;
alter table ShoppingOrder add comments text null;

alter table User_ add nickName varchar(100) null;
