create table Account_ (
	accountId VARCHAR(75) not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	parentAccountId VARCHAR(75) null,
	name VARCHAR(75) null,
	legalName VARCHAR(75) null,
	legalId VARCHAR(75) null,
	legalType VARCHAR(75) null,
	sicCode VARCHAR(75) null,
	tickerSymbol VARCHAR(75) null,
	industry VARCHAR(75) null,
	type_ VARCHAR(75) null,
	size_ VARCHAR(75) null
);

create table Address (
	addressId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	street1 VARCHAR(75) null,
	street2 VARCHAR(75) null,
	street3 VARCHAR(75) null,
	city VARCHAR(75) null,
	zip VARCHAR(75) null,
	regionId VARCHAR(75) null,
	countryId VARCHAR(75) null,
	typeId INTEGER,
	mailing BOOLEAN,
	primary_ BOOLEAN
);

create table BlogsCategory (
	categoryId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	parentCategoryId LONG,
	name VARCHAR(75) null,
	description STRING null
);

create table BlogsEntry (
	entryId LONG not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	categoryId LONG,
	title VARCHAR(75) null,
	content TEXT null,
	displayDate DATE null
);

create table BookmarksEntry (
	entryId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	createDate DATE null,
	modifiedDate DATE null,
	folderId LONG,
	name VARCHAR(75) null,
	url STRING null,
	comments STRING null,
	visits INTEGER
);

create table BookmarksFolder (
	folderId LONG not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	createDate DATE null,
	modifiedDate DATE null,
	parentFolderId LONG,
	name VARCHAR(75) null,
	description STRING null
);

create table CalEvent (
	eventId LONG not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	title VARCHAR(75) null,
	description STRING null,
	startDate DATE null,
	endDate DATE null,
	durationHour INTEGER,
	durationMinute INTEGER,
	allDay BOOLEAN,
	timeZoneSensitive BOOLEAN,
	type_ VARCHAR(75) null,
	repeating BOOLEAN,
	recurrence TEXT null,
	remindBy VARCHAR(75) null,
	firstReminder INTEGER,
	secondReminder INTEGER
);

create table Company (
	companyId VARCHAR(75) not null primary key,
	key_ TEXT null,
	portalURL VARCHAR(75) null,
	homeURL VARCHAR(75) null,
	mx VARCHAR(75) null
);

create table Contact_ (
	contactId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	accountId VARCHAR(75) null,
	parentContactId LONG,
	firstName VARCHAR(75) null,
	middleName VARCHAR(75) null,
	lastName VARCHAR(75) null,
	nickName VARCHAR(75) null,
	prefixId INTEGER,
	suffixId INTEGER,
	male BOOLEAN,
	birthday DATE null,
	smsSn VARCHAR(75) null,
	aimSn VARCHAR(75) null,
	icqSn VARCHAR(75) null,
	jabberSn VARCHAR(75) null,
	msnSn VARCHAR(75) null,
	skypeSn VARCHAR(75) null,
	ymSn VARCHAR(75) null,
	employeeStatusId VARCHAR(75) null,
	employeeNumber VARCHAR(75) null,
	jobTitle VARCHAR(75) null,
	jobClass VARCHAR(75) null,
	hoursOfOperation VARCHAR(75) null
);

create table Counter (
	name VARCHAR(75) not null primary key,
	currentId LONG
);

create table Country (
	countryId VARCHAR(75) not null primary key,
	name VARCHAR(75) null,
	a2 VARCHAR(75) null,
	a3 VARCHAR(75) null,
	number_ VARCHAR(75) null,
	idd_ VARCHAR(75) null,
	active_ BOOLEAN
);

create table CyrusUser (
	userId VARCHAR(75) not null primary key,
	password_ VARCHAR(75) not null
);

create table CyrusVirtual (
	emailAddress VARCHAR(75) not null primary key,
	userId VARCHAR(75) not null
);

create table DataTracker (
	dataTrackerId VARCHAR(75) not null primary key,
	companyId VARCHAR(75) not null,
	createdOn DATE null,
	createdByUserId VARCHAR(75) null,
	createdByUserName VARCHAR(75) null,
	updatedOn DATE null,
	updatedBy VARCHAR(75) null,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	active_ BOOLEAN
);

