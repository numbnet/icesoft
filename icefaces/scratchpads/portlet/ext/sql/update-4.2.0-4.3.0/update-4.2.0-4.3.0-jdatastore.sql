alter table Address alter column "addressId" type bigint;
alter table Address alter column "typeId" type integer;

alter table BlogsCategory alter column "categoryId" type bigint;
alter table BlogsCategory alter column "parentCategoryId" type bigint;

alter table BlogsEntry alter column "entryId" type bigint;
alter table BlogsEntry alter column "groupId" type bigint;
alter table BlogsEntry alter column "categoryId" type bigint;

alter table BookmarksEntry alter column "entryId" type bigint;

alter table BookmarksFolder alter column "folderId" type bigint;
alter table BookmarksFolder alter column "groupId" type bigint;
alter table BookmarksFolder alter column "parentFolderId" type bigint;

alter table CalEvent alter column "eventId" type bigint;
alter table CalEvent alter column "groupId" type bigint;

alter table Contact_ alter column "prefixId" type integer;
alter table Contact_ alter column "suffixId" type integer;
alter table Contact_ alter column "parentContactId" type bigint;

alter table Counter alter column "currentId" type bigint;

alter table DLFolder alter column "groupId" type bigint;

alter table EmailAddress alter column "emailAddressId" type bigint;
alter table EmailAddress alter column "typeId" type integer;

alter table Group_ alter column "groupId" type bigint;
alter table Group_ add creatorUserId varchar(75) null;
alter table Group_ alter column "parentGroupId" type bigint;
alter table Group_ add liveGroupId bigint;
alter table Group_ add active_ boolean null;
update Group_ set liveGroupId = -1;
update Group_ set friendlyURL = '' where className = 'com.liferay.portal.model.User';
update Group_ set active_ = TRUE;

alter table Groups_Orgs alter column "groupId" type bigint;

alter table Groups_Permissions alter column "groupId" type bigint;
alter table Groups_Permissions alter column "permissionId" type bigint;

alter table Groups_Roles alter column "groupId" type bigint;

alter table Groups_UserGroups alter column "groupId" type bigint;

alter table IGFolder alter column "groupId" type bigint;

alter table JournalArticle alter column "groupId" type bigint;

alter table JournalContentSearch alter column "groupId" type bigint;

alter table JournalStructure alter column "groupId" type bigint;

alter table JournalTemplate alter column "groupId" type bigint;

alter table Layout add iconImage boolean;
alter table Layout add css varchar(75) null;

alter table LayoutSet alter column "groupId" type bigint;
alter table LayoutSet add logo boolean;
alter table LayoutSet add css varchar(75) null;
update LayoutSet set logo = FALSE;

alter table ListType alter column "listTypeId" type integer;

create table MBBan (
	banId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate date null,
	modifiedDate date null,
	banUserId varchar(75) null
);

alter table MBCategory alter column "groupId" type bigint;

alter table MBStatsUser alter column "groupId" type bigint;

alter table Organization_ alter column "statusId" type integer;

alter table OrgGroupPermission alter column "permissionId" type bigint;
alter table OrgGroupPermission alter column "groupId" type bigint;

alter table OrgGroupRole alter column "groupId" type bigint;

alter table OrgLabor alter column "typeId" type integer;

alter table PasswordTracker alter column "passwordTrackerId" type bigint;

alter table Permission_ alter column "permissionId" type bigint;
alter table Permission_ alter column "resourceId" type bigint;

alter table Phone alter column "phoneId" type bigint;
alter table Phone alter column "typeId" type integer;

create table PluginSetting (
	pluginSettingId bigint not null primary key,
	companyId varchar(75) not null,
	pluginId varchar(75) null,
	pluginType varchar(75) null,
	roles varchar(75) null,
	active_ boolean
);

alter table PollsQuestion alter column "groupId" type bigint;

alter table Release_ alter column "releaseId" type bigint;
alter table Release_ add verified boolean;

alter table Resource_ alter column "resourceId" type bigint;
alter table Resource_ add codeId bigint;
alter table Resource_ drop typeId;

create table ResourceCode (
	codeId bigint not null primary key,
	companyId varchar(75) not null,
	name varchar(75) null,
	scope varchar(75) null
);

