-- alter_column_type Address addressId bigint;
-- alter_column_type Address typeId integer;

-- alter_column_type BlogsCategory categoryId bigint;
-- alter_column_type BlogsCategory parentCategoryId bigint;

-- alter_column_type BlogsEntry entryId bigint;
-- alter_column_type BlogsEntry groupId bigint;
-- alter_column_type BlogsEntry categoryId bigint;

-- alter_column_type BookmarksEntry entryId bigint;

-- alter_column_type BookmarksFolder folderId bigint;
-- alter_column_type BookmarksFolder groupId bigint;
-- alter_column_type BookmarksFolder parentFolderId bigint;

-- alter_column_type CalEvent eventId bigint;
-- alter_column_type CalEvent groupId bigint;

-- alter_column_type Contact_ prefixId integer;
-- alter_column_type Contact_ suffixId integer;
-- alter_column_type Contact_ parentContactId bigint;

-- alter_column_type Counter currentId bigint;

-- alter_column_type DLFolder groupId bigint;

-- alter_column_type EmailAddress emailAddressId bigint;
-- alter_column_type EmailAddress typeId integer;

-- alter_column_type Group_ groupId bigint;
alter table Group_ add creatorUserId varchar(75);
-- alter_column_type Group_ parentGroupId bigint;
alter table Group_ add liveGroupId bigint;
alter table Group_ add active_ smallint;
update Group_ set liveGroupId = -1;
update Group_ set friendlyURL = '' where className = 'com.liferay.portal.model.User';
update Group_ set active_ = 1;

-- alter_column_type Groups_Orgs groupId bigint;

-- alter_column_type Groups_Permissions groupId bigint;
-- alter_column_type Groups_Permissions permissionId bigint;

-- alter_column_type Groups_Roles groupId bigint;

-- alter_column_type Groups_UserGroups groupId bigint;

-- alter_column_type IGFolder groupId bigint;

-- alter_column_type JournalArticle groupId bigint;

-- alter_column_type JournalContentSearch groupId bigint;

-- alter_column_type JournalStructure groupId bigint;

-- alter_column_type JournalTemplate groupId bigint;

alter table Layout add iconImage smallint;
alter table Layout add css varchar(75);

-- alter_column_type LayoutSet groupId bigint;
alter table LayoutSet add logo smallint;
alter table LayoutSet add css varchar(75);
update LayoutSet set logo = 0;

-- alter_column_type ListType listTypeId integer;

create table MBBan (
	banId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	banUserId varchar(75)
);

-- alter_column_type MBCategory groupId bigint;

-- alter_column_type MBStatsUser groupId bigint;

-- alter_column_type Organization_ statusId integer;

-- alter_column_type OrgGroupPermission permissionId bigint;
-- alter_column_type OrgGroupPermission groupId bigint;

-- alter_column_type OrgGroupRole groupId bigint;

-- alter_column_type OrgLabor typeId integer;

-- alter_column_type PasswordTracker passwordTrackerId bigint;

-- alter_column_type Permission_ permissionId bigint;
-- alter_column_type Permission_ resourceId bigint;

-- alter_column_type Phone phoneId bigint;
-- alter_column_type Phone typeId integer;

create table PluginSetting (
	pluginSettingId bigint not null primary key,
	companyId varchar(75) not null,
	pluginId varchar(75),
	pluginType varchar(75),
	roles varchar(75),
	active_ smallint
);

-- alter_column_type PollsQuestion groupId bigint;

-- alter_column_type Release_ releaseId bigint;
alter table Release_ add verified smallint;

-- alter_column_type Resource_ resourceId bigint;
alter table Resource_ add codeId bigint;
alter table Resource_ drop typeId;

create table ResourceCode (
	codeId bigint not null primary key,
	companyId varchar(75) not null,
	name varchar(75),
	scope varchar(75)
);

alter table Role_ add type_ integer;
update Role_ SET type_ = 1;

-- alter_column_type Roles_Permissions permissionId bigint;

create table SCFrameworkVersion (
	frameworkVersionId bigint not null primary key,
	groupId bigint not null,
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
	productVersionId bigint,
	frameworkVersionId bigint,
	primary key (productVersionId, frameworkVersionId)
);

create table SCLicense (
	licenseId bigint not null primary key,
	name varchar(75),
	url varchar(1024),
	openSource smallint,
	active_ smallint,
	recommended smallint
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
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75),
	type_ varchar(75),
	shortDescription varchar(500),
	longDescription varchar(500),
	pageURL varchar(1024),
	repoGroupId varchar(75),
	repoArtifactId varchar(75)
);

create table SCProductVersion (
	productVersionId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	productEntryId bigint,
	version varchar(75),
	changeLog varchar(500),
	downloadPageURL varchar(1024),
	directDownloadURL varchar(1024),
	repoStoreArtifact smallint
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
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75),
	type_ varchar(75),
	shortDescription varchar(500),
	longDescription varchar(500),
	pageURL varchar(75),
	repoGroupId varchar(75),
	repoArtifactId varchar(75)
);

create table SCProductVersion (
	productVersionId bigint primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	productEntryId bigint,
	version varchar(75),
	changeLog varchar(500),
	downloadPageURL varchar(75),
	directDownloadURL varchar(75),
	repoStoreArtifact smallint
);

-- alter_column_type ShoppingCart groupId bigint;

-- alter_column_type ShoppingCategory groupId bigint;

-- alter_column_type ShoppingCoupon groupId bigint;

-- alter_column_type ShoppingOrder groupId bigint;

-- alter_column_type Subscription subscriptionId bigint;

create table TagsAsset (
	assetId bigint not null primary key,
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
	assetId bigint,
	entryId bigint,
	primary key (assetId, entryId)
);

create table TagsEntry (
	entryId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75)
);

create table TagsProperty (
	propertyId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	entryId bigint,
	key_ varchar(75),
	value varchar(75)
);

create table TagsSource (
	sourceId bigint not null primary key,
	parentSourceId bigint,
	name varchar(75),
	acronym varchar(75)
);

-- alter_column_type User_ contactId bigint;
alter table User_ add screenName varchar(75);
update User_ set screenName = userId;

create table UserGroupRole (
	userId varchar(75) not null,
	groupId bigint not null,
	roleId varchar(75) not null,
	primary key (userId, groupId, roleId)
);

-- alter_column_type Users_Groups groupId bigint;

-- alter_column_type Users_Permissions permissionId bigint;

-- alter_column_type Website websiteId bigint;
-- alter_column_type Website typeId integer;

-- alter_column_type WikiNode groupId bigint;
