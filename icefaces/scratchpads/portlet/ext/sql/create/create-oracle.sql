drop user &1 cascade;
create user &1 identified by &2;
grant connect,resource to &1;
connect &1/&2;
set define off;

create table Account_ (
	accountId varchar2(75) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentAccountId varchar2(75) null,
	name varchar2(75) null,
	legalName varchar2(75) null,
	legalId varchar2(75) null,
	legalType varchar2(75) null,
	sicCode varchar2(75) null,
	tickerSymbol varchar2(75) null,
	industry varchar2(75) null,
	type_ varchar2(75) null,
	size_ varchar2(75) null
);

create table Address (
	addressId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar2(75) null,
	classPK varchar2(75) null,
	street1 varchar2(75) null,
	street2 varchar2(75) null,
	street3 varchar2(75) null,
	city varchar2(75) null,
	zip varchar2(75) null,
	regionId varchar2(75) null,
	countryId varchar2(75) null,
	typeId number(30,0),
	mailing number(1, 0),
	primary_ number(1, 0)
);

create table BlogsCategory (
	categoryId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentCategoryId number(30,0),
	name varchar2(75) null,
	description varchar2(4000) null
);

create table BlogsEntry (
	entryId number(30,0) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	categoryId number(30,0),
	title varchar2(75) null,
	content clob null,
	displayDate timestamp null
);

create table BookmarksEntry (
	entryId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	folderId number(30,0),
	name varchar2(75) null,
	url varchar2(4000) null,
	comments varchar2(4000) null,
	visits number(30,0)
);

create table BookmarksFolder (
	folderId number(30,0) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentFolderId number(30,0),
	name varchar2(75) null,
	description varchar2(4000) null
);

create table CalEvent (
	eventId number(30,0) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	title varchar2(75) null,
	description varchar2(4000) null,
	startDate timestamp null,
	endDate timestamp null,
	durationHour number(30,0),
	durationMinute number(30,0),
	allDay number(1, 0),
	timeZoneSensitive number(1, 0),
	type_ varchar2(75) null,
	repeating number(1, 0),
	recurrence clob null,
	remindBy varchar2(75) null,
	firstReminder number(30,0),
	secondReminder number(30,0)
);

create table Company (
	companyId varchar2(75) not null primary key,
	key_ clob null,
	portalURL varchar2(75) null,
	homeURL varchar2(75) null,
	mx varchar2(75) null
);

create table Contact_ (
	contactId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	accountId varchar2(75) null,
	parentContactId number(30,0),
	firstName varchar2(75) null,
	middleName varchar2(75) null,
	lastName varchar2(75) null,
	nickName varchar2(75) null,
	prefixId number(30,0),
	suffixId number(30,0),
	male number(1, 0),
	birthday timestamp null,
	smsSn varchar2(75) null,
	aimSn varchar2(75) null,
	icqSn varchar2(75) null,
	jabberSn varchar2(75) null,
	msnSn varchar2(75) null,
	skypeSn varchar2(75) null,
	ymSn varchar2(75) null,
	employeeStatusId varchar2(75) null,
	employeeNumber varchar2(75) null,
	jobTitle varchar2(75) null,
	jobClass varchar2(75) null,
	hoursOfOperation varchar2(75) null
);

create table Counter (
	name varchar2(75) not null primary key,
	currentId number(30,0)
);

create table Country (
	countryId varchar2(75) not null primary key,
	name varchar2(75) null,
	a2 varchar2(75) null,
	a3 varchar2(75) null,
	number_ varchar2(75) null,
	idd_ varchar2(75) null,
	active_ number(1, 0)
);

create table CyrusUser (
	userId varchar2(75) not null primary key,
	password_ varchar2(75) not null
);

create table CyrusVirtual (
	emailAddress varchar2(75) not null primary key,
	userId varchar2(75) not null
);

create table DataTracker (
	dataTrackerId varchar2(75) not null primary key,
	companyId varchar2(75) not null,
	createdOn timestamp null,
	createdByUserId varchar2(75) null,
	createdByUserName varchar2(75) null,
	updatedOn timestamp null,
	updatedBy varchar2(75) null,
	className varchar2(75) null,
	classPK varchar2(75) null,
	active_ number(1, 0)
);

create table DLFileEntry (
	folderId varchar2(75) not null,
	name varchar2(100) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	versionUserId varchar2(75) null,
	versionUserName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	title varchar2(100) null,
	description varchar2(4000) null,
	version number(30,20),
	size_ number(30,0),
	readCount number(30,0),
	extraSettings clob null,
	primary key (folderId, name)
);

create table DLFileRank (
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	folderId varchar2(75) not null,
	name varchar2(100) not null,
	createDate timestamp null,
	primary key (companyId, userId, folderId, name)
);

create table DLFileShortcut (
	fileShortcutId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	folderId varchar2(75) null,
	toFolderId varchar2(75) null,
	toName varchar2(75) null
);

create table DLFileVersion (
	folderId varchar2(75) not null,
	name varchar2(100) not null,
	version number(30,20) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	size_ number(30,0),
	primary key (folderId, name, version)
);

create table DLFolder (
	folderId varchar2(75) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentFolderId varchar2(75) null,
	name varchar2(100) null,
	description varchar2(4000) null,
	lastPostDate timestamp null
);

create table EmailAddress (
	emailAddressId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar2(75) null,
	classPK varchar2(75) null,
	address varchar2(75) null,
	typeId number(30,0),
	primary_ number(1, 0)
);

create table Group_ (
	groupId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	creatorUserId varchar2(75) null,
	className varchar2(75) null,
	classPK varchar2(75) null,
	parentGroupId number(30,0),
	liveGroupId number(30,0),
	name varchar2(75) null,
	description varchar2(4000) null,
	type_ varchar2(75) null,
	friendlyURL varchar2(75) null,
	active_ number(1, 0)
);

create table Groups_Orgs (
	groupId number(30,0) not null,
	organizationId varchar2(75) not null,
	primary key (groupId, organizationId)
);

create table Groups_Permissions (
	groupId number(30,0) not null,
	permissionId number(30,0) not null,
	primary key (groupId, permissionId)
);

create table Groups_Roles (
	groupId number(30,0) not null,
	roleId varchar2(75) not null,
	primary key (groupId, roleId)
);

create table Groups_UserGroups (
	groupId number(30,0) not null,
	userGroupId varchar2(75) not null,
	primary key (groupId, userGroupId)
);

create table IGFolder (
	folderId varchar2(75) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentFolderId varchar2(75) null,
	name varchar2(75) null,
	description varchar2(4000) null
);

create table IGImage (
	companyId varchar2(75) not null,
	imageId varchar2(75) not null,
	userId varchar2(75) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	folderId varchar2(75) null,
	description varchar2(4000) null,
	height number(30,0),
	width number(30,0),
	size_ number(30,0),
	primary key (companyId, imageId)
);

create table Image (
	imageId varchar2(200) not null primary key,
	modifiedDate timestamp null,
	text_ clob null,
	type_ varchar2(75) null
);

create table JournalArticle (
	companyId varchar2(75) not null,
	groupId number(30,0) not null,
	articleId varchar2(75) not null,
	version number(30,20) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	title varchar2(75) null,
	description varchar2(4000) null,
	content clob null,
	type_ varchar2(75) null,
	structureId varchar2(75) null,
	templateId varchar2(75) null,
	displayDate timestamp null,
	approved number(1, 0),
	approvedByUserId varchar2(75) null,
	approvedByUserName varchar2(75) null,
	approvedDate timestamp null,
	expired number(1, 0),
	expirationDate timestamp null,
	reviewDate timestamp null,
	primary key (companyId, groupId, articleId, version)
);

create table JournalContentSearch (
	portletId varchar2(75) not null,
	layoutId varchar2(75) not null,
	ownerId varchar2(75) not null,
	articleId varchar2(75) not null,
	companyId varchar2(75) not null,
	groupId number(30,0) not null,
	primary key (portletId, layoutId, ownerId, articleId)
);

create table JournalStructure (
	companyId varchar2(75) not null,
	groupId number(30,0) not null,
	structureId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(75) null,
	description varchar2(4000) null,
	xsd clob null,
	primary key (companyId, groupId, structureId)
);

create table JournalTemplate (
	companyId varchar2(75) not null,
	groupId number(30,0) not null,
	templateId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	structureId varchar2(75) null,
	name varchar2(75) null,
	description varchar2(4000) null,
	xsl clob null,
	langType varchar2(75) null,
	smallImage number(1, 0),
	smallImageURL varchar2(75) null,
	primary key (companyId, groupId, templateId)
);

create table Layout (
	layoutId varchar2(75) not null,
	ownerId varchar2(75) not null,
	companyId varchar2(75) not null,
	parentLayoutId varchar2(75) null,
	name varchar2(4000) null,
	title varchar2(4000) null,
	type_ varchar2(75) null,
	typeSettings clob null,
	hidden_ number(1, 0),
	friendlyURL varchar2(75) null,
	iconImage number(1, 0),
	themeId varchar2(75) null,
	colorSchemeId varchar2(75) null,
	css varchar2(75) null,
	priority number(30,0),
	primary key (layoutId, ownerId)
);

create table LayoutSet (
	ownerId varchar2(75) not null primary key,
	companyId varchar2(75) not null,
	groupId number(30,0) not null,
	userId varchar2(75) not null,
	privateLayout number(1, 0),
	logo number(1, 0),
	themeId varchar2(75) null,
	colorSchemeId varchar2(75) null,
	css varchar2(75) null,
	pageCount number(30,0),
	virtualHost varchar2(75) null
);

create table ListType (
	listTypeId number(30,0) not null primary key,
	name varchar2(75) null,
	type_ varchar2(75) null
);

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

create table MBCategory (
	categoryId varchar2(75) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentCategoryId varchar2(75) null,
	name varchar2(75) null,
	description varchar2(4000) null,
	lastPostDate timestamp null
);

create table MBDiscussion (
	discussionId varchar2(75) not null primary key,
	className varchar2(75) null,
	classPK varchar2(75) null,
	threadId varchar2(75) null
);

create table MBMessage (
	topicId varchar2(75) not null,
	messageId varchar2(75) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	categoryId varchar2(75) null,
	threadId varchar2(75) null,
	parentMessageId varchar2(75) null,
	subject varchar2(75) null,
	body clob null,
	attachments number(1, 0),
	anonymous number(1, 0),
	primary key (topicId, messageId)
);

create table MBMessageFlag (
	topicId varchar2(75) not null,
	messageId varchar2(75) not null,
	userId varchar2(75) not null,
	flag varchar2(75) null,
	primary key (topicId, messageId, userId)
);

create table MBStatsUser (
	groupId number(30,0) not null,
	userId varchar2(75) not null,
	messageCount number(30,0),
	lastPostDate timestamp null,
	primary key (groupId, userId)
);

create table MBThread (
	threadId varchar2(75) not null primary key,
	categoryId varchar2(75) null,
	topicId varchar2(75) null,
	rootMessageId varchar2(75) null,
	messageCount number(30,0),
	viewCount number(30,0),
	lastPostByUserId varchar2(75) null,
	lastPostDate timestamp null,
	priority number(30,20)
);

create table Organization_ (
	organizationId varchar2(75) not null primary key,
	companyId varchar2(75) not null,
	parentOrganizationId varchar2(75) null,
	name varchar2(75) null,
	recursable number(1, 0),
	regionId varchar2(75) null,
	countryId varchar2(75) null,
	statusId number(30,0),
	comments varchar2(4000) null
);

create table OrgGroupPermission (
	organizationId varchar2(75) not null,
	groupId number(30,0) not null,
	permissionId number(30,0) not null,
	primary key (organizationId, groupId, permissionId)
);

create table OrgGroupRole (
	organizationId varchar2(75) not null,
	groupId number(30,0) not null,
	roleId varchar2(75) not null,
	primary key (organizationId, groupId, roleId)
);

create table OrgLabor (
	orgLaborId varchar2(75) not null primary key,
	organizationId varchar2(75) null,
	typeId number(30,0),
	sunOpen number(30,0),
	sunClose number(30,0),
	monOpen number(30,0),
	monClose number(30,0),
	tueOpen number(30,0),
	tueClose number(30,0),
	wedOpen number(30,0),
	wedClose number(30,0),
	thuOpen number(30,0),
	thuClose number(30,0),
	friOpen number(30,0),
	friClose number(30,0),
	satOpen number(30,0),
	satClose number(30,0)
);

create table PasswordTracker (
	passwordTrackerId number(30,0) not null primary key,
	userId varchar2(75) not null,
	createDate timestamp null,
	password_ varchar2(75) null
);

create table Permission_ (
	permissionId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	actionId varchar2(75) null,
	resourceId number(30,0)
);

create table Phone (
	phoneId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar2(75) null,
	classPK varchar2(75) null,
	number_ varchar2(75) null,
	extension varchar2(75) null,
	typeId number(30,0),
	primary_ number(1, 0)
);

create table PluginSetting (
	pluginSettingId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	pluginId varchar2(75) null,
	pluginType varchar2(75) null,
	roles varchar2(75) null,
	active_ number(1, 0)
);

create table PollsChoice (
	questionId varchar2(75) not null,
	choiceId varchar2(75) not null,
	description varchar2(75) null,
	primary key (questionId, choiceId)
);

create table PollsQuestion (
	questionId varchar2(75) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	title varchar2(75) null,
	description varchar2(4000) null,
	expirationDate timestamp null,
	lastVoteDate timestamp null
);

create table PollsVote (
	questionId varchar2(75) not null,
	userId varchar2(75) not null,
	choiceId varchar2(75) null,
	voteDate timestamp null,
	primary key (questionId, userId)
);

create table Portlet (
	portletId varchar2(75) not null,
	companyId varchar2(75) not null,
	roles varchar2(75) null,
	active_ number(1, 0),
	primary key (portletId, companyId)
);

create table PortletPreferences (
	portletId varchar2(75) not null,
	layoutId varchar2(75) not null,
	ownerId varchar2(75) not null,
	preferences clob null,
	primary key (portletId, layoutId, ownerId)
);

create table RatingsEntry (
	entryId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar2(75) null,
	classPK varchar2(75) null,
	score number(30,20)
);

create table RatingsStats (
	statsId number(30,0) not null primary key,
	className varchar2(75) null,
	classPK varchar2(75) null,
	totalEntries number(30,0),
	totalScore number(30,20),
	averageScore number(30,20)
);

create table Region (
	regionId varchar2(75) not null primary key,
	countryId varchar2(75) null,
	regionCode varchar2(75) null,
	name varchar2(75) null,
	active_ number(1, 0)
);

create table Release_ (
	releaseId number(30,0) not null primary key,
	createDate timestamp null,
	modifiedDate timestamp null,
	buildNumber number(30,0),
	buildDate timestamp null,
	verified number(1, 0)
);

create table Resource_ (
	resourceId number(30,0) not null primary key,
	codeId number(30,0),
	primKey varchar2(200) null
);

create table ResourceCode (
	codeId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	name varchar2(75) null,
	scope varchar2(75) null
);

create table Role_ (
	roleId varchar2(75) not null primary key,
	companyId varchar2(75) not null,
	className varchar2(75) null,
	classPK varchar2(75) null,
	name varchar2(75) null,
	description varchar2(4000) null,
	type_ number(30,0)
);

create table Roles_Permissions (
	roleId varchar2(75) not null,
	permissionId number(30,0) not null,
	primary key (roleId, permissionId)
);

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

create table ShoppingCart (
	cartId varchar2(75) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	itemIds varchar2(4000) null,
	couponIds varchar2(4000) null,
	altShipping number(30,0),
	insure number(1, 0)
);

create table ShoppingCategory (
	categoryId varchar2(75) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentCategoryId varchar2(75) null,
	name varchar2(75) null,
	description varchar2(4000) null
);

create table ShoppingCoupon (
	couponId varchar2(75) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(75) null,
	description varchar2(4000) null,
	startDate timestamp null,
	endDate timestamp null,
	active_ number(1, 0),
	limitCategories varchar2(4000) null,
	limitSkus varchar2(4000) null,
	minOrder number(30,20),
	discount number(30,20),
	discountType varchar2(75) null
);

create table ShoppingItem (
	itemId varchar2(75) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	categoryId varchar2(75) null,
	sku varchar2(75) null,
	name varchar2(200) null,
	description varchar2(4000) null,
	properties varchar2(4000) null,
	fields_ number(1, 0),
	fieldsQuantities varchar2(4000) null,
	minQuantity number(30,0),
	maxQuantity number(30,0),
	price number(30,20),
	discount number(30,20),
	taxable number(1, 0),
	shipping number(30,20),
	useShippingFormula number(1, 0),
	requiresShipping number(1, 0),
	stockQuantity number(30,0),
	featured_ number(1, 0),
	sale_ number(1, 0),
	smallImage number(1, 0),
	smallImageURL varchar2(75) null,
	mediumImage number(1, 0),
	mediumImageURL varchar2(75) null,
	largeImage number(1, 0),
	largeImageURL varchar2(75) null
);

create table ShoppingItemField (
	itemFieldId varchar2(75) not null primary key,
	itemId varchar2(75) null,
	name varchar2(75) null,
	values_ varchar2(4000) null,
	description varchar2(4000) null
);

create table ShoppingItemPrice (
	itemPriceId varchar2(75) not null primary key,
	itemId varchar2(75) null,
	minQuantity number(30,0),
	maxQuantity number(30,0),
	price number(30,20),
	discount number(30,20),
	taxable number(1, 0),
	shipping number(30,20),
	useShippingFormula number(1, 0),
	status number(30,0)
);

create table ShoppingOrder (
	orderId varchar2(75) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	tax number(30,20),
	shipping number(30,20),
	altShipping varchar2(75) null,
	requiresShipping number(1, 0),
	insure number(1, 0),
	insurance number(30,20),
	couponIds varchar2(75) null,
	couponDiscount number(30,20),
	billingFirstName varchar2(75) null,
	billingLastName varchar2(75) null,
	billingEmailAddress varchar2(75) null,
	billingCompany varchar2(75) null,
	billingStreet varchar2(75) null,
	billingCity varchar2(75) null,
	billingState varchar2(75) null,
	billingZip varchar2(75) null,
	billingCountry varchar2(75) null,
	billingPhone varchar2(75) null,
	shipToBilling number(1, 0),
	shippingFirstName varchar2(75) null,
	shippingLastName varchar2(75) null,
	shippingEmailAddress varchar2(75) null,
	shippingCompany varchar2(75) null,
	shippingStreet varchar2(75) null,
	shippingCity varchar2(75) null,
	shippingState varchar2(75) null,
	shippingZip varchar2(75) null,
	shippingCountry varchar2(75) null,
	shippingPhone varchar2(75) null,
	ccName varchar2(75) null,
	ccType varchar2(75) null,
	ccNumber varchar2(75) null,
	ccExpMonth number(30,0),
	ccExpYear number(30,0),
	ccVerNumber varchar2(75) null,
	comments varchar2(4000) null,
	ppTxnId varchar2(75) null,
	ppPaymentStatus varchar2(75) null,
	ppPaymentGross number(30,20),
	ppReceiverEmail varchar2(75) null,
	ppPayerEmail varchar2(75) null,
	sendOrderEmail number(1, 0),
	sendShippingEmail number(1, 0)
);

create table ShoppingOrderItem (
	orderId varchar2(75) not null,
	itemId varchar2(75) not null,
	sku varchar2(75) null,
	name varchar2(200) null,
	description varchar2(4000) null,
	properties varchar2(4000) null,
	price number(30,20),
	quantity number(30,0),
	shippedDate timestamp null,
	primary key (orderId, itemId)
);

create table Subscription (
	subscriptionId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar2(75) null,
	classPK varchar2(75) null,
	frequency varchar2(75) null
);

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

create table User_ (
	userId varchar2(75) not null primary key,
	companyId varchar2(75) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	contactId number(30,0),
	password_ varchar2(75) null,
	passwordEncrypted number(1, 0),
	passwordExpirationDate timestamp null,
	passwordReset number(1, 0),
	screenName varchar2(75) null,
	emailAddress varchar2(75) null,
	languageId varchar2(75) null,
	timeZoneId varchar2(75) null,
	greeting varchar2(75) null,
	resolution varchar2(75) null,
	comments varchar2(4000) null,
	loginDate timestamp null,
	loginIP varchar2(75) null,
	lastLoginDate timestamp null,
	lastLoginIP varchar2(75) null,
	failedLoginAttempts number(30,0),
	agreedToTermsOfUse number(1, 0),
	active_ number(1, 0)
);

create table UserGroup (
	userGroupId varchar2(75) not null primary key,
	companyId varchar2(75) not null,
	parentUserGroupId varchar2(75) null,
	name varchar2(75) null,
	description varchar2(4000) null
);

create table UserGroupRole (
	userId varchar2(75) not null,
	groupId number(30,0) not null,
	roleId varchar2(75) not null,
	primary key (userId, groupId, roleId)
);

create table UserIdMapper (
	userId varchar2(75) not null,
	type_ varchar2(75) not null,
	description varchar2(75) null,
	externalUserId varchar2(75) null,
	primary key (userId, type_)
);

create table Users_Groups (
	userId varchar2(75) not null,
	groupId number(30,0) not null,
	primary key (userId, groupId)
);

create table Users_Orgs (
	userId varchar2(75) not null,
	organizationId varchar2(75) not null,
	primary key (userId, organizationId)
);

create table Users_Permissions (
	userId varchar2(75) not null,
	permissionId number(30,0) not null,
	primary key (userId, permissionId)
);

create table Users_Roles (
	userId varchar2(75) not null,
	roleId varchar2(75) not null,
	primary key (userId, roleId)
);

create table Users_UserGroups (
	userId varchar2(75) not null,
	userGroupId varchar2(75) not null,
	primary key (userId, userGroupId)
);

create table UserTracker (
	userTrackerId varchar2(75) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	modifiedDate timestamp null,
	remoteAddr varchar2(75) null,
	remoteHost varchar2(75) null,
	userAgent varchar2(75) null
);

create table UserTrackerPath (
	userTrackerPathId varchar2(75) not null primary key,
	userTrackerId varchar2(75) null,
	path varchar2(4000) null,
	pathDate timestamp null
);

create table Website (
	websiteId number(30,0) not null primary key,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar2(75) null,
	classPK varchar2(75) null,
	url varchar2(75) null,
	typeId number(30,0),
	primary_ number(1, 0)
);

create table WikiNode (
	nodeId varchar2(75) not null primary key,
	groupId number(30,0) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar2(75) null,
	description varchar2(4000) null,
	lastPostDate timestamp null
);

create table WikiPage (
	nodeId varchar2(75) not null,
	title varchar2(75) not null,
	version number(30,20) not null,
	companyId varchar2(75) not null,
	userId varchar2(75) not null,
	userName varchar2(75) null,
	createDate timestamp null,
	content clob null,
	format varchar2(75) null,
	head number(1, 0),
	primary key (nodeId, title, version)
);



insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('1', 'Canada', 'CA', 'CAN', '124', '001', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('2', 'China', 'CN', 'CHN', '156', '086', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('3', 'France', 'FR', 'FRA', '250', '033', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('4', 'Germany', 'DE', 'DEU', '276', '049', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('5', 'Hong Kong', 'HK', 'HKG', '344', '852', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('6', 'Hungary', 'HU', 'HUN', '348', '036', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('7', 'Israel', 'IL', 'ISR', '376', '972', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('8', 'Italy', 'IT', 'ITA', '380', '039', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('9', 'Japan', 'JP', 'JPN', '392', '081', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('10', 'South Korea', 'KP', 'KOR', '410', '082', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('11', 'Netherlands', 'NL', 'NLD', '528', '031', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('12', 'Portugal', 'PT', 'PRT', '620', '351', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('13', 'Russia', 'RU', 'RUS', '643', '007', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('14', 'Singapore', 'SG', 'SGP', '702', '065', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('15', 'Spain', 'ES', 'ESP', '724', '034', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('16', 'Turkey', 'TR', 'TUR', '792', '090', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('17', 'Vietnam', 'VM', 'VNM', '704', '084', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('18', 'United Kingdom', 'GB', 'GBR', '826', '044', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('19', 'United States', 'US', 'USA', '840', '001', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('20', 'Afghanistan', 'AF', 'AFG', '4', '093', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('21', 'Albania', 'AL', 'ALB', '8', '355', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('22', 'Algeria', 'DZ', 'DZA', '12', '213', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('23', 'American Samoa', 'AS', 'ASM', '16', '684', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('24', 'Andorra', 'AD', 'AND', '20', '376', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('25', 'Angola', 'AO', 'AGO', '24', '244', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('26', 'Anguilla', 'AI', 'AIA', '660', '264', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('27', 'Antarctica', 'AQ', 'ATA', '10', '672', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('28', 'Antigua', 'AG', 'ATG', '28', '268', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('29', 'Argentina', 'AR', 'ARG', '32', '054', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('30', 'Armenia', 'AM', 'ARM', '51', '374', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('31', 'Aruba', 'AW', 'ABW', '533', '297', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('32', 'Australia', 'AU', 'AUS', '36', '061', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('33', 'Austria', 'AT', 'AUT', '40', '043', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('34', 'Azerbaijan', 'AZ', 'AZE', '31', '994', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('35', 'Bahamas', 'BS', 'BHS', '44', '242', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('36', 'Bahrain', 'BH', 'BHR', '48', '973', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('37', 'Bangladesh', 'BD', 'BGD', '50', '880', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('38', 'Barbados', 'BB', 'BRB', '52', '246', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('39', 'Belarus', 'BY', 'BLR', '112', '375', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('40', 'Belgium', 'BE', 'BEL', '56', '032', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('41', 'Belize', 'BZ', 'BLZ', '84', '501', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('42', 'Benin', 'BJ', 'BEN', '204', '229', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('43', 'Bermuda', 'BM', 'BMU', '60', '441', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('44', 'Bhutan', 'BT', 'BTN', '64', '975', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('45', 'Bolivia', 'BO', 'BOL', '68', '591', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('46', 'Bosnia-Herzegovina', 'BA', 'BIH', '70', '387', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('47', 'Botswana', 'BW', 'BWA', '72', '267', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('48', 'Brazil', 'BR', 'BRA', '76', '055', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('49', 'British Virgin Islands', 'VG', 'VGB', '92', '284', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('50', 'Brunei', 'BN', 'BRN', '96', '673', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('51`', 'Bulgaria', 'BG', 'BGR', '100', '359', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('52', 'Burkina Faso', 'BF', 'BFA', '854', '226', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('53', 'Burma (Myanmar)', 'MM', 'MMR', '104', '095', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('54', 'Burundi', 'BI', 'BDI', '108', '257', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('55', 'Cambodia', 'KH', 'KHM', '116', '855', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('56', 'Cameroon', 'CM', 'CMR', '120', '237', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('57', 'Cape Verde Island', 'CV', 'CPV', '132', '238', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('58', 'Cayman Islands', 'KY', 'CYM', '136', '345', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('59', 'Central African Republic', 'CF', 'CAF', '140', '236', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('60', 'Chad', 'TD', 'TCD', '148', '235', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('61', 'Chile', 'CL', 'CHL', '152', '056', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('62', 'Christmas Island', 'CX', 'CXR', '162', '061', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('63', 'Cocos Islands', 'CC', 'CCK', '166', '061', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('64', 'Colombia', 'CO', 'COL', '170', '057', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('65', 'Comoros', 'KM', 'COM', '174', '269', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('66', 'Republic of Congo', 'CD', 'COD', '180', '242', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('67', 'Democratic Republic of Congo', 'CG', 'COG', '178', '243', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('68', 'Cook Islands', 'CK', 'COK', '184', '682', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('69', 'Costa Rica', 'CI', 'CRI', '188', '506', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('70', 'Croatia', 'HR', 'HRV', '191', '385', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('71', 'Cuba', 'CU', 'CUB', '192', '053', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('72', 'Cyprus', 'CY', 'CYP', '196', '357', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('73', 'Czech Republic', 'CZ', 'CZE', '203', '420', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('74', 'Denmark', 'DK', 'DNK', '208', '045', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('75', 'Djibouti', 'DJ', 'DJI', '262', '253', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('76', 'Dominica', 'DM', 'DMA', '212', '767', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('77', 'Dominican Republic', 'DO', 'DOM', '214', '809', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('78', 'Ecuador', 'EC', 'ECU', '218', '593', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('79', 'Egypt', 'EG', 'EGY', '818', '020', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('80', 'El Salvador', 'SV', 'SLV', '222', '503', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('81', 'Equatorial Guinea', 'GQ', 'GNQ', '226', '240', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('82', 'Eritrea', 'ER', 'ERI', '232', '291', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('83', 'Estonia', 'EE', 'EST', '233', '372', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('84', 'Ethiopia', 'ET', 'ETH', '231', '251', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('85', 'Faeroe Islands', 'FO', 'FRO', '234', '298', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('86', 'Falkland Islands', 'FK', 'FLK', '238', '500', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('87', 'Fiji Islands', 'FJ', 'FJI', '242', '679', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('88', 'Finland', 'FI', 'FIN', '246', '358', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('89', 'French Guiana', 'GF', 'GUF', '254', '594', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('90', 'French Polynesia', 'PF', 'PYF', '258', '689', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('91', 'Gabon', 'GA', 'GAB', '266', '241', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('92', 'Gambia', 'GM', 'GMB', '270', '220', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('93', 'Georgia', 'GE', 'GEO', '268', '995', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('94', 'Ghana', 'GH', 'GHA', '288', '233', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('95', 'Gibraltar', 'GI', 'GIB', '292', '350', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('96', 'Greece', 'GR', 'GRC', '300', '030', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('97', 'Greenland', 'GL', 'GRL', '304', '299', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('98', 'Grenada', 'GD', 'GRD', '308', '473', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('99', 'Guadeloupe', 'GP', 'GLP', '312', '590', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('100', 'Guam', 'GU', 'GUM', '316', '671', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('101', 'Guatemala', 'GT', 'GTM', '320', '502', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('102', 'Guinea', 'GN', 'GIN', '324', '224', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('103', 'Guinea-Bissau', 'GW', 'GNB', '624', '245', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('104', 'Guyana', 'GY', 'GUY', '328', '592', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('105', 'Haiti', 'HT', 'HTI', '332', '509', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('106', 'Honduras', 'HN', 'HND', '340', '504', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('107', 'Iceland', 'IS', 'ISL', '352', '354', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('108', 'India', 'IN', 'IND', '356', '091', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('109', 'Indonesia', 'ID', 'IDN', '360', '062', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('110', 'Iran', 'IR', 'IRN', '364', '098', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('111', 'Iraq', 'IQ', 'IRQ', '368', '964', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('112', 'Ireland', 'IE', 'IRL', '372', '353', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('113', 'Ivory Coast', 'CI', 'CIV', '384', '225', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('114', 'Jamaica', 'JM', 'JAM', '388', '876', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('115', 'Jordan', 'JO', 'JOR', '400', '962', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('116', 'Kazakhstan', 'KZ', 'KAZ', '398', '007', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('117', 'Kenya', 'KE', 'KEN', '404', '254', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('118', 'Kiribati', 'KI', 'KIR', '408', '686', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('119', 'Kuwait', 'KW', 'KWT', '414', '965', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('120', 'North Korea', 'KP', 'PRK', '408', '850', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('121', 'Kyrgyzstan', 'KG', 'KGZ', '471', '996', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('122', 'Laos', 'LA', 'LAO', '418', '856', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('123', 'Latvia', 'LV', 'LVA', '428', '371', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('124', 'Lebanon', 'LB', 'LBN', '422', '961', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('125', 'Lesotho', 'LS', 'LSO', '426', '266', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('126', 'Liberia', 'LR', 'LBR', '430', '231', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('127', 'Libya', 'LY', 'LBY', '434', '218', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('128', 'Liechtenstein', 'LI', 'LIE', '438', '423', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('129', 'Lithuania', 'LT', 'LTU', '440', '370', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('130', 'Luxembourg', 'LU', 'LUX', '442', '352', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('131', 'Macau', 'MO', 'MAC', '446', '853', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('132', 'Macedonia', 'MK', 'MKD', '807', '389', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('133', 'Madagascar', 'MG', 'MDG', '450', '261', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('134', 'Malawi', 'MW', 'MWI', '454', '265', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('135', 'Malaysia', 'MY', 'MYS', '458', '060', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('136', 'Maldives', 'MV', 'MDV', '462', '960', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('137', 'Mali', 'ML', 'MLI', '466', '223', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('138', 'Malta', 'MT', 'MLT', '470', '356', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('139', 'Marshall Islands', 'MH', 'MHL', '584', '692', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('140', 'Martinique', 'MQ', 'MTQ', '474', '596', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('141', 'Mauritania', 'MR', 'MRT', '478', '222', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('142', 'Mauritius', 'MU', 'MUS', '480', '230', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('143', 'Mayotte Island', 'YT', 'MYT', '175', '269', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('144', 'Mexico', 'MX', 'MEX', '484', '052', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('145', 'Micronesia', 'FM', 'FSM', '583', '691', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('146', 'Moldova', 'MD', 'MDA', '498', '373', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('147', 'Monaco', 'MC', 'MCO', '492', '377', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('148', 'Mongolia', 'MN', 'MNG', '496', '976', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('149', 'Montserrat', 'MS', 'MSR', '500', '664', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('150', 'Morocco', 'MA', 'MAR', '504', '212', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('151', 'Mozambique', 'MZ', 'MOZ', '508', '258', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('152', 'Myanmar (Burma)', 'MM', 'MMR', '104', '095', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('153', 'Namibia', 'NA', 'NAM', '516', '264', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('154', 'Nauru', 'NR', 'NRU', '520', '674', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('155', 'Nepal', 'NP', 'NPL', '524', '977', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('156', 'Netherlands Antilles', 'AN', 'ANT', '530', '599', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('157', 'New Caledonia', 'NC', 'NCL', '540', '687', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('158', 'New Zealand', 'NZ', 'NZL', '554', '064', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('159', 'Nicaragua', 'NE', 'NER', '558', '505', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('160', 'Niger', 'NE', 'NER', '562', '227', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('161', 'Nigeria', 'NG', 'NGA', '566', '234', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('162', 'Niue', 'NU', 'NIU', '570', '683', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('163', 'Norfolk Island', 'NF', 'NFK', '574', '672', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('164', 'Norway', 'NO', 'NOR', '578', '047', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('165', 'Oman', 'OM', 'OMN', '512', '968', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('166', 'Pakistan', 'PK', 'PAK', '586', '092', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('167', 'Palau', 'PW', 'PLW', '585', '680', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('168', 'Palestine', 'PS', 'PSE', '275', '970', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('169', 'Panama', 'PA', 'PAN', '591', '507', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('170', 'Papua New Guinea', 'PG', 'PNG', '598', '675', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('171', 'Paraguay', 'PY', 'PRY', '600', '595', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('172', 'Peru', 'PE', 'PER', '604', '051', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('173', 'Philippines', 'PH', 'PHL', '608', '063', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('174', 'Poland', 'PL', 'POL', '616', '048', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('175', 'Puerto Rico', 'PR', 'PRI', '630', '787', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('176', 'Qatar', 'QA', 'QAT', '634', '974', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('177', 'Reunion Island', 'RE', 'REU', '638', '262', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('178', 'Romania', 'RO', 'ROU', '642', '040', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('179', 'Rwanda', 'RW', 'RWA', '646', '250', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('180', 'St. Helena', 'SH', 'SHN', '654', '290', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('181', 'St. Kitts', 'KN', 'KNA', '659', '869', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('182', 'St. Lucia', 'LC', 'LCA', '662', '758', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('183', 'St. Pierre & Miquelon', 'PM', 'SPM', '666', '508', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('184', 'St. Vincent', 'VC', 'VCT', '670', '784', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('185', 'San Marino', 'SM', 'SMR', '674', '378', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('186', 'Sao Tome & Principe', 'ST', 'STP', '678', '239', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('187', 'Saudi Arabia', 'SA', 'SAU', '682', '966', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('188', 'Senegal', 'SN', 'SEN', '686', '221', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('189', 'Serbia', 'CS', 'SCG', '891', '381', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('190', 'Seychelles', 'SC', 'SYC', '690', '248', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('191', 'Sierra Leone', 'SL', 'SLE', '694', '249', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('192', 'Slovakia', 'SK', 'SVK', '703', '421', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('193', 'Slovenia', 'SI', 'SVN', '705', '386', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('194', 'Solomon Islands', 'SB', 'SLB', '90', '677', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('195', 'Somalia', 'SO', 'SOM', '706', '252', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('196', 'South Africa', 'ZA', 'ZAF', '710', '027', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('197', 'Sri Lanka', 'LK', 'LKA', '144', '094', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('198', 'Sudan', 'SD', 'SDN', '736', '095', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('199', 'Suriname', 'SR', 'SUR', '740', '597', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('200', 'Swaziland', 'SZ', 'SWZ', '748', '268', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('201', 'Sweden', 'SE', 'SWE', '752', '046', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('202', 'Switzerland', 'CH', 'CHE', '756', '041', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('203', 'Syria', 'SY', 'SYR', '760', '963', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('204', 'Taiwan', 'TW', 'TWN', '158', '886', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('205', 'Tajikistan', 'TJ', 'TJK', '762', '992', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('206', 'Tanzania', 'TZ', 'TZA', '834', '255', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('207', 'Thailand', 'TH', 'THA', '764', '066', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('208', 'Togo', 'TG', 'TGO', '768', '228', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('209', 'Tonga', 'TO', 'TON', '776', '676', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('210', 'Trinidad & Tobago', 'TT', 'TTO', '780', '868', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('211', 'Tunisia', 'TN', 'TUN', '788', '216', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('212', 'Turkmenistan', 'TM', 'TKM', '795', '993', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('213', 'Turks & Caicos', 'TC', 'TCA', '796', '649', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('214', 'Tuvalu', 'TV', 'TUV', '798', '688', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('215', 'Uganda', 'UG', 'UGA', '800', '256', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('216', 'Ukraine', 'UA', 'UKR', '804', '380', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('217', 'United Arab Emirates', 'AE', 'ARE', '784', '971', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('218', 'Uruguay', 'UY', 'URY', '858', '598', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('219', 'Uzbekistan', 'UZ', 'UZB', '860', '998', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('220', 'Vanuatu', 'VU', 'VUT', '548', '678', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('221', 'Vatican City', 'VA', 'VAT', '336', '039', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('222', 'Venezuela', 'VE', 'VEN', '862', '058', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('223', 'Wallis & Futuna', 'WF', 'WLF', '876', '681', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('224', 'Western Samoa', 'EH', 'ESH', '732', '685', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('225', 'Yemen', 'YE', 'YEM', '887', '967', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('226', 'Yugoslavia', 'MK', 'MKD', '446', '381', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('227', 'Zambia', 'ZM', 'ZMB', '894', '260', 1);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('228', 'Zimbabwe', 'ZW', 'ZWE', '716', '263', 1);

insert into Region (regionId, countryId, regionCode, name, active_) values ('1', '19', 'AL', 'Alabama', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('2', '19', 'AK', 'Alaska', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('3', '19', 'AZ', 'Arizona', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('4', '19', 'AR', 'Arkansas', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('5', '19', 'CA', 'California', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('6', '19', 'CO', 'Colorado', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('7', '19', 'CT', 'Connecticut', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('8', '19', 'DC', 'District of Columbia', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('9', '19', 'DE', 'Delaware', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('10', '19', 'FL', 'Florida', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('11', '19', 'GA', 'Georgia', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('12', '19', 'HI', 'Hawaii', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('13', '19', 'ID', 'Idaho', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('14', '19', 'IL', 'Illinois', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('15', '19', 'IN', 'Indiana', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('16', '19', 'IA', 'Iowa', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('17', '19', 'KS', 'Kansas', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('18', '19', 'KY', 'Kentucky ', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('19', '19', 'LA', 'Louisiana ', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('20', '19', 'ME', 'Maine', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('21', '19', 'MD', 'Maryland', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('22', '19', 'MA', 'Massachusetts', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('23', '19', 'MI', 'Michigan', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('24', '19', 'MN', 'Minnesota', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('25', '19', 'MS', 'Mississippi', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('26', '19', 'MO', 'Missouri', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('27', '19', 'MT', 'Montana', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('28', '19', 'NE', 'Nebraska', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('29', '19', 'NV', 'Nevada', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('30', '19', 'NH', 'New Hampshire', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('31', '19', 'NJ', 'New Jersey', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('32', '19', 'NM', 'New Mexico', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('33', '19', 'NY', 'New York', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('34', '19', 'NC', 'North Carolina', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('35', '19', 'ND', 'North Dakota', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('36', '19', 'OH', 'Ohio', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('37', '19', 'OK', 'Oklahoma ', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('38', '19', 'OR', 'Oregon', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('39', '19', 'PA', 'Pennsylvania', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('40', '19', 'PR', 'Puerto Rico', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('41', '19', 'RI', 'Rhode Island', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('42', '19', 'SC', 'South Carolina', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('43', '19', 'SD', 'South Dakota', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('44', '19', 'TN', 'Tennessee', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('45', '19', 'TX', 'Texas', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('46', '19', 'UT', 'Utah', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('47', '19', 'VT', 'Vermont', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('48', '19', 'VA', 'Virginia', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('49', '19', 'WA', 'Washington', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('50', '19', 'WV', 'West Virginia', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('51', '19', 'WI', 'Wisconsin', 1);
insert into Region (regionId, countryId, regionCode, name, active_) values ('52', '19', 'WY', 'Wyoming', 1);

--
-- List types for accounts
--

insert into ListType (listTypeId, name, type_) values (10000, 'Billing', 'com.liferay.portal.model.Account.address');
insert into ListType (listTypeId, name, type_) values (10001, 'Other', 'com.liferay.portal.model.Account.address');
insert into ListType (listTypeId, name, type_) values (10002, 'P.O. Box', 'com.liferay.portal.model.Account.address');
insert into ListType (listTypeId, name, type_) values (10003, 'Shipping', 'com.liferay.portal.model.Account.address');

insert into ListType (listTypeId, name, type_) values (10004, 'E-mail', 'com.liferay.portal.model.Account.emailAddress');
insert into ListType (listTypeId, name, type_) values (10005, 'E-mail 2', 'com.liferay.portal.model.Account.emailAddress');
insert into ListType (listTypeId, name, type_) values (10006, 'E-mail 3', 'com.liferay.portal.model.Account.emailAddress');

insert into ListType (listTypeId, name, type_) values (10007, 'Fax', 'com.liferay.portal.model.Account.phone');
insert into ListType (listTypeId, name, type_) values (10008, 'Local', 'com.liferay.portal.model.Account.phone');
insert into ListType (listTypeId, name, type_) values (10009, 'Other', 'com.liferay.portal.model.Account.phone');
insert into ListType (listTypeId, name, type_) values (10010, 'Toll-Free', 'com.liferay.portal.model.Account.phone');
insert into ListType (listTypeId, name, type_) values (10011, 'TTY', 'com.liferay.portal.model.Account.phone');

insert into ListType (listTypeId, name, type_) values (10012, 'Intranet', 'com.liferay.portal.model.Account.website');
insert into ListType (listTypeId, name, type_) values (10013, 'Public', 'com.liferay.portal.model.Account.website');

--
-- List types for contacts
--

insert into ListType (listTypeId, name, type_) values (11000, 'Business', 'com.liferay.portal.model.Contact.address');
insert into ListType (listTypeId, name, type_) values (11001, 'Other', 'com.liferay.portal.model.Contact.address');
insert into ListType (listTypeId, name, type_) values (11002, 'Personal', 'com.liferay.portal.model.Contact.address');

insert into ListType (listTypeId, name, type_) values (11003, 'E-mail', 'com.liferay.portal.model.Contact.emailAddress');
insert into ListType (listTypeId, name, type_) values (11004, 'E-mail 2', 'com.liferay.portal.model.Contact.emailAddress');
insert into ListType (listTypeId, name, type_) values (11005, 'E-mail 3', 'com.liferay.portal.model.Contact.emailAddress');

insert into ListType (listTypeId, name, type_) values (11006, 'Business', 'com.liferay.portal.model.Contact.phone');
insert into ListType (listTypeId, name, type_) values (11007, 'Business Fax', 'com.liferay.portal.model.Contact.phone');
insert into ListType (listTypeId, name, type_) values (11008, 'Mobile', 'com.liferay.portal.model.Contact.phone');
insert into ListType (listTypeId, name, type_) values (11009, 'Other', 'com.liferay.portal.model.Contact.phone');
insert into ListType (listTypeId, name, type_) values (11010, 'Pager', 'com.liferay.portal.model.Contact.phone');
insert into ListType (listTypeId, name, type_) values (11011, 'Personal', 'com.liferay.portal.model.Contact.phone');
insert into ListType (listTypeId, name, type_) values (11012, 'Personal Fax', 'com.liferay.portal.model.Contact.phone');
insert into ListType (listTypeId, name, type_) values (11013, 'TTY', 'com.liferay.portal.model.Contact.phone');

insert into ListType (listTypeId, name, type_) values (11014, 'Dr.', 'com.liferay.portal.model.Contact.prefix');
insert into ListType (listTypeId, name, type_) values (11015, 'Mr.', 'com.liferay.portal.model.Contact.prefix');
insert into ListType (listTypeId, name, type_) values (11016, 'Mrs.', 'com.liferay.portal.model.Contact.prefix');
insert into ListType (listTypeId, name, type_) values (11017, 'Ms.', 'com.liferay.portal.model.Contact.prefix');

insert into ListType (listTypeId, name, type_) values (11020, 'II', 'com.liferay.portal.model.Contact.suffix');
insert into ListType (listTypeId, name, type_) values (11021, 'III', 'com.liferay.portal.model.Contact.suffix');
insert into ListType (listTypeId, name, type_) values (11022, 'IV', 'com.liferay.portal.model.Contact.suffix');
insert into ListType (listTypeId, name, type_) values (11023, 'Jr.', 'com.liferay.portal.model.Contact.suffix');
insert into ListType (listTypeId, name, type_) values (11024, 'PhD.', 'com.liferay.portal.model.Contact.suffix');
insert into ListType (listTypeId, name, type_) values (11025, 'Sr.', 'com.liferay.portal.model.Contact.suffix');

insert into ListType (listTypeId, name, type_) values (11026, 'Blog', 'com.liferay.portal.model.Contact.website');
insert into ListType (listTypeId, name, type_) values (11027, 'Business', 'com.liferay.portal.model.Contact.website');
insert into ListType (listTypeId, name, type_) values (11028, 'Other', 'com.liferay.portal.model.Contact.website');
insert into ListType (listTypeId, name, type_) values (11029, 'Personal', 'com.liferay.portal.model.Contact.website');

--
-- List types for organizations
--

insert into ListType (listTypeId, name, type_) values (12000, 'Billing', 'com.liferay.portal.model.Organization.address');
insert into ListType (listTypeId, name, type_) values (12001, 'Other', 'com.liferay.portal.model.Organization.address');
insert into ListType (listTypeId, name, type_) values (12002, 'P.O. Box', 'com.liferay.portal.model.Organization.address');
insert into ListType (listTypeId, name, type_) values (12003, 'Shipping', 'com.liferay.portal.model.Organization.address');

insert into ListType (listTypeId, name, type_) values (12004, 'E-mail', 'com.liferay.portal.model.Organization.emailAddress');
insert into ListType (listTypeId, name, type_) values (12005, 'E-mail 2', 'com.liferay.portal.model.Organization.emailAddress');
insert into ListType (listTypeId, name, type_) values (12006, 'E-mail 3', 'com.liferay.portal.model.Organization.emailAddress');

insert into ListType (listTypeId, name, type_) values (12007, 'Fax', 'com.liferay.portal.model.Organization.phone');
insert into ListType (listTypeId, name, type_) values (12008, 'Local', 'com.liferay.portal.model.Organization.phone');
insert into ListType (listTypeId, name, type_) values (12009, 'Other', 'com.liferay.portal.model.Organization.phone');
insert into ListType (listTypeId, name, type_) values (12010, 'Toll-Free', 'com.liferay.portal.model.Organization.phone');
insert into ListType (listTypeId, name, type_) values (12011, 'TTY', 'com.liferay.portal.model.Organization.phone');

insert into ListType (listTypeId, name, type_) values (12012, 'Administrative', 'com.liferay.portal.model.Organization.service');
insert into ListType (listTypeId, name, type_) values (12013, 'Contracts', 'com.liferay.portal.model.Organization.service');
insert into ListType (listTypeId, name, type_) values (12014, 'Donation', 'com.liferay.portal.model.Organization.service');
insert into ListType (listTypeId, name, type_) values (12015, 'Retail', 'com.liferay.portal.model.Organization.service');
insert into ListType (listTypeId, name, type_) values (12016, 'Training', 'com.liferay.portal.model.Organization.service');

insert into ListType (listTypeId, name, type_) values (12017, 'Full Member', 'com.liferay.portal.model.Organization.status');
insert into ListType (listTypeId, name, type_) values (12018, 'Provisional Member', 'com.liferay.portal.model.Organization.status');

insert into ListType (listTypeId, name, type_) values (12019, 'Intranet', 'com.liferay.portal.model.Organization.website');
insert into ListType (listTypeId, name, type_) values (12020, 'Public', 'com.liferay.portal.model.Organization.website');



insert into Counter values ('com.liferay.counter.model.Counter', 2000);
insert into Counter values ('com.liferay.portal.model.Organization', 1000);
insert into Counter values ('com.liferay.portal.model.Role', 1000);
insert into Counter values ('com.liferay.portal.model.User.liferay.com', 1000);
insert into Counter values ('com.liferay.portlet.imagegallery.model.IGFolder', 1000);
insert into Counter values ('com.liferay.portlet.imagegallery.model.IGImage.liferay.com', 1000);
insert into Counter values ('com.liferay.portlet.polls.model.PollsQuestion', 1000);
insert into Counter values ('com.liferay.portlet.shopping.model.ShoppingCategory', 1000);
insert into Counter values ('com.liferay.portlet.shopping.model.ShoppingItem', 1000);
insert into Counter values ('com.liferay.portlet.wiki.model.WikiNode', 1000);



insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_10Zv', '1', 'PUB.1', 'FRONTPAGE-AD', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_1XQI', '1', 'PUB.1', 'ABOUT-LIFERAY', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_5nBa', '1', 'PUB.1', 'EURO-TRAINING', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_SxQh', '1', 'PUB.1', 'HOME-SHOWCASE-FLASH', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_TYp9', '1', 'PUB.1', 'HOME-DOWNLOADS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_j7RH', '1', 'PUB.1', 'HOME-DEMO-IMAGE', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_njrf', '1', 'PUB.1', 'HOME-ANNOUNCEMENTS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_pZzX', '1', 'PUB.1', 'HOME-BANNER', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_fuMV', '2', 'PUB.1', 'PRODUCTS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_ih5S', '2', 'PUB.1', 'BANNER-PRODUCTS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_rWuH', '2', 'PUB.1', 'ADVERTS-HOSTING-NARROW', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_FuiH', '3', 'PUB.1', 'BANNER-STORIES', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Qsqv', '3', 'PUB.1', 'STORIES', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_7V3C', '4', 'PUB.1', 'BANNER-SERVICES', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_t6zG', '4', 'PUB.1', 'SERVICES', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_9djp', '5', 'PUB.1', 'GLOBAL-TEAM', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_g0sl', '5', 'PUB.1', 'BANNER-GLOBALTEAM', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_VoqX', '6', 'PUB.1', 'DEVZONE', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_WiHi', '6', 'PUB.1', 'GLOBAL-ADS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_YjTG', '6', 'PUB.1', 'BANNER-DEVZONE', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_YDim', '7', 'PUB.1', 'ABOUTUS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_zL59', '7', 'PUB.1', 'BANNER-ABOUTUS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_7q0R', '8', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-FEATURE-TEXT5', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_GnxQ', '8', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-FEATURE-OUTOFBOXPORTLETS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Rko0', '8', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-FEATURE-TEXT1', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_UqXp', '8', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-FEATURE-INTERNATIONALIZATION', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_UzUZ', '8', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-FEATURE-DRAGNDROP', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_eiGC', '8', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-FEATURE-THEMES', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_kBOZ', '8', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-FEATURE-TEXT4', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_mMfv', '8', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-FEATURE-SERVERAGNOSTIC', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_pFfk', '8', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-FEATURE-TEXT2', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_ptoc', '8', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-FEATURE-SUBTHEMES', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_qNVY', '8', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-FEATURE-TEXT3', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_AlsN', '9', 'PUB.1', 'PRODUCTS-CMS-XSL-EDITOR', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Bnki', '9', 'PUB.1', 'PRODUCTS-CMS-IMAGE-GALLERY', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_D69R', '9', 'PUB.1', 'PRODUCTS-CMS-STRUCTURED-CONTENT', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_f0cZ', '9', 'PUB.1', 'PRODUCTS-CMS-DOCUMENT-LIBRARY', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_OQFa', '15', 'PUB.1', 'PRODUCTS-LICENSING', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_yWLy', '20', 'PUB.1', 'STORIES-EDUCAMADRID', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Ywz7', '21', 'PUB.1', 'STORIES-GOODWILL', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_oDpc', '22', 'PUB.1', 'STORIES-JASONS-DELI', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_dDyI', '23', 'PUB.1', 'STORIES-KOSICE-AIRPORT', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_WADv', '25', 'PUB.1', 'STORIES-OAKWOOD', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_c9ZK', '26', 'PUB.1', 'STORIES-OFFICE-GATEWAY', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_KIzv', '27', 'PUB.1', 'STORIES-PEPKM', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_VkAM', '28', 'PUB.1', 'STORIES-WALDEN', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_0epD', '29', 'PUB.1', 'SERVICES-PRODUCTION-SUPPORT', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_lvdx', '29', 'PUB.1', 'ADVERTS-HOSTING-NARROW', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_BXDh', '30', 'PUB.1', 'SERVICES-CONSULTING', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_PZj4', '32', 'PUB.1', 'SERVICES-ONSITE-TRAINING', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_ZjVs', '33', 'PUB.1', 'SERVICES-PUBLICTRAINING', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_06ik', '34', 'PUB.1', 'GLOBAL-TEAM-TELECONSULT', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Nqi8', '34', 'PUB.1', 'GLOBAL-TEAM-SINETA', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_THic', '34', 'PUB.1', 'GLOBAL-TEAM-PREFERRED', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_UBC0', '34', 'PUB.1', 'GLOBAL-TEAM-PREMIER', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_gItp', '34', 'PUB.1', 'GLOBAL-TEAM-GERMINUS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_jG2r', '34', 'PUB.1', 'GLOBAL-TEAM-INTERBIZTECH', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_kxEY', '34', 'PUB.1', 'GLOBAL-TEAM-IPPON', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_5Grt', '35', 'PUB.1', 'GLOBAL-TEAM-REDHAT', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Bn5o', '35', 'PUB.1', 'GLOBAL-TEAM-NOVELL', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_zMzb', '36', 'PUB.1', 'GLOBAL-TEAM-LIPP-JOIN', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_GDMF', '38', 'PUB.1', 'PRODUCTS-PORTAL-SUPPORTED-VELOCITY', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_GEz5', '38', 'PUB.1', 'PRODUCTS-PORTAL-SUPPORTED-AJAX', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_H7pi', '38', 'PUB.1', 'PRODUCTS-PORTAL-SUPPORTED-WSRP', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_cTtF', '38', 'PUB.1', 'PRODUCTS-PORTAL-SUPPORTED-JSR168', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_k55O', '38', 'PUB.1', 'PRODUCTS-PORTAL-SUPPORTED-SPRING', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_nUAi', '38', 'PUB.1', 'PRODUCTS-PORTAL-SUPPORTED-HIBERNATE', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_oJ8q', '38', 'PUB.1', 'PRODUCTS-PORTAL-SUPPORTED-STRUTS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_oQE2', '38', 'PUB.1', 'PRODUCTS-PORTAL-SUPPORTED-JSF', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_08pB', '43', 'PUB.1', 'PRODUCTS-CMS-SUPPORTED-JSR170', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_gTej', '43', 'PUB.1', 'PRODUCTS-CMS-SUPPORTED-OTHER', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_gkzg', '45', 'PUB.1', 'ABOUTUS-LEADERSHIP', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_FF4S', '47', 'PUB.1', 'ABOUTUS-NEWS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_aUIw', '48', 'PUB.1', 'ABOUTUS-CONTACTUS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_aImY', '49', 'PUB.1', 'ABOUTUS-NEWS-ANOUNCEMENTS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_rF3Q', '50', 'PUB.1', 'ABOUTUS-NEWS-IN-THE-NEWS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_xhsz', '51', 'PUB.1', 'ABOUTUS-NEWS-BLOGWORTHY', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_YMX1', '52', 'PUB.1', 'DEVZONE-ARCHITECTURE', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_bTu1', '53', 'PUB.1', 'DEVZONE-DOCUMENTATION', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_KwwL', '56', 'PUB.1', 'DEVZONE-JAVADOCS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_oayh', '57', 'PUB.1', 'DEVZONE-FAQS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_MSqm', '58', 'PUB.1', 'DEVZONE-GLOSSARY', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_3FZM', '63', 'PUB.1', 'DEVZONE-ARCHITECTURE-PORTLETAPI', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_j5RR', '64', 'PUB.1', 'DEVZONE-ARCHITECTURE-STRUTSANDTILES', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_ZPNd', '65', 'PUB.1', 'DEVZONE-ARCHITECTURE-EJBSPRINGHIB', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_PdWQ', '66', 'PUB.1', 'DEVZONE-ARCHITECTURE-SOAPRMITUNNEL', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_9Ecu', '67', 'PUB.1', 'DEVZONE-ARCHITECTURE-ASP', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_poDH', '73', 'PUB.1', 'GLOBAL-TEAM-LIPP-REQUIREMENTS-BENEFITS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_OlTr', '74', 'PUB.1', 'GLOBAL-TEAM-LIPP-FAQ', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_d5Hf', '75', 'PUB.1', 'GLOBAL-TEAM-LIPP-PROGRAMSTRUCTURE', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_A5hL', '82', 'PUB.1', 'BANNER-DOWNLOADS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_QY8U', '82', 'PUB.1', 'ADVERTS-HOSTING-NARROW', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_S4Cj', '82', 'PUB.1', 'DOWNLOADS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_TB6c', '83', 'PUB.1', 'SERVICES-ENTERPRISE-SUPPORT', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_4asu', '85', 'PUB.1', 'DOWNLOADS-COMPLIANT-PORTLETS-SYNCEX', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Lcfp', '85', 'PUB.1', 'DOWNLOADS-COMPLIANT-PORTLETS-SOKOL-DEVELOPMENT', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_N1K2', '85', 'PUB.1', 'DOWNLOADS-COMPLIANT-PORTLETS-COGIX', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_cCaH', '85', 'PUB.1', 'DOWNLOADS-COMPLIANT-PORTLETS-SUN', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_cuqX', '85', 'PUB.1', 'DOWNLOADS-COMPLIANT-PORTLETS-PORTLETWORKS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_eNhf', '85', 'PUB.1', 'DOWNLOADS-COMPLIANT-PORTLETS-PORTLETBRIDGE', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_wDrj', '85', 'PUB.1', 'DOWNLOADS-COMPLIANT-PORTLETS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_mdV8', '86', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-DEPLOYMENTMATRIX', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_4zee', '87', 'PUB.1', 'SERVICES-PUBLICTRAINING-COURSETOPICS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_y9j4', '88', 'PUB.1', 'SERVICES-PUBLICTRAINING-REGISTRATION', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_jLBl', '89', 'PUB.1', 'DOWNLOADS-SAMPLES', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_AlsN', '90', 'PUB.1', 'PRODUCTS-MESH', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_VkAM', '92', 'PUB.1', 'STORIES-TELECENTROS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_VkAM', '93', 'PUB.1', 'STORIES-JUNTA', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_WADv', '94', 'PUB.1', 'STORIES-OTHER-CLIENTS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_2hUP', '95', 'PUB.1', 'DEVZONE-LIFECAST-SECURITY-FINE-GRAIN-PERMISSIONING', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_5e2u', '95', 'PUB.1', 'DEVZONE-LIFECAST-CMS-JOURNAL-ARTICLES', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_8hZN', '95', 'PUB.1', 'DEVZONE-LIFECAST-CMS-JOURNAL-JOURNAL-CONTENT', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_A8GS', '95', 'PUB.1', 'DEVZONE-LIFECAST-COMMUNITIES', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_BTbP', '95', 'PUB.1', 'DEVZONE-LIFECAST-USER-ADMIN-ORGANIZATION', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Fq3X', '95', 'PUB.1', 'DEVZONE-LIFECAST-CMS-DOCUMENT-LIBRARY', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Ja2P', '95', 'PUB.1', 'DEVZONE-LIFECAST-SECURITY-DELEGATE-ROLE-PERMISSION', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_KV9Z', '95', 'PUB.1', 'DEVZONE-LIFECAST-CMS-INTERNATIONALIZATION', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_LiYV', '95', 'PUB.1', 'DEVZONE-LIFECAST-USER-ADMIN-LOCATION', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_NzSr', '95', 'PUB.1', 'DEVZONE-LIFECAST-CMS-NAVIGATION-2', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_ObYn', '95', 'PUB.1', 'DEVZONE-LIFECAST-CMS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Paqq', '95', 'PUB.1', 'DEVZONE-LIFECAST-CMS-IMAGE-GALLERY', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_PjmG', '95', 'PUB.1', 'DEVZONE-LIFECAST-CMS-BREADCRUMB', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Rv7O', '95', 'PUB.1', 'DEVZONE-LIFECAST-SECURITY-ROLES-AND-PERMISSIONS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Shhg', '95', 'PUB.1', 'DEVZONE-LIFECAST-SECURITY-COMMUNITY-PERMISSIONS2', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Wp7E', '95', 'PUB.1', 'DEVZONE-LIFECAST-SECURITY-ASSIGN-PERMISSIONS-TO-ADD', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_XvcX', '95', 'PUB.1', 'DEVZONE-LIFECAST-USER-ADMIN', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_YavK', '95', 'PUB.1', 'DEVZONE-LIFECAST-SECURITY-COMMUNITY1', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_bTu1', '95', 'PUB.1', 'DEVZONE-LIFECAST-INSTALLATION', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_dnwJ', '95', 'PUB.1', 'DEVZONE-LIFECAST-USER-ADMIN-USER-GROUP', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_fhSs', '95', 'PUB.1', 'DEVZONE-LIFECAST-USER-ADMIN-COMMUNITIES-3', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_gxXW', '95', 'PUB.1', 'DEVZONE-LIFECAST-SECURITY', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_ihyj', '95', 'PUB.1', 'DEVZONE-LIFECAST-BASICS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_j663', '95', 'PUB.1', 'DEVZONE-LIFECAST-SECURITY-LOCATIONS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_lqvk', '95', 'PUB.1', 'DEVZONE-LIFECAST-SECURITY-DELEGATE-ENTERPRISE-PERMISSIONS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_sCao', '95', 'PUB.1', 'DEVZONE-LIFECAST-USER-ADMIN-COMMUNITIES-1', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_u0ta', '95', 'PUB.1', 'DEVZONE-LIFECAST-CMS-NAVIGATION', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_uhas', '95', 'PUB.1', 'DEVZONE-LIFECAST-USER-ADMIN-COMMUNITIES-2', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_vbuQ', '95', 'PUB.1', 'DEVZONE-LIFECAST-USER-ADMIN-COMMUNITIES-4', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_vke1', '95', 'PUB.1', 'DEVZONE-LIFECAST-USER-ADMIN-ENTERPRISE', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_wss6', '95', 'PUB.1', 'DEVZONE-LIFECAST-SECURITY-COMMUNITY-PERMISSIONS3', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_JY6T', '96', 'PUB.1', 'DEVZONE-FEATURES-ROADMAP', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Ywz7', '97', 'PUB.1', 'STORIES-STADUP', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_22DM', '98', 'PUB.1', 'SAMPLE-PUBLICDEMO', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_dWUS', '99', 'PUB.1', 'SERVICES-TRAINING', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_dWUS', '100', 'PUB.1', 'SERVICES-TRAINING-CERTIFICATION', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_ZjVs', '101', 'PUB.1', 'PUBLIC-TRAINING-EUROPE', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_eiGC', '102', 'PUB.1', 'PRODUCTS-LIFERAYPORTAL-BENCHMARKS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_pFfk', '102', 'PUB.1', 'PRODUCTS-LIFERAY-PORTAL-BENCHMARKS-RESULTS', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_Ts1z', '103', 'PUB.1', 'SERVICES-HOSTING', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_rU9t', '104', 'PUB.1', 'DEVZONE-FEATURES-ROADMAP-COWPER', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_VamE', '105', 'PUB.1', 'DEVZONE-FEATURES-ROADMAP-FUTURE-RELEASES', 'liferay.com', 1);
insert into JournalContentSearch (portletId, layoutId, ownerId, articleId, companyId, groupId) values ('56_INSTANCE_xzdr', '107', 'PUB.1', 'DEVZONE-FEATURES-ROADMAP-MACHEN', 'liferay.com', 1);




insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('1', 'PUB.1', 'liferay.com', '-1', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,de_DE,es_ES"> '||CHR(10)||'  <name>Home</name>  '||CHR(10)||'  <name language-id="de_DE">Home</name>  '||CHR(10)||'  <name language-id="es_ES">Inicio</name> '||CHR(10)||'</root>', '', 'portlet', 'column-3=56_INSTANCE_10Zv,56_INSTANCE_1XQI,56_INSTANCE_j7RH'||CHR(10)||'column-2=56_INSTANCE_SxQh'||CHR(10)||'column-1=56_INSTANCE_pZzX'||CHR(10)||'layout-template-id=2_2_columns'||CHR(10)||'column-4=56_INSTANCE_njrf,56_INSTANCE_5nBa,56_INSTANCE_TYp9', 0, '/home', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('2', 'PUB.1', 'liferay.com', '-1', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES,de_DE"> '||CHR(10)||'  <name>Products</name>  '||CHR(10)||'  <name language-id="es_ES">Productos</name>  '||CHR(10)||'  <name language-id="de_DE">Produkte</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_qyjU,56_INSTANCE_ih5S,56_INSTANCE_fuMV'||CHR(10)||'column-1=71_INSTANCE_RR44,56_INSTANCE_rWuH'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/products', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('3', 'PUB.1', 'liferay.com', '-1', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,de_DE,es_ES"> '||CHR(10)||'  <name>Stories</name>  '||CHR(10)||'  <name language-id="de_DE">Erfolgsgeschichten</name>  '||CHR(10)||'  <name language-id="es_ES">Casos de éxito</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_w3z9,56_INSTANCE_FuiH,56_INSTANCE_Qsqv'||CHR(10)||'column-1=71_INSTANCE_g60O'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('4', 'PUB.1', 'liferay.com', '-1', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES,de_DE"> '||CHR(10)||'  <name>Services</name>  '||CHR(10)||'  <name language-id="es_ES">Servicios</name>  '||CHR(10)||'  <name language-id="de_DE">Dienstleistungen</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_2QsS,56_INSTANCE_7V3C,56_INSTANCE_t6zG'||CHR(10)||'column-1=71_INSTANCE_IhN6'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services', '', '', 3);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('5', 'PUB.1', 'liferay.com', '-1', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,de_DE,es_ES"> '||CHR(10)||'  <name>Global Team</name>  '||CHR(10)||'  <name language-id="de_DE">Globales Team</name>  '||CHR(10)||'  <name language-id="es_ES">Equipo Global</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_mCw6,56_INSTANCE_g0sl,56_INSTANCE_9djp'||CHR(10)||'column-1=71_INSTANCE_U3Sf'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/team', '', '', 4);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('6', 'PUB.1', 'liferay.com', '-1', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,de_DE,es_ES"> '||CHR(10)||'  <name>Developer Zone</name>  '||CHR(10)||'  <name language-id="de_DE">Entwicklerzone</name>  '||CHR(10)||'  <name language-id="es_ES">Desarrolladores</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_eB5C,56_INSTANCE_YjTG,56_INSTANCE_VoqX'||CHR(10)||'column-1=71_INSTANCE_JjrK,56_INSTANCE_WiHi'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/devzone', '', '', 6);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('7', 'PUB.1', 'liferay.com', '-1', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES,de_DE"> '||CHR(10)||'  <name>About Us</name>  '||CHR(10)||'  <name language-id="es_ES">Quiénes somos</name>  '||CHR(10)||'  <name language-id="de_DE">Über uns</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_k7vx,56_INSTANCE_zL59,56_INSTANCE_YDim'||CHR(10)||'column-1=71_INSTANCE_Cofs'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/about', '', '', 7);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('8', 'PUB.1', 'liferay.com', '2', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Liferay Portal</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_l6LK,56_INSTANCE_Rko0,56_INSTANCE_eiGC,56_INSTANCE_ptoc,56_INSTANCE_UzUZ,56_INSTANCE_pFfk,56_INSTANCE_mMfv,56_INSTANCE_qNVY,56_INSTANCE_UqXp,56_INSTANCE_kBOZ,56_INSTANCE_GnxQ,56_INSTANCE_7q0R'||CHR(10)||'column-1=71_INSTANCE_iB27'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/products/portal', 'brochure', '01', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('9', 'PUB.1', 'liferay.com', '2', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Liferay Journal (CMS)</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_dWAs,56_INSTANCE_f0cZ,56_INSTANCE_Bnki,56_INSTANCE_D69R,56_INSTANCE_AlsN'||CHR(10)||'column-1=71_INSTANCE_ZOPP'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/products/journal', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('13', 'PUB.1', 'liferay.com', '2', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Live Demo</name>  '||CHR(10)||'  <name language-id="es_ES">Servidor de demo</name>'||CHR(10)||'</root>', '', 'url', 'url=http://demo.liferay.net'||CHR(10)||'description='||CHR(10)||'target=_blank'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/products/demo', '', '', 3);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('15', 'PUB.1', 'liferay.com', '2', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Licensing</name>  '||CHR(10)||'  <name language-id="es_ES">Licencia</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_KeA3,56_INSTANCE_OQFa'||CHR(10)||'column-1=71_INSTANCE_SwpJ'||CHR(10)||'state-min='||CHR(10)||'layout-template-id=2_columns_ii', 0, '/products/licensing', '', '', 4);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('20', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>EducaMadrid</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_UjKy,56_INSTANCE_yWLy'||CHR(10)||'column-1=71_INSTANCE_aSXf'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories/educamadrid', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('21', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Goodwill</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_V1ml,56_INSTANCE_Ywz7'||CHR(10)||'column-1=71_INSTANCE_L6lN'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories/goodwill', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('22', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Jason''s Deli</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_vwul,56_INSTANCE_oDpc'||CHR(10)||'column-1=71_INSTANCE_phO7'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories/jasons_deli', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('23', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Košice Airport</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_So1H,56_INSTANCE_dDyI'||CHR(10)||'column-1=71_INSTANCE_at0s'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories/kosice_airport', '', '', 4);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('25', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Oakwood</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_9VOe,56_INSTANCE_WADv'||CHR(10)||'column-1=71_INSTANCE_pdtS'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories/oakwood', '', '', 5);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('26', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Officegateway</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_NCYT,56_INSTANCE_c9ZK'||CHR(10)||'column-1=71_INSTANCE_Q6Bd'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories/officegateway', '', '', 6);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('27', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>RCM Managing Authority</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_LoFS,56_INSTANCE_KIzv'||CHR(10)||'column-1=71_INSTANCE_vfKv'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories/rcm_managing_authority', '', '', 7);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('28', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Walden Media</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_rrZx,56_INSTANCE_VkAM'||CHR(10)||'column-1=71_INSTANCE_p1zu'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories/walden_media', '', '', 10);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('29', 'PUB.1', 'liferay.com', '4', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Production Support</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_AlHH,56_INSTANCE_0epD'||CHR(10)||'column-1=71_INSTANCE_Bycc,56_INSTANCE_lvdx'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services/production', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('30', 'PUB.1', 'liferay.com', '4', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Consulting</name>  '||CHR(10)||'  <name language-id="es_ES">Consultoría</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_wo83,56_INSTANCE_BXDh'||CHR(10)||'column-1=71_INSTANCE_hVIZ'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services/consulting', '', '', 3);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('32', 'PUB.1', 'liferay.com', '99', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Custom Onsite Training</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_haR7,56_INSTANCE_PZj4'||CHR(10)||'column-1=71_INSTANCE_ZbPW'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services/training/onsite', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('33', 'PUB.1', 'liferay.com', '99', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Public Training</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_kpu1,56_INSTANCE_ZjVs'||CHR(10)||'column-1=71_INSTANCE_Bc8P'||CHR(10)||'state-min='||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services/training/public', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('34', 'PUB.1', 'liferay.com', '5', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>International Partners</name>  '||CHR(10)||'  <name language-id="es_ES">Partners internacionales</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_zvFQ,56_INSTANCE_UBC0,56_INSTANCE_gItp,56_INSTANCE_kxEY,56_INSTANCE_THic,56_INSTANCE_jG2r,56_INSTANCE_Nqi8,56_INSTANCE_06ik'||CHR(10)||'column-1=71_INSTANCE_iw1s'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/team/international', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('35', 'PUB.1', 'liferay.com', '5', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Technology Partners</name>  '||CHR(10)||'  <name language-id="es_ES">Partners tecnológicos</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_K3EN,56_INSTANCE_Bn5o,56_INSTANCE_5Grt'||CHR(10)||'column-1=71_INSTANCE_swtB'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/team/technology', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('36', 'PUB.1', 'liferay.com', '34', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Join the Team</name>  '||CHR(10)||'  <name language-id="es_ES">Únase al equipo</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_ak4K,56_INSTANCE_zMzb'||CHR(10)||'column-1=71_INSTANCE_XdlT'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/team/international/join', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('38', 'PUB.1', 'liferay.com', '8', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Supported Technologies</name>  '||CHR(10)||'  <name language-id="es_ES">Tecnologías soportadas</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_5Sev,56_INSTANCE_cTtF,56_INSTANCE_nUAi,56_INSTANCE_oQE2,56_INSTANCE_GEz5,56_INSTANCE_k55O,56_INSTANCE_oJ8q,56_INSTANCE_GDMF,56_INSTANCE_H7pi'||CHR(10)||'column-1=71_INSTANCE_UdjB'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/products/portal/supported_technologies', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('43', 'PUB.1', 'liferay.com', '9', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Supported Technologies</name>  '||CHR(10)||'  <name language-id="es_ES">Tecnologías soportadas</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_Qulc,56_INSTANCE_08pB,56_INSTANCE_gTej'||CHR(10)||'column-1=71_INSTANCE_bVjH'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/products/journal/supported_technologies', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('45', 'PUB.1', 'liferay.com', '7', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Leadership</name>  '||CHR(10)||'  <name language-id="es_ES">Liderazgo</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_iLCy,56_INSTANCE_gkzg'||CHR(10)||'column-1=71_INSTANCE_ln5C'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/about/leadership', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('47', 'PUB.1', 'liferay.com', '7', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>News</name>  '||CHR(10)||'  <name language-id="es_ES">Noticias</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_ZcUC,56_INSTANCE_FF4S'||CHR(10)||'column-1=71_INSTANCE_QGmH'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/about/news', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('48', 'PUB.1', 'liferay.com', '7', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Contact Us</name>  '||CHR(10)||'  <name language-id="es_ES">Contacto</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_vfGn,56_INSTANCE_aUIw'||CHR(10)||'column-1=71_INSTANCE_xYYe'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/about/contact', '', '', 3);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('49', 'PUB.1', 'liferay.com', '47', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Announcements</name>  '||CHR(10)||'  <name language-id="es_ES">Anuncios</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_YNTE,56_INSTANCE_aImY'||CHR(10)||'column-1=71_INSTANCE_0zuy'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/about/news/announcements', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('50', 'PUB.1', 'liferay.com', '47', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>In the News</name>  '||CHR(10)||'  <name language-id="es_ES">En la prensa</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_qDM4,56_INSTANCE_rF3Q'||CHR(10)||'column-1=71_INSTANCE_bjKu'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/about/news/in_the_news', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('51', 'PUB.1', 'liferay.com', '47', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Blogworthy</name>  '||CHR(10)||'  <name language-id="es_ES">Blogosfera</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_I8lz,56_INSTANCE_xhsz'||CHR(10)||'column-1=71_INSTANCE_ET4S'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/about/news/blogworthy', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('52', 'PUB.1', 'liferay.com', '6', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Architecture</name>  '||CHR(10)||'  <name language-id="es_ES">Arquitectura</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_y5Zx,56_INSTANCE_YMX1'||CHR(10)||'column-1=71_INSTANCE_yiIz'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/devzone/architecture', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('53', 'PUB.1', 'liferay.com', '6', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Documentation</name>  '||CHR(10)||'  <name language-id="es_ES">Documentacion</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_pORZ,56_INSTANCE_bTu1'||CHR(10)||'column-1=71_INSTANCE_zECO'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/devzone/documentation', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('55', 'PUB.1', 'liferay.com', '6', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Discussion Forums</name>  '||CHR(10)||'  <name language-id="es_ES">Foros de discusión</name> '||CHR(10)||'</root>', '', 'portlet', 'column-1=19'||CHR(10)||'url='||CHR(10)||'description='||CHR(10)||'target='||CHR(10)||'layout-template-id=1_column', 0, '/devzone/forums', '', '', 4);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('56', 'PUB.1', 'liferay.com', '6', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>JavaDocs API</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_iqeJ,56_INSTANCE_KwwL'||CHR(10)||'column-1=71_INSTANCE_aPmQ'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/devzone/javadocs', '', '', 5);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('57', 'PUB.1', 'liferay.com', '6', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>FAQs</name>  '||CHR(10)||'  <name language-id="es_ES">Preguntas frecuentes (FAQ)</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_EWJx,56_INSTANCE_oayh'||CHR(10)||'column-1=71_INSTANCE_7EDc'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/devzone/faqs', '', '', 6);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('58', 'PUB.1', 'liferay.com', '6', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Glossary</name>  '||CHR(10)||'  <name language-id="es_ES">Glosario</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_wKvS,56_INSTANCE_MSqm'||CHR(10)||'column-1=71_INSTANCE_Dw2X'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/devzone/glossary', '', '', 7);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('63', 'PUB.1', 'liferay.com', '52', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Portlet API (JSR-168)</name>  '||CHR(10)||'  <name language-id="es_ES">API de Portlets (JSR-168)</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_3dSC,56_INSTANCE_3FZM'||CHR(10)||'column-1=71_INSTANCE_J1im'||CHR(10)||'layout-template-id=2_columns_ii', 0, '', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('64', 'PUB.1', 'liferay.com', '52', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Struts and Tiles</name>  '||CHR(10)||'  <name language-id="es_ES">Struts y Tiles</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_rSVD,56_INSTANCE_j5RR'||CHR(10)||'column-1=71_INSTANCE_KZXL'||CHR(10)||'layout-template-id=2_columns_ii', 0, '', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('65', 'PUB.1', 'liferay.com', '52', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Session EJBs, Spring, and Hibernate</name>  '||CHR(10)||'  <name language-id="es_ES">EJBs de sesión, Spring e Hibernate</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_WKU0,56_INSTANCE_ZPNd'||CHR(10)||'column-1=71_INSTANCE_sI9U'||CHR(10)||'layout-template-id=2_columns_ii', 0, '', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('66', 'PUB.1', 'liferay.com', '52', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>SOAP, RMI, and Tunneling</name>  '||CHR(10)||'  <name language-id="es_ES">SOAP, RMI y Tunneling</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_9JMT,56_INSTANCE_PdWQ'||CHR(10)||'column-1=71_INSTANCE_ARHZ'||CHR(10)||'layout-template-id=2_columns_ii', 0, '', '', '', 3);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('67', 'PUB.1', 'liferay.com', '52', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root>'||CHR(10)||'  <name>Application Service Provider</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_0Dde,56_INSTANCE_9Ecu'||CHR(10)||'column-1=71_INSTANCE_FRBX'||CHR(10)||'layout-template-id=2_columns_ii', 0, '', '', '', 4);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('73', 'PUB.1', 'liferay.com', '34', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Requirement and Benefits</name>  '||CHR(10)||'  <name language-id="es_ES">Requisitos y beneficios</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_rEvb,56_INSTANCE_poDH'||CHR(10)||'column-1=71_INSTANCE_Bo0g'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/team/international/reqs_benefits', '', '', 3);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('74', 'PUB.1', 'liferay.com', '34', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>FAQ</name>  '||CHR(10)||'  <name language-id="es_ES">Preguntas frecuentes</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_5XIG,56_INSTANCE_OlTr'||CHR(10)||'column-1=71_INSTANCE_302W'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/team/international/faq', '', '', 4);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('75', 'PUB.1', 'liferay.com', '34', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Program Structure</name>  '||CHR(10)||'  <name language-id="es_ES">Programa</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_FIc2,56_INSTANCE_d5Hf'||CHR(10)||'column-1=71_INSTANCE_f3hz'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/team/international/structure', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('82', 'PUB.1', 'liferay.com', '-1', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Downloads</name>  '||CHR(10)||'  <name language-id="es_ES">Descargas</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_MKI5,56_INSTANCE_A5hL,56_INSTANCE_S4Cj'||CHR(10)||'column-1=71_INSTANCE_hnHi,56_INSTANCE_QY8U'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/downloads', '', '', 5);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('83', 'PUB.1', 'liferay.com', '4', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Enterprise Support</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_xAWV,56_INSTANCE_TB6c'||CHR(10)||'column-1=71_INSTANCE_EJFr'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/support/enterprise', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('84', 'PUB.1', 'liferay.com', '82', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Community Themes</name>  '||CHR(10)||'  <name language-id="es_ES">Temas proporcionados por la comunidad</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_KPRv,38'||CHR(10)||'column-1=71_INSTANCE_I0QN'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/downloads/themes', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('85', 'PUB.1', 'liferay.com', '82', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>JSR-168 Compliant Portlets</name>  '||CHR(10)||'  <name language-id="es_ES">Portlets JSR-168</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_xezx,56_INSTANCE_wDrj,56_INSTANCE_N1K2,56_INSTANCE_eNhf,56_INSTANCE_cuqX,56_INSTANCE_Lcfp,56_INSTANCE_cCaH,56_INSTANCE_4asu'||CHR(10)||'column-1=71_INSTANCE_rcIX'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/downloads/portlets', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('86', 'PUB.1', 'liferay.com', '8', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Deployment Matrix</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_7PUz,56_INSTANCE_mdV8'||CHR(10)||'column-1=71_INSTANCE_7rzL'||CHR(10)||'state-min='||CHR(10)||'layout-template-id=2_columns_ii', 0, '/products/portal/deployment_matrix', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('87', 'PUB.1', 'liferay.com', '33', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Course Topics</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_LP58,56_INSTANCE_4zee'||CHR(10)||'column-1=71_INSTANCE_zehb'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services/training/public/course_topics', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('88', 'PUB.1', 'liferay.com', '33', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Registration</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_tIPq,56_INSTANCE_y9j4'||CHR(10)||'column-1=71_INSTANCE_N9XJ'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services/training/public/registration', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('89', 'PUB.1', 'liferay.com', '82', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Liferay Samples</name>  '||CHR(10)||'  <name language-id="es_ES">Ejemplos proporcionados por Liferay</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_j3tr,56_INSTANCE_jLBl'||CHR(10)||'column-1=71_INSTANCE_sAqv'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/downloads/samples', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('90', 'PUB.1', 'liferay.com', '2', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Liferay Mesh</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_dWAs,56_INSTANCE_AlsN'||CHR(10)||'column-1=71_INSTANCE_ZOPP'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/products/mesh', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('91', 'PUB.1', 'liferay.com', '7', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Site Map</name>  '||CHR(10)||'  <name language-id="es_ES">Mapa de la web</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_vfGn,85_INSTANCE_LHLO'||CHR(10)||'column-1=71_INSTANCE_xYYe'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/about/site_map', '', '', 4);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('92', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Telecentros</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_rrZx,56_INSTANCE_VkAM'||CHR(10)||'column-1=71_INSTANCE_p1zu'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories/telecentros', '', '', 9);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('93', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Junta de Castilla-León</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_rrZx,56_INSTANCE_VkAM'||CHR(10)||'column-1=71_INSTANCE_p1zu'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories/junta', '', '', 3);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('94', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Other Clients</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_9VOe,56_INSTANCE_WADv'||CHR(10)||'column-1=71_INSTANCE_pdtS'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/clients', '', '', 11);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('95', 'PUB.1', 'liferay.com', '6', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Lifecast Tutorials</name>  '||CHR(10)||'  <name language-id="es_ES">Video Tutoriales</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_pORZ,56_INSTANCE_ihyj,56_INSTANCE_bTu1,56_INSTANCE_ObYn,56_INSTANCE_PjmG,56_INSTANCE_Fq3X,56_INSTANCE_Paqq,56_INSTANCE_KV9Z,56_INSTANCE_8hZN,56_INSTANCE_5e2u,56_INSTANCE_u0ta,56_INSTANCE_NzSr,56_INSTANCE_gxXW,56_INSTANCE_Wp7E,56_INSTANCE_YavK,56_INSTANCE_Shhg,56_INSTANCE_wss6,56_INSTANCE_lqvk,56_INSTANCE_Ja2P,56_INSTANCE_2hUP,56_INSTANCE_j663,56_INSTANCE_Rv7O,56_INSTANCE_A8GS,56_INSTANCE_sCao,56_INSTANCE_uhas,56_INSTANCE_fhSs,56_INSTANCE_vbuQ,56_INSTANCE_XvcX,56_INSTANCE_vke1,56_INSTANCE_LiYV,56_INSTANCE_BTbP,56_INSTANCE_dnwJ'||CHR(10)||'column-1=71_INSTANCE_zECO'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/devzone/lifecast', '', '', 3);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('96', 'PUB.1', 'liferay.com', '6', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Features &amp; Road Map</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_8pGt,56_INSTANCE_JY6T'||CHR(10)||'column-1=71_INSTANCE_Pb3s'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/devzone/features_and_road_map', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('97', 'PUB.1', 'liferay.com', '3', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Stadup</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_V1ml,56_INSTANCE_Ywz7'||CHR(10)||'column-1=71_INSTANCE_L6lN'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/stories/stadup', '', '', 8);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('98', 'PUB.1', 'liferay.com', '4', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Public Demo</name>  '||CHR(10)||'  <name language-id="es_ES">Demo Pública</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_8aj6,56_INSTANCE_22DM'||CHR(10)||'column-1=71_INSTANCE_00Rw'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services/public_demo', '', '', 5);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('99', 'PUB.1', 'liferay.com', '4', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root available-locales="en_US,es_ES"> '||CHR(10)||'  <name>Training</name>  '||CHR(10)||'  <name language-id="es_ES">Formación</name>'||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_8eXh,56_INSTANCE_dWUS'||CHR(10)||'column-1=71_INSTANCE_suFw'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services/training', '', '', 4);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('100', 'PUB.1', 'liferay.com', '99', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Liferay Certification</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_8eXh,56_INSTANCE_dWUS'||CHR(10)||'column-1=71_INSTANCE_suFw'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services/training/certification', '', '', 3);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('101', 'PUB.1', 'liferay.com', '99', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Public Training - Europe</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_kpu1,56_INSTANCE_ZjVs'||CHR(10)||'column-1=71_INSTANCE_Bc8P'||CHR(10)||'state-min='||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services/training/public/europe', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('102', 'PUB.1', 'liferay.com', '8', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Benchmarks</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_l6LK,56_INSTANCE_eiGC,56_INSTANCE_pFfk'||CHR(10)||'column-1=71_INSTANCE_iB27'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/products/portal/benchmarks', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('103', 'PUB.1', 'liferay.com', '4', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Hosting</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_n8xu,56_INSTANCE_Ts1z'||CHR(10)||'column-1=71_INSTANCE_ZdrZ'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/services/hosting', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('104', 'PUB.1', 'liferay.com', '96', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>4.1.0 Cowper (Mid-Late July 2006)</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_LKSE,56_INSTANCE_rU9t'||CHR(10)||'column-1=71_INSTANCE_85kQ'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/devzone/features_and_road_map/cowper', '', '', 0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('105', 'PUB.1', 'liferay.com', '96', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Future Releases</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_ZRT0,56_INSTANCE_VamE'||CHR(10)||'column-1=71_INSTANCE_F0iM'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/devzone/features_and_road_map/future_releases', '', '', 2);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('107', 'PUB.1', 'liferay.com', '96', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root> '||CHR(10)||'  <name>Machen (Sept-Oct 2006)</name> '||CHR(10)||'</root>', '', 'portlet', 'column-2=73_INSTANCE_v9Pr,56_INSTANCE_xzdr'||CHR(10)||'column-1=71_INSTANCE_kc70'||CHR(10)||'layout-template-id=2_columns_ii', 0, '/devzone/features_and_road_map/machen', '', '', 1);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, title, type_, typeSettings, hidden_, friendlyURL, themeId, colorSchemeId, priority) values ('108', 'PUB.1', 'liferay.com', '-1', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root>'||CHR(10)||'  <name>Admin</name>'||CHR(10)||'</root>', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root>'||CHR(10)||'  <title></title>'||CHR(10)||'</root>', 'portlet', 'column-1='||CHR(10)||'layout-template-id=2_columns_ii'||CHR(10)||'sitemap-include=1'||CHR(10)||'sitemap-changefreq=daily'||CHR(10)||'meta-robots='||CHR(10)||'meta-description='||CHR(10)||'javascript-3='||CHR(10)||'sitemap-priority='||CHR(10)||'javascript-2='||CHR(10)||'javascript-1='||CHR(10)||'meta-keywords='||CHR(10)||'column-2=15', 1, '/admin', '', '', 8);

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_10Zv', '1', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>FRONTPAGE-AD</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_1XQI', '1', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title-en_US</name><value>About Liferay</value></preference><preference><name>portlet-setup-title</name><value>About Liferay</value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>article-id</name><value>ABOUT-LIFERAY</value></preference><preference><name>portlet-setup-use-custom-title</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_5nBa', '1', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>EURO-TRAINING</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_SxQh', '1', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title-en_US</name><value>Solutions Showcase</value></preference><preference><name>portlet-setup-title</name><value>Solutions Showcase</value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>article-id</name><value>HOME-SHOWCASE-FLASH</value></preference><preference><name>portlet-setup-use-custom-title</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_TYp9', '1', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title-en_US</name><value>Downloads</value></preference><preference><name>portlet-setup-title</name><value>Downloads</value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>article-id</name><value>HOME-DOWNLOADS</value></preference><preference><name>portlet-setup-use-custom-title</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_j7RH', '1', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title-en_US</name><value>Preview Liferay Now</value></preference><preference><name>portlet-setup-title</name><value>Preview Liferay Now</value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>article-id</name><value>HOME-DEMO-IMAGE</value></preference><preference><name>portlet-setup-use-custom-title</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_njrf', '1', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title-en_US</name><value>News</value></preference><preference><name>portlet-setup-title</name><value>News</value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>article-id</name><value>HOME-ANNOUNCEMENTS</value></preference><preference><name>portlet-setup-use-custom-title</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_pZzX', '1', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>HOME-BANNER</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_fuMV', '2', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_ih5S', '2', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>BANNER-PRODUCTS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_rWuH', '2', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>ADVERTS-HOSTING-NARROW</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_RR44', '2', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>portlet-title</name><value></value></preference><preference><name>show-portlet-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_qyjU', '2', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_FuiH', '3', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>BANNER-STORIES</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Qsqv', '3', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title-en_US</name><value>Stories</value></preference><preference><name>portlet-setup-title</name><value>Stories</value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES</value></preference><preference><name>portlet-setup-use-custom-title</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_g60O', '3', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_w3z9', '3', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_7V3C', '4', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>BANNER-SERVICES</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_t6zG', '4', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>SERVICES</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_IhN6', '4', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_2QsS', '4', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_9djp', '5', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_g0sl', '5', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>BANNER-GLOBALTEAM</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_U3Sf', '5', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_mCw6', '5', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_VoqX', '6', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_WiHi', '6', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>GLOBAL-ADS</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_YjTG', '6', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>BANNER-DEVZONE</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_JjrK', '6', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>portlet-title</name><value></value></preference><preference><name>show-portlet-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_eB5C', '6', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_YDim', '7', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>ABOUTUS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_zL59', '7', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>BANNER-ABOUTUS</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_Cofs', '7', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_k7vx', '7', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_7q0R', '8', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-FEATURE-TEXT5</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_GnxQ', '8', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-FEATURE-OUTOFBOXPORTLETS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Rko0', '8', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-FEATURE-TEXT1</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_UqXp', '8', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-FEATURE-INTERNATIONALIZATION</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_UzUZ', '8', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-FEATURE-DRAGNDROP</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_eiGC', '8', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-FEATURE-THEMES</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_kBOZ', '8', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-FEATURE-TEXT4</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_mMfv', '8', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-FEATURE-SERVERAGNOSTIC</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_pFfk', '8', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-FEATURE-TEXT2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_ptoc', '8', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-FEATURE-SUBTHEMES</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_qNVY', '8', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-FEATURE-TEXT3</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_iB27', '8', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_l6LK', '8', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_AlsN', '9', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-CMS-XSL-EDITOR</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Bnki', '9', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-CMS-IMAGE-GALLERY</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_D69R', '9', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-CMS-STRUCTURED-CONTENT</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_f0cZ', '9', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-CMS-DOCUMENT-LIBRARY</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_ZOPP', '9', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_dWAs', '9', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');


insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_OQFa', '15', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LICENSING</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_SwpJ', '15', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_KeA3', '15', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_yWLy', '20', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>STORIES-EDUCAMADRID</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_aSXf', '20', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_UjKy', '20', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Ywz7', '21', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES-GOODWILL</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_L6lN', '21', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_V1ml', '21', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_oDpc', '22', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES-JASONS-DELI</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_phO7', '22', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_vwul', '22', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_dDyI', '23', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES-KOSICE-AIRPORT</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_at0s', '23', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_So1H', '23', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_WADv', '25', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES-OAKWOOD</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_pdtS', '25', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_9VOe', '25', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_c9ZK', '26', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES-OFFICE-GATEWAY</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_Q6Bd', '26', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_NCYT', '26', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_KIzv', '27', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES-PEPKM</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_vfKv', '27', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_LoFS', '27', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_VkAM', '28', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES-WALDEN</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_p1zu', '28', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_rrZx', '28', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_0epD', '29', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>SERVICES-PRODUCTION-SUPPORT</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_lvdx', '29', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>ADVERTS-HOSTING-NARROW</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_Bycc', '29', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_AlHH', '29', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_BXDh', '30', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>SERVICES-CONSULTING</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_hVIZ', '30', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_wo83', '30', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_PZj4', '32', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>SERVICES-ONSITE-TRAINING</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_ZbPW', '32', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_haR7', '32', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_ZjVs', '33', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>SERVICES-PUBLICTRAINING</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_Bc8P', '33', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_kpu1', '33', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_06ik', '34', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-TELECONSULT</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Nqi8', '34', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-SINETA</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_THic', '34', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-PREFERRED</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_UBC0', '34', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-PREMIER</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_gItp', '34', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-GERMINUS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_jG2r', '34', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-INTERBIZTECH</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_kxEY', '34', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-IPPON</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_iw1s', '34', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_zvFQ', '34', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_5Grt', '35', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-REDHAT</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Bn5o', '35', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-NOVELL</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_swtB', '35', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>3</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_K3EN', '35', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_zMzb', '36', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-LIPP-JOIN</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_XdlT', '36', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_ak4K', '36', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_GDMF', '38', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-PORTAL-SUPPORTED-VELOCITY</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_GEz5', '38', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-PORTAL-SUPPORTED-AJAX</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_H7pi', '38', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-PORTAL-SUPPORTED-WSRP</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_cTtF', '38', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-PORTAL-SUPPORTED-JSR168</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_k55O', '38', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-PORTAL-SUPPORTED-SPRING</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_nUAi', '38', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-PORTAL-SUPPORTED-HIBERNATE</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_oJ8q', '38', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-PORTAL-SUPPORTED-STRUTS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_oQE2', '38', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-PORTAL-SUPPORTED-JSF</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_UdjB', '38', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_5Sev', '38', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_08pB', '43', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-CMS-SUPPORTED-JSR170</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_gTej', '43', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-CMS-SUPPORTED-OTHER</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_bVjH', '43', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_Qulc', '43', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_gkzg', '45', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>ABOUTUS-LEADERSHIP</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_ln5C', '45', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_iLCy', '45', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_FF4S', '47', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>ABOUTUS-NEWS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_QGmH', '47', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>portlet-title</name><value></value></preference><preference><name>show-portlet-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_ZcUC', '47', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_aUIw', '48', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>ABOUTUS-CONTACTUS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_xYYe', '48', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_vfGn', '48', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_aImY', '49', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>ABOUTUS-NEWS-ANOUNCEMENTS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_0zuy', '49', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_YNTE', '49', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_rF3Q', '50', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>ABOUTUS-NEWS-IN-THE-NEWS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_bjKu', '50', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_qDM4', '50', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_xhsz', '51', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>ABOUTUS-NEWS-BLOGWORTHY</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_ET4S', '51', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_I8lz', '51', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_YMX1', '52', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-ARCHITECTURE</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_yiIz', '52', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_y5Zx', '52', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_bTu1', '53', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-DOCUMENTATION</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_zECO', '53', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_pORZ', '53', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('19', '55', 'PUB.1', '<portlet-preferences xmlns="http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd"><preference><name>ranks</name><value>Youngling=0</value><value>Padawan=25</value><value>Jedi Knight=100</value><value>Jedi Master=250</value><value>Jedi Council Member=500</value><value>Yoda=1000</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_KwwL', '56', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-JAVADOCS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_aPmQ', '56', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_iqeJ', '56', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_oayh', '57', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-FAQS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_7EDc', '57', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_EWJx', '57', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_MSqm', '58', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-GLOSSARY</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_Dw2X', '58', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_wKvS', '58', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_3FZM', '63', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-ARCHITECTURE-PORTLETAPI</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_J1im', '63', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_3dSC', '63', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_j5RR', '64', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-ARCHITECTURE-STRUTSANDTILES</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_KZXL', '64', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_rSVD', '64', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_ZPNd', '65', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-ARCHITECTURE-EJBSPRINGHIB</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_sI9U', '65', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_WKU0', '65', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_PdWQ', '66', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-ARCHITECTURE-SOAPRMITUNNEL</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_ARHZ', '66', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_9JMT', '66', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_9Ecu', '67', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-ARCHITECTURE-ASP</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_FRBX', '67', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_0Dde', '67', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_poDH', '73', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-LIPP-REQUIREMENTS-BENEFITS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_Bo0g', '73', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>3</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_rEvb', '73', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_OlTr', '74', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-LIPP-FAQ</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_302W', '74', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>3</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_5XIG', '74', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_d5Hf', '75', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>GLOBAL-TEAM-LIPP-PROGRAMSTRUCTURE</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_f3hz', '75', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>3</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_FIc2', '75', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_A5hL', '82', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>BANNER-DOWNLOADS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_QY8U', '82', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>ADVERTS-HOSTING-NARROW</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_S4Cj', '82', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DOWNLOADS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_hnHi', '82', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>3</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_MKI5', '82', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_TB6c', '83', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>SERVICES-ENTERPRISE-SUPPORT</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_EJFr', '83', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_xAWV', '83', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('38', '84', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_I0QN', '84', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>3</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_KPRv', '84', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_4asu', '85', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DOWNLOADS-COMPLIANT-PORTLETS-SYNCEX</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Lcfp', '85', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DOWNLOADS-COMPLIANT-PORTLETS-SOKOL-DEVELOPMENT</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_N1K2', '85', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DOWNLOADS-COMPLIANT-PORTLETS-COGIX</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_cCaH', '85', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DOWNLOADS-COMPLIANT-PORTLETS-SUN</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_cuqX', '85', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DOWNLOADS-COMPLIANT-PORTLETS-PORTLETWORKS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_eNhf', '85', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DOWNLOADS-COMPLIANT-PORTLETS-PORTLETBRIDGE</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_wDrj', '85', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DOWNLOADS-COMPLIANT-PORTLETS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_rcIX', '85', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>3</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_xezx', '85', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_mdV8', '86', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-DEPLOYMENTMATRIX</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_7rzL', '86', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_7PUz', '86', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_4zee', '87', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>SERVICES-PUBLICTRAINING-COURSETOPICS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_zehb', '87', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_LP58', '87', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_y9j4', '88', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>SERVICES-PUBLICTRAINING-REGISTRATION</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_N9XJ', '88', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_tIPq', '88', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_jLBl', '89', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DOWNLOADS-SAMPLES</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_sAqv', '89', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>3</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_j3tr', '89', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_AlsN', '90', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-MESH</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_ZOPP', '90', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_dWAs', '90', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_xYYe', '91', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_vfGn', '91', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('85_INSTANCE_LHLO', '91', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_VkAM', '92', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES-TELECENTROS</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_p1zu', '92', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_rrZx', '92', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_VkAM', '93', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES-JUNTA</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_p1zu', '93', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_rrZx', '93', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_WADv', '94', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES-OTHER-CLIENTS</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_pdtS', '94', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_9VOe', '94', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_2hUP', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-SECURITY-FINE-GRAIN-PERMISSIONING</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_5e2u', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-CMS-JOURNAL-ARTICLES</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_8hZN', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-CMS-JOURNAL-JOURNAL-CONTENT</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_A8GS', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-COMMUNITIES</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_BTbP', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-USER-ADMIN-ORGANIZATION</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Fq3X', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-CMS-DOCUMENT-LIBRARY</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Ja2P', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-SECURITY-DELEGATE-ROLE-PERMISSION</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_KV9Z', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-CMS-INTERNATIONALIZATION</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_LiYV', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-USER-ADMIN-LOCATION</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_NzSr', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-CMS-NAVIGATION-2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_ObYn', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-CMS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Paqq', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-CMS-IMAGE-GALLERY</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_PjmG', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-CMS-BREADCRUMB</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Rv7O', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-SECURITY-ROLES-AND-PERMISSIONS</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Shhg', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-SECURITY-COMMUNITY-PERMISSIONS2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Wp7E', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-SECURITY-ASSIGN-PERMISSIONS-TO-ADD</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_XvcX', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-USER-ADMIN</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_YavK', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-SECURITY-COMMUNITY1</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_bTu1', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-INSTALLATION</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_dnwJ', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-USER-ADMIN-USER-GROUP</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_fhSs', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-USER-ADMIN-COMMUNITIES-3</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_gxXW', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-SECURITY</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_ihyj', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-BASICS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_j663', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-SECURITY-LOCATIONS</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_lqvk', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-SECURITY-DELEGATE-ENTERPRISE-PERMISSIONS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_sCao', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-USER-ADMIN-COMMUNITIES-1</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_u0ta', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-CMS-NAVIGATION</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_uhas', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-USER-ADMIN-COMMUNITIES-2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_vbuQ', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-USER-ADMIN-COMMUNITIES-4</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_vke1', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-USER-ADMIN-ENTERPRISE</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_wss6', '95', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-LIFECAST-SECURITY-COMMUNITY-PERMISSIONS3</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_zECO', '95', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_pORZ', '95', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_JY6T', '96', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value>4.0.0 Final</value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-FEATURES-ROADMAP</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_Pb3s', '96', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_8pGt', '96', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Ywz7', '97', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>STORIES-STADUP</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_L6lN', '97', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_V1ml', '97', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_22DM', '98', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>SAMPLE-PUBLICDEMO</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_00Rw', '98', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_8aj6', '98', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_dWUS', '99', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>SERVICES-TRAINING</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_suFw', '99', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_8eXh', '99', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_dWUS', '100', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>article-id</name><value>SERVICES-TRAINING-CERTIFICATION</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_suFw', '100', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_8eXh', '100', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_ZjVs', '101', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PUBLIC-TRAINING-EUROPE</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_Bc8P', '101', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_kpu1', '101', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_eiGC', '102', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAYPORTAL-BENCHMARKS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_pFfk', '102', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>PRODUCTS-LIFERAY-PORTAL-BENCHMARKS-RESULTS</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_iB27', '102', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>iteration-style</name><value>4</value></preference><preference><name>display-style</name><value>4</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_l6LK', '102', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_Ts1z', '103', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>SERVICES-HOSTING</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_ZdrZ', '103', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_n8xu', '103', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_rU9t', '104', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-FEATURES-ROADMAP-COWPER</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_85kQ', '104', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_LKSE', '104', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_VamE', '105', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-FEATURES-ROADMAP-FUTURE-RELEASES</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_F0iM', '105', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_ZRT0', '105', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference><preference><name>display-style</name><value>2</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('56_INSTANCE_xzdr', '107', 'PUB.1', '<portlet-preferences><preference><name>group-id</name><value>1</value></preference><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>article-id</name><value>DEVZONE-FEATURES-ROADMAP-MACHEN</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('71_INSTANCE_kc70', '107', 'PUB.1', '<portlet-preferences><preference><name>bullet-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>true</value></preference><preference><name>display-style</name><value>4</value></preference></portlet-preferences>');
insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('73_INSTANCE_v9Pr', '107', 'PUB.1', '<portlet-preferences><preference><name>portlet-setup-title</name><value></value></preference><preference><name>portlet-setup-css</name><value></value></preference><preference><name>display-style</name><value>2</value></preference><preference><name>portlet-setup-show-borders</name><value>false</value></preference></portlet-preferences>');

insert into PortletPreferences (portletId, layoutId, ownerId, preferences) values ('15', '108', 'PUB.1', '<portlet-preferences />');


--
-- Company
--

insert into Company (companyId, portalURL, homeURL, mx) values ('liferay.com', 'localhost:8080', 'localhost:8080', 'liferay.com');
insert into Account_ (accountId, companyId, userId, userName, createDate, modifiedDate, parentAccountId, name, legalName, legalId, legalType, sicCode, tickerSymbol, industry, type_, size_) values ('liferay.com', 'default', 'liferay.com.default', '', sysdate, sysdate, '-1', 'Liferay', 'Liferay, LLC', '', '', '', '', '', '', '');

--
-- Groups
--

insert into Group_ (groupId, companyId, parentGroupId, liveGroupId, name, friendlyURL, active_) values (1, 'liferay.com', -1, -1, 'Guest', '/guest', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.1', 'liferay.com', 1, '1', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.1', 'liferay.com', 1, '1', 0, 0, 'brochure', '01', 89.0);

insert into Group_ (groupId, companyId, parentGroupId, liveGroupId, name, friendlyURL, active_) values (3, 'liferay.com', -1, -1, 'Support', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.3', 'liferay.com', 3, '3', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.3', 'liferay.com', 3, '3', 0, 0, 'classic', '01', 0.0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, type_, typeSettings, hidden_, friendlyURL, priority) values ('1', 'PRI.3', 'liferay.com', '-1', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root>'||CHR(10)||'  <name>Support</name>'||CHR(10)||'</root>', 'portlet', 'layout-template-id=2_columns_ii'||CHR(10)||'column-1=3,'||CHR(10)||'column-2=19,', 0, '', 0.0);

--
-- Organizations
--

insert into Organization_ (organizationId, companyId, parentOrganizationId, name, recursable, regionId, countryId, statusId, comments) values ('1', 'liferay.com', '-1', 'Liferay USA', 0, '5', '19', 12017, '');

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (4, 'liferay.com', 'com.liferay.portal.model.Organization', '1', -1, -1, '4', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.4', 'liferay.com', 4, '4', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.4', 'liferay.com', 4, '4', 0, 0, 'classic', '01', 0.0);

insert into Organization_ (organizationId, companyId, parentOrganizationId, name, recursable, regionId, countryId, statusId, comments) values ('2', 'liferay.com', '1', 'Liferay Los Angeles', 0, '5', '19', 12017, '');

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (5, 'liferay.com', 'com.liferay.portal.model.Organization', '2', -1, -1, '5', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.5', 'liferay.com', 5, '5', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.5', 'liferay.com', 5, '5', 0, 0, 'classic', '01', 0.0);

insert into Organization_ (organizationId, companyId, parentOrganizationId, name, recursable, regionId, countryId, statusId, comments) values ('3', 'liferay.com', '1', 'Liferay San Francisco', 0, '5', '19', 12017, '');

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (6, 'liferay.com', 'com.liferay.portal.model.Organization', '3', -1, -1, '6', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.6', 'liferay.com', 6, '6', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.6', 'liferay.com', 6, '6', 0, 0, 'classic', '01', 0.0);

insert into Organization_ (organizationId, companyId, parentOrganizationId, name, recursable, regionId, countryId, statusId, comments) values ('4', 'liferay.com', '1', 'Liferay Chicago', 0, '14', '19', 12017, '');

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (7, 'liferay.com', 'com.liferay.portal.model.Organization', '4', -1, -1, '7', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.7', 'liferay.com', 7, '7', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.7', 'liferay.com', 7, '7', 0, 0, 'classic', '01', 0.0);

insert into Organization_ (organizationId, companyId, parentOrganizationId, name, recursable, regionId, countryId, statusId, comments) values ('5', 'liferay.com', '1', 'Liferay New York', 0, '33', '19', 12017, '');

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (8, 'liferay.com', 'com.liferay.portal.model.Organization', '5', -1, -1, '8', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.8', 'liferay.com', 8, '8', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.8', 'liferay.com', 8, '8', 0, 0, 'classic', '01', 0.0);

insert into Organization_ (organizationId, companyId, parentOrganizationId, name, recursable, regionId, countryId, statusId, comments) values ('6', 'liferay.com', '-1', 'Liferay Europe', 0, '', '15', 12017, '');

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (9, 'liferay.com', 'com.liferay.portal.model.Organization', '6', -1, -1, '9', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.9', 'liferay.com', 9, '9', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.9', 'liferay.com', 9, '9', 0, 0, 'classic', '01', 0.0);

insert into Organization_ (organizationId, companyId, parentOrganizationId, name, recursable, regionId, countryId, statusId, comments) values ('7', 'liferay.com', '6', 'Liferay London', 0, '', '18', 12017, '');

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (10, 'liferay.com', 'com.liferay.portal.model.Organization', '7', -1, -1, '10', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.10', 'liferay.com', 10, '10', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.10', 'liferay.com', 10, '10', 0, 0, 'classic', '01', 0.0);

insert into Organization_ (organizationId, companyId, parentOrganizationId, name, recursable, regionId, countryId, statusId, comments) values ('8', 'liferay.com', '6', 'Liferay Madrid', 0, '', '15', 12017, '');

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (11, 'liferay.com', 'com.liferay.portal.model.Organization', '8', -1, -1, '11', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.11', 'liferay.com', 11, '11', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.11', 'liferay.com', 11, '11', 0, 0, 'classic', '01', 0.0);

insert into Organization_ (organizationId, companyId, parentOrganizationId, name, recursable, regionId, countryId, statusId, comments) values ('9', 'liferay.com', '-1', 'Liferay Asia', 0, '', '2', 12017, '');

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (12, 'liferay.com', 'com.liferay.portal.model.Organization', '9', -1, -1, '12', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.12', 'liferay.com', 12, '12', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.12', 'liferay.com', 12, '12', 0, 0, 'classic', '01', 0.0);

insert into Organization_ (organizationId, companyId, parentOrganizationId, name, recursable, regionId, countryId, statusId, comments) values ('10', 'liferay.com', '9', 'Liferay Hong Kong', 0, '', '2', 12017, '');

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (13, 'liferay.com', 'com.liferay.portal.model.Organization', '10', -1, -1, '13', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.13', 'liferay.com', 13, '13', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.13', 'liferay.com', 13, '13', 0, 0, 'classic', '01', 0.0);

insert into Organization_ (organizationId, companyId, parentOrganizationId, name, recursable, regionId, countryId, statusId, comments) values ('11', 'liferay.com', '9', 'Liferay Shanghai', 0, '', '2', 12017, '');

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (14, 'liferay.com', 'com.liferay.portal.model.Organization', '11', -1, -1, '14', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.14', 'liferay.com', 14, '14', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.14', 'liferay.com', 14, '14', 0, 0, 'classic', '01', 0.0);

--
-- Roles
--

insert into Role_ (roleId, companyId, name, type_) values ('1', 'liferay.com', 'Administrator', 1);
insert into Role_ (roleId, companyId, name, type_) values ('2', 'liferay.com', 'Guest', 1);
insert into Role_ (roleId, companyId, name, type_) values ('3', 'liferay.com', 'Power User', 1);
insert into Role_ (roleId, companyId, name, type_) values ('4', 'liferay.com', 'User', 1);

--
-- User (default)
--

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.default', 'default', sysdate, sysdate, 1000, 'password', 0, 0, 'liferay.com.default', 'default@liferay.com', 'Welcome!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1000, 'default', 'liferay.com.default', '', sysdate, sysdate, 'default', -1, '', '', '', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

--
-- User (test@liferay.com)
--

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.1', 'liferay.com', sysdate, sysdate, 1001, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'joebloggs', 'test@liferay.com', 'Welcome Joe Bloggs!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1001, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Joe', '', 'Bloggs', 'Duke', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (15, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.1', -1, -1, '15', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.15', 'liferay.com', 15, '15', 1, 0, 'classic', '01', 1.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.15', 'liferay.com', 15, '15', 0, 0, 'classic', '01', 0.0);
insert into Layout (layoutId, ownerId, companyId, parentLayoutId, name, type_, typeSettings, hidden_, friendlyURL, priority) values ('1', 'PRI.15', 'liferay.com', '-1', '<?xml version="1.0"?>'||CHR(10)||''||CHR(10)||'<root>'||CHR(10)||'  <name>Home A1</name>'||CHR(10)||'</root>', 'portlet', 'column-1=71_INSTANCE_OY0d,61,65,'||CHR(10)||'column-2=9,29,79,8,'||CHR(10)||'layout-template-id=2_columns_ii'||CHR(10)||'', 0, '', 0.0);

insert into Users_Groups values ('liferay.com.1', 1);
insert into Users_Groups values ('liferay.com.1', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.1', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.1', '2');

insert into Users_Roles values ('liferay.com.1', '1');
insert into Users_Roles values ('liferay.com.1', '3');
insert into Users_Roles values ('liferay.com.1', '4');

--
-- User (test.mail@liferay.com)
--

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.35', 'liferay.com', sysdate, sysdate, 1002, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 1, 'testmail', 'test.mail@liferay.com', 'Welcome Test Mail!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1002, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'Mail', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (16, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.35', -1, -1, '16', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.16', 'liferay.com', 16, '16', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.16', 'liferay.com', 16, '16', 0, 0, 'classic', '01', 0.0);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.35', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.35', '2');

insert into Users_Roles values ('liferay.com.35', '3');
insert into Users_Roles values ('liferay.com.35', '4');



insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.100', 'liferay.com', sysdate, sysdate, 1100, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax1', 'test.lax.1@liferay.com', 'Welcome Test LAX 1!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1100, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 1', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (100, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.100', -1, -1, '100', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.100', 'liferay.com', 100, '100', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.100', 'liferay.com', 100, '100', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.100', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.100', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.100', '2');

insert into Users_Roles values ('liferay.com.100', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.101', 'liferay.com', sysdate, sysdate, 1101, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax2', 'test.lax.2@liferay.com', 'Welcome Test LAX 2!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1101, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 2', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (101, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.101', -1, -1, '101', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.101', 'liferay.com', 101, '101', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.101', 'liferay.com', 101, '101', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.101', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.101', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.101', '2');

insert into Users_Roles values ('liferay.com.101', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.102', 'liferay.com', sysdate, sysdate, 1102, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax3', 'test.lax.3@liferay.com', 'Welcome Test LAX 3!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1102, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 3', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (102, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.102', -1, -1, '102', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.102', 'liferay.com', 102, '102', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.102', 'liferay.com', 102, '102', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.102', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.102', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.102', '2');

insert into Users_Roles values ('liferay.com.102', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.103', 'liferay.com', sysdate, sysdate, 1103, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax4', 'test.lax.4@liferay.com', 'Welcome Test LAX 4!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1103, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 4', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (103, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.103', -1, -1, '103', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.103', 'liferay.com', 103, '103', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.103', 'liferay.com', 103, '103', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.103', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.103', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.103', '2');

insert into Users_Roles values ('liferay.com.103', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.104', 'liferay.com', sysdate, sysdate, 1104, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax5', 'test.lax.5@liferay.com', 'Welcome Test LAX 5!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1104, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 5', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (104, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.104', -1, -1, '104', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.104', 'liferay.com', 104, '104', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.104', 'liferay.com', 104, '104', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.104', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.104', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.104', '2');

insert into Users_Roles values ('liferay.com.104', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.105', 'liferay.com', sysdate, sysdate, 1105, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax6', 'test.lax.6@liferay.com', 'Welcome Test LAX 6!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1105, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 6', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (105, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.105', -1, -1, '105', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.105', 'liferay.com', 105, '105', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.105', 'liferay.com', 105, '105', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.105', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.105', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.105', '2');

insert into Users_Roles values ('liferay.com.105', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.106', 'liferay.com', sysdate, sysdate, 1106, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax7', 'test.lax.7@liferay.com', 'Welcome Test LAX 7!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1106, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 7', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (106, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.106', -1, -1, '106', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.106', 'liferay.com', 106, '106', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.106', 'liferay.com', 106, '106', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.106', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.106', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.106', '2');

insert into Users_Roles values ('liferay.com.106', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.107', 'liferay.com', sysdate, sysdate, 1107, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax8', 'test.lax.8@liferay.com', 'Welcome Test LAX 8!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1107, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 8', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (107, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.107', -1, -1, '107', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.107', 'liferay.com', 107, '107', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.107', 'liferay.com', 107, '107', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.107', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.107', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.107', '2');

insert into Users_Roles values ('liferay.com.107', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.108', 'liferay.com', sysdate, sysdate, 1108, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax9', 'test.lax.9@liferay.com', 'Welcome Test LAX 9!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1108, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 9', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (108, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.108', -1, -1, '108', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.108', 'liferay.com', 108, '108', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.108', 'liferay.com', 108, '108', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.108', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.108', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.108', '2');

insert into Users_Roles values ('liferay.com.108', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.109', 'liferay.com', sysdate, sysdate, 1109, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax10', 'test.lax.10@liferay.com', 'Welcome Test LAX 10!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1109, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 10', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (109, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.109', -1, -1, '109', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.109', 'liferay.com', 109, '109', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.109', 'liferay.com', 109, '109', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.109', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.109', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.109', '2');

insert into Users_Roles values ('liferay.com.109', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.110', 'liferay.com', sysdate, sysdate, 1110, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax11', 'test.lax.11@liferay.com', 'Welcome Test LAX 11!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1110, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 11', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (110, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.110', -1, -1, '110', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.110', 'liferay.com', 110, '110', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.110', 'liferay.com', 110, '110', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.110', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.110', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.110', '2');

insert into Users_Roles values ('liferay.com.110', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.111', 'liferay.com', sysdate, sysdate, 1111, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax12', 'test.lax.12@liferay.com', 'Welcome Test LAX 12!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1111, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 12', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (111, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.111', -1, -1, '111', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.111', 'liferay.com', 111, '111', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.111', 'liferay.com', 111, '111', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.111', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.111', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.111', '2');

insert into Users_Roles values ('liferay.com.111', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.112', 'liferay.com', sysdate, sysdate, 1112, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax13', 'test.lax.13@liferay.com', 'Welcome Test LAX 13!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1112, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 13', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (112, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.112', -1, -1, '112', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.112', 'liferay.com', 112, '112', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.112', 'liferay.com', 112, '112', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.112', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.112', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.112', '2');

insert into Users_Roles values ('liferay.com.112', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.113', 'liferay.com', sysdate, sysdate, 1113, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax14', 'test.lax.14@liferay.com', 'Welcome Test LAX 14!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1113, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 14', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (113, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.113', -1, -1, '113', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.113', 'liferay.com', 113, '113', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.113', 'liferay.com', 113, '113', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.113', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.113', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.113', '2');

insert into Users_Roles values ('liferay.com.113', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.114', 'liferay.com', sysdate, sysdate, 1114, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax15', 'test.lax.15@liferay.com', 'Welcome Test LAX 15!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1114, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 15', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (114, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.114', -1, -1, '114', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.114', 'liferay.com', 114, '114', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.114', 'liferay.com', 114, '114', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.114', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.114', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.114', '2');

insert into Users_Roles values ('liferay.com.114', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.115', 'liferay.com', sysdate, sysdate, 1115, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax16', 'test.lax.16@liferay.com', 'Welcome Test LAX 16!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1115, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 16', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (115, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.115', -1, -1, '115', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.115', 'liferay.com', 115, '115', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.115', 'liferay.com', 115, '115', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.115', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.115', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.115', '2');

insert into Users_Roles values ('liferay.com.115', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.116', 'liferay.com', sysdate, sysdate, 1116, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax17', 'test.lax.17@liferay.com', 'Welcome Test LAX 17!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1116, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 17', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (116, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.116', -1, -1, '116', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.116', 'liferay.com', 116, '116', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.116', 'liferay.com', 116, '116', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.116', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.116', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.116', '2');

insert into Users_Roles values ('liferay.com.116', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.117', 'liferay.com', sysdate, sysdate, 1117, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax18', 'test.lax.18@liferay.com', 'Welcome Test LAX 18!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1117, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 18', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (117, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.117', -1, -1, '117', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.117', 'liferay.com', 117, '117', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.117', 'liferay.com', 117, '117', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.117', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.117', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.117', '2');

insert into Users_Roles values ('liferay.com.117', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.118', 'liferay.com', sysdate, sysdate, 1118, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax19', 'test.lax.19@liferay.com', 'Welcome Test LAX 19!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1118, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 19', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (118, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.118', -1, -1, '118', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.118', 'liferay.com', 118, '118', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.118', 'liferay.com', 118, '118', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.118', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.118', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.118', '2');

insert into Users_Roles values ('liferay.com.118', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.119', 'liferay.com', sysdate, sysdate, 1119, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax20', 'test.lax.20@liferay.com', 'Welcome Test LAX 20!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1119, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 20', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (119, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.119', -1, -1, '119', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.119', 'liferay.com', 119, '119', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.119', 'liferay.com', 119, '119', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.119', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.119', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.119', '2');

insert into Users_Roles values ('liferay.com.119', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.120', 'liferay.com', sysdate, sysdate, 1120, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax21', 'test.lax.21@liferay.com', 'Welcome Test LAX 21!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1120, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 21', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (120, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.120', -1, -1, '120', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.120', 'liferay.com', 120, '120', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.120', 'liferay.com', 120, '120', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.120', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.120', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.120', '2');

insert into Users_Roles values ('liferay.com.120', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.121', 'liferay.com', sysdate, sysdate, 1121, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax22', 'test.lax.22@liferay.com', 'Welcome Test LAX 22!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1121, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 22', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (121, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.121', -1, -1, '121', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.121', 'liferay.com', 121, '121', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.121', 'liferay.com', 121, '121', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.121', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.121', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.121', '2');

insert into Users_Roles values ('liferay.com.121', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.122', 'liferay.com', sysdate, sysdate, 1122, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax23', 'test.lax.23@liferay.com', 'Welcome Test LAX 23!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1122, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 23', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (122, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.122', -1, -1, '122', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.122', 'liferay.com', 122, '122', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.122', 'liferay.com', 122, '122', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.122', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.122', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.122', '2');

insert into Users_Roles values ('liferay.com.122', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.123', 'liferay.com', sysdate, sysdate, 1123, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax24', 'test.lax.24@liferay.com', 'Welcome Test LAX 24!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1123, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 24', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (123, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.123', -1, -1, '123', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.123', 'liferay.com', 123, '123', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.123', 'liferay.com', 123, '123', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.123', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.123', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.123', '2');

insert into Users_Roles values ('liferay.com.123', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.124', 'liferay.com', sysdate, sysdate, 1124, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax25', 'test.lax.25@liferay.com', 'Welcome Test LAX 25!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1124, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 25', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (124, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.124', -1, -1, '124', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.124', 'liferay.com', 124, '124', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.124', 'liferay.com', 124, '124', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.124', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.124', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.124', '2');

insert into Users_Roles values ('liferay.com.124', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.125', 'liferay.com', sysdate, sysdate, 1125, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax26', 'test.lax.26@liferay.com', 'Welcome Test LAX 26!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1125, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 26', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (125, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.125', -1, -1, '125', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.125', 'liferay.com', 125, '125', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.125', 'liferay.com', 125, '125', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.125', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.125', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.125', '2');

insert into Users_Roles values ('liferay.com.125', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.126', 'liferay.com', sysdate, sysdate, 1126, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax27', 'test.lax.27@liferay.com', 'Welcome Test LAX 27!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1126, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 27', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (126, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.126', -1, -1, '126', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.126', 'liferay.com', 126, '126', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.126', 'liferay.com', 126, '126', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.126', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.126', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.126', '2');

insert into Users_Roles values ('liferay.com.126', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.127', 'liferay.com', sysdate, sysdate, 1127, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax28', 'test.lax.28@liferay.com', 'Welcome Test LAX 28!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1127, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 28', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (127, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.127', -1, -1, '127', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.127', 'liferay.com', 127, '127', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.127', 'liferay.com', 127, '127', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.127', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.127', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.127', '2');

insert into Users_Roles values ('liferay.com.127', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.128', 'liferay.com', sysdate, sysdate, 1128, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax29', 'test.lax.29@liferay.com', 'Welcome Test LAX 29!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1128, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 29', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (128, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.128', -1, -1, '128', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.128', 'liferay.com', 128, '128', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.128', 'liferay.com', 128, '128', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.128', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.128', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.128', '2');

insert into Users_Roles values ('liferay.com.128', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.129', 'liferay.com', sysdate, sysdate, 1129, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax30', 'test.lax.30@liferay.com', 'Welcome Test LAX 30!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1129, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 30', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (129, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.129', -1, -1, '129', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.129', 'liferay.com', 129, '129', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.129', 'liferay.com', 129, '129', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.129', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.129', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.129', '2');

insert into Users_Roles values ('liferay.com.129', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.130', 'liferay.com', sysdate, sysdate, 1130, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax31', 'test.lax.31@liferay.com', 'Welcome Test LAX 31!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1130, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 31', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (130, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.130', -1, -1, '130', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.130', 'liferay.com', 130, '130', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.130', 'liferay.com', 130, '130', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.130', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.130', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.130', '2');

insert into Users_Roles values ('liferay.com.130', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.131', 'liferay.com', sysdate, sysdate, 1131, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax32', 'test.lax.32@liferay.com', 'Welcome Test LAX 32!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1131, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 32', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (131, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.131', -1, -1, '131', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.131', 'liferay.com', 131, '131', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.131', 'liferay.com', 131, '131', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.131', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.131', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.131', '2');

insert into Users_Roles values ('liferay.com.131', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.132', 'liferay.com', sysdate, sysdate, 1132, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax33', 'test.lax.33@liferay.com', 'Welcome Test LAX 33!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1132, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 33', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (132, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.132', -1, -1, '132', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.132', 'liferay.com', 132, '132', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.132', 'liferay.com', 132, '132', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.132', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.132', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.132', '2');

insert into Users_Roles values ('liferay.com.132', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.133', 'liferay.com', sysdate, sysdate, 1133, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax34', 'test.lax.34@liferay.com', 'Welcome Test LAX 34!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1133, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 34', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (133, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.133', -1, -1, '133', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.133', 'liferay.com', 133, '133', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.133', 'liferay.com', 133, '133', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.133', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.133', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.133', '2');

insert into Users_Roles values ('liferay.com.133', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.134', 'liferay.com', sysdate, sysdate, 1134, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax35', 'test.lax.35@liferay.com', 'Welcome Test LAX 35!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1134, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 35', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (134, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.134', -1, -1, '134', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.134', 'liferay.com', 134, '134', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.134', 'liferay.com', 134, '134', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.134', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.134', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.134', '2');

insert into Users_Roles values ('liferay.com.134', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.135', 'liferay.com', sysdate, sysdate, 1135, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax36', 'test.lax.36@liferay.com', 'Welcome Test LAX 36!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1135, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 36', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (135, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.135', -1, -1, '135', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.135', 'liferay.com', 135, '135', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.135', 'liferay.com', 135, '135', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.135', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.135', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.135', '2');

insert into Users_Roles values ('liferay.com.135', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.136', 'liferay.com', sysdate, sysdate, 1136, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax37', 'test.lax.37@liferay.com', 'Welcome Test LAX 37!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1136, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 37', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (136, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.136', -1, -1, '136', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.136', 'liferay.com', 136, '136', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.136', 'liferay.com', 136, '136', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.136', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.136', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.136', '2');

insert into Users_Roles values ('liferay.com.136', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.137', 'liferay.com', sysdate, sysdate, 1137, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax38', 'test.lax.38@liferay.com', 'Welcome Test LAX 38!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1137, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 38', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (137, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.137', -1, -1, '137', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.137', 'liferay.com', 137, '137', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.137', 'liferay.com', 137, '137', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.137', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.137', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.137', '2');

insert into Users_Roles values ('liferay.com.137', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.138', 'liferay.com', sysdate, sysdate, 1138, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax39', 'test.lax.39@liferay.com', 'Welcome Test LAX 39!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1138, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 39', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (138, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.138', -1, -1, '138', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.138', 'liferay.com', 138, '138', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.138', 'liferay.com', 138, '138', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.138', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.138', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.138', '2');

insert into Users_Roles values ('liferay.com.138', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.139', 'liferay.com', sysdate, sysdate, 1139, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax40', 'test.lax.40@liferay.com', 'Welcome Test LAX 40!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1139, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 40', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (139, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.139', -1, -1, '139', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.139', 'liferay.com', 139, '139', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.139', 'liferay.com', 139, '139', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.139', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.139', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.139', '2');

insert into Users_Roles values ('liferay.com.139', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.140', 'liferay.com', sysdate, sysdate, 1140, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax41', 'test.lax.41@liferay.com', 'Welcome Test LAX 41!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1140, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 41', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (140, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.140', -1, -1, '140', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.140', 'liferay.com', 140, '140', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.140', 'liferay.com', 140, '140', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.140', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.140', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.140', '2');

insert into Users_Roles values ('liferay.com.140', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.141', 'liferay.com', sysdate, sysdate, 1141, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax42', 'test.lax.42@liferay.com', 'Welcome Test LAX 42!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1141, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 42', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (141, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.141', -1, -1, '141', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.141', 'liferay.com', 141, '141', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.141', 'liferay.com', 141, '141', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.141', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.141', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.141', '2');

insert into Users_Roles values ('liferay.com.141', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.142', 'liferay.com', sysdate, sysdate, 1142, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax43', 'test.lax.43@liferay.com', 'Welcome Test LAX 43!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1142, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 43', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (142, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.142', -1, -1, '142', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.142', 'liferay.com', 142, '142', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.142', 'liferay.com', 142, '142', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.142', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.142', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.142', '2');

insert into Users_Roles values ('liferay.com.142', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.143', 'liferay.com', sysdate, sysdate, 1143, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax44', 'test.lax.44@liferay.com', 'Welcome Test LAX 44!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1143, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 44', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (143, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.143', -1, -1, '143', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.143', 'liferay.com', 143, '143', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.143', 'liferay.com', 143, '143', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.143', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.143', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.143', '2');

insert into Users_Roles values ('liferay.com.143', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.144', 'liferay.com', sysdate, sysdate, 1144, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax45', 'test.lax.45@liferay.com', 'Welcome Test LAX 45!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1144, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 45', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (144, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.144', -1, -1, '144', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.144', 'liferay.com', 144, '144', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.144', 'liferay.com', 144, '144', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.144', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.144', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.144', '2');

insert into Users_Roles values ('liferay.com.144', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.145', 'liferay.com', sysdate, sysdate, 1145, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax46', 'test.lax.46@liferay.com', 'Welcome Test LAX 46!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1145, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 46', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (145, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.145', -1, -1, '145', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.145', 'liferay.com', 145, '145', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.145', 'liferay.com', 145, '145', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.145', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.145', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.145', '2');

insert into Users_Roles values ('liferay.com.145', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.146', 'liferay.com', sysdate, sysdate, 1146, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax47', 'test.lax.47@liferay.com', 'Welcome Test LAX 47!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1146, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 47', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (146, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.146', -1, -1, '146', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.146', 'liferay.com', 146, '146', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.146', 'liferay.com', 146, '146', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.146', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.146', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.146', '2');

insert into Users_Roles values ('liferay.com.146', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.147', 'liferay.com', sysdate, sysdate, 1147, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax48', 'test.lax.48@liferay.com', 'Welcome Test LAX 48!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1147, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 48', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (147, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.147', -1, -1, '147', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.147', 'liferay.com', 147, '147', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.147', 'liferay.com', 147, '147', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.147', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.147', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.147', '2');

insert into Users_Roles values ('liferay.com.147', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.148', 'liferay.com', sysdate, sysdate, 1148, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax49', 'test.lax.49@liferay.com', 'Welcome Test LAX 49!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1148, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 49', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (148, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.148', -1, -1, '148', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.148', 'liferay.com', 148, '148', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.148', 'liferay.com', 148, '148', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.148', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.148', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.148', '2');

insert into Users_Roles values ('liferay.com.148', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.149', 'liferay.com', sysdate, sysdate, 1149, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax50', 'test.lax.50@liferay.com', 'Welcome Test LAX 50!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1149, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 50', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (149, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.149', -1, -1, '149', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.149', 'liferay.com', 149, '149', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.149', 'liferay.com', 149, '149', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.149', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.149', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.149', '2');

insert into Users_Roles values ('liferay.com.149', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.150', 'liferay.com', sysdate, sysdate, 1150, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax51', 'test.lax.51@liferay.com', 'Welcome Test LAX 51!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1150, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 51', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (150, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.150', -1, -1, '150', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.150', 'liferay.com', 150, '150', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.150', 'liferay.com', 150, '150', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.150', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.150', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.150', '2');

insert into Users_Roles values ('liferay.com.150', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.151', 'liferay.com', sysdate, sysdate, 1151, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax52', 'test.lax.52@liferay.com', 'Welcome Test LAX 52!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1151, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 52', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (151, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.151', -1, -1, '151', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.151', 'liferay.com', 151, '151', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.151', 'liferay.com', 151, '151', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.151', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.151', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.151', '2');

insert into Users_Roles values ('liferay.com.151', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.152', 'liferay.com', sysdate, sysdate, 1152, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax53', 'test.lax.53@liferay.com', 'Welcome Test LAX 53!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1152, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 53', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (152, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.152', -1, -1, '152', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.152', 'liferay.com', 152, '152', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.152', 'liferay.com', 152, '152', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.152', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.152', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.152', '2');

insert into Users_Roles values ('liferay.com.152', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.153', 'liferay.com', sysdate, sysdate, 1153, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax54', 'test.lax.54@liferay.com', 'Welcome Test LAX 54!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1153, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 54', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (153, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.153', -1, -1, '153', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.153', 'liferay.com', 153, '153', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.153', 'liferay.com', 153, '153', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.153', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.153', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.153', '2');

insert into Users_Roles values ('liferay.com.153', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.154', 'liferay.com', sysdate, sysdate, 1154, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax55', 'test.lax.55@liferay.com', 'Welcome Test LAX 55!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1154, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 55', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (154, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.154', -1, -1, '154', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.154', 'liferay.com', 154, '154', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.154', 'liferay.com', 154, '154', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.154', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.154', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.154', '2');

insert into Users_Roles values ('liferay.com.154', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.155', 'liferay.com', sysdate, sysdate, 1155, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax56', 'test.lax.56@liferay.com', 'Welcome Test LAX 56!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1155, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 56', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (155, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.155', -1, -1, '155', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.155', 'liferay.com', 155, '155', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.155', 'liferay.com', 155, '155', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.155', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.155', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.155', '2');

insert into Users_Roles values ('liferay.com.155', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.156', 'liferay.com', sysdate, sysdate, 1156, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax57', 'test.lax.57@liferay.com', 'Welcome Test LAX 57!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1156, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 57', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (156, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.156', -1, -1, '156', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.156', 'liferay.com', 156, '156', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.156', 'liferay.com', 156, '156', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.156', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.156', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.156', '2');

insert into Users_Roles values ('liferay.com.156', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.157', 'liferay.com', sysdate, sysdate, 1157, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax58', 'test.lax.58@liferay.com', 'Welcome Test LAX 58!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1157, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 58', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (157, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.157', -1, -1, '157', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.157', 'liferay.com', 157, '157', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.157', 'liferay.com', 157, '157', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.157', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.157', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.157', '2');

insert into Users_Roles values ('liferay.com.157', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.158', 'liferay.com', sysdate, sysdate, 1158, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax59', 'test.lax.59@liferay.com', 'Welcome Test LAX 59!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1158, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 59', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (158, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.158', -1, -1, '158', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.158', 'liferay.com', 158, '158', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.158', 'liferay.com', 158, '158', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.158', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.158', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.158', '2');

insert into Users_Roles values ('liferay.com.158', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.159', 'liferay.com', sysdate, sysdate, 1159, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax60', 'test.lax.60@liferay.com', 'Welcome Test LAX 60!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1159, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 60', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (159, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.159', -1, -1, '159', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.159', 'liferay.com', 159, '159', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.159', 'liferay.com', 159, '159', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.159', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.159', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.159', '2');

insert into Users_Roles values ('liferay.com.159', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.160', 'liferay.com', sysdate, sysdate, 1160, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax61', 'test.lax.61@liferay.com', 'Welcome Test LAX 61!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1160, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 61', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (160, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.160', -1, -1, '160', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.160', 'liferay.com', 160, '160', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.160', 'liferay.com', 160, '160', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.160', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.160', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.160', '2');

insert into Users_Roles values ('liferay.com.160', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.161', 'liferay.com', sysdate, sysdate, 1161, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax62', 'test.lax.62@liferay.com', 'Welcome Test LAX 62!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1161, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 62', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (161, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.161', -1, -1, '161', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.161', 'liferay.com', 161, '161', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.161', 'liferay.com', 161, '161', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.161', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.161', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.161', '2');

insert into Users_Roles values ('liferay.com.161', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.162', 'liferay.com', sysdate, sysdate, 1162, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax63', 'test.lax.63@liferay.com', 'Welcome Test LAX 63!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1162, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 63', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (162, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.162', -1, -1, '162', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.162', 'liferay.com', 162, '162', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.162', 'liferay.com', 162, '162', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.162', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.162', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.162', '2');

insert into Users_Roles values ('liferay.com.162', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.163', 'liferay.com', sysdate, sysdate, 1163, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax64', 'test.lax.64@liferay.com', 'Welcome Test LAX 64!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1163, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 64', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (163, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.163', -1, -1, '163', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.163', 'liferay.com', 163, '163', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.163', 'liferay.com', 163, '163', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.163', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.163', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.163', '2');

insert into Users_Roles values ('liferay.com.163', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.164', 'liferay.com', sysdate, sysdate, 1164, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax65', 'test.lax.65@liferay.com', 'Welcome Test LAX 65!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1164, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 65', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (164, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.164', -1, -1, '164', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.164', 'liferay.com', 164, '164', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.164', 'liferay.com', 164, '164', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.164', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.164', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.164', '2');

insert into Users_Roles values ('liferay.com.164', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.165', 'liferay.com', sysdate, sysdate, 1165, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax66', 'test.lax.66@liferay.com', 'Welcome Test LAX 66!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1165, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 66', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (165, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.165', -1, -1, '165', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.165', 'liferay.com', 165, '165', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.165', 'liferay.com', 165, '165', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.165', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.165', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.165', '2');

insert into Users_Roles values ('liferay.com.165', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.166', 'liferay.com', sysdate, sysdate, 1166, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax67', 'test.lax.67@liferay.com', 'Welcome Test LAX 67!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1166, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 67', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (166, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.166', -1, -1, '166', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.166', 'liferay.com', 166, '166', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.166', 'liferay.com', 166, '166', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.166', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.166', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.166', '2');

insert into Users_Roles values ('liferay.com.166', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.167', 'liferay.com', sysdate, sysdate, 1167, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax68', 'test.lax.68@liferay.com', 'Welcome Test LAX 68!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1167, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 68', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (167, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.167', -1, -1, '167', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.167', 'liferay.com', 167, '167', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.167', 'liferay.com', 167, '167', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.167', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.167', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.167', '2');

insert into Users_Roles values ('liferay.com.167', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.168', 'liferay.com', sysdate, sysdate, 1168, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax69', 'test.lax.69@liferay.com', 'Welcome Test LAX 69!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1168, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 69', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (168, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.168', -1, -1, '168', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.168', 'liferay.com', 168, '168', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.168', 'liferay.com', 168, '168', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.168', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.168', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.168', '2');

insert into Users_Roles values ('liferay.com.168', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.169', 'liferay.com', sysdate, sysdate, 1169, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax70', 'test.lax.70@liferay.com', 'Welcome Test LAX 70!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1169, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 70', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (169, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.169', -1, -1, '169', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.169', 'liferay.com', 169, '169', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.169', 'liferay.com', 169, '169', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.169', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.169', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.169', '2');

insert into Users_Roles values ('liferay.com.169', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.170', 'liferay.com', sysdate, sysdate, 1170, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax71', 'test.lax.71@liferay.com', 'Welcome Test LAX 71!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1170, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 71', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (170, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.170', -1, -1, '170', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.170', 'liferay.com', 170, '170', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.170', 'liferay.com', 170, '170', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.170', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.170', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.170', '2');

insert into Users_Roles values ('liferay.com.170', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.171', 'liferay.com', sysdate, sysdate, 1171, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax72', 'test.lax.72@liferay.com', 'Welcome Test LAX 72!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1171, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 72', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (171, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.171', -1, -1, '171', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.171', 'liferay.com', 171, '171', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.171', 'liferay.com', 171, '171', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.171', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.171', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.171', '2');

insert into Users_Roles values ('liferay.com.171', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.172', 'liferay.com', sysdate, sysdate, 1172, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax73', 'test.lax.73@liferay.com', 'Welcome Test LAX 73!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1172, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 73', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (172, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.172', -1, -1, '172', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.172', 'liferay.com', 172, '172', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.172', 'liferay.com', 172, '172', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.172', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.172', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.172', '2');

insert into Users_Roles values ('liferay.com.172', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.173', 'liferay.com', sysdate, sysdate, 1173, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax74', 'test.lax.74@liferay.com', 'Welcome Test LAX 74!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1173, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 74', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (173, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.173', -1, -1, '173', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.173', 'liferay.com', 173, '173', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.173', 'liferay.com', 173, '173', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.173', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.173', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.173', '2');

insert into Users_Roles values ('liferay.com.173', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.174', 'liferay.com', sysdate, sysdate, 1174, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax75', 'test.lax.75@liferay.com', 'Welcome Test LAX 75!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1174, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 75', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (174, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.174', -1, -1, '174', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.174', 'liferay.com', 174, '174', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.174', 'liferay.com', 174, '174', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.174', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.174', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.174', '2');

insert into Users_Roles values ('liferay.com.174', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.175', 'liferay.com', sysdate, sysdate, 1175, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax76', 'test.lax.76@liferay.com', 'Welcome Test LAX 76!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1175, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 76', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (175, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.175', -1, -1, '175', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.175', 'liferay.com', 175, '175', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.175', 'liferay.com', 175, '175', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.175', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.175', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.175', '2');

insert into Users_Roles values ('liferay.com.175', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.176', 'liferay.com', sysdate, sysdate, 1176, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax77', 'test.lax.77@liferay.com', 'Welcome Test LAX 77!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1176, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 77', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (176, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.176', -1, -1, '176', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.176', 'liferay.com', 176, '176', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.176', 'liferay.com', 176, '176', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.176', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.176', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.176', '2');

insert into Users_Roles values ('liferay.com.176', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.177', 'liferay.com', sysdate, sysdate, 1177, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax78', 'test.lax.78@liferay.com', 'Welcome Test LAX 78!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1177, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 78', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (177, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.177', -1, -1, '177', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.177', 'liferay.com', 177, '177', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.177', 'liferay.com', 177, '177', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.177', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.177', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.177', '2');

insert into Users_Roles values ('liferay.com.177', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.178', 'liferay.com', sysdate, sysdate, 1178, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax79', 'test.lax.79@liferay.com', 'Welcome Test LAX 79!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1178, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 79', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (178, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.178', -1, -1, '178', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.178', 'liferay.com', 178, '178', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.178', 'liferay.com', 178, '178', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.178', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.178', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.178', '2');

insert into Users_Roles values ('liferay.com.178', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.179', 'liferay.com', sysdate, sysdate, 1179, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax80', 'test.lax.80@liferay.com', 'Welcome Test LAX 80!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1179, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 80', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (179, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.179', -1, -1, '179', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.179', 'liferay.com', 179, '179', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.179', 'liferay.com', 179, '179', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.179', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.179', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.179', '2');

insert into Users_Roles values ('liferay.com.179', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.180', 'liferay.com', sysdate, sysdate, 1180, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax81', 'test.lax.81@liferay.com', 'Welcome Test LAX 81!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1180, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 81', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (180, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.180', -1, -1, '180', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.180', 'liferay.com', 180, '180', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.180', 'liferay.com', 180, '180', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.180', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.180', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.180', '2');

insert into Users_Roles values ('liferay.com.180', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.181', 'liferay.com', sysdate, sysdate, 1181, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax82', 'test.lax.82@liferay.com', 'Welcome Test LAX 82!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1181, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 82', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (181, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.181', -1, -1, '181', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.181', 'liferay.com', 181, '181', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.181', 'liferay.com', 181, '181', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.181', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.181', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.181', '2');

insert into Users_Roles values ('liferay.com.181', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.182', 'liferay.com', sysdate, sysdate, 1182, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax83', 'test.lax.83@liferay.com', 'Welcome Test LAX 83!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1182, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 83', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (182, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.182', -1, -1, '182', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.182', 'liferay.com', 182, '182', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.182', 'liferay.com', 182, '182', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.182', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.182', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.182', '2');

insert into Users_Roles values ('liferay.com.182', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.183', 'liferay.com', sysdate, sysdate, 1183, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax84', 'test.lax.84@liferay.com', 'Welcome Test LAX 84!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1183, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 84', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (183, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.183', -1, -1, '183', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.183', 'liferay.com', 183, '183', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.183', 'liferay.com', 183, '183', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.183', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.183', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.183', '2');

insert into Users_Roles values ('liferay.com.183', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.184', 'liferay.com', sysdate, sysdate, 1184, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax85', 'test.lax.85@liferay.com', 'Welcome Test LAX 85!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1184, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 85', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (184, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.184', -1, -1, '184', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.184', 'liferay.com', 184, '184', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.184', 'liferay.com', 184, '184', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.184', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.184', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.184', '2');

insert into Users_Roles values ('liferay.com.184', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.185', 'liferay.com', sysdate, sysdate, 1185, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax86', 'test.lax.86@liferay.com', 'Welcome Test LAX 86!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1185, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 86', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (185, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.185', -1, -1, '185', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.185', 'liferay.com', 185, '185', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.185', 'liferay.com', 185, '185', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.185', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.185', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.185', '2');

insert into Users_Roles values ('liferay.com.185', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.186', 'liferay.com', sysdate, sysdate, 1186, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax87', 'test.lax.87@liferay.com', 'Welcome Test LAX 87!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1186, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 87', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (186, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.186', -1, -1, '186', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.186', 'liferay.com', 186, '186', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.186', 'liferay.com', 186, '186', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.186', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.186', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.186', '2');

insert into Users_Roles values ('liferay.com.186', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.187', 'liferay.com', sysdate, sysdate, 1187, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax88', 'test.lax.88@liferay.com', 'Welcome Test LAX 88!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1187, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 88', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (187, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.187', -1, -1, '187', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.187', 'liferay.com', 187, '187', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.187', 'liferay.com', 187, '187', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.187', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.187', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.187', '2');

insert into Users_Roles values ('liferay.com.187', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.188', 'liferay.com', sysdate, sysdate, 1188, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax89', 'test.lax.89@liferay.com', 'Welcome Test LAX 89!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1188, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 89', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (188, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.188', -1, -1, '188', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.188', 'liferay.com', 188, '188', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.188', 'liferay.com', 188, '188', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.188', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.188', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.188', '2');

insert into Users_Roles values ('liferay.com.188', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.189', 'liferay.com', sysdate, sysdate, 1189, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax90', 'test.lax.90@liferay.com', 'Welcome Test LAX 90!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1189, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 90', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (189, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.189', -1, -1, '189', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.189', 'liferay.com', 189, '189', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.189', 'liferay.com', 189, '189', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.189', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.189', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.189', '2');

insert into Users_Roles values ('liferay.com.189', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.190', 'liferay.com', sysdate, sysdate, 1190, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax91', 'test.lax.91@liferay.com', 'Welcome Test LAX 91!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1190, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 91', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (190, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.190', -1, -1, '190', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.190', 'liferay.com', 190, '190', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.190', 'liferay.com', 190, '190', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.190', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.190', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.190', '2');

insert into Users_Roles values ('liferay.com.190', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.191', 'liferay.com', sysdate, sysdate, 1191, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax92', 'test.lax.92@liferay.com', 'Welcome Test LAX 92!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1191, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 92', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (191, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.191', -1, -1, '191', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.191', 'liferay.com', 191, '191', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.191', 'liferay.com', 191, '191', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.191', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.191', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.191', '2');

insert into Users_Roles values ('liferay.com.191', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.192', 'liferay.com', sysdate, sysdate, 1192, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax93', 'test.lax.93@liferay.com', 'Welcome Test LAX 93!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1192, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 93', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (192, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.192', -1, -1, '192', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.192', 'liferay.com', 192, '192', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.192', 'liferay.com', 192, '192', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.192', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.192', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.192', '2');

insert into Users_Roles values ('liferay.com.192', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.193', 'liferay.com', sysdate, sysdate, 1193, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax94', 'test.lax.94@liferay.com', 'Welcome Test LAX 94!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1193, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 94', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (193, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.193', -1, -1, '193', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.193', 'liferay.com', 193, '193', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.193', 'liferay.com', 193, '193', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.193', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.193', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.193', '2');

insert into Users_Roles values ('liferay.com.193', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.194', 'liferay.com', sysdate, sysdate, 1194, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax95', 'test.lax.95@liferay.com', 'Welcome Test LAX 95!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1194, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 95', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (194, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.194', -1, -1, '194', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.194', 'liferay.com', 194, '194', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.194', 'liferay.com', 194, '194', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.194', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.194', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.194', '2');

insert into Users_Roles values ('liferay.com.194', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.195', 'liferay.com', sysdate, sysdate, 1195, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax96', 'test.lax.96@liferay.com', 'Welcome Test LAX 96!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1195, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 96', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (195, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.195', -1, -1, '195', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.195', 'liferay.com', 195, '195', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.195', 'liferay.com', 195, '195', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.195', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.195', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.195', '2');

insert into Users_Roles values ('liferay.com.195', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.196', 'liferay.com', sysdate, sysdate, 1196, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax97', 'test.lax.97@liferay.com', 'Welcome Test LAX 97!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1196, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 97', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (196, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.196', -1, -1, '196', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.196', 'liferay.com', 196, '196', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.196', 'liferay.com', 196, '196', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.196', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.196', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.196', '2');

insert into Users_Roles values ('liferay.com.196', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.197', 'liferay.com', sysdate, sysdate, 1197, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax98', 'test.lax.98@liferay.com', 'Welcome Test LAX 98!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1197, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 98', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (197, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.197', -1, -1, '197', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.197', 'liferay.com', 197, '197', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.197', 'liferay.com', 197, '197', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.197', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.197', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.197', '2');

insert into Users_Roles values ('liferay.com.197', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.198', 'liferay.com', sysdate, sysdate, 1198, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax99', 'test.lax.99@liferay.com', 'Welcome Test LAX 99!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1198, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 99', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (198, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.198', -1, -1, '198', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.198', 'liferay.com', 198, '198', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.198', 'liferay.com', 198, '198', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.198', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.198', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.198', '2');

insert into Users_Roles values ('liferay.com.198', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.199', 'liferay.com', sysdate, sysdate, 1199, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lax100', 'test.lax.100@liferay.com', 'Welcome Test LAX 100!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1199, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LAX 100', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (199, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.199', -1, -1, '199', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.199', 'liferay.com', 199, '199', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.199', 'liferay.com', 199, '199', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.199', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.199', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.199', '2');

insert into Users_Roles values ('liferay.com.199', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.200', 'liferay.com', sysdate, sysdate, 1200, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'sfo1', 'test.sfo.1@liferay.com', 'Welcome Test SFO 1!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1200, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'SFO 1', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (200, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.200', -1, -1, '200', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.200', 'liferay.com', 200, '200', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.200', 'liferay.com', 200, '200', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.200', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.200', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.200', '3');

insert into Users_Roles values ('liferay.com.200', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.201', 'liferay.com', sysdate, sysdate, 1201, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'sfo2', 'test.sfo.2@liferay.com', 'Welcome Test SFO 2!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1201, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'SFO 2', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (201, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.201', -1, -1, '201', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.201', 'liferay.com', 201, '201', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.201', 'liferay.com', 201, '201', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.201', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.201', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.201', '3');

insert into Users_Roles values ('liferay.com.201', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.202', 'liferay.com', sysdate, sysdate, 1202, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'sfo3', 'test.sfo.3@liferay.com', 'Welcome Test SFO 3!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1202, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'SFO 3', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (202, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.202', -1, -1, '202', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.202', 'liferay.com', 202, '202', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.202', 'liferay.com', 202, '202', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.202', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.202', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.202', '3');

insert into Users_Roles values ('liferay.com.202', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.203', 'liferay.com', sysdate, sysdate, 1203, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'sfo4', 'test.sfo.4@liferay.com', 'Welcome Test SFO 4!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1203, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'SFO 4', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (203, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.203', -1, -1, '203', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.203', 'liferay.com', 203, '203', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.203', 'liferay.com', 203, '203', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.203', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.203', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.203', '3');

insert into Users_Roles values ('liferay.com.203', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.204', 'liferay.com', sysdate, sysdate, 1204, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'sfo5', 'test.sfo.5@liferay.com', 'Welcome Test SFO 5!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1204, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'SFO 5', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (204, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.204', -1, -1, '204', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.204', 'liferay.com', 204, '204', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.204', 'liferay.com', 204, '204', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.204', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.204', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.204', '3');

insert into Users_Roles values ('liferay.com.204', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.205', 'liferay.com', sysdate, sysdate, 1205, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'sfo6', 'test.sfo.6@liferay.com', 'Welcome Test SFO 6!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1205, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'SFO 6', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (205, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.205', -1, -1, '205', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.205', 'liferay.com', 205, '205', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.205', 'liferay.com', 205, '205', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.205', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.205', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.205', '3');

insert into Users_Roles values ('liferay.com.205', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.206', 'liferay.com', sysdate, sysdate, 1206, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'sfo7', 'test.sfo.7@liferay.com', 'Welcome Test SFO 7!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1206, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'SFO 7', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (206, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.206', -1, -1, '206', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.206', 'liferay.com', 206, '206', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.206', 'liferay.com', 206, '206', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.206', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.206', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.206', '3');

insert into Users_Roles values ('liferay.com.206', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.207', 'liferay.com', sysdate, sysdate, 1207, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'sfo8', 'test.sfo.8@liferay.com', 'Welcome Test SFO 8!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1207, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'SFO 8', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (207, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.207', -1, -1, '207', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.207', 'liferay.com', 207, '207', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.207', 'liferay.com', 207, '207', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.207', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.207', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.207', '3');

insert into Users_Roles values ('liferay.com.207', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.208', 'liferay.com', sysdate, sysdate, 1208, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'sfo9', 'test.sfo.9@liferay.com', 'Welcome Test SFO 9!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1208, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'SFO 9', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (208, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.208', -1, -1, '208', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.208', 'liferay.com', 208, '208', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.208', 'liferay.com', 208, '208', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.208', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.208', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.208', '3');

insert into Users_Roles values ('liferay.com.208', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.209', 'liferay.com', sysdate, sysdate, 1209, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'sfo10', 'test.sfo.10@liferay.com', 'Welcome Test SFO 10!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1209, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'SFO 10', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (209, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.209', -1, -1, '209', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.209', 'liferay.com', 209, '209', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.209', 'liferay.com', 209, '209', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.209', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.209', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.209', '3');

insert into Users_Roles values ('liferay.com.209', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.300', 'liferay.com', sysdate, sysdate, 1300, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'ord1', 'test.ord.1@liferay.com', 'Welcome Test ORD 1!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1300, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'ORD 1', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (300, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.300', -1, -1, '300', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.300', 'liferay.com', 300, '300', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.300', 'liferay.com', 300, '300', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.300', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.300', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.300', '4');

insert into Users_Roles values ('liferay.com.300', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.301', 'liferay.com', sysdate, sysdate, 1301, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'ord2', 'test.ord.2@liferay.com', 'Welcome Test ORD 2!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1301, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'ORD 2', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (301, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.301', -1, -1, '301', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.301', 'liferay.com', 301, '301', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.301', 'liferay.com', 301, '301', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.301', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.301', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.301', '4');

insert into Users_Roles values ('liferay.com.301', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.302', 'liferay.com', sysdate, sysdate, 1302, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'ord3', 'test.ord.3@liferay.com', 'Welcome Test ORD 3!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1302, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'ORD 3', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (302, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.302', -1, -1, '302', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.302', 'liferay.com', 302, '302', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.302', 'liferay.com', 302, '302', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.302', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.302', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.302', '4');

insert into Users_Roles values ('liferay.com.302', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.303', 'liferay.com', sysdate, sysdate, 1303, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'ord4', 'test.ord.4@liferay.com', 'Welcome Test ORD 4!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1303, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'ORD 4', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (303, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.303', -1, -1, '303', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.303', 'liferay.com', 303, '303', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.303', 'liferay.com', 303, '303', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.303', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.303', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.303', '4');

insert into Users_Roles values ('liferay.com.303', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.304', 'liferay.com', sysdate, sysdate, 1304, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'ord5', 'test.ord.5@liferay.com', 'Welcome Test ORD 5!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1304, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'ORD 5', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (304, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.304', -1, -1, '304', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.304', 'liferay.com', 304, '304', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.304', 'liferay.com', 304, '304', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.304', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.304', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.304', '4');

insert into Users_Roles values ('liferay.com.304', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.305', 'liferay.com', sysdate, sysdate, 1305, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'ord6', 'test.ord.6@liferay.com', 'Welcome Test ORD 6!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1305, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'ORD 6', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (305, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.305', -1, -1, '305', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.305', 'liferay.com', 305, '305', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.305', 'liferay.com', 305, '305', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.305', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.305', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.305', '4');

insert into Users_Roles values ('liferay.com.305', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.306', 'liferay.com', sysdate, sysdate, 1306, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'ord7', 'test.ord.7@liferay.com', 'Welcome Test ORD 7!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1306, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'ORD 7', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (306, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.306', -1, -1, '306', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.306', 'liferay.com', 306, '306', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.306', 'liferay.com', 306, '306', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.306', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.306', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.306', '4');

insert into Users_Roles values ('liferay.com.306', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.307', 'liferay.com', sysdate, sysdate, 1307, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'ord8', 'test.ord.8@liferay.com', 'Welcome Test ORD 8!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1307, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'ORD 8', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (307, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.307', -1, -1, '307', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.307', 'liferay.com', 307, '307', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.307', 'liferay.com', 307, '307', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.307', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.307', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.307', '4');

insert into Users_Roles values ('liferay.com.307', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.308', 'liferay.com', sysdate, sysdate, 1308, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'ord9', 'test.ord.9@liferay.com', 'Welcome Test ORD 9!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1308, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'ORD 9', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (308, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.308', -1, -1, '308', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.308', 'liferay.com', 308, '308', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.308', 'liferay.com', 308, '308', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.308', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.308', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.308', '4');

insert into Users_Roles values ('liferay.com.308', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.309', 'liferay.com', sysdate, sysdate, 1309, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'ord10', 'test.ord.10@liferay.com', 'Welcome Test ORD 10!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1309, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'ORD 10', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (309, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.309', -1, -1, '309', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.309', 'liferay.com', 309, '309', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.309', 'liferay.com', 309, '309', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.309', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.309', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.309', '4');

insert into Users_Roles values ('liferay.com.309', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.400', 'liferay.com', sysdate, sysdate, 1400, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'nyc1', 'test.nyc.1@liferay.com', 'Welcome Test NYC 1!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1400, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'NYC 1', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (400, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.400', -1, -1, '400', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.400', 'liferay.com', 400, '400', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.400', 'liferay.com', 400, '400', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.400', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.400', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.400', '5');

insert into Users_Roles values ('liferay.com.400', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.401', 'liferay.com', sysdate, sysdate, 1401, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'nyc2', 'test.nyc.2@liferay.com', 'Welcome Test NYC 2!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1401, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'NYC 2', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (401, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.401', -1, -1, '401', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.401', 'liferay.com', 401, '401', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.401', 'liferay.com', 401, '401', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.401', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.401', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.401', '5');

insert into Users_Roles values ('liferay.com.401', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.402', 'liferay.com', sysdate, sysdate, 1402, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'nyc3', 'test.nyc.3@liferay.com', 'Welcome Test NYC 3!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1402, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'NYC 3', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (402, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.402', -1, -1, '402', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.402', 'liferay.com', 402, '402', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.402', 'liferay.com', 402, '402', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.402', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.402', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.402', '5');

insert into Users_Roles values ('liferay.com.402', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.403', 'liferay.com', sysdate, sysdate, 1403, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'nyc4', 'test.nyc.4@liferay.com', 'Welcome Test NYC 4!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1403, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'NYC 4', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (403, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.403', -1, -1, '403', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.403', 'liferay.com', 403, '403', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.403', 'liferay.com', 403, '403', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.403', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.403', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.403', '5');

insert into Users_Roles values ('liferay.com.403', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.404', 'liferay.com', sysdate, sysdate, 1404, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'nyc5', 'test.nyc.5@liferay.com', 'Welcome Test NYC 5!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1404, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'NYC 5', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (404, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.404', -1, -1, '404', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.404', 'liferay.com', 404, '404', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.404', 'liferay.com', 404, '404', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.404', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.404', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.404', '5');

insert into Users_Roles values ('liferay.com.404', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.405', 'liferay.com', sysdate, sysdate, 1405, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'nyc6', 'test.nyc.6@liferay.com', 'Welcome Test NYC 6!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1405, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'NYC 6', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (405, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.405', -1, -1, '405', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.405', 'liferay.com', 405, '405', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.405', 'liferay.com', 405, '405', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.405', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.405', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.405', '5');

insert into Users_Roles values ('liferay.com.405', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.406', 'liferay.com', sysdate, sysdate, 1406, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'nyc7', 'test.nyc.7@liferay.com', 'Welcome Test NYC 7!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1406, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'NYC 7', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (406, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.406', -1, -1, '406', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.406', 'liferay.com', 406, '406', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.406', 'liferay.com', 406, '406', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.406', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.406', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.406', '5');

insert into Users_Roles values ('liferay.com.406', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.407', 'liferay.com', sysdate, sysdate, 1407, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'nyc8', 'test.nyc.8@liferay.com', 'Welcome Test NYC 8!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1407, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'NYC 8', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (407, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.407', -1, -1, '407', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.407', 'liferay.com', 407, '407', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.407', 'liferay.com', 407, '407', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.407', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.407', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.407', '5');

insert into Users_Roles values ('liferay.com.407', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.408', 'liferay.com', sysdate, sysdate, 1408, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'nyc9', 'test.nyc.9@liferay.com', 'Welcome Test NYC 9!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1408, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'NYC 9', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (408, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.408', -1, -1, '408', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.408', 'liferay.com', 408, '408', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.408', 'liferay.com', 408, '408', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.408', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.408', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.408', '5');

insert into Users_Roles values ('liferay.com.408', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.409', 'liferay.com', sysdate, sysdate, 1409, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'nyc10', 'test.nyc.10@liferay.com', 'Welcome Test NYC 10!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1409, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'NYC 10', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (409, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.409', -1, -1, '409', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.409', 'liferay.com', 409, '409', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.409', 'liferay.com', 409, '409', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.409', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.409', '1');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.409', '5');

insert into Users_Roles values ('liferay.com.409', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.500', 'liferay.com', sysdate, sysdate, 1500, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lon1', 'test.lon.1@liferay.com', 'Welcome Test LON 1!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1500, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LON 1', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (500, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.500', -1, -1, '500', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.500', 'liferay.com', 500, '500', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.500', 'liferay.com', 500, '500', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.500', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.500', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.500', '7');

insert into Users_Roles values ('liferay.com.500', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.501', 'liferay.com', sysdate, sysdate, 1501, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lon2', 'test.lon.2@liferay.com', 'Welcome Test LON 2!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1501, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LON 2', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (501, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.501', -1, -1, '501', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.501', 'liferay.com', 501, '501', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.501', 'liferay.com', 501, '501', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.501', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.501', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.501', '7');

insert into Users_Roles values ('liferay.com.501', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.502', 'liferay.com', sysdate, sysdate, 1502, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lon3', 'test.lon.3@liferay.com', 'Welcome Test LON 3!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1502, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LON 3', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (502, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.502', -1, -1, '502', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.502', 'liferay.com', 502, '502', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.502', 'liferay.com', 502, '502', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.502', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.502', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.502', '7');

insert into Users_Roles values ('liferay.com.502', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.503', 'liferay.com', sysdate, sysdate, 1503, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lon4', 'test.lon.4@liferay.com', 'Welcome Test LON 4!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1503, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LON 4', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (503, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.503', -1, -1, '503', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.503', 'liferay.com', 503, '503', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.503', 'liferay.com', 503, '503', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.503', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.503', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.503', '7');

insert into Users_Roles values ('liferay.com.503', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.504', 'liferay.com', sysdate, sysdate, 1504, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lon5', 'test.lon.5@liferay.com', 'Welcome Test LON 5!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1504, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LON 5', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (504, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.504', -1, -1, '504', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.504', 'liferay.com', 504, '504', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.504', 'liferay.com', 504, '504', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.504', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.504', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.504', '7');

insert into Users_Roles values ('liferay.com.504', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.505', 'liferay.com', sysdate, sysdate, 1505, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lon6', 'test.lon.6@liferay.com', 'Welcome Test LON 6!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1505, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LON 6', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (505, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.505', -1, -1, '505', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.505', 'liferay.com', 505, '505', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.505', 'liferay.com', 505, '505', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.505', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.505', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.505', '7');

insert into Users_Roles values ('liferay.com.505', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.506', 'liferay.com', sysdate, sysdate, 1506, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lon7', 'test.lon.7@liferay.com', 'Welcome Test LON 7!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1506, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LON 7', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (506, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.506', -1, -1, '506', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.506', 'liferay.com', 506, '506', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.506', 'liferay.com', 506, '506', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.506', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.506', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.506', '7');

insert into Users_Roles values ('liferay.com.506', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.507', 'liferay.com', sysdate, sysdate, 1507, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lon8', 'test.lon.8@liferay.com', 'Welcome Test LON 8!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1507, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LON 8', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (507, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.507', -1, -1, '507', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.507', 'liferay.com', 507, '507', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.507', 'liferay.com', 507, '507', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.507', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.507', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.507', '7');

insert into Users_Roles values ('liferay.com.507', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.508', 'liferay.com', sysdate, sysdate, 1508, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lon9', 'test.lon.9@liferay.com', 'Welcome Test LON 9!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1508, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LON 9', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (508, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.508', -1, -1, '508', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.508', 'liferay.com', 508, '508', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.508', 'liferay.com', 508, '508', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.508', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.508', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.508', '7');

insert into Users_Roles values ('liferay.com.508', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.509', 'liferay.com', sysdate, sysdate, 1509, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'lon10', 'test.lon.10@liferay.com', 'Welcome Test LON 10!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1509, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'LON 10', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (509, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.509', -1, -1, '509', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.509', 'liferay.com', 509, '509', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.509', 'liferay.com', 509, '509', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.509', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.509', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.509', '7');

insert into Users_Roles values ('liferay.com.509', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.600', 'liferay.com', sysdate, sysdate, 1600, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'mad1', 'test.mad.1@liferay.com', 'Welcome Test MAD 1!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1600, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'MAD 1', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (600, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.600', -1, -1, '600', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.600', 'liferay.com', 600, '600', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.600', 'liferay.com', 600, '600', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.600', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.600', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.600', '8');

insert into Users_Roles values ('liferay.com.600', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.601', 'liferay.com', sysdate, sysdate, 1601, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'mad2', 'test.mad.2@liferay.com', 'Welcome Test MAD 2!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1601, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'MAD 2', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (601, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.601', -1, -1, '601', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.601', 'liferay.com', 601, '601', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.601', 'liferay.com', 601, '601', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.601', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.601', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.601', '8');

insert into Users_Roles values ('liferay.com.601', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.602', 'liferay.com', sysdate, sysdate, 1602, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'mad3', 'test.mad.3@liferay.com', 'Welcome Test MAD 3!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1602, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'MAD 3', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (602, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.602', -1, -1, '602', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.602', 'liferay.com', 602, '602', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.602', 'liferay.com', 602, '602', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.602', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.602', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.602', '8');

insert into Users_Roles values ('liferay.com.602', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.603', 'liferay.com', sysdate, sysdate, 1603, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'mad4', 'test.mad.4@liferay.com', 'Welcome Test MAD 4!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1603, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'MAD 4', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (603, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.603', -1, -1, '603', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.603', 'liferay.com', 603, '603', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.603', 'liferay.com', 603, '603', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.603', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.603', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.603', '8');

insert into Users_Roles values ('liferay.com.603', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.604', 'liferay.com', sysdate, sysdate, 1604, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'mad5', 'test.mad.5@liferay.com', 'Welcome Test MAD 5!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1604, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'MAD 5', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (604, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.604', -1, -1, '604', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.604', 'liferay.com', 604, '604', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.604', 'liferay.com', 604, '604', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.604', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.604', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.604', '8');

insert into Users_Roles values ('liferay.com.604', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.605', 'liferay.com', sysdate, sysdate, 1605, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'mad6', 'test.mad.6@liferay.com', 'Welcome Test MAD 6!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1605, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'MAD 6', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (605, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.605', -1, -1, '605', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.605', 'liferay.com', 605, '605', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.605', 'liferay.com', 605, '605', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.605', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.605', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.605', '8');

insert into Users_Roles values ('liferay.com.605', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.606', 'liferay.com', sysdate, sysdate, 1606, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'mad7', 'test.mad.7@liferay.com', 'Welcome Test MAD 7!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1606, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'MAD 7', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (606, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.606', -1, -1, '606', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.606', 'liferay.com', 606, '606', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.606', 'liferay.com', 606, '606', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.606', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.606', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.606', '8');

insert into Users_Roles values ('liferay.com.606', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.607', 'liferay.com', sysdate, sysdate, 1607, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'mad8', 'test.mad.8@liferay.com', 'Welcome Test MAD 8!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1607, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'MAD 8', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (607, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.607', -1, -1, '607', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.607', 'liferay.com', 607, '607', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.607', 'liferay.com', 607, '607', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.607', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.607', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.607', '8');

insert into Users_Roles values ('liferay.com.607', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.608', 'liferay.com', sysdate, sysdate, 1608, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'mad9', 'test.mad.9@liferay.com', 'Welcome Test MAD 9!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1608, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'MAD 9', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (608, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.608', -1, -1, '608', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.608', 'liferay.com', 608, '608', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.608', 'liferay.com', 608, '608', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.608', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.608', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.608', '8');

insert into Users_Roles values ('liferay.com.608', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.609', 'liferay.com', sysdate, sysdate, 1609, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'mad10', 'test.mad.10@liferay.com', 'Welcome Test MAD 10!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1609, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'MAD 10', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (609, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.609', -1, -1, '609', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.609', 'liferay.com', 609, '609', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.609', 'liferay.com', 609, '609', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.609', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.609', '6');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.609', '8');

insert into Users_Roles values ('liferay.com.609', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.700', 'liferay.com', sysdate, sysdate, 1700, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'hkg1', 'test.hkg.1@liferay.com', 'Welcome Test HKG 1!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1700, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'HKG 1', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (700, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.700', -1, -1, '700', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.700', 'liferay.com', 700, '700', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.700', 'liferay.com', 700, '700', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.700', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.700', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.700', '10');

insert into Users_Roles values ('liferay.com.700', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.701', 'liferay.com', sysdate, sysdate, 1701, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'hkg2', 'test.hkg.2@liferay.com', 'Welcome Test HKG 2!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1701, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'HKG 2', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (701, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.701', -1, -1, '701', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.701', 'liferay.com', 701, '701', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.701', 'liferay.com', 701, '701', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.701', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.701', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.701', '10');

insert into Users_Roles values ('liferay.com.701', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.702', 'liferay.com', sysdate, sysdate, 1702, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'hkg3', 'test.hkg.3@liferay.com', 'Welcome Test HKG 3!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1702, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'HKG 3', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (702, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.702', -1, -1, '702', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.702', 'liferay.com', 702, '702', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.702', 'liferay.com', 702, '702', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.702', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.702', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.702', '10');

insert into Users_Roles values ('liferay.com.702', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.703', 'liferay.com', sysdate, sysdate, 1703, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'hkg4', 'test.hkg.4@liferay.com', 'Welcome Test HKG 4!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1703, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'HKG 4', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (703, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.703', -1, -1, '703', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.703', 'liferay.com', 703, '703', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.703', 'liferay.com', 703, '703', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.703', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.703', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.703', '10');

insert into Users_Roles values ('liferay.com.703', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.704', 'liferay.com', sysdate, sysdate, 1704, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'hkg5', 'test.hkg.5@liferay.com', 'Welcome Test HKG 5!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1704, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'HKG 5', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (704, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.704', -1, -1, '704', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.704', 'liferay.com', 704, '704', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.704', 'liferay.com', 704, '704', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.704', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.704', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.704', '10');

insert into Users_Roles values ('liferay.com.704', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.705', 'liferay.com', sysdate, sysdate, 1705, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'hkg6', 'test.hkg.6@liferay.com', 'Welcome Test HKG 6!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1705, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'HKG 6', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (705, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.705', -1, -1, '705', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.705', 'liferay.com', 705, '705', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.705', 'liferay.com', 705, '705', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.705', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.705', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.705', '10');

insert into Users_Roles values ('liferay.com.705', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.706', 'liferay.com', sysdate, sysdate, 1706, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'hkg7', 'test.hkg.7@liferay.com', 'Welcome Test HKG 7!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1706, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'HKG 7', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (706, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.706', -1, -1, '706', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.706', 'liferay.com', 706, '706', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.706', 'liferay.com', 706, '706', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.706', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.706', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.706', '10');

insert into Users_Roles values ('liferay.com.706', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.707', 'liferay.com', sysdate, sysdate, 1707, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'hkg8', 'test.hkg.8@liferay.com', 'Welcome Test HKG 8!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1707, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'HKG 8', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (707, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.707', -1, -1, '707', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.707', 'liferay.com', 707, '707', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.707', 'liferay.com', 707, '707', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.707', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.707', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.707', '10');

insert into Users_Roles values ('liferay.com.707', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.708', 'liferay.com', sysdate, sysdate, 1708, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'hkg9', 'test.hkg.9@liferay.com', 'Welcome Test HKG 9!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1708, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'HKG 9', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (708, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.708', -1, -1, '708', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.708', 'liferay.com', 708, '708', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.708', 'liferay.com', 708, '708', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.708', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.708', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.708', '10');

insert into Users_Roles values ('liferay.com.708', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.709', 'liferay.com', sysdate, sysdate, 1709, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'hkg10', 'test.hkg.10@liferay.com', 'Welcome Test HKG 10!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1709, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'HKG 10', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (709, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.709', -1, -1, '709', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.709', 'liferay.com', 709, '709', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.709', 'liferay.com', 709, '709', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.709', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.709', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.709', '10');

insert into Users_Roles values ('liferay.com.709', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.800', 'liferay.com', sysdate, sysdate, 1800, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'pvg1', 'test.pvg.1@liferay.com', 'Welcome Test PVG 1!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1800, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'PVG 1', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (800, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.800', -1, -1, '800', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.800', 'liferay.com', 800, '800', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.800', 'liferay.com', 800, '800', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.800', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.800', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.800', '11');

insert into Users_Roles values ('liferay.com.800', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.801', 'liferay.com', sysdate, sysdate, 1801, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'pvg2', 'test.pvg.2@liferay.com', 'Welcome Test PVG 2!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1801, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'PVG 2', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (801, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.801', -1, -1, '801', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.801', 'liferay.com', 801, '801', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.801', 'liferay.com', 801, '801', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.801', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.801', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.801', '11');

insert into Users_Roles values ('liferay.com.801', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.802', 'liferay.com', sysdate, sysdate, 1802, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'pvg3', 'test.pvg.3@liferay.com', 'Welcome Test PVG 3!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1802, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'PVG 3', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (802, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.802', -1, -1, '802', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.802', 'liferay.com', 802, '802', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.802', 'liferay.com', 802, '802', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.802', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.802', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.802', '11');

insert into Users_Roles values ('liferay.com.802', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.803', 'liferay.com', sysdate, sysdate, 1803, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'pvg4', 'test.pvg.4@liferay.com', 'Welcome Test PVG 4!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1803, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'PVG 4', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (803, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.803', -1, -1, '803', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.803', 'liferay.com', 803, '803', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.803', 'liferay.com', 803, '803', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.803', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.803', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.803', '11');

insert into Users_Roles values ('liferay.com.803', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.804', 'liferay.com', sysdate, sysdate, 1804, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'pvg5', 'test.pvg.5@liferay.com', 'Welcome Test PVG 5!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1804, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'PVG 5', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (804, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.804', -1, -1, '804', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.804', 'liferay.com', 804, '804', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.804', 'liferay.com', 804, '804', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.804', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.804', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.804', '11');

insert into Users_Roles values ('liferay.com.804', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.805', 'liferay.com', sysdate, sysdate, 1805, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'pvg6', 'test.pvg.6@liferay.com', 'Welcome Test PVG 6!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1805, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'PVG 6', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (805, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.805', -1, -1, '805', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.805', 'liferay.com', 805, '805', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.805', 'liferay.com', 805, '805', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.805', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.805', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.805', '11');

insert into Users_Roles values ('liferay.com.805', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.806', 'liferay.com', sysdate, sysdate, 1806, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'pvg7', 'test.pvg.7@liferay.com', 'Welcome Test PVG 7!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1806, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'PVG 7', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (806, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.806', -1, -1, '806', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.806', 'liferay.com', 806, '806', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.806', 'liferay.com', 806, '806', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.806', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.806', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.806', '11');

insert into Users_Roles values ('liferay.com.806', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.807', 'liferay.com', sysdate, sysdate, 1807, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'pvg8', 'test.pvg.8@liferay.com', 'Welcome Test PVG 8!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1807, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'PVG 8', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (807, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.807', -1, -1, '807', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.807', 'liferay.com', 807, '807', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.807', 'liferay.com', 807, '807', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.807', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.807', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.807', '11');

insert into Users_Roles values ('liferay.com.807', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.808', 'liferay.com', sysdate, sysdate, 1808, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'pvg9', 'test.pvg.9@liferay.com', 'Welcome Test PVG 9!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1808, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'PVG 9', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (808, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.808', -1, -1, '808', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.808', 'liferay.com', 808, '808', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.808', 'liferay.com', 808, '808', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.808', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.808', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.808', '11');

insert into Users_Roles values ('liferay.com.808', '4');

insert into User_ (userId, companyId, createDate, modifiedDate, contactId, password_, passwordEncrypted, passwordReset, screenName, emailAddress, greeting, loginDate, failedLoginAttempts, agreedToTermsOfUse, active_) values ('liferay.com.809', 'liferay.com', sysdate, sysdate, 1809, 'qUqP5cyxm6YcTAhz05Hph5gvu9M=', 1, 0, 'pvg10', 'test.pvg.10@liferay.com', 'Welcome Test PVG 10!', sysdate, 0, 1, 1);
insert into Contact_ (contactId, companyId, userId, userName, createDate, modifiedDate, accountId, parentContactId, firstName, middleName, lastName, nickName, male, birthday) values (1809, 'liferay.com', 'liferay.com.1', 'Joe Bloggs', sysdate, sysdate, 'liferay.com', -1, 'Test', '', 'PVG 10', '', 1, to_date('1970-01-01 00:00:00','YYYY-MM-DD HH24:MI:SS'));

insert into Group_ (groupId, companyId, className, classPK, parentGroupId, liveGroupId, name, friendlyURL, active_) values (809, 'liferay.com', 'com.liferay.portal.model.User', 'liferay.com.809', -1, -1, '809', '', 1);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PRI.809', 'liferay.com', 809, '809', 1, 0, 'classic', '01', 0.0);
insert into LayoutSet (ownerId, companyId, groupId, userId, privateLayout, logo, themeId, colorSchemeId, pageCount) values ('PUB.809', 'liferay.com', 809, '809', 0, 0, 'classic', '01', 0.0);

insert into Users_Groups values ('liferay.com.809', 3);

insert into Users_Orgs (userId, organizationId) values ('liferay.com.809', '9');
insert into Users_Orgs (userId, organizationId) values ('liferay.com.809', '11');

insert into Users_Roles values ('liferay.com.809', '4');





insert into Release_ (releaseId, createDate, modifiedDate, buildNumber, verified) values ('1', sysdate, sysdate, 3901, 1);





commit;


create index IX_93D5AD4E on Address (companyId);
create index IX_4F4BDD05 on Address (companyId, className);
create index IX_DB84CC7E on Address (companyId, className, classPK);
create index IX_845FAC7D on Address (companyId, className, classPK, mailing);
create index IX_E47E614F on Address (companyId, className, classPK, primary_);
create index IX_5BC8B0D4 on Address (userId);

create index IX_C49DD10C on BlogsCategory (parentCategoryId);

create index IX_B0608DF4 on BlogsEntry (categoryId);
create index IX_72EF6041 on BlogsEntry (companyId);
create index IX_81A50303 on BlogsEntry (groupId);

create index IX_443BDC38 on BookmarksEntry (folderId);

create index IX_7F703619 on BookmarksFolder (groupId);
create index IX_967799C0 on BookmarksFolder (groupId, parentFolderId);

create index IX_12EE4898 on CalEvent (groupId);
create index IX_4FDDD2BF on CalEvent (groupId, repeating);
create index IX_FCD7C63D on CalEvent (groupId, type_);

create index IX_66D496A3 on Contact_ (companyId);

create index IX_25D734CD on Country (active_);

create index IX_24A846D1 on DLFileEntry (folderId);

create index IX_40B56512 on DLFileRank (folderId, name);
create index IX_EED06670 on DLFileRank (userId);

create index IX_E56EC6AD on DLFileShortcut (folderId);
create index IX_CA2708A2 on DLFileShortcut (toFolderId, toName);

create index IX_9CD91DB6 on DLFileVersion (folderId, name);

create index IX_A74DB14C on DLFolder (companyId);
create index IX_F2EA1ACE on DLFolder (groupId);
create index IX_49C37475 on DLFolder (groupId, parentFolderId);
create index IX_51556082 on DLFolder (parentFolderId, name);

create index IX_1BB072CA on EmailAddress (companyId);
create index IX_A9801209 on EmailAddress (companyId, className);
create index IX_C161FBFA on EmailAddress (companyId, className, classPK);
create index IX_F5B365CB on EmailAddress (companyId, className, classPK, primary_);
create index IX_7B43CD8 on EmailAddress (userId);

create index IX_5849ABF2 on Group_ (companyId, className, classPK);
create index IX_5BDDB872 on Group_ (companyId, friendlyURL);
create index IX_5AA68501 on Group_ (companyId, name);
create index IX_16218A38 on Group_ (liveGroupId);

create index LIFERAY_001 on Groups_Permissions (permissionId);

create index IX_206498F8 on IGFolder (groupId);
create index IX_1A605E9F on IGFolder (groupId, parentFolderId);

create index IX_4438CA80 on IGImage (folderId);

create index IX_DFF98523 on JournalArticle (companyId);
create index IX_29BD22DA on JournalArticle (companyId, groupId, articleId);
create index IX_A1D78A45 on JournalArticle (companyId, groupId, articleId, approved);
create index IX_A0C28B17 on JournalArticle (companyId, groupId, structureId);
create index IX_EC743CD0 on JournalArticle (companyId, groupId, templateId);
create index IX_9356F865 on JournalArticle (groupId);

create index IX_4D73E06F on JournalContentSearch (companyId, groupId, articleId);
create index IX_972C13BA on JournalContentSearch (groupId);
create index IX_ABEEA675 on JournalContentSearch (layoutId, ownerId);
create index IX_F09DD5EE on JournalContentSearch (ownerId);
create index IX_4A642025 on JournalContentSearch (ownerId, groupId, articleId);

create index IX_47EF5658 on JournalStructure (companyId, structureId);
create index IX_B97F5608 on JournalStructure (groupId);

create index IX_AB3E5F05 on JournalTemplate (companyId, groupId, structureId);
create index IX_D7A3867A on JournalTemplate (companyId, templateId);
create index IX_77923653 on JournalTemplate (groupId);

create index IX_1A0B984E on Layout (ownerId);
create index IX_E230D266 on Layout (ownerId, friendlyURL);
create index IX_9AF212B1 on Layout (ownerId, parentLayoutId);

create index IX_A34FBC19 on LayoutSet (companyId, virtualHost);
create index IX_A40B8BEC on LayoutSet (groupId);

create index IX_2932DD37 on ListType (type_);

create index IX_69951A25 on MBBan (banUserId);
create index IX_5C3FF12A on MBBan (groupId);
create index IX_8ABC4E3B on MBBan (groupId, banUserId);
create index IX_48814BBA on MBBan (userId);

create index IX_BC735DCF on MBCategory (companyId);
create index IX_BB870C11 on MBCategory (groupId);
create index IX_ED292508 on MBCategory (groupId, parentCategoryId);

create index IX_B628DAD3 on MBDiscussion (className, classPK);

create index IX_3C865EE5 on MBMessage (categoryId);
create index IX_138C7F1E on MBMessage (categoryId, threadId);
create index IX_75B95071 on MBMessage (threadId);
create index IX_A7038CD7 on MBMessage (threadId, parentMessageId);

create index IX_EE1CA456 on MBMessageFlag (topicId);
create index IX_93BF5C9C on MBMessageFlag (topicId, messageId);
create index IX_E1F34690 on MBMessageFlag (topicId, userId);
create index IX_7B2917BE on MBMessageFlag (userId);

create index IX_A00A898F on MBStatsUser (groupId);
create index IX_FAB5A88B on MBStatsUser (groupId, messageCount);
create index IX_847F92B5 on MBStatsUser (userId);

create index IX_CB854772 on MBThread (categoryId);

create index IX_A425F71A on OrgGroupPermission (groupId);
create index IX_6C53DA4E on OrgGroupPermission (permissionId);

create index IX_4A527DD3 on OrgGroupRole (groupId);
create index IX_AB044D1C on OrgGroupRole (roleId);

create index IX_6AF0D434 on OrgLabor (organizationId);

create index IX_834BCEB6 on Organization_ (companyId);
create index IX_E301BDF5 on Organization_ (companyId, name);
create index IX_418E4522 on Organization_ (companyId, parentOrganizationId);

create index IX_326F75BD on PasswordTracker (userId);

create index IX_4D19C2B8 on Permission_ (actionId, resourceId);
create index IX_F090C113 on Permission_ (resourceId);

create index IX_9F704A14 on Phone (companyId);
create index IX_139DA87F on Phone (companyId, className);
create index IX_A074A44 on Phone (companyId, className, classPK);
create index IX_2CAADF95 on Phone (companyId, className, classPK, primary_);
create index IX_F202B9CE on Phone (userId);

create index IX_B9746445 on PluginSetting (companyId);
create index IX_7171B2E8 on PluginSetting (companyId, pluginId, pluginType);

create index IX_EC370F10 on PollsChoice (questionId);

create index IX_9FF342EA on PollsQuestion (groupId);

create index IX_12112599 on PollsVote (questionId);
create index IX_FE3220E9 on PollsVote (questionId, choiceId);

create index IX_80CC9508 on Portlet (companyId);

create index IX_8B1E639D on PortletPreferences (layoutId);
create index IX_4A6293E1 on PortletPreferences (layoutId, ownerId);
create index IX_3EAB5A5A on PortletPreferences (ownerId);
create index IX_8E6DA3A1 on PortletPreferences (portletId);

create index IX_EA9B85B2 on RatingsEntry (className, classPK);
create index IX_9941DAEC on RatingsEntry (userId, className, classPK);

create index IX_8366321F on RatingsStats (className, classPK);

create index IX_2D9A426F on Region (active_);
create index IX_16D87CA7 on Region (countryId);
create index IX_11FB3E42 on Region (countryId, active_);

create index IX_717FDD47 on ResourceCode (companyId);
create index IX_A32C097E on ResourceCode (companyId, name, scope);
create index IX_AACAFF40 on ResourceCode (name);

create index IX_2578FBD3 on Resource_ (codeId);
create index IX_67DE7856 on Resource_ (codeId, primKey);

create index IX_449A10B9 on Role_ (companyId);
create index IX_ED284C69 on Role_ (companyId, className, classPK);
create index IX_EBC931B8 on Role_ (companyId, name);

create index LIFERAY_002 on Roles_Permissions (permissionId);

create index IX_C98C0D78 on SCFrameworkVersion (companyId);
create index IX_272991FA on SCFrameworkVersion (groupId);
create index IX_6E1764F on SCFrameworkVersion (groupId, active_);

create index IX_1C841592 on SCLicense (active_);
create index IX_5327BB79 on SCLicense (active_, recommended);

create index IX_5D25244F on SCProductEntry (companyId);
create index IX_72F87291 on SCProductEntry (groupId);
create index IX_98E6A9CB on SCProductEntry (groupId, userId);

create index IX_8377A211 on SCProductVersion (productEntryId);

create index IX_C28B41DC on ShoppingCart (groupId);
create index IX_54101CC8 on ShoppingCart (userId);

create index IX_5F615D3E on ShoppingCategory (groupId);
create index IX_1E6464F5 on ShoppingCategory (groupId, parentCategoryId);

create index IX_3251AF16 on ShoppingCoupon (groupId);

create index IX_C8EACF2E on ShoppingItem (categoryId);
create index IX_1C717CA6 on ShoppingItem (companyId, sku);

create index IX_6D5F9B87 on ShoppingItemField (itemId);

create index IX_EA6FD516 on ShoppingItemPrice (itemId);

create index IX_119B5630 on ShoppingOrder (groupId, userId, ppPaymentStatus);

create index IX_B5F82C7A on ShoppingOrderItem (orderId);

create index IX_E00DE435 on Subscription (companyId, className, classPK);
create index IX_FC7B066F on Subscription (companyId, userId, className, classPK);
create index IX_54243AFD on Subscription (userId);

create index IX_92B431ED on TagsAsset (className, classPK);

create index IX_10563688 on TagsEntry (companyId, name);

create index IX_C134234 on TagsProperty (companyId);
create index IX_EB974D08 on TagsProperty (companyId, key_);
create index IX_5200A629 on TagsProperty (entryId);
create index IX_F505253D on TagsProperty (entryId, key_);

create index IX_524FEFCE on UserGroup (companyId);
create index IX_23EAD0D on UserGroup (companyId, name);
create index IX_69771487 on UserGroup (companyId, parentUserGroupId);

create index IX_1B988D7A on UserGroupRole (groupId);
create index IX_887A2C95 on UserGroupRole (roleId);
create index IX_887BE56A on UserGroupRole (userId);
create index IX_4D040680 on UserGroupRole (userId, groupId);

create index IX_E60EA987 on UserIdMapper (userId);

create index IX_29BA1CF5 on UserTracker (companyId);
create index IX_E4EFBA8D on UserTracker (userId);

create index IX_14D8BCC0 on UserTrackerPath (userTrackerId);

create index IX_3A1E834E on User_ (companyId);
create index IX_615E9F7A on User_ (companyId, emailAddress);
create index IX_765A87C6 on User_ (companyId, password_);
create index IX_9782AD88 on User_ (companyId, userId);
create index IX_5ADBE171 on User_ (contactId);
create index IX_480DC765 on User_ (screenName);

create index LIFERAY_003 on Users_Permissions (permissionId);

create index IX_96F07007 on Website (companyId);
create index IX_66A45CAC on Website (companyId, className);
create index IX_5233F8B7 on Website (companyId, className, classPK);
create index IX_82125A48 on Website (companyId, className, classPK, primary_);
create index IX_F75690BB on Website (userId);

create index IX_5D6FE3F0 on WikiNode (companyId);
create index IX_B480A672 on WikiNode (groupId);

create index IX_C8A9C476 on WikiPage (nodeId);
create index IX_E7F635CA on WikiPage (nodeId, head);
create index IX_997EEDD2 on WikiPage (nodeId, title);
create index IX_E745EA26 on WikiPage (nodeId, title, head);



quit