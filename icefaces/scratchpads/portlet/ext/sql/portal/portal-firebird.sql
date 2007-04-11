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
	addressId int64 not null primary key,
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
	categoryId int64 not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentCategoryId int64,
	name varchar(75),
	description varchar(4000)
);

create table BlogsEntry (
	entryId int64 not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	categoryId int64,
	title varchar(75),
	content blob,
	displayDate timestamp
);

create table BookmarksEntry (
	entryId int64 not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	createDate timestamp,
	modifiedDate timestamp,
	folderId int64,
	name varchar(75),
	url varchar(4000),
	comments varchar(4000),
	visits integer
);

create table BookmarksFolder (
	folderId int64 not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	createDate timestamp,
	modifiedDate timestamp,
	parentFolderId int64,
	name varchar(75),
	description varchar(4000)
);

create table CalEvent (
	eventId int64 not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	title varchar(75),
	description varchar(4000),
	startDate timestamp,
	endDate timestamp,
	durationHour integer,
	durationMinute integer,
	allDay smallint,
	timeZoneSensitive smallint,
	type_ varchar(75),
	repeating smallint,
	recurrence blob,
	remindBy varchar(75),
	firstReminder integer,
	secondReminder integer
);

create table Company (
	companyId varchar(75) not null primary key,
	key_ blob,
	portalURL varchar(75),
	homeURL varchar(75),
	mx varchar(75)
);

create table Contact_ (
	contactId int64 not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	accountId varchar(75),
	parentContactId int64,
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
	currentId int64
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
	description varchar(4000),
	version double precision,
	size_ integer,
	readCount integer,
	extraSettings blob,
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
	fileShortcutId int64 not null primary key,
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
	version double precision not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	size_ integer,
	primary key (folderId, name, version)
);

create table DLFolder (
	folderId varchar(75) not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentFolderId varchar(75),
	name varchar(100),
	description varchar(4000),
	lastPostDate timestamp
);

create table EmailAddress (
	emailAddressId int64 not null primary key,
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
	groupId int64 not null primary key,
	companyId varchar(75) not null,
	creatorUserId varchar(75),
	className varchar(75),
	classPK varchar(75),
	parentGroupId int64,
	liveGroupId int64,
	name varchar(75),
	description varchar(4000),
	type_ varchar(75),
	friendlyURL varchar(75),
	active_ smallint
);

create table Groups_Orgs (
	groupId int64 not null,
	organizationId varchar(75) not null,
	primary key (groupId, organizationId)
);

create table Groups_Permissions (
	groupId int64 not null,
	permissionId int64 not null,
	primary key (groupId, permissionId)
);

create table Groups_Roles (
	groupId int64 not null,
	roleId varchar(75) not null,
	primary key (groupId, roleId)
);

create table Groups_UserGroups (
	groupId int64 not null,
	userGroupId varchar(75) not null,
	primary key (groupId, userGroupId)
);

create table IGFolder (
	folderId varchar(75) not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	createDate timestamp,
	modifiedDate timestamp,
	parentFolderId varchar(75),
	name varchar(75),
	description varchar(4000)
);

create table IGImage (
	companyId varchar(75) not null,
	imageId varchar(75) not null,
	userId varchar(75) not null,
	createDate timestamp,
	modifiedDate timestamp,
	folderId varchar(75),
	description varchar(4000),
	height integer,
	width integer,
	size_ integer,
	primary key (companyId, imageId)
);

create table Image (
	imageId varchar(200) not null primary key,
	modifiedDate timestamp,
	text_ blob,
	type_ varchar(75)
);

create table JournalArticle (
	companyId varchar(75) not null,
	groupId int64 not null,
	articleId varchar(75) not null,
	version double precision not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	title varchar(75),
	description varchar(4000),
	content blob,
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
	groupId int64 not null,
	primary key (portletId, layoutId, ownerId, articleId)
);

create table JournalStructure (
	companyId varchar(75) not null,
	groupId int64 not null,
	structureId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75),
	description varchar(4000),
	xsd blob,
	primary key (companyId, groupId, structureId)
);

create table JournalTemplate (
	companyId varchar(75) not null,
	groupId int64 not null,
	templateId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	structureId varchar(75),
	name varchar(75),
	description varchar(4000),
	xsl blob,
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
	name varchar(4000),
	title varchar(4000),
	type_ varchar(75),
	typeSettings blob,
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
	groupId int64 not null,
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
	banId int64 not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	banUserId varchar(75)
);

create table MBCategory (
	categoryId varchar(75) not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentCategoryId varchar(75),
	name varchar(75),
	description varchar(4000),
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
	body blob,
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
	groupId int64 not null,
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
	priority double precision
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
	comments varchar(4000)
);

create table OrgGroupPermission (
	organizationId varchar(75) not null,
	groupId int64 not null,
	permissionId int64 not null,
	primary key (organizationId, groupId, permissionId)
);

