drop database lportal;
create database lportal;
connect to lportal;
create table Account_ (
	accountId varchar(75) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentAccountId varchar(75),
	name varchar(75),
	legalName varchar(75),
	legalId varchar(75),
	legalType varchar(75),
	sicCode varchar(75),
	tickerSymbol varchar(75),
	industry varchar(75),
	type_ varchar(75),
	size_ varchar(75)
);

create table Address (
	addressId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	street1 varchar(75),
	street2 varchar(75),
	street3 varchar(75),
	city varchar(75),
	zip varchar(75),
	regionId varchar(75),
	countryId varchar(75),
	typeId integer,
	mailing smallint,
	primary_ smallint
);

create table BlogsCategory (
	categoryId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentCategoryId bigint,
	name varchar(75),
	description varchar(500)
);

create table BlogsEntry (
	entryId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	categoryId bigint,
	title varchar(75),
	content clob,
	displayDate timestamp
);

create table BookmarksEntry (
	entryId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	createDate timestamp,
	modifiedDate timestamp,
	folderId bigint,
	name varchar(75),
	url varchar(500),
	comments varchar(500),
	visits integer
);

create table BookmarksFolder (
	folderId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	createDate timestamp,
	modifiedDate timestamp,
	parentFolderId bigint,
	name varchar(75),
	description varchar(500)
);

create table CalEvent (
	eventId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	title varchar(75),
	description varchar(500),
	startDate timestamp,
	endDate timestamp,
	durationHour integer,
	durationMinute integer,
	allDay smallint,
	timeZoneSensitive smallint,
	type_ varchar(75),
	repeating smallint,
	recurrence clob,
	remindBy varchar(75),
	firstReminder integer,
	secondReminder integer
);

create table Company (
	companyId varchar(75) not null primary key,
	key_ clob,
	portalURL varchar(75),
	homeURL varchar(75),
	mx varchar(75)
);

create table Contact_ (
	contactId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	accountId varchar(75),
	parentContactId bigint,
	firstName varchar(75),
	middleName varchar(75),
	lastName varchar(75),
	nickName varchar(75),
	prefixId integer,
	suffixId integer,
	male smallint,
	birthday timestamp,
	smsSn varchar(75),
	aimSn varchar(75),
	icqSn varchar(75),
	jabberSn varchar(75),
	msnSn varchar(75),
	skypeSn varchar(75),
	ymSn varchar(75),
	employeeStatusId varchar(75),
	employeeNumber varchar(75),
	jobTitle varchar(75),
	jobClass varchar(75),
	hoursOfOperation varchar(75)
);

create table Counter (
	name varchar(75) not null primary key,
	currentId bigint
);

create table Country (
	countryId varchar(75) not null primary key,
	name varchar(75),
	a2 varchar(75),
	a3 varchar(75),
	number_ varchar(75),
	idd_ varchar(75),
	active_ smallint
);

create table CyrusUser (
	userId varchar(75) not null primary key,
	password_ varchar(75) not null
);

create table CyrusVirtual (
	emailAddress varchar(75) not null primary key,
	userId varchar(75) not null
);

create table DataTracker (
	dataTrackerId varchar(75) not null primary key,
	companyId varchar(75) not null,
	createdOn timestamp,
	createdByUserId varchar(75),
	createdByUserName varchar(75),
	updatedOn timestamp,
	updatedBy varchar(75),
	className varchar(75),
	classPK varchar(75),
	active_ smallint
);

create table DLFileEntry (
	folderId varchar(75) not null,
	name varchar(100) not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	versionUserId varchar(75),
	versionUserName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	title varchar(100),
	description varchar(500),
	version double,
	size_ integer,
	readCount integer,
	extraSettings clob,
	primary key (folderId, name)
);

create table DLFileRank (
	companyId varchar(75) not null,
	userId varchar(75) not null,
	folderId varchar(75) not null,
	name varchar(100) not null,
	createDate timestamp,
	primary key (companyId, userId, folderId, name)
);

