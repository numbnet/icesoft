alter table Address alter column bigint not null;;
alter table Address alter column int not null;;

alter table BlogsCategory alter column bigint not null;;
alter table BlogsCategory alter column bigint not null;;

alter table BlogsEntry alter column bigint not null;;
alter table BlogsEntry alter column bigint not null;;
alter table BlogsEntry alter column bigint not null;;

alter table BookmarksEntry alter column bigint not null;;

alter table BookmarksFolder alter column bigint not null;;
alter table BookmarksFolder alter column bigint not null;;
alter table BookmarksFolder alter column bigint not null;;

alter table CalEvent alter column bigint not null;;
alter table CalEvent alter column bigint not null;;

alter table Contact_ alter column int not null;;
alter table Contact_ alter column int not null;;
alter table Contact_ alter column bigint not null;;

alter table Counter alter column bigint not null;;

alter table DLFolder alter column bigint not null;;

alter table EmailAddress alter column bigint not null;;
alter table EmailAddress alter column int not null;;

alter table Group_ alter column bigint not null;;
alter table Group_ add creatorUserId varchar(75) null;
alter table Group_ alter column bigint not null;;
alter table Group_ add liveGroupId bigint;
alter table Group_ add active_ bit null;
update Group_ set liveGroupId = -1;
update Group_ set friendlyURL = '' where className = 'com.liferay.portal.model.User';
update Group_ set active_ = true;

alter table Groups_Orgs alter column bigint not null;;

alter table Groups_Permissions alter column bigint not null;;
alter table Groups_Permissions alter column bigint not null;;

alter table Groups_Roles alter column bigint not null;;

alter table Groups_UserGroups alter column bigint not null;;

alter table IGFolder alter column bigint not null;;

alter table JournalArticle alter column bigint not null;;

alter table JournalContentSearch alter column bigint not null;;

alter table JournalStructure alter column bigint not null;;

alter table JournalTemplate alter column bigint not null;;

alter table Layout add iconImage bit;
alter table Layout add css varchar(75) null;

alter table LayoutSet alter column bigint not null;;
alter table LayoutSet add logo bit;
alter table LayoutSet add css varchar(75) null;
update LayoutSet set logo = false;

alter table ListType alter column int not null;;

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

alter table MBCategory alter column bigint not null;;

alter table MBStatsUser alter column bigint not null;;

alter table Organization_ alter column int not null;;

alter table OrgGroupPermission alter column bigint not null;;
alter table OrgGroupPermission alter column bigint not null;;

alter table OrgGroupRole alter column bigint not null;;

alter table OrgLabor alter column int not null;;

alter table PasswordTracker alter column bigint not null;;

alter table Permission_ alter column bigint not null;;
alter table Permission_ alter column bigint not null;;

alter table Phone alter column bigint not null;;
alter table Phone alter column int not null;;

create table PluginSetting (
	pluginSettingId bigint not null primary key,
	companyId varchar(75) not null,
	pluginId varchar(75) null,
	pluginType varchar(75) null,
	roles varchar(75) null,
	active_ bit
);

alter table PollsQuestion alter column bigint not null;;

alter table Release_ alter column bigint not null;;
alter table Release_ add verified bit;

alter table Resource_ alter column bigint not null;;
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

alter table Roles_Permissions alter column bigint not null;;

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
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(75) null,
	type_ varchar(75) null,
	shortDescription longvarchar null,
	longDescription longvarchar null,
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
	changeLog longvarchar null,
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
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(75) null,
	type_ varchar(75) null,
	shortDescription longvarchar null,
	longDescription longvarchar null,
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
	changeLog longvarchar null,
	downloadPageURL varchar(75) null,
	directDownloadURL varchar(75) null,
	repoStoreArtifact bit
);

alter table ShoppingCart alter column bigint not null;;

alter table ShoppingCategory alter column bigint not null;;

alter table ShoppingCoupon alter column bigint not null;;

alter table ShoppingOrder alter column bigint not null;;

alter table Subscription alter column bigint not null;;

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

alter table User_ alter column bigint not null;;
alter table User_ add screenName varchar(75) null;
update User_ set screenName = userId;

create table UserGroupRole (
	userId varchar(75) not null,
	groupId bigint not null,
	roleId varchar(75) not null,
	primary key (userId, groupId, roleId)
);

alter table Users_Groups alter column bigint not null;;

alter table Users_Permissions alter column bigint not null;;

alter table Website alter column bigint not null;;
alter table Website alter column int not null;;

alter table WikiNode alter column bigint not null;;