alter table Role_ add type_ integer;
update Role_ SET type_ = 1;

alter table Roles_Permissions alter column "permissionId" type bigint;

create table SCFrameworkVersion (
	frameworkVersionId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate date null,
	modifiedDate date null,
	name varchar(75) null,
	url varchar(1024) null,
	active_ boolean,
	priority integer
);

create table SCFrameworkVersions_SCProductVersions (
	productVersionId bigint,
	frameworkVersionId bigint,
	primary key (productVersionId, frameworkVersionId)
);

create table SCLicense (
	licenseId bigint not null primary key,
	name varchar(75) null,
	url varchar(1024) null,
	openSource boolean,
	active_ boolean,
	recommended boolean
);

create table SCLicenses_SCProductEntries (
	productEntryId bigint,
	licenseId bigint,
	primary key (productEntryId, licenseId)
);

create table SCProductEntry (
	productEntryId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate date null,
	modifiedDate date null,
	name varchar(75) null,
	type_ varchar(75) null,
	shortDescription long varchar null,
	longDescription long varchar null,
	pageURL varchar(1024) null,
	repoGroupId varchar(75) null,
	repoArtifactId varchar(75) null
);

create table SCProductVersion (
	productVersionId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate date null,
	modifiedDate date null,
	productEntryId bigint,
	version varchar(75) null,
	changeLog long varchar null,
	downloadPageURL varchar(1024) null,
	directDownloadURL varchar(1024) null,
	repoStoreArtifact boolean
);

create table SCLicenses_SCProductEntries (
	productEntryId bigint,
	licenseId bigint,
	primary key (productEntryId, licenseId)
);

create table SCProductEntry (
	productEntryId bigint primary key,
	groupId bigint,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate date null,
	modifiedDate date null,
	name varchar(75) null,
	type_ varchar(75) null,
	shortDescription long varchar null,
	longDescription long varchar null,
	pageURL varchar(75) null,
	repoGroupId varchar(75) null,
	repoArtifactId varchar(75) null
);

create table SCProductVersion (
	productVersionId bigint primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate date null,
	modifiedDate date null,
	productEntryId bigint,
	version varchar(75) null,
	changeLog long varchar null,
	downloadPageURL varchar(75) null,
	directDownloadURL varchar(75) null,
	repoStoreArtifact boolean
);

alter table ShoppingCart alter column "groupId" type bigint;

alter table ShoppingCategory alter column "groupId" type bigint;

alter table ShoppingCoupon alter column "groupId" type bigint;

alter table ShoppingOrder alter column "groupId" type bigint;

alter table Subscription alter column "subscriptionId" type bigint;

create table TagsAsset (
	assetId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate date null,
	modifiedDate date null,
	className varchar(75) null,
	classPK varchar(75) null,
	startDate date null,
	endDate date null,
	publishDate date null,
	expirationDate date null,
	mimeType varchar(75) null,
	title varchar(75) null,
	url varchar(75) null,
	height integer,
	width integer
);

create table TagsAssets_TagsEntries (
	assetId bigint,
	entryId bigint,
	primary key (assetId, entryId)
);

create table TagsEntry (
	entryId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate date null,
	modifiedDate date null,
	name varchar(75) null
);

create table TagsProperty (
	propertyId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate date null,
	modifiedDate date null,
	entryId bigint,
	key_ varchar(75) null,
	value varchar(75) null
);

create table TagsSource (
	sourceId bigint not null primary key,
	parentSourceId bigint,
	name varchar(75) null,
	acronym varchar(75) null
);

alter table User_ alter column "contactId" type bigint;
alter table User_ add screenName varchar(75) null;
update User_ set screenName = userId;

create table UserGroupRole (
	userId varchar(75) not null,
	groupId bigint not null,
	roleId varchar(75) not null,
	primary key (userId, groupId, roleId)
);

alter table Users_Groups alter column "groupId" type bigint;

alter table Users_Permissions alter column "permissionId" type bigint;

alter table Website alter column "websiteId" type bigint;
alter table Website alter column "typeId" type integer;

alter table WikiNode alter column "groupId" type bigint;