create table DLFileShortcut (
	fileShortcutId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	folderId varchar(75),
	toFolderId varchar(75),
	toName varchar(75)
);

create table DLFileVersion (
	folderId varchar(75) not null,
	name varchar(100) not null,
	version double not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	size_ integer,
	primary key (folderId, name, version)
);

create table DLFolder (
	folderId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentFolderId varchar(75),
	name varchar(100),
	description varchar(500),
	lastPostDate timestamp
);

create table EmailAddress (
	emailAddressId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	address varchar(75),
	typeId integer,
	primary_ smallint
);

create table Group_ (
	groupId bigint not null primary key,
	companyId varchar(75) not null,
	creatorUserId varchar(75),
	className varchar(75),
	classPK varchar(75),
	parentGroupId bigint,
	liveGroupId bigint,
	name varchar(75),
	description varchar(500),
	type_ varchar(75),
	friendlyURL varchar(75),
	active_ smallint
);

create table Groups_Orgs (
	groupId bigint not null,
	organizationId varchar(75) not null,
	primary key (groupId, organizationId)
);

create table Groups_Permissions (
	groupId bigint not null,
	permissionId bigint not null,
	primary key (groupId, permissionId)
);

create table Groups_Roles (
	groupId bigint not null,
	roleId varchar(75) not null,
	primary key (groupId, roleId)
);

create table Groups_UserGroups (
	groupId bigint not null,
	userGroupId varchar(75) not null,
	primary key (groupId, userGroupId)
);

create table IGFolder (
	folderId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	createDate timestamp,
	modifiedDate timestamp,
	parentFolderId varchar(75),
	name varchar(75),
	description varchar(500)
);

create table IGImage (
	companyId varchar(75) not null,
	imageId varchar(75) not null,
	userId varchar(75) not null,
	createDate timestamp,
	modifiedDate timestamp,
	folderId varchar(75),
	description varchar(500),
	height integer,
	width integer,
	size_ integer,
	primary key (companyId, imageId)
);

create table Image (
	imageId varchar(200) not null primary key,
	modifiedDate timestamp,
	text_ clob,
	type_ varchar(75)
);

create table JournalArticle (
	companyId varchar(75) not null,
	groupId bigint not null,
	articleId varchar(75) not null,
	version double not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	title varchar(75),
	description varchar(500),
	content clob,
	type_ varchar(75),
	structureId varchar(75),
	templateId varchar(75),
	displayDate timestamp,
	approved smallint,
	approvedByUserId varchar(75),
	approvedByUserName varchar(75),
	approvedDate timestamp,
	expired smallint,
	expirationDate timestamp,
	reviewDate timestamp,
	primary key (companyId, groupId, articleId, version)
);

create table JournalContentSearch (
	portletId varchar(75) not null,
	layoutId varchar(75) not null,
	ownerId varchar(75) not null,
	articleId varchar(75) not null,
	companyId varchar(75) not null,
	groupId bigint not null,
	primary key (portletId, layoutId, ownerId, articleId)
);

create table JournalStructure (
	companyId varchar(75) not null,
	groupId bigint not null,
	structureId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75),
	description varchar(500),
	xsd clob,
	primary key (companyId, groupId, structureId)
);

create table JournalTemplate (
	companyId varchar(75) not null,
	groupId bigint not null,
	templateId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	structureId varchar(75),
	name varchar(75),
	description varchar(500),
	xsl clob,
	langType varchar(75),
	smallImage smallint,
	smallImageURL varchar(75),
	primary key (companyId, groupId, templateId)
);

create table Layout (
	layoutId varchar(75) not null,
	ownerId varchar(75) not null,
	companyId varchar(75) not null,
	parentLayoutId varchar(75),
	name varchar(500),
	title varchar(500),
	type_ varchar(75),
	typeSettings clob,
	hidden_ smallint,
	friendlyURL varchar(75),
	iconImage smallint,
	themeId varchar(75),
	colorSchemeId varchar(75),
	css varchar(75),
	priority integer,
	primary key (layoutId, ownerId)
);