create table DLFileEntry (
	folderId VARCHAR(75) not null,
	name VARCHAR(100) not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	versionUserId VARCHAR(75) null,
	versionUserName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	title VARCHAR(100) null,
	description STRING null,
	version DOUBLE,
	size_ INTEGER,
	readCount INTEGER,
	extraSettings TEXT null,
	primary key (folderId, name)
);

create table DLFileRank (
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	folderId VARCHAR(75) not null,
	name VARCHAR(100) not null,
	createDate DATE null,
	primary key (companyId, userId, folderId, name)
);

create table DLFileShortcut (
	fileShortcutId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	folderId VARCHAR(75) null,
	toFolderId VARCHAR(75) null,
	toName VARCHAR(75) null
);

create table DLFileVersion (
	folderId VARCHAR(75) not null,
	name VARCHAR(100) not null,
	version DOUBLE not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	size_ INTEGER,
	primary key (folderId, name, version)
);

create table DLFolder (
	folderId VARCHAR(75) not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	parentFolderId VARCHAR(75) null,
	name VARCHAR(100) null,
	description STRING null,
	lastPostDate DATE null
);

create table EmailAddress (
	emailAddressId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	address VARCHAR(75) null,
	typeId INTEGER,
	primary_ BOOLEAN
);

create table Group_ (
	groupId LONG not null primary key,
	companyId VARCHAR(75) not null,
	creatorUserId VARCHAR(75) null,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	parentGroupId LONG,
	liveGroupId LONG,
	name VARCHAR(75) null,
	description STRING null,
	type_ VARCHAR(75) null,
	friendlyURL VARCHAR(75) null,
	active_ BOOLEAN
);

create table Groups_Orgs (
	groupId LONG not null,
	organizationId VARCHAR(75) not null,
	primary key (groupId, organizationId)
);

create table Groups_Permissions (
	groupId LONG not null,
	permissionId LONG not null,
	primary key (groupId, permissionId)
);

create table Groups_Roles (
	groupId LONG not null,
	roleId VARCHAR(75) not null,
	primary key (groupId, roleId)
);

create table Groups_UserGroups (
	groupId LONG not null,
	userGroupId VARCHAR(75) not null,
	primary key (groupId, userGroupId)
);

create table IGFolder (
	folderId VARCHAR(75) not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	createDate DATE null,
	modifiedDate DATE null,
	parentFolderId VARCHAR(75) null,
	name VARCHAR(75) null,
	description STRING null
);

create table IGImage (
	companyId VARCHAR(75) not null,
	imageId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	createDate DATE null,
	modifiedDate DATE null,
	folderId VARCHAR(75) null,
	description STRING null,
	height INTEGER,
	width INTEGER,
	size_ INTEGER,
	primary key (companyId, imageId)
);

create table Image (
	imageId VARCHAR(200) not null primary key,
	modifiedDate DATE null,
	text_ TEXT null,
	type_ VARCHAR(75) null
);

create table JournalArticle (
	companyId VARCHAR(75) not null,
	groupId LONG not null,
	articleId VARCHAR(75) not null,
	version DOUBLE not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	title VARCHAR(75) null,
	description STRING null,
	content TEXT null,
	type_ VARCHAR(75) null,
	structureId VARCHAR(75) null,
	templateId VARCHAR(75) null,
	displayDate DATE null,
	approved BOOLEAN,
	approvedByUserId VARCHAR(75) null,
	approvedByUserName VARCHAR(75) null,
	approvedDate DATE null,
	expired BOOLEAN,
	expirationDate DATE null,
	reviewDate DATE null,
	primary key (companyId, groupId, articleId, version)
);

create table JournalContentSearch (
	portletId VARCHAR(75) not null,
	layoutId VARCHAR(75) not null,
	ownerId VARCHAR(75) not null,
	articleId VARCHAR(75) not null,
	companyId VARCHAR(75) not null,
	groupId LONG not null,
	primary key (portletId, layoutId, ownerId, articleId)
);

create table JournalStructure (
	companyId VARCHAR(75) not null,
	groupId LONG not null,
	structureId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null,
	description STRING null,
	xsd TEXT null,
	primary key (companyId, groupId, structureId)
);

create table JournalTemplate (
	companyId VARCHAR(75) not null,
	groupId LONG not null,
	templateId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	structureId VARCHAR(75) null,
	name VARCHAR(75) null,
	description STRING null,
	xsl TEXT null,
	langType VARCHAR(75) null,
	smallImage BOOLEAN,
	smallImageURL VARCHAR(75) null,
	primary key (companyId, groupId, templateId)
);

