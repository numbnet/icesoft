alter table Address alter column addressId decimal(20,0)
go
alter table Address alter column typeId int;

alter table BlogsCategory alter column categoryId decimal(20,0)
go
alter table BlogsCategory alter column parentCategoryId decimal(20,0)
go

alter table BlogsEntry alter column entryId decimal(20,0)
go
alter table BlogsEntry alter column groupId decimal(20,0)
go
alter table BlogsEntry alter column categoryId decimal(20,0)
go

alter table BookmarksEntry alter column entryId decimal(20,0)
go

alter table BookmarksFolder alter column folderId decimal(20,0)
go
alter table BookmarksFolder alter column groupId decimal(20,0)
go
alter table BookmarksFolder alter column parentFolderId decimal(20,0)
go

alter table CalEvent alter column eventId decimal(20,0)
go
alter table CalEvent alter column groupId decimal(20,0)
go

alter table Contact_ alter column prefixId int;
alter table Contact_ alter column suffixId int;
alter table Contact_ alter column parentContactId decimal(20,0)
go

alter table Counter alter column currentId decimal(20,0)
go

alter table DLFolder alter column groupId decimal(20,0)
go

alter table EmailAddress alter column emailAddressId decimal(20,0)
go
alter table EmailAddress alter column typeId int;

alter table Group_ alter column groupId decimal(20,0)
go
alter table Group_ add creatorUserId varchar(75) null;
alter table Group_ alter column parentGroupId decimal(20,0)
go
alter table Group_ add liveGroupId decimal(20,0)
go
alter table Group_ add active_ int null;
update Group_ set liveGroupId = -1;
update Group_ set friendlyURL = '' where className = 'com.liferay.portal.model.User';
update Group_ set active_ = 1;

alter table Groups_Orgs alter column groupId decimal(20,0)
go

alter table Groups_Permissions alter column groupId decimal(20,0)
go
alter table Groups_Permissions alter column permissionId decimal(20,0)
go

alter table Groups_Roles alter column groupId decimal(20,0)
go

alter table Groups_UserGroups alter column groupId decimal(20,0)
go

alter table IGFolder alter column groupId decimal(20,0)
go

alter table JournalArticle alter column groupId decimal(20,0)
go

alter table JournalContentSearch alter column groupId decimal(20,0)
go

alter table JournalStructure alter column groupId decimal(20,0)
go

alter table JournalTemplate alter column groupId decimal(20,0)
go

alter table Layout add iconImage int;
alter table Layout add css varchar(75) null;

alter table LayoutSet alter column groupId decimal(20,0)
go
alter table LayoutSet add logo int;
alter table LayoutSet add css varchar(75) null;
update LayoutSet set logo = 0;

alter table ListType alter column listTypeId int;

create table MBBan (
	banId decimal(20,0) not null primary key,
	groupId decimal(20,0) not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	banUserId varchar(75) null
)
go

alter table MBCategory alter column groupId decimal(20,0)
go

alter table MBStatsUser alter column groupId decimal(20,0)
go

alter table Organization_ alter column statusId int;

alter table OrgGroupPermission alter column permissionId decimal(20,0)
go
alter table OrgGroupPermission alter column groupId decimal(20,0)
go

alter table OrgGroupRole alter column groupId decimal(20,0)
go

alter table OrgLabor alter column typeId int;

alter table PasswordTracker alter column passwordTrackerId decimal(20,0)
go

alter table Permission_ alter column permissionId decimal(20,0)
go
alter table Permission_ alter column resourceId decimal(20,0)
go

alter table Phone alter column phoneId decimal(20,0)
go
alter table Phone alter column typeId int;

create table PluginSetting (
	pluginSettingId decimal(20,0) not null primary key,
	companyId varchar(75) not null,
	pluginId varchar(75) null,
	pluginType varchar(75) null,
	roles varchar(75) null,
	active_ int
)
go

alter table PollsQuestion alter column groupId decimal(20,0)
go

