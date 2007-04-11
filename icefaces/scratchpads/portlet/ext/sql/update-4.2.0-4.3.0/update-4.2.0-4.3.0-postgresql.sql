alter table Address alter addressId type bigint;
alter table Address alter typeId type integer;

alter table BlogsCategory alter categoryId type bigint;
alter table BlogsCategory alter parentCategoryId type bigint;

alter table BlogsEntry alter entryId type bigint;
alter table BlogsEntry alter groupId type bigint;
alter table BlogsEntry alter categoryId type bigint;

alter table BookmarksEntry alter entryId type bigint;

alter table BookmarksFolder alter folderId type bigint;
alter table BookmarksFolder alter groupId type bigint;
alter table BookmarksFolder alter parentFolderId type bigint;

alter table CalEvent alter eventId type bigint;
alter table CalEvent alter groupId type bigint;

alter table Contact_ alter prefixId type integer;
alter table Contact_ alter suffixId type integer;
alter table Contact_ alter parentContactId type bigint;

alter table Counter alter currentId type bigint;

alter table DLFolder alter groupId type bigint;

alter table EmailAddress alter emailAddressId type bigint;
alter table EmailAddress alter typeId type integer;

alter table Group_ alter groupId type bigint;
alter table Group_ add creatorUserId varchar(75) null;
alter table Group_ alter parentGroupId type bigint;
alter table Group_ add liveGroupId bigint;
alter table Group_ add active_ bool null;
update Group_ set liveGroupId = -1;
update Group_ set friendlyURL = '' where className = 'com.liferay.portal.model.User';
update Group_ set active_ = true;

alter table Groups_Orgs alter groupId type bigint;

alter table Groups_Permissions alter groupId type bigint;
alter table Groups_Permissions alter permissionId type bigint;

alter table Groups_Roles alter groupId type bigint;

alter table Groups_UserGroups alter groupId type bigint;

alter table IGFolder alter groupId type bigint;

alter table JournalArticle alter groupId type bigint;

alter table JournalContentSearch alter groupId type bigint;

alter table JournalStructure alter groupId type bigint;

alter table JournalTemplate alter groupId type bigint;

alter table Layout add iconImage bool;
alter table Layout add css varchar(75) null;

alter table LayoutSet alter groupId type bigint;
alter table LayoutSet add logo bool;
alter table LayoutSet add css varchar(75) null;
update LayoutSet set logo = false;

alter table ListType alter listTypeId type integer;

create table MBBan (
	banId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	banUserId varchar(75) null
);

alter table MBCategory alter groupId type bigint;

alter table MBStatsUser alter groupId type bigint;

alter table Organization_ alter statusId type integer;

alter table OrgGroupPermission alter permissionId type bigint;
alter table OrgGroupPermission alter groupId type bigint;

alter table OrgGroupRole alter groupId type bigint;

alter table OrgLabor alter typeId type integer;

alter table PasswordTracker alter passwordTrackerId type bigint;

alter table Permission_ alter permissionId type bigint;
alter table Permission_ alter resourceId type bigint;

alter table Phone alter phoneId type bigint;
alter table Phone alter typeId type integer;

create table PluginSetting (
	pluginSettingId bigint not null primary key,
	companyId varchar(75) not null,
	pluginId varchar(75) null,
	pluginType varchar(75) null,
	roles varchar(75) null,
	active_ bool
);

alter table PollsQuestion alter groupId type bigint;

alter table Release_ alter releaseId type bigint;
alter table Release_ add verified bool;

alter table Resource_ alter resourceId type bigint;
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

alter table Roles_Permissions alter permissionId type bigint;

create table SCFrameworkVersion (
	frameworkVersionId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(75) null,
	url varchar(1024) null,
	active_ bool,
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
	openSource bool,
	active_ bool,
	recommended bool
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
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(75) null,
	type_ varchar(75) null,
	shortDescription text null,
	longDescription text null,
	pageURL varchar(1024) null,
	repoGroupId varchar(75) null,
	repoArtifactId varchar(75) null
);

create table SCProductVersion (
	productVersionId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	productEntryId bigint,
	version varchar(75) null,
	changeLog text null,
	downloadPageURL varchar(1024) null,
	directDownloadURL varchar(1024) null,
	repoStoreArtifact bool
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
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(75) null,
	type_ varchar(75) null,
	shortDescription text null,
	longDescription text null,
	pageURL varchar(75) null,
	repoGroupId varchar(75) null,
	repoArtifactId varchar(75) null
);

create table SCProductVersion (
	productVersionId bigint primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	productEntryId bigint,
	version varchar(75) null,
	changeLog text null,
	downloadPageURL varchar(75) null,
	directDownloadURL varchar(75) null,
	repoStoreArtifact bool
);

alter table ShoppingCart alter groupId type bigint;

alter table ShoppingCategory alter groupId type bigint;

alter table ShoppingCoupon alter groupId type bigint;

alter table ShoppingOrder alter groupId type bigint;

alter table Subscription alter subscriptionId type bigint;

create table TagsAsset (
	assetId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar(75) null,
	classPK varchar(75) null,
	startDate timestamp null,
	endDate timestamp null,
	publishDate timestamp null,
	expirationDate timestamp null,
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
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(75) null
);

create table TagsProperty (
	propertyId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
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

alter table User_ alter contactId type bigint;
alter table User_ add screenName varchar(75) null;
update User_ set screenName = userId;

create table UserGroupRole (
	userId varchar(75) not null,
	groupId bigint not null,
	roleId varchar(75) not null,
	primary key (userId, groupId, roleId)
);

alter table Users_Groups alter groupId type bigint;

alter table Users_Permissions alter permissionId type bigint;

alter table Website alter websiteId type bigint;
alter table Website alter typeId type integer;

alter table WikiNode alter groupId type bigint;
