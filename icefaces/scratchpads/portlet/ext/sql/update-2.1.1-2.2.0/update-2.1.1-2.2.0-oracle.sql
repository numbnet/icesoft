alter table ABContact add middleName varchar2(100) null;
alter table ABContact add nickName varchar2(100) null;
alter table ABContact add homePager varchar2(100) null;
alter table ABContact add homeTollFree varchar2(100) null;
alter table ABContact add homeEmailAddress varchar2(100) null;
alter table ABContact add businessPager varchar2(100) null;
alter table ABContact add businessTollFree varchar2(100) null;
alter table ABContact add businessEmailAddress varchar2(100) null;
alter table ABContact add employeeNumber varchar2(100) null;
alter table ABContact add jobTitle varchar2(100) null;
alter table ABContact add jobClass varchar2(100) null;
alter table ABContact add hoursOfOperation varchar2(4000) null;
alter table ABContact add timeZoneId varchar2(100) null;
alter table ABContact add comments varchar2(4000) null;

alter table BlogsComments add content clob null;

alter table BlogsEntry add title varchar2(100) null;
alter table BlogsEntry add displayDate timestamp null;
alter table BlogsEntry add propsCount number(30,0);
alter table BlogsEntry add commentsCount number(30,0);

create table BlogsUser (
	userId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	entryId varchar2(100) not null,
	lastPostDate timestamp null
);

alter table JournalArticle add version number(30,20) not null default '';
alter table JournalArticle add type_ varchar2(100) null;
alter table JournalArticle add structureId varchar2(100) null;
alter table JournalArticle add templateId varchar2(100) null;
alter table JournalArticle drop primary key;
alter table JournalArticle add primary key (articleId, version);

create table JournalStructure (
	structureId varchar2(100) not null primary key,
	portletId varchar2(100) not null,
	groupId varchar2(100) not null,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(100) null,
	description varchar2(4000) null,
	xsd clob null
);

create table JournalTemplate (
	templateId varchar2(100) not null primary key,
	portletId varchar2(100) not null,
	groupId varchar2(100) not null,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	userName varchar2(100) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	structureId varchar2(100) null,
	name varchar2(100) null,
	description varchar2(4000) null,
	xsl clob null,
	smallImage number(1, 0),
	smallImageURL varchar2(100) null
);

create table MailReceipt (
	receiptId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	recipientName varchar2(100) null,
	recipientAddress varchar2(100) null,
	subject varchar2(100) null,
	sentDate timestamp null,
	readCount number(30,0),
	firstReadDate timestamp null,
	lastReadDate timestamp null
);

alter table MBThread add messageCount number(30,0);
alter table MBThread add lastPostDate timestamp null;

alter table ShoppingCart add couponIds varchar2(4000) null;

create table ShoppingCoupon (
	couponId varchar2(100) not null primary key,
	companyId varchar2(100) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(100) null,
	description varchar2(4000) null,
	startDate timestamp null,
	endDate timestamp null,
	active_ number(1, 0),
	limitCategories varchar2(4000) null,
	limitSkus varchar2(4000) null,
	minOrder number(30,20),
	discount number(30,20),
	discountType varchar2(100) null
);

alter table ShoppingOrder add couponIds varchar2(4000) null;
alter table ShoppingOrder add couponDiscount number(30,20);
alter table ShoppingOrder add comments clob null;

alter table User_ add nickName varchar2(100) null;
