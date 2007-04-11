drop table Account;
create table Account_ (
	accountId varchar(75) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentAccountId varchar(75),
	name varchar(75),
	legalName varchar(75),
	legalId varchar(75),
	legalType varchar(75),
	sicCode varchar(75),
	tickerSymbol varchar(75),
	industry varchar(75),
	type_ varchar(75),
	size_ varchar(75)
);

alter table Address add street3 varchar(75);
alter table Address add regionId varchar(75);
alter table Address add countryId varchar(75);
alter table Address add typeId varchar(75);
alter table Address add mailing smallint;
alter table Address add primary_ smallint;

alter table BlogsCategory add userName varchar(75);
alter table BlogsCategory add parentCategoryId varchar(75);
alter table BlogsCategory add description varchar(4000);

drop table BlogsComments;

alter table BlogsEntry add groupId varchar(75) not null default '';
alter table BlogsEntry add userName varchar(75);

drop table BlogsLink;

drop table BlogsProps;

drop table BlogsReferer;

drop table BlogsUser;

alter table BookmarksFolder add description varchar(4000);

drop table Contact;
create table Contact_ (
	contactId varchar(75) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	accountId varchar(75),
	parentContactId varchar(75),
	firstName varchar(75),
	middleName varchar(75),
	lastName varchar(75),
	nickName varchar(75),
	prefixId varchar(75),
	suffixId varchar(75),
	male smallint,
	birthday timestamp,
	smsSn varchar(75),
	aimSn varchar(75),
	icqSn varchar(75),
	msnSn varchar(75),
	skypeSn varchar(75),
	ymSn varchar(75),
	employeeStatusId varchar(75),
	employeeNumber varchar(75),
	jobTitle varchar(75),
	jobClass varchar(75),
	hoursOfOperation varchar(75)
);

create table Country (
	countryId varchar(75) not null primary key,
	countryCode varchar(75),
	name varchar(75),
	active_ smallint
);

create table DataTracker (
	dataTrackerId varchar(75) not null primary key,
	companyId varchar(75) not null,
	createdOn timestamp,
	createdByUserId varchar(75),
	createdByUserName varchar(75),
	updatedOn timestamp,
	updatedBy varchar(75),
	className varchar(75),
	classPK varchar(75),
	active_ smallint
);

create table DLFileEntry (
	folderId varchar(75) not null,
	name varchar(100) not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	versionUserId varchar(75),
	versionUserName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	title varchar(100),
	description varchar(4000),
	version double precision,
	size_ integer,
	readCount integer,
	primary key (folderId, name)
);

drop table DLFileRank;
create table DLFileRank (
	companyId varchar(75) not null,
	userId varchar(75) not null,
	folderId varchar(75) not null,
	name varchar(100) not null,
	createDate timestamp,
	primary key (companyId, userId, folderId, name)
);

drop table DLFileVersion;
create table DLFileVersion (
	folderId varchar(75) not null,
	name varchar(100) not null,
	version double precision not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	size_ integer,
	primary key (folderId, name, version)
);

create table DLFolder (
	folderId varchar(75) not null primary key,
	groupId varchar(75) not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentFolderId varchar(75),
	name varchar(100),
	description varchar(4000),
	lastPostDate timestamp
);

create table EmailAddress (
	emailAddressId varchar(75) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	address varchar(75),
	typeId varchar(75),
	primary_ smallint
);

alter table Group_ add className varchar(75);
alter table Group_ add classPK varchar(75);
alter table Group_ add description varchar(4000);
alter table Group_ add availability varchar(75);

create table Groups_Permissions (
	groupId  varchar(75) not null,
	permissionId varchar(75) not null
);

alter table IGFolder add description varchar(4000);

alter table JournalArticle add approvedDate timestamp;
alter table JournalArticle add expired smallint;
alter table JournalArticle add reviewDate timestamp;

alter table JournalContentSearch alter column "userId" to "ownerId";
alter table JournalContentSearch add companyId varchar(100) not null default '';

alter table JournalTemplate add langType varchar(75);

alter table Layout alter column "userId" to "ownerId";
alter table Layout alter column "name" type varchar(4000);
alter table Layout add hidden_ smallint;
alter table Layout add themeId varchar(75);
alter table Layout add colorSchemeId varchar(75);

create table LayoutSet (
	ownerId varchar(75) not null primary key,
	companyId varchar(75) not null,
	groupId varchar(75) not null,
	userId varchar(75) not null,
	privateLayout smallint,
	themeId varchar(75),
	colorSchemeId varchar(75),
	pageCount integer
);

create table ListType (
	listTypeId varchar(75) not null primary key,
	name varchar(75),
	type_ varchar(75)
);

create table MBCategory (
	categoryId varchar(75) not null primary key,
	groupId varchar(75) not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentCategoryId varchar(75),
	name varchar(75),
	description varchar(4000)
);

create table MBDiscussion (
	discussionId varchar(75) not null primary key,
	className varchar(75),
	classPK varchar(75),
	threadId varchar(75)
);

alter table MBTopic add categoryId varchar(75);

drop table NetworkAddress;

