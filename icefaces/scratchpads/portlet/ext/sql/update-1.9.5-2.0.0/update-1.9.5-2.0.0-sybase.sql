alter table Address add companyId varchar(100) not null default '';
alter table Address add userName varchar(100) null;
alter table Address add createDate datetime null;
alter table Address add modifiedDate datetime null;
alter table Address add className varchar(100) null;
alter table Address add classPK varchar(100) null;
update Address set companyId = 'liferay.com';
update Address set userName = '';
update Address set createDate = getdate()
go
update Address set modifiedDate = getdate()
go
update Address set className = 'com.liferay.portal.model.User';
update Address set classPK = userId;

create table CyrusUser (
	userId varchar(100) not null primary key,
	password_ varchar(100) not null
)
go

create table CyrusVirtual (
	emailAddress varchar(100) not null primary key,
	userId varchar(100) not null
)
go

alter table Group_ add parentGroupId varchar(100) null;
update Group_ set parentGroupId = '-1';

alter table Guestbook add modifiedDate datetime null;

alter table Layout add stateMax varchar(100) null;
alter table Layout add stateMin varchar(100) null;
alter table Layout add modeEdit varchar(100) null;
alter table Layout add modeHelp varchar(100) null;

create table MBMessageFlag (
	topicId varchar(100) not null,
	messageId varchar(100) not null,
	userId varchar(100) not null,
	flag varchar(100) null,
	primary key (topicId, messageId, userId)
)
go

drop table Portlet;
create table Portlet (
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	defaultPreferences text null,
	narrow int,
	roles varchar(1000) null,
	active_ int,
	primary key (portletId, groupId, companyId)
)
go

drop table PortletPreference;
create table PortletPreferences (
	portletId varchar(100) not null,
	userId varchar(100) not null,
	layoutId varchar(100) not null,
	preferences text null,
	primary key (portletId, userId, layoutId)
)
go

alter table ShoppingItem add fields_ int;
alter table ShoppingItem add fieldsQuantities varchar(1000) null;

create table ShoppingItemField (
	itemFieldId varchar(100) not null primary key,
	itemId varchar(100) null,
	name varchar(100) null,
	values_ varchar(1000) null,
	description varchar(1000) null
)
go

alter table ShoppingOrder add modifiedDate datetime null;
alter table ShoppingOrder add ccName varchar(100) null;
alter table ShoppingOrder add ccType varchar(100) null;
alter table ShoppingOrder add ccNumber varchar(100) null;
alter table ShoppingOrder add ccExpMonth int;
alter table ShoppingOrder add ccExpYear int;
alter table ShoppingOrder add ccVerNumber varchar(100) null;
alter table ShoppingOrder add sendOrderEmail int;
alter table ShoppingOrder add sendShippingEmail int;
update ShoppingOrder set modifiedDate = createDate;
update ShoppingOrder set sendOrderEmail = 0;
update ShoppingOrder set sendShippingEmail = 0;

alter table User_ add passwordEncrypted int;
alter table User_ add passwordExpirationDate datetime null;