create table Layout (
	layoutId VARCHAR(75) not null,
	ownerId VARCHAR(75) not null,
	companyId VARCHAR(75) not null,
	parentLayoutId VARCHAR(75) null,
	name STRING null,
	title STRING null,
	type_ VARCHAR(75) null,
	typeSettings TEXT null,
	hidden_ BOOLEAN,
	friendlyURL VARCHAR(75) null,
	iconImage BOOLEAN,
	themeId VARCHAR(75) null,
	colorSchemeId VARCHAR(75) null,
	css VARCHAR(75) null,
	priority INTEGER,
	primary key (layoutId, ownerId)
);

create table LayoutSet (
	ownerId VARCHAR(75) not null primary key,
	companyId VARCHAR(75) not null,
	groupId LONG not null,
	userId VARCHAR(75) not null,
	privateLayout BOOLEAN,
	logo BOOLEAN,
	themeId VARCHAR(75) null,
	colorSchemeId VARCHAR(75) null,
	css VARCHAR(75) null,
	pageCount INTEGER,
	virtualHost VARCHAR(75) null
);

create table ListType (
	listTypeId INTEGER not null primary key,
	name VARCHAR(75) null,
	type_ VARCHAR(75) null
);

create table MBBan (
	banId LONG not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	banUserId VARCHAR(75) null
);

create table MBCategory (
	categoryId VARCHAR(75) not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	parentCategoryId VARCHAR(75) null,
	name VARCHAR(75) null,
	description STRING null,
	lastPostDate DATE null
);

create table MBDiscussion (
	discussionId VARCHAR(75) not null primary key,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	threadId VARCHAR(75) null
);

create table MBMessage (
	topicId VARCHAR(75) not null,
	messageId VARCHAR(75) not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	categoryId VARCHAR(75) null,
	threadId VARCHAR(75) null,
	parentMessageId VARCHAR(75) null,
	subject VARCHAR(75) null,
	body TEXT null,
	attachments BOOLEAN,
	anonymous BOOLEAN,
	primary key (topicId, messageId)
);

create table MBMessageFlag (
	topicId VARCHAR(75) not null,
	messageId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	flag VARCHAR(75) null,
	primary key (topicId, messageId, userId)
);

create table MBStatsUser (
	groupId LONG not null,
	userId VARCHAR(75) not null,
	messageCount INTEGER,
	lastPostDate DATE null,
	primary key (groupId, userId)
);

create table MBThread (
	threadId VARCHAR(75) not null primary key,
	categoryId VARCHAR(75) null,
	topicId VARCHAR(75) null,
	rootMessageId VARCHAR(75) null,
	messageCount INTEGER,
	viewCount INTEGER,
	lastPostByUserId VARCHAR(75) null,
	lastPostDate DATE null,
	priority DOUBLE
);

create table Organization_ (
	organizationId VARCHAR(75) not null primary key,
	companyId VARCHAR(75) not null,
	parentOrganizationId VARCHAR(75) null,
	name VARCHAR(75) null,
	recursable BOOLEAN,
	regionId VARCHAR(75) null,
	countryId VARCHAR(75) null,
	statusId INTEGER,
	comments STRING null
);

create table OrgGroupPermission (
	organizationId VARCHAR(75) not null,
	groupId LONG not null,
	permissionId LONG not null,
	primary key (organizationId, groupId, permissionId)
);

create table OrgGroupRole (
	organizationId VARCHAR(75) not null,
	groupId LONG not null,
	roleId VARCHAR(75) not null,
	primary key (organizationId, groupId, roleId)
);

create table OrgLabor (
	orgLaborId VARCHAR(75) not null primary key,
	organizationId VARCHAR(75) null,
	typeId INTEGER,
	sunOpen INTEGER,
	sunClose INTEGER,
	monOpen INTEGER,
	monClose INTEGER,
	tueOpen INTEGER,
	tueClose INTEGER,
	wedOpen INTEGER,
	wedClose INTEGER,
	thuOpen INTEGER,
	thuClose INTEGER,
	friOpen INTEGER,
	friClose INTEGER,
	satOpen INTEGER,
	satClose INTEGER
);

create table PasswordTracker (
	passwordTrackerId LONG not null primary key,
	userId VARCHAR(75) not null,
	createDate DATE null,
	password_ VARCHAR(75) null
);

