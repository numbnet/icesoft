alter table Address modify addressId number(30,0);
alter table Address modify typeId number(30,0);

alter table BlogsCategory modify categoryId number(30,0);
alter table BlogsCategory modify parentCategoryId number(30,0);

alter table BlogsEntry modify entryId number(30,0);
alter table BlogsEntry modify groupId number(30,0);
alter table BlogsEntry modify categoryId number(30,0);

alter table BookmarksEntry modify entryId number(30,0);

alter table BookmarksFolder modify folderId number(30,0);
alter table BookmarksFolder modify groupId number(30,0);
alter table BookmarksFolder modify parentFolderId number(30,0);

alter table CalEvent modify eventId number(30,0);
alter table CalEvent modify groupId number(30,0);

alter table Contact_ modify prefixId number(30,0);
alter table Contact_ modify suffixId number(30,0);
alter table Contact_ modify parentContactId number(30,0);

alter table Counter modify currentId number(30,0);

alter table DLFolder modify groupId number(30,0);

alter table EmailAddress modify emailAddressId number(30,0);
alter table EmailAddress modify typeId number(30,0);

alter table Group_ modify groupId number(30,0);
alter table Group_ add creatorUserId varchar2(75) null;
alter table Group_ modify parentGroupId number(30,0);
alter table Group_ add liveGroupId number(30,0);
alter table Group_ add active_ number(1, 0) null;
update Group_ set liveGroupId = -1;
update Group_ set friendlyURL = '' where className = 'com.liferay.portal.model.User';
update Group_ set active_ = 1;

alter table Groups_Orgs modify groupId number(30,0);

alter table Groups_Permissions modify groupId number(30,0);
alter table Groups_Permissions modify permissionId number(30,0);

alter table Groups_Roles modify groupId number(30,0);

alter table Groups_UserGroups modify groupId number(30,0);

alter table IGFolder modify groupId number(30,0);

alter table JournalArticle modify groupId number(30,0);

alter table JournalContentSearch modify groupId number(30,0);

alter table JournalStructure modify groupId number(30,0);

alter table JournalTemplate modify groupId number(30,0);

alter table Layout add iconImage number(1, 0);
alter table Layout add css varchar2(75) null;

alter table LayoutSet modify groupId number(30,0);
alter table LayoutSet add logo number(1, 0);
alter table LayoutSet add css varchar2(75) null;
update LayoutSet set logo = 0;

alter table ListType modify listTypeId number(30,0);

create table MBBan (
	banId number(30,0) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	banUserId varchar2(75) null
);

alter table MBCategory modify groupId number(30,0);

alter table MBStatsUser modify groupId number(30,0);

alter table Organization_ modify statusId number(30,0);

alter table OrgGroupPermission modify permissionId number(30,0);
alter table OrgGroupPermission modify groupId number(30,0);

alter table OrgGroupRole modify groupId number(30,0);

alter table OrgLabor modify typeId number(30,0);

alter table PasswordTracker modify passwordTrackerId number(30,0);

alter table Permission_ modify permissionId number(30,0);
alter table Permission_ modify resourceId number(30,0);

alter table Phone modify phoneId number(30,0);
alter table Phone modify typeId number(30,0);

create table PluginSetting (
	pluginSettingId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	pluginId varchar2(75) null,
	pluginType varchar2(75) null,
	roles varchar2(75) null,
	active_ number(1, 0)
);

alter table PollsQuestion modify groupId number(30,0);

alter table Release_ modify releaseId number(30,0);
alter table Release_ add verified number(1, 0);

alter table Resource_ modify resourceId number(30,0);
alter table Resource_ add codeId number(30,0);
alter table Resource_ drop typeId;

create table ResourceCode (
	codeId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	name varchar2(75) null,
	scope varchar2(75) null
);

alter table Role_ add type_ number(30,0);
update Role_ SET type_ = 1;

alter table Roles_Permissions modify permissionId number(30,0);

