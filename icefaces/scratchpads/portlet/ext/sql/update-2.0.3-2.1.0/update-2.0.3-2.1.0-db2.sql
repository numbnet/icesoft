alter table CalEvent add groupId varchar(100) not null default '';
update CalEvent set groupId = '-1';

create table DLFileRank (
	companyId varchar(100) not null,
	userId varchar(100) not null,
	repositoryId varchar(100) not null,
	fileName varchar(100) not null,
	createDate timestamp,
	primary key (companyId, userId, repositoryId, fileName)
);

alter table DLRepository add groupId varchar(100) not null default '';
update DLRepository set groupId = '-1';

alter table IGFolder add groupId varchar(100) not null default '';
update IGFolder set groupId = '-1';

alter table MBTopic add portletId varchar(100) not null default '';
alter table MBTopic add groupId varchar(100) not null default '';
update MBTopic set portletId = '19';
update MBTopic set groupId = '-1';

alter table JournalArticle add portletId varchar(100) not null default '';
alter table JournalArticle add groupId varchar(100) not null default '';
update JournalArticle set portletId = '15';
update JournalArticle set groupId = '-1';

alter table ShoppingItem add supplierUserId varchar(100);
alter table ShoppingItem add featured_ smallint;
alter table ShoppingItem add sale_ smallint;
update ShoppingItem set featured_ = 1;
update ShoppingItem set sale_ = 1;

alter table ShoppingOrderItem add supplierUserId varchar(100);

create table PasswordTracker (
	passwordTrackerId varchar(100) not null primary key,
	userId varchar(100) not null,
	createDate timestamp not null,
	password_ varchar(100) not null
);

create table PollsDisplay (
	layoutId varchar(100) not null,
	userId varchar(100) not null,
	portletId varchar(100) not null,
	questionId varchar(100) not null,
	primary key (layoutId, userId, portletId)
);

alter table PollsQuestion add portletId varchar(100) not null default '';
alter table PollsQuestion add groupId varchar(100) not null default '';
alter table PollsQuestion add expirationDate timestamp;
update PollsQuestion set portletId = '25';
update PollsQuestion set groupId = '-1';

alter table User_ add passwordReset smallint;

create table WikiDisplay (
	layoutId varchar(100) not null,
	userId varchar(100) not null,
	portletId varchar(100) not null,
	nodeId varchar(100) not null,
	showBorders smallint,
	primary key (layoutId, userId, portletId)
);