create table LayoutSet (
	ownerId varchar(75) not null primary key,
	companyId varchar(75) not null,
	groupId bigint not null,
	userId varchar(75) not null,
	privateLayout smallint,
	logo smallint,
	themeId varchar(75),
	colorSchemeId varchar(75),
	css varchar(75),
	pageCount integer,
	virtualHost varchar(75)
);

create table ListType (
	listTypeId integer not null primary key,
	name varchar(75),
	type_ varchar(75)
);

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

create table MBCategory (
	categoryId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentCategoryId varchar(75),
	name varchar(75),
	description varchar(500),
	lastPostDate timestamp
);

create table MBDiscussion (
	discussionId varchar(75) not null primary key,
	className varchar(75),
	classPK varchar(75),
	threadId varchar(75)
);

create table MBMessage (
	topicId varchar(75) not null,
	messageId varchar(75) not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	categoryId varchar(75),
	threadId varchar(75),
	parentMessageId varchar(75),
	subject varchar(75),
	body clob,
	attachments smallint,
	anonymous smallint,
	primary key (topicId, messageId)
);

create table MBMessageFlag (
	topicId varchar(75) not null,
	messageId varchar(75) not null,
	userId varchar(75) not null,
	flag varchar(75),
	primary key (topicId, messageId, userId)
);

create table MBStatsUser (
	groupId bigint not null,
	userId varchar(75) not null,
	messageCount integer,
	lastPostDate timestamp,
	primary key (groupId, userId)
);

create table MBThread (
	threadId varchar(75) not null primary key,
	categoryId varchar(75),
	topicId varchar(75),
	rootMessageId varchar(75),
	messageCount integer,
	viewCount integer,
	lastPostByUserId varchar(75),
	lastPostDate timestamp,
	priority double
);

create table Organization_ (
	organizationId varchar(75) not null primary key,
	companyId varchar(75) not null,
	parentOrganizationId varchar(75),
	name varchar(75),
	recursable smallint,
	regionId varchar(75),
	countryId varchar(75),
	statusId integer,
	comments varchar(500)
);

create table OrgGroupPermission (
	organizationId varchar(75) not null,
	groupId bigint not null,
	permissionId bigint not null,
	primary key (organizationId, groupId, permissionId)
);

create table OrgGroupRole (
	organizationId varchar(75) not null,
	groupId bigint not null,
	roleId varchar(75) not null,
	primary key (organizationId, groupId, roleId)
);

create table OrgLabor (
	orgLaborId varchar(75) not null primary key,
	organizationId varchar(75),
	typeId integer,
	sunOpen integer,
	sunClose integer,
	monOpen integer,
	monClose integer,
	tueOpen integer,
	tueClose integer,
	wedOpen integer,
	wedClose integer,
	thuOpen integer,
	thuClose integer,
	friOpen integer,
	friClose integer,
	satOpen integer,
	satClose integer
);

create table PasswordTracker (
	passwordTrackerId bigint not null primary key,
	userId varchar(75) not null,
	createDate timestamp,
	password_ varchar(75)
);

create table Permission_ (
	permissionId bigint not null primary key,
	companyId varchar(75) not null,
	actionId varchar(75),
	resourceId bigint
);

create table Phone (
	phoneId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	number_ varchar(75),
	extension varchar(75),
	typeId integer,
	primary_ smallint
);

create table PluginSetting (
	pluginSettingId bigint not null primary key,
	companyId varchar(75) not null,
	pluginId varchar(75),
	pluginType varchar(75),
	roles varchar(75),
	active_ smallint
);

create table PollsChoice (
	questionId varchar(75) not null,
	choiceId varchar(75) not null,
	description varchar(75),
	primary key (questionId, choiceId)
);

create table PollsQuestion (
	questionId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	title varchar(75),
	description varchar(500),
	expirationDate timestamp,
	lastVoteDate timestamp
);

create table PollsVote (
	questionId varchar(75) not null,
	userId varchar(75) not null,
	choiceId varchar(75),
	voteDate timestamp,
	primary key (questionId, userId)
);

