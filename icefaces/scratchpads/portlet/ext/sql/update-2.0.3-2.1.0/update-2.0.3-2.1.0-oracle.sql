alter table CalEvent add groupId varchar2(100) not null default '';
update CalEvent set groupId = '-1';

create table DLFileRank (
	companyId varchar2(100) not null,
	userId varchar2(100) not null,
	repositoryId varchar2(100) not null,
	fileName varchar2(100) not null,
	createDate timestamp null,
	primary key (companyId, userId, repositoryId, fileName)
);

alter table DLRepository add groupId varchar2(100) not null default '';
update DLRepository set groupId = '-1';

alter table IGFolder add groupId varchar2(100) not null default '';
update IGFolder set groupId = '-1';

alter table MBTopic add portletId varchar2(100) not null default '';
alter table MBTopic add groupId varchar2(100) not null default '';
update MBTopic set portletId = '19';
update MBTopic set groupId = '-1';

alter table JournalArticle add portletId varchar2(100) not null default '';
alter table JournalArticle add groupId varchar2(100) not null default '';
update JournalArticle set portletId = '15';
update JournalArticle set groupId = '-1';

alter table ShoppingItem add supplierUserId varchar2(100) null;
alter table ShoppingItem add featured_ number(1, 0);
alter table ShoppingItem add sale_ number(1, 0);
update ShoppingItem set featured_ = 1;
update ShoppingItem set sale_ = 1;

alter table ShoppingOrderItem add supplierUserId varchar2(100) null;

create table PasswordTracker (
	passwordTrackerId varchar2(100) not null primary key,
	userId varchar2(100) not null,
	createDate timestamp not null,
	password_ varchar2(100) not null
);

create table PollsDisplay (
	layoutId varchar2(100) not null,
	userId varchar2(100) not null,
	portletId varchar2(100) not null,
	questionId varchar2(100) not null,
	primary key (layoutId, userId, portletId)
);

alter table PollsQuestion add portletId varchar2(100) not null default '';
alter table PollsQuestion add groupId varchar2(100) not null default '';
alter table PollsQuestion add expirationDate timestamp null;
update PollsQuestion set portletId = '25';
update PollsQuestion set groupId = '-1';

alter table User_ add passwordReset number(1, 0);

create table WikiDisplay (
	layoutId varchar2(100) not null,
	userId varchar2(100) not null,
	portletId varchar2(100) not null,
	nodeId varchar2(100) not null,
	showBorders number(1, 0),
	primary key (layoutId, userId, portletId)
);