create table SCFrameworkVersion (
	frameworkVersionId number(30,0) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(75) null,
	url varchar2(1024) null,
	active_ number(1, 0),
	priority number(30,0)
);

create table SCFrameworkVersions_SCProductVersions (
	productVersionId number(30,0),
	frameworkVersionId number(30,0),
	primary key (productVersionId, frameworkVersionId)
);

create table SCLicense (
	licenseId number(30,0) not null primary key,
	name varchar2(75) null,
	url varchar2(1024) null,
	openSource number(1, 0),
	active_ number(1, 0),
	recommended number(1, 0)
);

create table SCLicenses_SCProductEntries (
	productEntryId number(30,0),
	licenseId number(30,0),
	primary key (productEntryId, licenseId)
);

create table SCProductEntry (
	productEntryId number(30,0) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(75) null,
	type_ varchar2(75) null,
	shortDescription varchar2(4000) null,
	longDescription varchar2(4000) null,
	pageURL varchar2(1024) null,
	repoGroupId varchar2(75) null,
	repoArtifactId varchar2(75) null
);

create table SCProductVersion (
	productVersionId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	productEntryId number(30,0),
	version varchar2(75) null,
	changeLog varchar2(4000) null,
	downloadPageURL varchar2(1024) null,
	directDownloadURL varchar2(1024) null,
	repoStoreArtifact number(1, 0)
);

create table SCLicenses_SCProductEntries (
	productEntryId number(30,0),
	licenseId number(30,0),
	primary key (productEntryId, licenseId)
);

create table SCProductEntry (
	productEntryId number(30,0) primary key,
	groupId number(30,0),
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(75) null,
	type_ varchar2(75) null,
	shortDescription varchar2(4000) null,
	longDescription varchar2(4000) null,
	pageURL varchar2(75) null,
	repoGroupId varchar2(75) null,
	repoArtifactId varchar2(75) null
);

create table SCProductVersion (
	productVersionId number(30,0) primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	productEntryId number(30,0),
	version varchar2(75) null,
	changeLog varchar2(4000) null,
	downloadPageURL varchar2(75) null,
	directDownloadURL varchar2(75) null,
	repoStoreArtifact number(1, 0)
);

alter table ShoppingCart modify groupId number(30,0);

alter table ShoppingCategory modify groupId number(30,0);

alter table ShoppingCoupon modify groupId number(30,0);

alter table ShoppingOrder modify groupId number(30,0);

alter table Subscription modify subscriptionId number(30,0);

create table TagsAsset (
	assetId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar2(75) null,
	classPK varchar2(75) null,
	startDate timestamp null,
	endDate timestamp null,
	publishDate timestamp null,
	expirationDate timestamp null,
	mimeType varchar2(75) null,
	title varchar2(75) null,
	url varchar2(75) null,
	height number(30,0),
	width number(30,0)
);

create table TagsAssets_TagsEntries (
	assetId number(30,0),
	entryId number(30,0),
	primary key (assetId, entryId)
);

create table TagsEntry (
	entryId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(75) null
);

create table TagsProperty (
	propertyId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	entryId number(30,0),
	key_ varchar2(75) null,
	value varchar2(75) null
);

create table TagsSource (
	sourceId number(30,0) not null primary key,
	parentSourceId number(30,0),
	name varchar2(75) null,
	acronym varchar2(75) null
);

alter table User_ modify contactId number(30,0);
alter table User_ add screenName varchar2(75) null;
update User_ set screenName = userId;

create table UserGroupRole (
	userId varchar2(75) not null,
	groupId number(30,0) not null,
	roleId varchar2(75) not null,
	primary key (userId, groupId, roleId)
);

alter table Users_Groups modify groupId number(30,0);

alter table Users_Permissions modify permissionId number(30,0);

alter table Website modify websiteId number(30,0);
alter table Website modify typeId number(30,0);

alter table WikiNode modify groupId number(30,0);
