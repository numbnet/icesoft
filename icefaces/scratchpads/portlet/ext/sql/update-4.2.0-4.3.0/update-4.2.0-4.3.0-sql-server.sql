alter table Address alter column addressId bigint;
alter table Address alter column typeId int;

alter table BlogsCategory alter column categoryId bigint;
alter table BlogsCategory alter column parentCategoryId bigint;

alter table BlogsEntry alter column entryId bigint;
alter table BlogsEntry alter column groupId bigint;
alter table BlogsEntry alter column categoryId bigint;

alter table BookmarksEntry alter column entryId bigint;

alter table BookmarksFolder alter column folderId bigint;
alter table BookmarksFolder alter column groupId bigint;
alter table BookmarksFolder alter column parentFolderId bigint;

alter table CalEvent alter column eventId bigint;
alter table CalEvent alter column groupId bigint;

alter table Contact_ alter column prefixId int;
alter table Contact_ alter column suffixId int;
alter table Contact_ alter column parentContactId bigint;

alter table Counter alter column currentId bigint;

alter table DLFolder alter column groupId bigint;

alter table EmailAddress alter column emailAddressId bigint;
alter table EmailAddress alter column typeId int;

alter table Group_ alter column groupId bigint;
alter table Group_ add creatorUserId varchar(75) null;
alter table Group_ alter column parentGroupId bigint;
alter table Group_ add liveGroupId bigint;
alter table Group_ add active_ bit null;
update Group_ set liveGroupId = -1;
update Group_ set friendlyURL = '' where className = 'com.liferay.portal.model.User';
update Group_ set active_ = 1;

alter table Groups_Orgs alter column groupId bigint;

alter table Groups_Permissions alter column groupId bigint;
alter table Groups_Permissions alter column permissionId bigint;

alter table Groups_Roles alter column groupId bigint;

alter table Groups_UserGroups alter column groupId bigint;

alter table IGFolder alter column groupId bigint;

alter table JournalArticle alter column groupId bigint;

alter table JournalContentSearch alter column groupId bigint;

alter table JournalStructure alter column groupId bigint;

alter table JournalTemplate alter column groupId bigint;

alter table Layout add iconImage bit;
alter table Layout add css varchar(75) null;

alter table LayoutSet alter column groupId bigint;
alter table LayoutSet add logo bit;
alter table LayoutSet add css varchar(75) null;
update LayoutSet set logo = 0;

alter table ListType alter column listTypeId int;

create table MBBan (
	banId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	banUserId varchar(75) null
);

alter table MBCategory alter column groupId bigint;

alter table MBStatsUser alter column groupId bigint;

alter table Organization_ alter column statusId int;

alter table OrgGroupPermission alter column permissionId bigint;
alter table OrgGroupPermission alter column groupId bigint;

alter table OrgGroupRole alter column groupId bigint;

alter table OrgLabor alter column typeId int;

alter table PasswordTracker alter column passwordTrackerId bigint;

alter table Permission_ alter column permissionId bigint;
alter table Permission_ alter column resourceId bigint;

alter table Phone alter column phoneId bigint;
alter table Phone alter column typeId int;

create table PluginSetting (
	pluginSettingId bigint not null primary key,
	companyId varchar(75) not null,
	pluginId varchar(75) null,
	pluginType varchar(75) null,
	roles varchar(75) null,
	active_ bit
);

alter table PollsQuestion alter column groupId bigint;

alter table Release_ alter column releaseId bigint;
alter table Release_ add verified bit;

alter table Resource_ alter column resourceId bigint;
alter table Resource_ add codeId bigint;
alter table Resource_ drop typeId;

create table ResourceCode (
	codeId bigint not null primary key,
	companyId varchar(75) not null,
	name varchar(75) null,
	scope varchar(75) null
);

alter table Role_ add type_ int;
update Role_ SET type_ = 1;

alter table Roles_Permissions alter column permissionId bigint;

create table SCFrameworkVersion (
	frameworkVersionId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(75) null,
	url varchar(1024) null,
	active_ bit,
	priority int
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
	openSource bit,
	active_ bit,
	recommended bit
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
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(75) null,
	type_ varchar(75) null,
	shortDescription varchar(1000) null,
	longDescription varchar(1000) null,
	pageURL varchar(1024) null,
	repoGroupId varchar(75) null,
	repoArtifactId varchar(75) null
);

create table SCProductVersion (
	productVersionId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	productEntryId bigint,
	version varchar(75) null,
	changeLog varchar(1000) null,
	downloadPageURL varchar(1024) null,
	directDownloadURL varchar(1024) null,
	repoStoreArtifact bit
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
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(75) null,
	type_ varchar(75) null,
	shortDescription varchar(1000) null,
	longDescription varchar(1000) null,
	pageURL varchar(75) null,
	repoGroupId varchar(75) null,
	repoArtifactId varchar(75) null
);

create table SCProductVersion (
	productVersionId bigint primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	productEntryId bigint,
	version varchar(75) null,
	changeLog varchar(1000) null,
	downloadPageURL varchar(75) null,
	directDownloadURL varchar(75) null,
	repoStoreArtifact bit
);

alter table ShoppingCart alter column groupId bigint;

alter table ShoppingCategory alter column groupId bigint;

alter table ShoppingCoupon alter column groupId bigint;

alter table ShoppingOrder alter column groupId bigint;

alter table Subscription alter column subscriptionId bigint;

create table TagsAsset (
	assetId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	className varchar(75) null,
	classPK varchar(75) null,
	startDate datetime null,
	endDate datetime null,
	publishDate datetime null,
	expirationDate datetime null,
	mimeType varchar(75) null,
	title varchar(75) null,
	url varchar(75) null,
	height int,
	width int
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
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(75) null
);

create table TagsProperty (
	propertyId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
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

alter table User_ alter column contactId bigint;
alter table User_ add screenName varchar(75) null;
update User_ set screenName = userId;

create table UserGroupRole (
	userId varchar(75) not null,
	groupId bigint not null,
	roleId varchar(75) not null,
	primary key (userId, groupId, roleId)
);

alter table Users_Groups alter column groupId bigint;

alter table Users_Permissions alter column permissionId bigint;

alter table Website alter column websiteId bigint;
alter table Website alter column typeId int;

alter table WikiNode alter column groupId bigint;
