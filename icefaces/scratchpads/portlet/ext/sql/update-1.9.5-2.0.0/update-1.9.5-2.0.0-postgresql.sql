alter table Address add companyId varchar(100) not null default '';
alter table Address add userName varchar(100) null;
alter table Address add createDate timestamp null;
alter table Address add modifiedDate timestamp null;
alter table Address add className varchar(100) null;
alter table Address add classPK varchar(100) null;
update Address set companyId = 'liferay.com';
update Address set userName = '';
update Address set createDate = current_timestamp;
update Address set modifiedDate = current_timestamp;
update Address set className = 'com.liferay.portal.model.User';
update Address set classPK = userId;

create table CyrusUser (
	userId varchar(100) not null primary key,
	password_ varchar(100) not null
);

create table CyrusVirtual (
	emailAddress varchar(100) not null primary key,
	userId varchar(100) not null
);

alter table Group_ add parentGroupId varchar(100) null;
update Group_ set parentGroupId = '-1';

alter table Guestbook add modifiedDate timestamp null;

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
);

drop table Portlet;
create table Portlet (
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	defaultPreferences text null,
	narrow bool,
	roles text null,
	active_ bool,
	primary key (portletId, groupId, companyId)
);

drop table PortletPreference;
create table PortletPreferences (
	portletId varchar(100) not null,
	userId varchar(100) not null,
	layoutId varchar(100) not null,
	preferences text null,
	primary key (portletId, userId, layoutId)
);

alter table ShoppingItem add fields_ bool;
alter table ShoppingItem add fieldsQuantities text null;

create table ShoppingItemField (
	itemFieldId varchar(100) not null primary key,
	itemId varchar(100) null,
	name varchar(100) null,
	values_ text null,
	description text null
);

alter table ShoppingOrder add modifiedDate timestamp null;
alter table ShoppingOrder add ccName varchar(100) null;
alter table ShoppingOrder add ccType varchar(100) null;
alter table ShoppingOrder add ccNumber varchar(100) null;
alter table ShoppingOrder add ccExpMonth integer;
alter table ShoppingOrder add ccExpYear integer;
alter table ShoppingOrder add ccVerNumber varchar(100) null;
alter table ShoppingOrder add sendOrderEmail bool;
alter table ShoppingOrder add sendShippingEmail bool;
update ShoppingOrder set modifiedDate = createDate;
update ShoppingOrder set sendOrderEmail = false;
update ShoppingOrder set sendShippingEmail = false;

alter table User_ add passwordEncrypted bool;
alter table User_ add passwordExpirationDate timestamp null;