alter table Release_ alter column releaseId decimal(20,0)
go
alter table Release_ add verified int;

alter table Resource_ alter column resourceId decimal(20,0)
go
alter table Resource_ add codeId decimal(20,0)
go
alter table Resource_ drop typeId;

create table ResourceCode (
	codeId decimal(20,0) not null primary key,
	companyId varchar(75) not null,
	name varchar(75) null,
	scope varchar(75) null
)
go

alter table Role_ add type_ int;
update Role_ SET type_ = 1;

alter table Roles_Permissions alter column permissionId decimal(20,0)
go

create table SCFrameworkVersion (
	frameworkVersionId decimal(20,0) not null primary key,
	groupId decimal(20,0) not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(75) null,
	url varchar(1024) null,
	active_ int,
	priority int
)
go

create table SCFrameworkVersions_SCProductVersions (
	productVersionId decimal(20,0),
	frameworkVersionId decimal(20,0),
	primary key (productVersionId, frameworkVersionId)
)
go

create table SCLicense (
	licenseId decimal(20,0) not null primary key,
	name varchar(75) null,
	url varchar(1024) null,
	openSource int,
	active_ int,
	recommended int
)
go

create table SCLicenses_SCProductEntries (
	productEntryId decimal(20,0),
	licenseId decimal(20,0),
	primary key (productEntryId, licenseId)
)
go

create table SCProductEntry (
	productEntryId decimal(20,0) not null primary key,
	groupId decimal(20,0) not null,
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
)
go

create table SCProductVersion (
	productVersionId decimal(20,0) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	productEntryId decimal(20,0),
	version varchar(75) null,
	changeLog varchar(1000) null,
	downloadPageURL varchar(1024) null,
	directDownloadURL varchar(1024) null,
	repoStoreArtifact int
)
go

create table SCLicenses_SCProductEntries (
	productEntryId decimal(20,0),
	licenseId decimal(20,0),
	primary key (productEntryId, licenseId)
)
go

create table SCProductEntry (
	productEntryId decimal(20,0) primary key,
	groupId decimal(20,0),
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
)
go

create table SCProductVersion (
	productVersionId decimal(20,0) primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	productEntryId decimal(20,0),
	version varchar(75) null,
	changeLog varchar(1000) null,
	downloadPageURL varchar(75) null,
	directDownloadURL varchar(75) null,
	repoStoreArtifact int
)
go

alter table ShoppingCart alter column groupId decimal(20,0)
go

alter table ShoppingCategory alter column groupId decimal(20,0)
go

alter table ShoppingCoupon alter column groupId decimal(20,0)
go

alter table ShoppingOrder alter column groupId decimal(20,0)
go

alter table Subscription alter column subscriptionId decimal(20,0)
go

create table TagsAsset (
	assetId decimal(20,0) not null primary key,
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
)
go

create table TagsAssets_TagsEntries (
	assetId decimal(20,0),
	entryId decimal(20,0),
	primary key (assetId, entryId)
)
go

create table TagsEntry (
	entryId decimal(20,0) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(75) null
)
go

create table TagsProperty (
	propertyId decimal(20,0) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	entryId decimal(20,0),
	key_ varchar(75) null,
	value varchar(75) null
)
go

create table TagsSource (
	sourceId decimal(20,0) not null primary key,
	parentSourceId decimal(20,0),
	name varchar(75) null,
	acronym varchar(75) null
)
go

alter table User_ alter column contactId decimal(20,0)
go
alter table User_ add screenName varchar(75) null;
update User_ set screenName = userId;

create table UserGroupRole (
	userId varchar(75) not null,
	groupId decimal(20,0) not null,
	roleId varchar(75) not null,
	primary key (userId, groupId, roleId)
)
go

alter table Users_Groups alter column groupId decimal(20,0)
go

alter table Users_Permissions alter column permissionId decimal(20,0)
go

alter table Website alter column websiteId decimal(20,0)
go
alter table Website alter column typeId int;

alter table WikiNode alter column groupId decimal(20,0)
go