create table OrgGroupRole (
	organizationId varchar(75) not null,
	groupId int64 not null,
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
	passwordTrackerId int64 not null primary key,
	userId varchar(75) not null,
	createDate timestamp,
	password_ varchar(75)
);

create table Permission_ (
	permissionId int64 not null primary key,
	companyId varchar(75) not null,
	actionId varchar(75),
	resourceId int64
);

create table Phone (
	phoneId int64 not null primary key,
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
	pluginSettingId int64 not null primary key,
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
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	title varchar(75),
	description varchar(4000),
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
	preferences blob,
	primary key (portletId, layoutId, ownerId)
);

create table RatingsEntry (
	entryId int64 not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	className varchar(75),
	classPK varchar(75),
	score double precision
);

create table RatingsStats (
	statsId int64 not null primary key,
	className varchar(75),
	classPK varchar(75),
	totalEntries integer,
	totalScore double precision,
	averageScore double precision
);

create table Region (
	regionId varchar(75) not null primary key,
	countryId varchar(75),
	regionCode varchar(75),
	name varchar(75),
	active_ smallint
);

create table Release_ (
	releaseId int64 not null primary key,
	createDate timestamp,
	modifiedDate timestamp,
	buildNumber integer,
	buildDate timestamp,
	verified smallint
);

create table Resource_ (
	resourceId int64 not null primary key,
	codeId int64,
	primKey varchar(200)
);

create table ResourceCode (
	codeId int64 not null primary key,
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
	description varchar(4000),
	type_ integer
);

create table Roles_Permissions (
	roleId varchar(75) not null,
	permissionId int64 not null,
	primary key (roleId, permissionId)
);

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

create table ShoppingCart (
	cartId varchar(75) not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	itemIds varchar(4000),
	couponIds varchar(4000),
	altShipping integer,
	insure smallint
);

create table ShoppingCategory (
	categoryId varchar(75) not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	parentCategoryId varchar(75),
	name varchar(75),
	description varchar(4000)
);

create table ShoppingCoupon (
	couponId varchar(75) not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75),
	description varchar(4000),
	startDate timestamp,
	endDate timestamp,
	active_ smallint,
	limitCategories varchar(4000),
	limitSkus varchar(4000),
	minOrder double precision,
	discount double precision,
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
	description varchar(4000),
	properties varchar(4000),
	fields_ smallint,
	fieldsQuantities varchar(4000),
	minQuantity integer,
	maxQuantity integer,
	price double precision,
	discount double precision,
	taxable smallint,
	shipping double precision,
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
	values_ varchar(4000),
	description varchar(4000)
);

create table ShoppingItemPrice (
	itemPriceId varchar(75) not null primary key,
	itemId varchar(75),
	minQuantity integer,
	maxQuantity integer,
	price double precision,
	discount double precision,
	taxable smallint,
	shipping double precision,
	useShippingFormula smallint,
	status integer
);

create table ShoppingOrder (
	orderId varchar(75) not null primary key,
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	tax double precision,
	shipping double precision,
	altShipping varchar(75),
	requiresShipping smallint,
	insure smallint,
	insurance double precision,
	couponIds varchar(75),
	couponDiscount double precision,
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
	comments varchar(4000),
	ppTxnId varchar(75),
	ppPaymentStatus varchar(75),
	ppPaymentGross double precision,
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
	description varchar(4000),
	properties varchar(4000),
	price double precision,
	quantity integer,
	shippedDate timestamp,
	primary key (orderId, itemId)
);

create table Subscription (
	subscriptionId int64 not null primary key,
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

create table User_ (
	userId varchar(75) not null primary key,
	companyId varchar(75) not null,
	createDate timestamp,
	modifiedDate timestamp,
	contactId int64,
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
	comments varchar(4000),
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
	description varchar(4000)
);

create table UserGroupRole (
	userId varchar(75) not null,
	groupId int64 not null,
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
	groupId int64 not null,
	primary key (userId, groupId)
);

create table Users_Orgs (
	userId varchar(75) not null,
	organizationId varchar(75) not null,
	primary key (userId, organizationId)
);

create table Users_Permissions (
	userId varchar(75) not null,
	permissionId int64 not null,
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
	path varchar(4000),
	pathDate timestamp
);

create table Website (
	websiteId int64 not null primary key,
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
	groupId int64 not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	modifiedDate timestamp,
	name varchar(75),
	description varchar(4000),
	lastPostDate timestamp
);

create table WikiPage (
	nodeId varchar(75) not null,
	title varchar(75) not null,
	version double precision not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75),
	createDate timestamp,
	content blob,
	format varchar(75),
	head smallint,
	primary key (nodeId, title, version)
);





--
-- List types for accounts
--





--
-- List types for contacts
--







--
-- List types for organizations
--




























































































--
-- Company
--


--
-- Groups
--



--
-- Organizations
--























--
-- Roles
--


--
-- User (default)
--


--
-- User (test@liferay.com)
--






--
-- User (test.mail@liferay.com)
--


































































































































































































































































































































































































































































































































































































































































































































































































































































































commit;