create table Portlet (
	portletId varchar(75) not null,
	companyId varchar(75) not null,
	roles varchar(75),
	active_ smallint,
	primary key (portletId, companyId)
);

create table PortletPreferences (
	portletId varchar(75) not null,
	layoutId varchar(75) not null,
	ownerId varchar(75) not null,
	preferences clob,
	primary key (portletId, layoutId, ownerId)
);

create table RatingsEntry (
	entryId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	score double
);

create table RatingsStats (
	statsId bigint not null primary key,
	className varchar(75),
	classPK varchar(75),
	totalEntries integer,
	totalScore double,
	averageScore double
);

create table Region (
	regionId varchar(75) not null primary key,
	countryId varchar(75),
	regionCode varchar(75),
	name varchar(75),
	active_ smallint
);

create table Release_ (
	releaseId bigint not null primary key,
	createDate timestamp,
	modifiedDate timestamp,
	buildNumber integer,
	buildDate timestamp,
	verified smallint
);

create table Resource_ (
	resourceId bigint not null primary key,
	codeId bigint,
	primKey varchar(200)
);

create table ResourceCode (
	codeId bigint not null primary key,
	companyId varchar(75) not null,
	name varchar(75),
	scope varchar(75)
);

create table Role_ (
	roleId varchar(75) not null primary key,
	companyId varchar(75) not null,
	className varchar(75),
	classPK varchar(75),
	name varchar(75),
	description varchar(500),
	type_ integer
);

create table Roles_Permissions (
	roleId varchar(75) not null,
	permissionId bigint not null,
	primary key (roleId, permissionId)
);

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

create table ShoppingCart (
	cartId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	itemIds varchar(500),
	couponIds varchar(500),
	altShipping integer,
	insure smallint
);

create table ShoppingCategory (
	categoryId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentCategoryId varchar(75),
	name varchar(75),
	description varchar(500)
);

create table ShoppingCoupon (
	couponId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75),
	description varchar(500),
	startDate timestamp,
	endDate timestamp,
	active_ smallint,
	limitCategories varchar(500),
	limitSkus varchar(500),
	minOrder double,
	discount double,
	discountType varchar(75)
);

create table ShoppingItem (
	itemId varchar(75) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	categoryId varchar(75),
	sku varchar(75),
	name varchar(200),
	description varchar(500),
	properties varchar(500),
	fields_ smallint,
	fieldsQuantities varchar(500),
	minQuantity integer,
	maxQuantity integer,
	price double,
	discount double,
	taxable smallint,
	shipping double,
	useShippingFormula smallint,
	requiresShipping smallint,
	stockQuantity integer,
	featured_ smallint,
	sale_ smallint,
	smallImage smallint,
	smallImageURL varchar(75),
	mediumImage smallint,
	mediumImageURL varchar(75),
	largeImage smallint,
	largeImageURL varchar(75)
);

create table ShoppingItemField (
	itemFieldId varchar(75) not null primary key,
	itemId varchar(75),
	name varchar(75),
	values_ varchar(500),
	description varchar(500)
);

create table ShoppingItemPrice (
	itemPriceId varchar(75) not null primary key,
	itemId varchar(75),
	minQuantity integer,
	maxQuantity integer,
	price double,
	discount double,
	taxable smallint,
	shipping double,
	useShippingFormula smallint,
	status integer
);