create table Permission_ (
	permissionId LONG not null primary key,
	companyId VARCHAR(75) not null,
	actionId VARCHAR(75) null,
	resourceId LONG
);

create table Phone (
	phoneId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	number_ VARCHAR(75) null,
	extension VARCHAR(75) null,
	typeId INTEGER,
	primary_ BOOLEAN
);

create table PluginSetting (
	pluginSettingId LONG not null primary key,
	companyId VARCHAR(75) not null,
	pluginId VARCHAR(75) null,
	pluginType VARCHAR(75) null,
	roles VARCHAR(75) null,
	active_ BOOLEAN
);

create table PollsChoice (
	questionId VARCHAR(75) not null,
	choiceId VARCHAR(75) not null,
	description VARCHAR(75) null,
	primary key (questionId, choiceId)
);

create table PollsQuestion (
	questionId VARCHAR(75) not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	title VARCHAR(75) null,
	description STRING null,
	expirationDate DATE null,
	lastVoteDate DATE null
);

create table PollsVote (
	questionId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	choiceId VARCHAR(75) null,
	voteDate DATE null,
	primary key (questionId, userId)
);

create table Portlet (
	portletId VARCHAR(75) not null,
	companyId VARCHAR(75) not null,
	roles VARCHAR(75) null,
	active_ BOOLEAN,
	primary key (portletId, companyId)
);

create table PortletPreferences (
	portletId VARCHAR(75) not null,
	layoutId VARCHAR(75) not null,
	ownerId VARCHAR(75) not null,
	preferences TEXT null,
	primary key (portletId, layoutId, ownerId)
);

create table RatingsEntry (
	entryId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	score DOUBLE
);

create table RatingsStats (
	statsId LONG not null primary key,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	totalEntries INTEGER,
	totalScore DOUBLE,
	averageScore DOUBLE
);

create table Region (
	regionId VARCHAR(75) not null primary key,
	countryId VARCHAR(75) null,
	regionCode VARCHAR(75) null,
	name VARCHAR(75) null,
	active_ BOOLEAN
);

create table Release_ (
	releaseId LONG not null primary key,
	createDate DATE null,
	modifiedDate DATE null,
	buildNumber INTEGER,
	buildDate DATE null,
	verified BOOLEAN
);

create table Resource_ (
	resourceId LONG not null primary key,
	codeId LONG,
	primKey VARCHAR(200) null
);

create table ResourceCode (
	codeId LONG not null primary key,
	companyId VARCHAR(75) not null,
	name VARCHAR(75) null,
	scope VARCHAR(75) null
);

create table Role_ (
	roleId VARCHAR(75) not null primary key,
	companyId VARCHAR(75) not null,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	name VARCHAR(75) null,
	description STRING null,
	type_ INTEGER
);

create table Roles_Permissions (
	roleId VARCHAR(75) not null,
	permissionId LONG not null,
	primary key (roleId, permissionId)
);

create table SCFrameworkVersion (
	frameworkVersionId LONG not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null,
	url VARCHAR(1024) null,
	active_ BOOLEAN,
	priority INTEGER
);

create table SCFrameworkVersions_SCProductVersions (
	productVersionId LONG,
	frameworkVersionId LONG,
	primary key (productVersionId, frameworkVersionId)
);

create table SCLicense (
	licenseId LONG not null primary key,
	name VARCHAR(75) null,
	url VARCHAR(1024) null,
	openSource BOOLEAN,
	active_ BOOLEAN,
	recommended BOOLEAN
);

create table SCLicenses_SCProductEntries (
	productEntryId LONG,
	licenseId LONG,
	primary key (productEntryId, licenseId)
);

create table SCProductEntry (
	productEntryId LONG not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null,
	type_ VARCHAR(75) null,
	shortDescription STRING null,
	longDescription STRING null,
	pageURL VARCHAR(1024) null,
	repoGroupId VARCHAR(75) null,
	repoArtifactId VARCHAR(75) null
);

create table SCProductVersion (
	productVersionId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	productEntryId LONG,
	version VARCHAR(75) null,
	changeLog STRING null,
	downloadPageURL VARCHAR(1024) null,
	directDownloadURL VARCHAR(1024) null,
	repoStoreArtifact BOOLEAN
);

create table ShoppingCart (
	cartId VARCHAR(75) not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	itemIds STRING null,
	couponIds STRING null,
	altShipping INTEGER,
	insure BOOLEAN
);

