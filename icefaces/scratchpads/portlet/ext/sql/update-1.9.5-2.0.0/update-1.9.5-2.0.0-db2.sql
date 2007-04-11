alter table Address add companyId varchar(100) not null default '';
alter table Address add userName varchar(100);
alter table Address add createDate timestamp;
alter table Address add modifiedDate timestamp;
alter table Address add className varchar(100);
alter table Address add classPK varchar(100);
update Address set companyId = 'liferay.com';
update Address set userName = '';
update Address set createDate = current timestamp;
update Address set modifiedDate = current timestamp;
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

alter table Group_ add parentGroupId varchar(100);
update Group_ set parentGroupId = '-1';

alter table Guestbook add modifiedDate timestamp;

alter table Layout add stateMax varchar(100);
alter table Layout add stateMin varchar(100);
alter table Layout add modeEdit varchar(100);
alter table Layout add modeHelp varchar(100);

create table MBMessageFlag (
	topicId varchar(100) not null,
	messageId varchar(100) not null,
	userId varchar(100) not null,
	flag varchar(100),
	primary key (topicId, messageId, userId)
);

drop table Portlet;
create table Portlet (
	portletId varchar(100) not null,
	groupId varchar(100) not null,
	companyId varchar(100) not null,
	defaultPreferences clob,
	narrow smallint,
	roles varchar(500),
	active_ smallint,
	primary key (portletId, groupId, companyId)
);

drop table PortletPreference;
create table PortletPreferences (
	portletId varchar(100) not null,
	userId varchar(100) not null,
	layoutId varchar(100) not null,
	preferences clob,
	primary key (portletId, userId, layoutId)
);

alter table ShoppingItem add fields_ smallint;
alter table ShoppingItem add fieldsQuantities varchar(500);

create table ShoppingItemField (
	itemFieldId varchar(100) not null primary key,
	itemId varchar(100),
	name varchar(100),
	values_ varchar(500),
	description varchar(500)
);

alter table ShoppingOrder add modifiedDate timestamp;
alter table ShoppingOrder add ccName varchar(100);
alter table ShoppingOrder add ccType varchar(100);
alter table ShoppingOrder add ccNumber varchar(100);
alter table ShoppingOrder add ccExpMonth integer;
alter table ShoppingOrder add ccExpYear integer;
alter table ShoppingOrder add ccVerNumber varchar(100);
alter table ShoppingOrder add sendOrderEmail smallint;
alter table ShoppingOrder add sendShippingEmail smallint;
update ShoppingOrder set modifiedDate = createDate;
update ShoppingOrder set sendOrderEmail = 0;
update ShoppingOrder set sendShippingEmail = 0;

alter table User_ add passwordEncrypted smallint;
alter table User_ add passwordExpirationDate timestamp;