create table Organization_ (
	organizationId varchar(75) not null primary key,
	companyId varchar(75) not null,
	parentOrganizationId varchar(75),
	name varchar(75),
	recursable smallint,
	regionId varchar(75),
	countryId varchar(75),
	statusId varchar(75),
	comments varchar(4000)
);

create table OrgGroupPermission (
	organizationId varchar(75) not null,
	groupId varchar(75) not null,
	permissionId varchar(75) not null,
	primary key (organizationId, groupId, permissionId)
);

create table OrgGroupRole (
	organizationId varchar(75) not null,
	groupId varchar(75) not null,
	roleId varchar(75) not null,
	primary key (organizationId, groupId, roleId)
);

create table OrgLabor (
	orgLaborId varchar(75) not null primary key,
	organizationId varchar(75),
	typeId varchar(75),
	sunOpen integer,
	sunClose integer,
	monOpen integer,
	monClose integer,
	tueOpen integer,
	tueClose integer,
	wedOpen integer,
	wedClose integer,
	thuOpen integer,
	thuClose integer,
	friOpen integer,
	friClose integer,
	satOpen integer,
	satClose integer
);

create table Permission_ (
	permissionId varchar(75) not null primary key,
	companyId varchar(75) not null,
	actionId varchar(75),
	resourceId varchar(75)
);

create table Phone (
	phoneId varchar(75) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	number_ varchar(75),
	extension varchar(75),
	typeId varchar(75),
	primary_ smallint
);

alter table PortletPreferences alter column "userId" to "ownerId";

create table Region (
	regionId varchar(75) not null primary key,
	countryId varchar(75),
	regionCode varchar(75),
	name varchar(75),
	active_ smallint
);

create table Resource_ (
	resourceId varchar(25) not null primary key,
	companyId varchar(25) not null,
	name varchar(75),
	typeId varchar(15),
	scope varchar(15),
	primKey varchar(200)
);

alter table Role_ add className varchar(75);
alter table Role_ add classPK varchar(75);

create table Roles_Permissions (
	roleId varchar(75) not null,
	permissionId varchar(75) not null
);

drop table ShoppingCart;
create table ShoppingCart (
	cartId varchar(75) not null primary key,
	groupId varchar(75) not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	itemIds varchar(4000),
	couponIds varchar(4000),
	altShipping integer,
	insure smallint
);

alter table ShoppingCategory add groupId varchar(75) not null default '';
alter table ShoppingCategory add userId varchar(75) not null default '';
alter table ShoppingCategory add userName varchar(75);
alter table ShoppingCategory add description varchar(4000);

alter table ShoppingCoupon add groupId varchar(75) not null default '';
alter table ShoppingCoupon add userId varchar(75) not null default '';
alter table ShoppingCoupon add userName varchar(75);

alter table ShoppingItem add userId varchar(75) not null default '';
alter table ShoppingItem add userName varchar(75);

drop table ShoppingOrder;
create table ShoppingOrder (
	orderId varchar(75) not null primary key,
	groupId varchar(75) not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	tax double precision,
	shipping double precision,
	altShipping varchar(75),
	requiresShipping smallint,
	insure smallint,
	insurance double precision,
	couponIds varchar(75),
	couponDiscount double precision,
	billingFirstName varchar(75),
	billingLastName varchar(75),
	billingEmailAddress varchar(75),
	billingCompany varchar(75),
	billingStreet varchar(75),
	billingCity varchar(75),
	billingState varchar(75),
	billingZip varchar(75),
	billingCountry varchar(75),
	billingPhone varchar(75),
	shipToBilling smallint,
	shippingFirstName varchar(75),
	shippingLastName varchar(75),
	shippingEmailAddress varchar(75),
	shippingCompany varchar(75),
	shippingStreet varchar(75),
	shippingCity varchar(75),
	shippingState varchar(75),
	shippingZip varchar(75),
	shippingCountry varchar(75),
	shippingPhone varchar(75),
	ccName varchar(75),
	ccType varchar(75),
	ccNumber varchar(75),
	ccExpMonth integer,
	ccExpYear integer,
	ccVerNumber varchar(75),
	comments varchar(4000),
	ppTxnId varchar(75),
	ppPaymentStatus varchar(75),
	ppPaymentGross double precision,
	ppReceiverEmail varchar(75),
	ppPayerEmail varchar(75),
	sendOrderEmail smallint,
	sendShippingEmail smallint
);

create table Subscription (
	subscriptionId varchar(75) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	frequency varchar(75)
);

alter table User_ add contactId varchar(75);
alter table User_ add modifiedDate timestamp;

create table Users_Orgs (
	userId varchar(75) not null,
	organizationId varchar(75) not null
);

create table Users_Permissions (
	userId varchar(75) not null,
	permissionId varchar(75) not null
);

create table Website (
	websiteId varchar(75) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	url varchar(75),
	typeId varchar(75),
	primary_ smallint
);

alter table WikiNode add groupId varchar(75) not null default '';



--
-- List types for accounts
--





--
-- List types for contacts
--







--
-- List types for organizations
--