create table ShoppingCategory (
	categoryId VARCHAR(75) not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	parentCategoryId VARCHAR(75) null,
	name VARCHAR(75) null,
	description STRING null
);

create table ShoppingCoupon (
	couponId VARCHAR(75) not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null,
	description STRING null,
	startDate DATE null,
	endDate DATE null,
	active_ BOOLEAN,
	limitCategories STRING null,
	limitSkus STRING null,
	minOrder DOUBLE,
	discount DOUBLE,
	discountType VARCHAR(75) null
);

create table ShoppingItem (
	itemId VARCHAR(75) not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	categoryId VARCHAR(75) null,
	sku VARCHAR(75) null,
	name VARCHAR(200) null,
	description STRING null,
	properties STRING null,
	fields_ BOOLEAN,
	fieldsQuantities STRING null,
	minQuantity INTEGER,
	maxQuantity INTEGER,
	price DOUBLE,
	discount DOUBLE,
	taxable BOOLEAN,
	shipping DOUBLE,
	useShippingFormula BOOLEAN,
	requiresShipping BOOLEAN,
	stockQuantity INTEGER,
	featured_ BOOLEAN,
	sale_ BOOLEAN,
	smallImage BOOLEAN,
	smallImageURL VARCHAR(75) null,
	mediumImage BOOLEAN,
	mediumImageURL VARCHAR(75) null,
	largeImage BOOLEAN,
	largeImageURL VARCHAR(75) null
);

create table ShoppingItemField (
	itemFieldId VARCHAR(75) not null primary key,
	itemId VARCHAR(75) null,
	name VARCHAR(75) null,
	values_ STRING null,
	description STRING null
);

create table ShoppingItemPrice (
	itemPriceId VARCHAR(75) not null primary key,
	itemId VARCHAR(75) null,
	minQuantity INTEGER,
	maxQuantity INTEGER,
	price DOUBLE,
	discount DOUBLE,
	taxable BOOLEAN,
	shipping DOUBLE,
	useShippingFormula BOOLEAN,
	status INTEGER
);

create table ShoppingOrder (
	orderId VARCHAR(75) not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	tax DOUBLE,
	shipping DOUBLE,
	altShipping VARCHAR(75) null,
	requiresShipping BOOLEAN,
	insure BOOLEAN,
	insurance DOUBLE,
	couponIds VARCHAR(75) null,
	couponDiscount DOUBLE,
	billingFirstName VARCHAR(75) null,
	billingLastName VARCHAR(75) null,
	billingEmailAddress VARCHAR(75) null,
	billingCompany VARCHAR(75) null,
	billingStreet VARCHAR(75) null,
	billingCity VARCHAR(75) null,
	billingState VARCHAR(75) null,
	billingZip VARCHAR(75) null,
	billingCountry VARCHAR(75) null,
	billingPhone VARCHAR(75) null,
	shipToBilling BOOLEAN,
	shippingFirstName VARCHAR(75) null,
	shippingLastName VARCHAR(75) null,
	shippingEmailAddress VARCHAR(75) null,
	shippingCompany VARCHAR(75) null,
	shippingStreet VARCHAR(75) null,
	shippingCity VARCHAR(75) null,
	shippingState VARCHAR(75) null,
	shippingZip VARCHAR(75) null,
	shippingCountry VARCHAR(75) null,
	shippingPhone VARCHAR(75) null,
	ccName VARCHAR(75) null,
	ccType VARCHAR(75) null,
	ccNumber VARCHAR(75) null,
	ccExpMonth INTEGER,
	ccExpYear INTEGER,
	ccVerNumber VARCHAR(75) null,
	comments STRING null,
	ppTxnId VARCHAR(75) null,
	ppPaymentStatus VARCHAR(75) null,
	ppPaymentGross DOUBLE,
	ppReceiverEmail VARCHAR(75) null,
	ppPayerEmail VARCHAR(75) null,
	sendOrderEmail BOOLEAN,
	sendShippingEmail BOOLEAN
);

create table ShoppingOrderItem (
	orderId VARCHAR(75) not null,
	itemId VARCHAR(75) not null,
	sku VARCHAR(75) null,
	name VARCHAR(200) null,
	description STRING null,
	properties STRING null,
	price DOUBLE,
	quantity INTEGER,
	shippedDate DATE null,
	primary key (orderId, itemId)
);