create table ShoppingOrder (
	orderId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	tax double,
	shipping double,
	altShipping varchar(75),
	requiresShipping smallint,
	insure smallint,
	insurance double,
	couponIds varchar(75),
	couponDiscount double,
	billingFirstName varchar(75),
	billingLastName varchar(75),
	billingEmailAddress varchar(75),
	billingCompany varchar(75),
	billingStreet varchar(75),
	billingCity varchar(75),
	billingState varchar(75),
	billingZip varchar(75),
	billingCountry varchar(75),
	billingPhone varchar(75),
	shipToBilling smallint,
	shippingFirstName varchar(75),
	shippingLastName varchar(75),
	shippingEmailAddress varchar(75),
	shippingCompany varchar(75),
	shippingStreet varchar(75),
	shippingCity varchar(75),
	shippingState varchar(75),
	shippingZip varchar(75),
	shippingCountry varchar(75),
	shippingPhone varchar(75),
	ccName varchar(75),
	ccType varchar(75),
	ccNumber varchar(75),
	ccExpMonth integer,
	ccExpYear integer,
	ccVerNumber varchar(75),
	comments varchar(500),
	ppTxnId varchar(75),
	ppPaymentStatus varchar(75),
	ppPaymentGross double,
	ppReceiverEmail varchar(75),
	ppPayerEmail varchar(75),
	sendOrderEmail smallint,
	sendShippingEmail smallint
);

create table ShoppingOrderItem (
	orderId varchar(75) not null,
	itemId varchar(75) not null,
	sku varchar(75),
	name varchar(200),
	description varchar(500),
	properties varchar(500),
	price double,
	quantity integer,
	shippedDate timestamp,
	primary key (orderId, itemId)
);

create table Subscription (
	subscriptionId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	frequency varchar(75)
);

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

create table User_ (
	userId varchar(75) not null primary key,
	companyId varchar(75) not null,
	createDate timestamp,
	modifiedDate timestamp,
	contactId bigint,
	password_ varchar(75),
	passwordEncrypted smallint,
	passwordExpirationDate timestamp,
	passwordReset smallint,
	screenName varchar(75),
	emailAddress varchar(75),
	languageId varchar(75),
	timeZoneId varchar(75),
	greeting varchar(75),
	resolution varchar(75),
	comments varchar(500),
	loginDate timestamp,
	loginIP varchar(75),
	lastLoginDate timestamp,
	lastLoginIP varchar(75),
	failedLoginAttempts integer,
	agreedToTermsOfUse smallint,
	active_ smallint
);

create table UserGroup (
	userGroupId varchar(75) not null primary key,
	companyId varchar(75) not null,
	parentUserGroupId varchar(75),
	name varchar(75),
	description varchar(500)
);

create table UserGroupRole (
	userId varchar(75) not null,
	groupId bigint not null,
	roleId varchar(75) not null,
	primary key (userId, groupId, roleId)
);

create table UserIdMapper (
	userId varchar(75) not null,
	type_ varchar(75) not null,
	description varchar(75),
	externalUserId varchar(75),
	primary key (userId, type_)
);

create table Users_Groups (
	userId varchar(75) not null,
	groupId bigint not null,
	primary key (userId, groupId)
);

create table Users_Orgs (
	userId varchar(75) not null,
	organizationId varchar(75) not null,
	primary key (userId, organizationId)
);

create table Users_Permissions (
	userId varchar(75) not null,
	permissionId bigint not null,
	primary key (userId, permissionId)
);

create table Users_Roles (
	userId varchar(75) not null,
	roleId varchar(75) not null,
	primary key (userId, roleId)
);

create table Users_UserGroups (
	userId varchar(75) not null,
	userGroupId varchar(75) not null,
	primary key (userId, userGroupId)
);

create table UserTracker (
	userTrackerId varchar(75) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	modifiedDate timestamp,
	remoteAddr varchar(75),
	remoteHost varchar(75),
	userAgent varchar(75)
);

create table UserTrackerPath (
	userTrackerPathId varchar(75) not null primary key,
	userTrackerId varchar(75),
	path varchar(500),
	pathDate timestamp
);

create table Website (
	websiteId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	url varchar(75),
	typeId integer,
	primary_ smallint
);

create table WikiNode (
	nodeId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75),
	description varchar(500),
	lastPostDate timestamp
);

create table WikiPage (
	nodeId varchar(75) not null,
	title varchar(75) not null,
	version double not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	content clob,
	format varchar(75),
	head smallint,
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


insert into Release_ (releaseId, createDate, modifiedDate, buildNumber, verified) values ('1', current timestamp, current timestamp, 3901, 1);


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


