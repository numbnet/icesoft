alter table Address add companyId varchar2(100) not null default '';
alter table Address add userName varchar2(100) null;
alter table Address add createDate timestamp null;
alter table Address add modifiedDate timestamp null;
alter table Address add className varchar2(100) null;
alter table Address add classPK varchar2(100) null;
update Address set companyId = 'liferay.com';
update Address set userName = '';
update Address set createDate = sysdate;
update Address set modifiedDate = sysdate;
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

alter table Group_ add parentGroupId varchar2(100) null;
update Group_ set parentGroupId = '-1';

alter table Guestbook add modifiedDate timestamp null;

alter table Layout add stateMax varchar2(100) null;
alter table Layout add stateMin varchar2(100) null;
alter table Layout add modeEdit varchar2(100) null;
alter table Layout add modeHelp varchar2(100) null;

create table MBMessageFlag (
	topicId varchar2(100) not null,
	messageId varchar2(100) not null,
	userId varchar2(100) not null,
	flag varchar2(100) null,
	primary key (topicId, messageId, userId)
);

drop table Portlet;
create table Portlet (
	portletId varchar2(100) not null,
	groupId varchar2(100) not null,
	companyId varchar2(100) not null,
	defaultPreferences clob null,
	narrow number(1, 0),
	roles varchar2(4000) null,
	active_ number(1, 0),
	primary key (portletId, groupId, companyId)
);

drop table PortletPreference;
create table PortletPreferences (
	portletId varchar2(100) not null,
	userId varchar2(100) not null,
	layoutId varchar2(100) not null,
	preferences clob null,
	primary key (portletId, userId, layoutId)
);

alter table ShoppingItem add fields_ number(1, 0);
alter table ShoppingItem add fieldsQuantities varchar2(4000) null;

create table ShoppingItemField (
	itemFieldId varchar2(100) not null primary key,
	itemId varchar2(100) null,
	name varchar2(100) null,
	values_ varchar2(4000) null,
	description varchar2(4000) null
);

alter table ShoppingOrder add modifiedDate timestamp null;
alter table ShoppingOrder add ccName varchar2(100) null;
alter table ShoppingOrder add ccType varchar2(100) null;
alter table ShoppingOrder add ccNumber varchar2(100) null;
alter table ShoppingOrder add ccExpMonth number(30,0);
alter table ShoppingOrder add ccExpYear number(30,0);
alter table ShoppingOrder add ccVerNumber varchar2(100) null;
alter table ShoppingOrder add sendOrderEmail number(1, 0);
alter table ShoppingOrder add sendShippingEmail number(1, 0);
update ShoppingOrder set modifiedDate = createDate;
update ShoppingOrder set sendOrderEmail = 0;
update ShoppingOrder set sendShippingEmail = 0;

alter table User_ add passwordEncrypted number(1, 0);
alter table User_ add passwordExpirationDate timestamp null;