create table Subscription (
	subscriptionId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	frequency VARCHAR(75) null
);

create table TagsAsset (
	assetId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	startDate DATE null,
	endDate DATE null,
	publishDate DATE null,
	expirationDate DATE null,
	mimeType VARCHAR(75) null,
	title VARCHAR(75) null,
	url VARCHAR(75) null,
	height INTEGER,
	width INTEGER
);

create table TagsAssets_TagsEntries (
	assetId LONG,
	entryId LONG,
	primary key (assetId, entryId)
);

create table TagsEntry (
	entryId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null
);

create table TagsProperty (
	propertyId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	entryId LONG,
	key_ VARCHAR(75) null,
	value VARCHAR(75) null
);

create table TagsSource (
	sourceId LONG not null primary key,
	parentSourceId LONG,
	name VARCHAR(75) null,
	acronym VARCHAR(75) null
);

create table User_ (
	userId VARCHAR(75) not null primary key,
	companyId VARCHAR(75) not null,
	createDate DATE null,
	modifiedDate DATE null,
	contactId LONG,
	password_ VARCHAR(75) null,
	passwordEncrypted BOOLEAN,
	passwordExpirationDate DATE null,
	passwordReset BOOLEAN,
	screenName VARCHAR(75) null,
	emailAddress VARCHAR(75) null,
	languageId VARCHAR(75) null,
	timeZoneId VARCHAR(75) null,
	greeting VARCHAR(75) null,
	resolution VARCHAR(75) null,
	comments STRING null,
	loginDate DATE null,
	loginIP VARCHAR(75) null,
	lastLoginDate DATE null,
	lastLoginIP VARCHAR(75) null,
	failedLoginAttempts INTEGER,
	agreedToTermsOfUse BOOLEAN,
	active_ BOOLEAN
);

create table UserGroup (
	userGroupId VARCHAR(75) not null primary key,
	companyId VARCHAR(75) not null,
	parentUserGroupId VARCHAR(75) null,
	name VARCHAR(75) null,
	description STRING null
);

create table UserGroupRole (
	userId VARCHAR(75) not null,
	groupId LONG not null,
	roleId VARCHAR(75) not null,
	primary key (userId, groupId, roleId)
);

create table UserIdMapper (
	userId VARCHAR(75) not null,
	type_ VARCHAR(75) not null,
	description VARCHAR(75) null,
	externalUserId VARCHAR(75) null,
	primary key (userId, type_)
);

create table Users_Groups (
	userId VARCHAR(75) not null,
	groupId LONG not null,
	primary key (userId, groupId)
);

create table Users_Orgs (
	userId VARCHAR(75) not null,
	organizationId VARCHAR(75) not null,
	primary key (userId, organizationId)
);

create table Users_Permissions (
	userId VARCHAR(75) not null,
	permissionId LONG not null,
	primary key (userId, permissionId)
);

create table Users_Roles (
	userId VARCHAR(75) not null,
	roleId VARCHAR(75) not null,
	primary key (userId, roleId)
);

create table Users_UserGroups (
	userId VARCHAR(75) not null,
	userGroupId VARCHAR(75) not null,
	primary key (userId, userGroupId)
);

create table UserTracker (
	userTrackerId VARCHAR(75) not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	modifiedDate DATE null,
	remoteAddr VARCHAR(75) null,
	remoteHost VARCHAR(75) null,
	userAgent VARCHAR(75) null
);

create table UserTrackerPath (
	userTrackerPathId VARCHAR(75) not null primary key,
	userTrackerId VARCHAR(75) null,
	path STRING null,
	pathDate DATE null
);

create table Website (
	websiteId LONG not null primary key,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	className VARCHAR(75) null,
	classPK VARCHAR(75) null,
	url VARCHAR(75) null,
	typeId INTEGER,
	primary_ BOOLEAN
);

create table WikiNode (
	nodeId VARCHAR(75) not null primary key,
	groupId LONG not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null,
	description STRING null,
	lastPostDate DATE null
);

create table WikiPage (
	nodeId VARCHAR(75) not null,
	title VARCHAR(75) not null,
	version DOUBLE not null,
	companyId VARCHAR(75) not null,
	userId VARCHAR(75) not null,
	userName VARCHAR(75) null,
	createDate DATE null,
	content TEXT null,
	format VARCHAR(75) null,
	head BOOLEAN,
	primary key (nodeId, title, version)
);
