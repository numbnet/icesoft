alter table Address modify addressId bigint;
alter table Address modify typeId integer;

alter table BlogsCategory modify categoryId bigint;
alter table BlogsCategory modify parentCategoryId bigint;

alter table BlogsEntry modify entryId bigint;
alter table BlogsEntry modify groupId bigint;
alter table BlogsEntry modify categoryId bigint;

alter table BookmarksEntry modify entryId bigint;

alter table BookmarksFolder modify folderId bigint;
alter table BookmarksFolder modify groupId bigint;
alter table BookmarksFolder modify parentFolderId bigint;

alter table CalEvent modify eventId bigint;
alter table CalEvent modify groupId bigint;

alter table Contact_ modify prefixId integer;
alter table Contact_ modify suffixId integer;
alter table Contact_ modify parentContactId bigint;

alter table Counter modify currentId bigint;

alter table DLFolder modify groupId bigint;

alter table EmailAddress modify emailAddressId bigint;
alter table EmailAddress modify typeId integer;

alter table Group_ modify groupId bigint;
alter table Group_ add creatorUserId varchar(75) null;
alter table Group_ modify parentGroupId bigint;
alter table Group_ add liveGroupId bigint;
alter table Group_ add active_ tinyint null;
update Group_ set liveGroupId = -1;
update Group_ set friendlyURL = '' where className = 'com.liferay.portal.model.User';
update Group_ set active_ = 1;

alter table Groups_Orgs modify groupId bigint;

alter table Groups_Permissions modify groupId bigint;
alter table Groups_Permissions modify permissionId bigint;

alter table Groups_Roles modify groupId bigint;

alter table Groups_UserGroups modify groupId bigint;

alter table IGFolder modify groupId bigint;

alter table JournalArticle modify groupId bigint;

alter table JournalContentSearch modify groupId bigint;

alter table JournalStructure modify groupId bigint;

alter table JournalTemplate modify groupId bigint;

alter table Layout add iconImage tinyint;
alter table Layout add css varchar(75) null;

alter table LayoutSet modify groupId bigint;
alter table LayoutSet add logo tinyint;
alter table LayoutSet add css varchar(75) null;
update LayoutSet set logo = 0;

alter table ListType modify listTypeId integer;

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

alter table MBCategory modify groupId bigint;

alter table MBStatsUser modify groupId bigint;

alter table Organization_ modify statusId integer;

alter table OrgGroupPermission modify permissionId bigint;
alter table OrgGroupPermission modify groupId bigint;

alter table OrgGroupRole modify groupId bigint;

alter table OrgLabor modify typeId integer;

alter table PasswordTracker modify passwordTrackerId bigint;

alter table Permission_ modify permissionId bigint;
alter table Permission_ modify resourceId bigint;

alter table Phone modify phoneId bigint;
alter table Phone modify typeId integer;

create table PluginSetting (
	pluginSettingId bigint not null primary key,
	companyId varchar(75) not null,
	pluginId varchar(75) null,
	pluginType varchar(75) null,
	roles varchar(75) null,
	active_ tinyint
);

alter table PollsQuestion modify groupId bigint;

alter table Release_ modify releaseId bigint;
alter table Release_ add verified tinyint;

alter table Resource_ modify resourceId bigint;
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

alter table Roles_Permissions modify permissionId bigint;

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
	active_ tinyint,
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
	openSource tinyint,
	active_ tinyint,
	recommended tinyint
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
	shortDescription longtext null,
	longDescription longtext null,
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
	changeLog longtext null,
	downloadPageURL varchar(1024) null,
	directDownloadURL varchar(1024) null,
	repoStoreArtifact tinyint
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
	shortDescription longtext null,
	longDescription longtext null,
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
	changeLog longtext null,
	downloadPageURL varchar(75) null,
	directDownloadURL varchar(75) null,
	repoStoreArtifact tinyint
);

alter table ShoppingCart modify groupId bigint;

alter table ShoppingCategory modify groupId bigint;

alter table ShoppingCoupon modify groupId bigint;

alter table ShoppingOrder modify groupId bigint;

alter table Subscription modify subscriptionId bigint;

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

alter table User_ modify contactId bigint;
alter table User_ add screenName varchar(75) null;
update User_ set screenName = userId;

create table UserGroupRole (
	userId varchar(75) not null,
	groupId bigint not null,
	roleId varchar(75) not null,
	primary key (userId, groupId, roleId)
);

alter table Users_Groups modify groupId bigint;

alter table Users_Permissions modify permissionId bigint;

alter table Website modify websiteId bigint;
alter table Website modify typeId integer;

alter table WikiNode modify groupId bigint;
