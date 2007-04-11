alter table Address alter column "addressId" type int64;
alter table Address alter column "typeId" type integer;

alter table BlogsCategory alter column "categoryId" type int64;
alter table BlogsCategory alter column "parentCategoryId" type int64;

alter table BlogsEntry alter column "entryId" type int64;
alter table BlogsEntry alter column "groupId" type int64;
alter table BlogsEntry alter column "categoryId" type int64;

alter table BookmarksEntry alter column "entryId" type int64;

alter table BookmarksFolder alter column "folderId" type int64;
alter table BookmarksFolder alter column "groupId" type int64;
alter table BookmarksFolder alter column "parentFolderId" type int64;

alter table CalEvent alter column "eventId" type int64;
alter table CalEvent alter column "groupId" type int64;

alter table Contact_ alter column "prefixId" type integer;
alter table Contact_ alter column "suffixId" type integer;
alter table Contact_ alter column "parentContactId" type int64;

alter table Counter alter column "currentId" type int64;

alter table DLFolder alter column "groupId" type int64;

alter table EmailAddress alter column "emailAddressId" type int64;
alter table EmailAddress alter column "typeId" type integer;

alter table Group_ alter column "groupId" type int64;
alter table Group_ add creatorUserId varchar(75);
alter table Group_ alter column "parentGroupId" type int64;
alter table Group_ add liveGroupId int64;
alter table Group_ add active_ smallint;

alter table Groups_Orgs alter column "groupId" type int64;

alter table Groups_Permissions alter column "groupId" type int64;
alter table Groups_Permissions alter column "permissionId" type int64;

alter table Groups_Roles alter column "groupId" type int64;

alter table Groups_UserGroups alter column "groupId" type int64;

alter table IGFolder alter column "groupId" type int64;

alter table JournalArticle alter column "groupId" type int64;

alter table JournalContentSearch alter column "groupId" type int64;

alter table JournalStructure alter column "groupId" type int64;

alter table JournalTemplate alter column "groupId" type int64;

alter table Layout add iconImage smallint;
alter table Layout add css varchar(75);

alter table LayoutSet alter column "groupId" type int64;
alter table LayoutSet add logo smallint;
alter table LayoutSet add css varchar(75);

alter table ListType alter column "listTypeId" type integer;

create table MBBan (
	banId int64 not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	banUserId varchar(75)
);

alter table MBCategory alter column "groupId" type int64;

alter table MBStatsUser alter column "groupId" type int64;

alter table Organization_ alter column "statusId" type integer;

alter table OrgGroupPermission alter column "permissionId" type int64;
alter table OrgGroupPermission alter column "groupId" type int64;

alter table OrgGroupRole alter column "groupId" type int64;

alter table OrgLabor alter column "typeId" type integer;

alter table PasswordTracker alter column "passwordTrackerId" type int64;

alter table Permission_ alter column "permissionId" type int64;
alter table Permission_ alter column "resourceId" type int64;

alter table Phone alter column "phoneId" type int64;
alter table Phone alter column "typeId" type integer;

create table PluginSetting (
	pluginSettingId int64 not null primary key,
	companyId varchar(75) not null,
	pluginId varchar(75),
	pluginType varchar(75),
	roles varchar(75),
	active_ smallint
);

alter table PollsQuestion alter column "groupId" type int64;

alter table Release_ alter column "releaseId" type int64;
alter table Release_ add verified smallint;

alter table Resource_ alter column "resourceId" type int64;
alter table Resource_ add codeId int64;
alter table Resource_ drop typeId;

create table ResourceCode (
	codeId int64 not null primary key,
	companyId varchar(75) not null,
	name varchar(75),
	scope varchar(75)
);

alter table Role_ add type_ integer;

alter table Roles_Permissions alter column "permissionId" type int64;

create table SCFrameworkVersion (
	frameworkVersionId int64 not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75),
	url varchar(1024),
	active_ smallint,
	priority integer
);

create table SCFrameworkVersions_SCProductVersions (
	productVersionId int64,
	frameworkVersionId int64,
	primary key (productVersionId, frameworkVersionId)
);

create table SCLicense (
	licenseId int64 not null primary key,
	name varchar(75),
	url varchar(1024),
	openSource smallint,
	active_ smallint,
	recommended smallint
);

create table SCLicenses_SCProductEntries (
	productEntryId int64,
	licenseId int64,
	primary key (productEntryId, licenseId)
);

create table SCProductEntry (
	productEntryId int64 not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75),
	type_ varchar(75),
	shortDescription varchar(4000),
	longDescription varchar(4000),
	pageURL varchar(1024),
	repoGroupId varchar(75),
	repoArtifactId varchar(75)
);

create table SCProductVersion (
	productVersionId int64 not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	productEntryId int64,
	version varchar(75),
	changeLog varchar(4000),
	downloadPageURL varchar(1024),
	directDownloadURL varchar(1024),
	repoStoreArtifact smallint
);

create table SCLicenses_SCProductEntries (
	productEntryId int64,
	licenseId int64,
	primary key (productEntryId, licenseId)
);

create table SCProductEntry (
	productEntryId int64 primary key,
	groupId int64,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75),
	type_ varchar(75),
	shortDescription varchar(4000),
	longDescription varchar(4000),
	pageURL varchar(75),
	repoGroupId varchar(75),
	repoArtifactId varchar(75)
);

create table SCProductVersion (
	productVersionId int64 primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	productEntryId int64,
	version varchar(75),
	changeLog varchar(4000),
	downloadPageURL varchar(75),
	directDownloadURL varchar(75),
	repoStoreArtifact smallint
);

alter table ShoppingCart alter column "groupId" type int64;

alter table ShoppingCategory alter column "groupId" type int64;

alter table ShoppingCoupon alter column "groupId" type int64;

alter table ShoppingOrder alter column "groupId" type int64;

alter table Subscription alter column "subscriptionId" type int64;

create table TagsAsset (
	assetId int64 not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	startDate timestamp,
	endDate timestamp,
	publishDate timestamp,
	expirationDate timestamp,
	mimeType varchar(75),
	title varchar(75),
	url varchar(75),
	height integer,
	width integer
);

create table TagsAssets_TagsEntries (
	assetId int64,
	entryId int64,
	primary key (assetId, entryId)
);

create table TagsEntry (
	entryId int64 not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75)
);

create table TagsProperty (
	propertyId int64 not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	entryId int64,
	key_ varchar(75),
	value varchar(75)
);

create table TagsSource (
	sourceId int64 not null primary key,
	parentSourceId int64,
	name varchar(75),
	acronym varchar(75)
);

alter table User_ alter column "contactId" type int64;
alter table User_ add screenName varchar(75);

create table UserGroupRole (
	userId varchar(75) not null,
	groupId int64 not null,
	roleId varchar(75) not null,
	primary key (userId, groupId, roleId)
);

alter table Users_Groups alter column "groupId" type int64;

alter table Users_Permissions alter column "permissionId" type int64;

alter table Website alter column "websiteId" type int64;
alter table Website alter column "typeId" type integer;

alter table WikiNode alter column "groupId" type int64;
