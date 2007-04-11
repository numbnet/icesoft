drop database lportal;
create database lportal encoding = 'UNICODE';
\c lportal;

create table Account_ (
	accountId varchar(75) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentAccountId varchar(75) null,
	name varchar(75) null,
	legalName varchar(75) null,
	legalId varchar(75) null,
	legalType varchar(75) null,
	sicCode varchar(75) null,
	tickerSymbol varchar(75) null,
	industry varchar(75) null,
	type_ varchar(75) null,
	size_ varchar(75) null
);

create table Address (
	addressId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar(75) null,
	classPK varchar(75) null,
	street1 varchar(75) null,
	street2 varchar(75) null,
	street3 varchar(75) null,
	city varchar(75) null,
	zip varchar(75) null,
	regionId varchar(75) null,
	countryId varchar(75) null,
	typeId integer,
	mailing bool,
	primary_ bool
);

create table BlogsCategory (
	categoryId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentCategoryId bigint,
	name varchar(75) null,
	description text null
);

create table BlogsEntry (
	entryId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	categoryId bigint,
	title varchar(75) null,
	content text null,
	displayDate timestamp null
);

create table BookmarksEntry (
	entryId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	folderId bigint,
	name varchar(75) null,
	url text null,
	comments text null,
	visits integer
);

create table BookmarksFolder (
	folderId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentFolderId bigint,
	name varchar(75) null,
	description text null
);

create table CalEvent (
	eventId bigint not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	title varchar(75) null,
	description text null,
	startDate timestamp null,
	endDate timestamp null,
	durationHour integer,
	durationMinute integer,
	allDay bool,
	timeZoneSensitive bool,
	type_ varchar(75) null,
	repeating bool,
	recurrence text null,
	remindBy varchar(75) null,
	firstReminder integer,
	secondReminder integer
);

create table Company (
	companyId varchar(75) not null primary key,
	key_ text null,
	portalURL varchar(75) null,
	homeURL varchar(75) null,
	mx varchar(75) null
);

create table Contact_ (
	contactId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	accountId varchar(75) null,
	parentContactId bigint,
	firstName varchar(75) null,
	middleName varchar(75) null,
	lastName varchar(75) null,
	nickName varchar(75) null,
	prefixId integer,
	suffixId integer,
	male bool,
	birthday timestamp null,
	smsSn varchar(75) null,
	aimSn varchar(75) null,
	icqSn varchar(75) null,
	jabberSn varchar(75) null,
	msnSn varchar(75) null,
	skypeSn varchar(75) null,
	ymSn varchar(75) null,
	employeeStatusId varchar(75) null,
	employeeNumber varchar(75) null,
	jobTitle varchar(75) null,
	jobClass varchar(75) null,
	hoursOfOperation varchar(75) null
);

create table Counter (
	name varchar(75) not null primary key,
	currentId bigint
);

create table Country (
	countryId varchar(75) not null primary key,
	name varchar(75) null,
	a2 varchar(75) null,
	a3 varchar(75) null,
	number_ varchar(75) null,
	idd_ varchar(75) null,
	active_ bool
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
	createdOn timestamp null,
	createdByUserId varchar(75) null,
	createdByUserName varchar(75) null,
	updatedOn timestamp null,
	updatedBy varchar(75) null,
	className varchar(75) null,
	classPK varchar(75) null,
	active_ bool
);

create table DLFileEntry (
	folderId varchar(75) not null,
	name varchar(100) not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	versionUserId varchar(75) null,
	versionUserName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	title varchar(100) null,
	description text null,
	version double precision,
	size_ integer,
	readCount integer,
	extraSettings text null,
	primary key (folderId, name)
);

create table DLFileRank (
	companyId varchar(75) not null,
	userId varchar(75) not null,
	folderId varchar(75) not null,
	name varchar(100) not null,
	createDate timestamp null,
	primary key (companyId, userId, folderId, name)
);

create table DLFileShortcut (
	fileShortcutId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	folderId varchar(75) null,
	toFolderId varchar(75) null,
	toName varchar(75) null
);

create table DLFileVersion (
	folderId varchar(75) not null,
	name varchar(100) not null,
	version double precision not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	size_ integer,
	primary key (folderId, name, version)
);

create table DLFolder (
	folderId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentFolderId varchar(75) null,
	name varchar(100) null,
	description text null,
	lastPostDate timestamp null
);

create table EmailAddress (
	emailAddressId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar(75) null,
	classPK varchar(75) null,
	address varchar(75) null,
	typeId integer,
	primary_ bool
);

create table Group_ (
	groupId bigint not null primary key,
	companyId varchar(75) not null,
	creatorUserId varchar(75) null,
	className varchar(75) null,
	classPK varchar(75) null,
	parentGroupId bigint,
	liveGroupId bigint,
	name varchar(75) null,
	description text null,
	type_ varchar(75) null,
	friendlyURL varchar(75) null,
	active_ bool
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
	createDate timestamp null,
	modifiedDate timestamp null,
	parentFolderId varchar(75) null,
	name varchar(75) null,
	description text null
);

create table IGImage (
	companyId varchar(75) not null,
	imageId varchar(75) not null,
	userId varchar(75) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	folderId varchar(75) null,
	description text null,
	height integer,
	width integer,
	size_ integer,
	primary key (companyId, imageId)
);

create table Image (
	imageId varchar(200) not null primary key,
	modifiedDate timestamp null,
	text_ text null,
	type_ varchar(75) null
);

create table JournalArticle (
	companyId varchar(75) not null,
	groupId bigint not null,
	articleId varchar(75) not null,
	version double precision not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	title varchar(75) null,
	description text null,
	content text null,
	type_ varchar(75) null,
	structureId varchar(75) null,
	templateId varchar(75) null,
	displayDate timestamp null,
	approved bool,
	approvedByUserId varchar(75) null,
	approvedByUserName varchar(75) null,
	approvedDate timestamp null,
	expired bool,
	expirationDate timestamp null,
	reviewDate timestamp null,
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
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(75) null,
	description text null,
	xsd text null,
	primary key (companyId, groupId, structureId)
);

create table JournalTemplate (
	companyId varchar(75) not null,
	groupId bigint not null,
	templateId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	structureId varchar(75) null,
	name varchar(75) null,
	description text null,
	xsl text null,
	langType varchar(75) null,
	smallImage bool,
	smallImageURL varchar(75) null,
	primary key (companyId, groupId, templateId)
);

create table Layout (
	layoutId varchar(75) not null,
	ownerId varchar(75) not null,
	companyId varchar(75) not null,
	parentLayoutId varchar(75) null,
	name text null,
	title text null,
	type_ varchar(75) null,
	typeSettings text null,
	hidden_ bool,
	friendlyURL varchar(75) null,
	iconImage bool,
	themeId varchar(75) null,
	colorSchemeId varchar(75) null,
	css varchar(75) null,
	priority integer,
	primary key (layoutId, ownerId)
);

create table LayoutSet (
	ownerId varchar(75) not null primary key,
	companyId varchar(75) not null,
	groupId bigint not null,
	userId varchar(75) not null,
	privateLayout bool,
	logo bool,
	themeId varchar(75) null,
	colorSchemeId varchar(75) null,
	css varchar(75) null,
	pageCount integer,
	virtualHost varchar(75) null
);

create table ListType (
	listTypeId integer not null primary key,
	name varchar(75) null,
	type_ varchar(75) null
);

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

create table MBCategory (
	categoryId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentCategoryId varchar(75) null,
	name varchar(75) null,
	description text null,
	lastPostDate timestamp null
);

create table MBDiscussion (
	discussionId varchar(75) not null primary key,
	className varchar(75) null,
	classPK varchar(75) null,
	threadId varchar(75) null
);

create table MBMessage (
	topicId varchar(75) not null,
	messageId varchar(75) not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	categoryId varchar(75) null,
	threadId varchar(75) null,
	parentMessageId varchar(75) null,
	subject varchar(75) null,
	body text null,
	attachments bool,
	anonymous bool,
	primary key (topicId, messageId)
);

create table MBMessageFlag (
	topicId varchar(75) not null,
	messageId varchar(75) not null,
	userId varchar(75) not null,
	flag varchar(75) null,
	primary key (topicId, messageId, userId)
);

create table MBStatsUser (
	groupId bigint not null,
	userId varchar(75) not null,
	messageCount integer,
	lastPostDate timestamp null,
	primary key (groupId, userId)
);

create table MBThread (
	threadId varchar(75) not null primary key,
	categoryId varchar(75) null,
	topicId varchar(75) null,
	rootMessageId varchar(75) null,
	messageCount integer,
	viewCount integer,
	lastPostByUserId varchar(75) null,
	lastPostDate timestamp null,
	priority double precision
);

create table Organization_ (
	organizationId varchar(75) not null primary key,
	companyId varchar(75) not null,
	parentOrganizationId varchar(75) null,
	name varchar(75) null,
	recursable bool,
	regionId varchar(75) null,
	countryId varchar(75) null,
	statusId integer,
	comments text null
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
	organizationId varchar(75) null,
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
	createDate timestamp null,
	password_ varchar(75) null
);

create table Permission_ (
	permissionId bigint not null primary key,
	companyId varchar(75) not null,
	actionId varchar(75) null,
	resourceId bigint
);

create table Phone (
	phoneId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar(75) null,
	classPK varchar(75) null,
	number_ varchar(75) null,
	extension varchar(75) null,
	typeId integer,
	primary_ bool
);

create table PluginSetting (
	pluginSettingId bigint not null primary key,
	companyId varchar(75) not null,
	pluginId varchar(75) null,
	pluginType varchar(75) null,
	roles varchar(75) null,
	active_ bool
);

create table PollsChoice (
	questionId varchar(75) not null,
	choiceId varchar(75) not null,
	description varchar(75) null,
	primary key (questionId, choiceId)
);

create table PollsQuestion (
	questionId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	title varchar(75) null,
	description text null,
	expirationDate timestamp null,
	lastVoteDate timestamp null
);

create table PollsVote (
	questionId varchar(75) not null,
	userId varchar(75) not null,
	choiceId varchar(75) null,
	voteDate timestamp null,
	primary key (questionId, userId)
);

create table Portlet (
	portletId varchar(75) not null,
	companyId varchar(75) not null,
	roles varchar(75) null,
	active_ bool,
	primary key (portletId, companyId)
);

create table PortletPreferences (
	portletId varchar(75) not null,
	layoutId varchar(75) not null,
	ownerId varchar(75) not null,
	preferences text null,
	primary key (portletId, layoutId, ownerId)
);

create table RatingsEntry (
	entryId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar(75) null,
	classPK varchar(75) null,
	score double precision
);

create table RatingsStats (
	statsId bigint not null primary key,
	className varchar(75) null,
	classPK varchar(75) null,
	totalEntries integer,
	totalScore double precision,
	averageScore double precision
);

create table Region (
	regionId varchar(75) not null primary key,
	countryId varchar(75) null,
	regionCode varchar(75) null,
	name varchar(75) null,
	active_ bool
);

create table Release_ (
	releaseId bigint not null primary key,
	createDate timestamp null,
	modifiedDate timestamp null,
	buildNumber integer,
	buildDate timestamp null,
	verified bool
);

create table Resource_ (
	resourceId bigint not null primary key,
	codeId bigint,
	primKey varchar(200) null
);

create table ResourceCode (
	codeId bigint not null primary key,
	companyId varchar(75) not null,
	name varchar(75) null,
	scope varchar(75) null
);

create table Role_ (
	roleId varchar(75) not null primary key,
	companyId varchar(75) not null,
	className varchar(75) null,
	classPK varchar(75) null,
	name varchar(75) null,
	description text null,
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

create table ShoppingCart (
	cartId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	itemIds text null,
	couponIds text null,
	altShipping integer,
	insure bool
);

create table ShoppingCategory (
	categoryId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	parentCategoryId varchar(75) null,
	name varchar(75) null,
	description text null
);

create table ShoppingCoupon (
	couponId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(75) null,
	description text null,
	startDate timestamp null,
	endDate timestamp null,
	active_ bool,
	limitCategories text null,
	limitSkus text null,
	minOrder double precision,
	discount double precision,
	discountType varchar(75) null
);

create table ShoppingItem (
	itemId varchar(75) not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	categoryId varchar(75) null,
	sku varchar(75) null,
	name varchar(200) null,
	description text null,
	properties text null,
	fields_ bool,
	fieldsQuantities text null,
	minQuantity integer,
	maxQuantity integer,
	price double precision,
	discount double precision,
	taxable bool,
	shipping double precision,
	useShippingFormula bool,
	requiresShipping bool,
	stockQuantity integer,
	featured_ bool,
	sale_ bool,
	smallImage bool,
	smallImageURL varchar(75) null,
	mediumImage bool,
	mediumImageURL varchar(75) null,
	largeImage bool,
	largeImageURL varchar(75) null
);

create table ShoppingItemField (
	itemFieldId varchar(75) not null primary key,
	itemId varchar(75) null,
	name varchar(75) null,
	values_ text null,
	description text null
);

create table ShoppingItemPrice (
	itemPriceId varchar(75) not null primary key,
	itemId varchar(75) null,
	minQuantity integer,
	maxQuantity integer,
	price double precision,
	discount double precision,
	taxable bool,
	shipping double precision,
	useShippingFormula bool,
	status integer
);

create table ShoppingOrder (
	orderId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	tax double precision,
	shipping double precision,
	altShipping varchar(75) null,
	requiresShipping bool,
	insure bool,
	insurance double precision,
	couponIds varchar(75) null,
	couponDiscount double precision,
	billingFirstName varchar(75) null,
	billingLastName varchar(75) null,
	billingEmailAddress varchar(75) null,
	billingCompany varchar(75) null,
	billingStreet varchar(75) null,
	billingCity varchar(75) null,
	billingState varchar(75) null,
	billingZip varchar(75) null,
	billingCountry varchar(75) null,
	billingPhone varchar(75) null,
	shipToBilling bool,
	shippingFirstName varchar(75) null,
	shippingLastName varchar(75) null,
	shippingEmailAddress varchar(75) null,
	shippingCompany varchar(75) null,
	shippingStreet varchar(75) null,
	shippingCity varchar(75) null,
	shippingState varchar(75) null,
	shippingZip varchar(75) null,
	shippingCountry varchar(75) null,
	shippingPhone varchar(75) null,
	ccName varchar(75) null,
	ccType varchar(75) null,
	ccNumber varchar(75) null,
	ccExpMonth integer,
	ccExpYear integer,
	ccVerNumber varchar(75) null,
	comments text null,
	ppTxnId varchar(75) null,
	ppPaymentStatus varchar(75) null,
	ppPaymentGross double precision,
	ppReceiverEmail varchar(75) null,
	ppPayerEmail varchar(75) null,
	sendOrderEmail bool,
	sendShippingEmail bool
);

create table ShoppingOrderItem (
	orderId varchar(75) not null,
	itemId varchar(75) not null,
	sku varchar(75) null,
	name varchar(200) null,
	description text null,
	properties text null,
	price double precision,
	quantity integer,
	shippedDate timestamp null,
	primary key (orderId, itemId)
);

create table Subscription (
	subscriptionId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar(75) null,
	classPK varchar(75) null,
	frequency varchar(75) null
);

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

create table User_ (
	userId varchar(75) not null primary key,
	companyId varchar(75) not null,
	createDate timestamp null,
	modifiedDate timestamp null,
	contactId bigint,
	password_ varchar(75) null,
	passwordEncrypted bool,
	passwordExpirationDate timestamp null,
	passwordReset bool,
	screenName varchar(75) null,
	emailAddress varchar(75) null,
	languageId varchar(75) null,
	timeZoneId varchar(75) null,
	greeting varchar(75) null,
	resolution varchar(75) null,
	comments text null,
	loginDate timestamp null,
	loginIP varchar(75) null,
	lastLoginDate timestamp null,
	lastLoginIP varchar(75) null,
	failedLoginAttempts integer,
	agreedToTermsOfUse bool,
	active_ bool
);

create table UserGroup (
	userGroupId varchar(75) not null primary key,
	companyId varchar(75) not null,
	parentUserGroupId varchar(75) null,
	name varchar(75) null,
	description text null
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
	description varchar(75) null,
	externalUserId varchar(75) null,
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
	modifiedDate timestamp null,
	remoteAddr varchar(75) null,
	remoteHost varchar(75) null,
	userAgent varchar(75) null
);

create table UserTrackerPath (
	userTrackerPathId varchar(75) not null primary key,
	userTrackerId varchar(75) null,
	path text null,
	pathDate timestamp null
);

create table Website (
	websiteId bigint not null primary key,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	className varchar(75) null,
	classPK varchar(75) null,
	url varchar(75) null,
	typeId integer,
	primary_ bool
);

create table WikiNode (
	nodeId varchar(75) not null primary key,
	groupId bigint not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	modifiedDate timestamp null,
	name varchar(75) null,
	description text null,
	lastPostDate timestamp null
);

create table WikiPage (
	nodeId varchar(75) not null,
	title varchar(75) not null,
	version double precision not null,
	companyId varchar(75) not null,
	userId varchar(75) not null,
	userName varchar(75) null,
	createDate timestamp null,
	content text null,
	format varchar(75) null,
	head bool,
	primary key (nodeId, title, version)
);



insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('1', 'Canada', 'CA', 'CAN', '124', '001', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('2', 'China', 'CN', 'CHN', '156', '086', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('3', 'France', 'FR', 'FRA', '250', '033', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('4', 'Germany', 'DE', 'DEU', '276', '049', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('5', 'Hong Kong', 'HK', 'HKG', '344', '852', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('6', 'Hungary', 'HU', 'HUN', '348', '036', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('7', 'Israel', 'IL', 'ISR', '376', '972', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('8', 'Italy', 'IT', 'ITA', '380', '039', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('9', 'Japan', 'JP', 'JPN', '392', '081', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('10', 'South Korea', 'KP', 'KOR', '410', '082', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('11', 'Netherlands', 'NL', 'NLD', '528', '031', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('12', 'Portugal', 'PT', 'PRT', '620', '351', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('13', 'Russia', 'RU', 'RUS', '643', '007', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('14', 'Singapore', 'SG', 'SGP', '702', '065', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('15', 'Spain', 'ES', 'ESP', '724', '034', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('16', 'Turkey', 'TR', 'TUR', '792', '090', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('17', 'Vietnam', 'VM', 'VNM', '704', '084', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('18', 'United Kingdom', 'GB', 'GBR', '826', '044', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('19', 'United States', 'US', 'USA', '840', '001', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('20', 'Afghanistan', 'AF', 'AFG', '4', '093', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('21', 'Albania', 'AL', 'ALB', '8', '355', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('22', 'Algeria', 'DZ', 'DZA', '12', '213', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('23', 'American Samoa', 'AS', 'ASM', '16', '684', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('24', 'Andorra', 'AD', 'AND', '20', '376', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('25', 'Angola', 'AO', 'AGO', '24', '244', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('26', 'Anguilla', 'AI', 'AIA', '660', '264', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('27', 'Antarctica', 'AQ', 'ATA', '10', '672', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('28', 'Antigua', 'AG', 'ATG', '28', '268', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('29', 'Argentina', 'AR', 'ARG', '32', '054', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('30', 'Armenia', 'AM', 'ARM', '51', '374', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('31', 'Aruba', 'AW', 'ABW', '533', '297', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('32', 'Australia', 'AU', 'AUS', '36', '061', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('33', 'Austria', 'AT', 'AUT', '40', '043', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('34', 'Azerbaijan', 'AZ', 'AZE', '31', '994', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('35', 'Bahamas', 'BS', 'BHS', '44', '242', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('36', 'Bahrain', 'BH', 'BHR', '48', '973', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('37', 'Bangladesh', 'BD', 'BGD', '50', '880', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('38', 'Barbados', 'BB', 'BRB', '52', '246', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('39', 'Belarus', 'BY', 'BLR', '112', '375', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('40', 'Belgium', 'BE', 'BEL', '56', '032', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('41', 'Belize', 'BZ', 'BLZ', '84', '501', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('42', 'Benin', 'BJ', 'BEN', '204', '229', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('43', 'Bermuda', 'BM', 'BMU', '60', '441', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('44', 'Bhutan', 'BT', 'BTN', '64', '975', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('45', 'Bolivia', 'BO', 'BOL', '68', '591', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('46', 'Bosnia-Herzegovina', 'BA', 'BIH', '70', '387', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('47', 'Botswana', 'BW', 'BWA', '72', '267', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('48', 'Brazil', 'BR', 'BRA', '76', '055', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('49', 'British Virgin Islands', 'VG', 'VGB', '92', '284', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('50', 'Brunei', 'BN', 'BRN', '96', '673', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('51`', 'Bulgaria', 'BG', 'BGR', '100', '359', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('52', 'Burkina Faso', 'BF', 'BFA', '854', '226', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('53', 'Burma (Myanmar)', 'MM', 'MMR', '104', '095', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('54', 'Burundi', 'BI', 'BDI', '108', '257', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('55', 'Cambodia', 'KH', 'KHM', '116', '855', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('56', 'Cameroon', 'CM', 'CMR', '120', '237', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('57', 'Cape Verde Island', 'CV', 'CPV', '132', '238', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('58', 'Cayman Islands', 'KY', 'CYM', '136', '345', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('59', 'Central African Republic', 'CF', 'CAF', '140', '236', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('60', 'Chad', 'TD', 'TCD', '148', '235', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('61', 'Chile', 'CL', 'CHL', '152', '056', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('62', 'Christmas Island', 'CX', 'CXR', '162', '061', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('63', 'Cocos Islands', 'CC', 'CCK', '166', '061', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('64', 'Colombia', 'CO', 'COL', '170', '057', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('65', 'Comoros', 'KM', 'COM', '174', '269', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('66', 'Republic of Congo', 'CD', 'COD', '180', '242', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('67', 'Democratic Republic of Congo', 'CG', 'COG', '178', '243', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('68', 'Cook Islands', 'CK', 'COK', '184', '682', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('69', 'Costa Rica', 'CI', 'CRI', '188', '506', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('70', 'Croatia', 'HR', 'HRV', '191', '385', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('71', 'Cuba', 'CU', 'CUB', '192', '053', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('72', 'Cyprus', 'CY', 'CYP', '196', '357', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('73', 'Czech Republic', 'CZ', 'CZE', '203', '420', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('74', 'Denmark', 'DK', 'DNK', '208', '045', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('75', 'Djibouti', 'DJ', 'DJI', '262', '253', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('76', 'Dominica', 'DM', 'DMA', '212', '767', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('77', 'Dominican Republic', 'DO', 'DOM', '214', '809', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('78', 'Ecuador', 'EC', 'ECU', '218', '593', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('79', 'Egypt', 'EG', 'EGY', '818', '020', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('80', 'El Salvador', 'SV', 'SLV', '222', '503', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('81', 'Equatorial Guinea', 'GQ', 'GNQ', '226', '240', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('82', 'Eritrea', 'ER', 'ERI', '232', '291', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('83', 'Estonia', 'EE', 'EST', '233', '372', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('84', 'Ethiopia', 'ET', 'ETH', '231', '251', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('85', 'Faeroe Islands', 'FO', 'FRO', '234', '298', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('86', 'Falkland Islands', 'FK', 'FLK', '238', '500', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('87', 'Fiji Islands', 'FJ', 'FJI', '242', '679', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('88', 'Finland', 'FI', 'FIN', '246', '358', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('89', 'French Guiana', 'GF', 'GUF', '254', '594', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('90', 'French Polynesia', 'PF', 'PYF', '258', '689', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('91', 'Gabon', 'GA', 'GAB', '266', '241', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('92', 'Gambia', 'GM', 'GMB', '270', '220', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('93', 'Georgia', 'GE', 'GEO', '268', '995', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('94', 'Ghana', 'GH', 'GHA', '288', '233', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('95', 'Gibraltar', 'GI', 'GIB', '292', '350', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('96', 'Greece', 'GR', 'GRC', '300', '030', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('97', 'Greenland', 'GL', 'GRL', '304', '299', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('98', 'Grenada', 'GD', 'GRD', '308', '473', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('99', 'Guadeloupe', 'GP', 'GLP', '312', '590', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('100', 'Guam', 'GU', 'GUM', '316', '671', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('101', 'Guatemala', 'GT', 'GTM', '320', '502', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('102', 'Guinea', 'GN', 'GIN', '324', '224', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('103', 'Guinea-Bissau', 'GW', 'GNB', '624', '245', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('104', 'Guyana', 'GY', 'GUY', '328', '592', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('105', 'Haiti', 'HT', 'HTI', '332', '509', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('106', 'Honduras', 'HN', 'HND', '340', '504', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('107', 'Iceland', 'IS', 'ISL', '352', '354', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('108', 'India', 'IN', 'IND', '356', '091', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('109', 'Indonesia', 'ID', 'IDN', '360', '062', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('110', 'Iran', 'IR', 'IRN', '364', '098', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('111', 'Iraq', 'IQ', 'IRQ', '368', '964', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('112', 'Ireland', 'IE', 'IRL', '372', '353', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('113', 'Ivory Coast', 'CI', 'CIV', '384', '225', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('114', 'Jamaica', 'JM', 'JAM', '388', '876', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('115', 'Jordan', 'JO', 'JOR', '400', '962', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('116', 'Kazakhstan', 'KZ', 'KAZ', '398', '007', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('117', 'Kenya', 'KE', 'KEN', '404', '254', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('118', 'Kiribati', 'KI', 'KIR', '408', '686', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('119', 'Kuwait', 'KW', 'KWT', '414', '965', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('120', 'North Korea', 'KP', 'PRK', '408', '850', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('121', 'Kyrgyzstan', 'KG', 'KGZ', '471', '996', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('122', 'Laos', 'LA', 'LAO', '418', '856', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('123', 'Latvia', 'LV', 'LVA', '428', '371', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('124', 'Lebanon', 'LB', 'LBN', '422', '961', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('125', 'Lesotho', 'LS', 'LSO', '426', '266', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('126', 'Liberia', 'LR', 'LBR', '430', '231', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('127', 'Libya', 'LY', 'LBY', '434', '218', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('128', 'Liechtenstein', 'LI', 'LIE', '438', '423', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('129', 'Lithuania', 'LT', 'LTU', '440', '370', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('130', 'Luxembourg', 'LU', 'LUX', '442', '352', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('131', 'Macau', 'MO', 'MAC', '446', '853', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('132', 'Macedonia', 'MK', 'MKD', '807', '389', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('133', 'Madagascar', 'MG', 'MDG', '450', '261', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('134', 'Malawi', 'MW', 'MWI', '454', '265', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('135', 'Malaysia', 'MY', 'MYS', '458', '060', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('136', 'Maldives', 'MV', 'MDV', '462', '960', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('137', 'Mali', 'ML', 'MLI', '466', '223', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('138', 'Malta', 'MT', 'MLT', '470', '356', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('139', 'Marshall Islands', 'MH', 'MHL', '584', '692', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('140', 'Martinique', 'MQ', 'MTQ', '474', '596', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('141', 'Mauritania', 'MR', 'MRT', '478', '222', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('142', 'Mauritius', 'MU', 'MUS', '480', '230', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('143', 'Mayotte Island', 'YT', 'MYT', '175', '269', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('144', 'Mexico', 'MX', 'MEX', '484', '052', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('145', 'Micronesia', 'FM', 'FSM', '583', '691', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('146', 'Moldova', 'MD', 'MDA', '498', '373', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('147', 'Monaco', 'MC', 'MCO', '492', '377', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('148', 'Mongolia', 'MN', 'MNG', '496', '976', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('149', 'Montserrat', 'MS', 'MSR', '500', '664', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('150', 'Morocco', 'MA', 'MAR', '504', '212', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('151', 'Mozambique', 'MZ', 'MOZ', '508', '258', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('152', 'Myanmar (Burma)', 'MM', 'MMR', '104', '095', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('153', 'Namibia', 'NA', 'NAM', '516', '264', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('154', 'Nauru', 'NR', 'NRU', '520', '674', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('155', 'Nepal', 'NP', 'NPL', '524', '977', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('156', 'Netherlands Antilles', 'AN', 'ANT', '530', '599', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('157', 'New Caledonia', 'NC', 'NCL', '540', '687', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('158', 'New Zealand', 'NZ', 'NZL', '554', '064', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('159', 'Nicaragua', 'NE', 'NER', '558', '505', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('160', 'Niger', 'NE', 'NER', '562', '227', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('161', 'Nigeria', 'NG', 'NGA', '566', '234', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('162', 'Niue', 'NU', 'NIU', '570', '683', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('163', 'Norfolk Island', 'NF', 'NFK', '574', '672', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('164', 'Norway', 'NO', 'NOR', '578', '047', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('165', 'Oman', 'OM', 'OMN', '512', '968', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('166', 'Pakistan', 'PK', 'PAK', '586', '092', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('167', 'Palau', 'PW', 'PLW', '585', '680', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('168', 'Palestine', 'PS', 'PSE', '275', '970', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('169', 'Panama', 'PA', 'PAN', '591', '507', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('170', 'Papua New Guinea', 'PG', 'PNG', '598', '675', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('171', 'Paraguay', 'PY', 'PRY', '600', '595', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('172', 'Peru', 'PE', 'PER', '604', '051', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('173', 'Philippines', 'PH', 'PHL', '608', '063', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('174', 'Poland', 'PL', 'POL', '616', '048', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('175', 'Puerto Rico', 'PR', 'PRI', '630', '787', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('176', 'Qatar', 'QA', 'QAT', '634', '974', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('177', 'Reunion Island', 'RE', 'REU', '638', '262', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('178', 'Romania', 'RO', 'ROU', '642', '040', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('179', 'Rwanda', 'RW', 'RWA', '646', '250', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('180', 'St. Helena', 'SH', 'SHN', '654', '290', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('181', 'St. Kitts', 'KN', 'KNA', '659', '869', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('182', 'St. Lucia', 'LC', 'LCA', '662', '758', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('183', 'St. Pierre & Miquelon', 'PM', 'SPM', '666', '508', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('184', 'St. Vincent', 'VC', 'VCT', '670', '784', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('185', 'San Marino', 'SM', 'SMR', '674', '378', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('186', 'Sao Tome & Principe', 'ST', 'STP', '678', '239', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('187', 'Saudi Arabia', 'SA', 'SAU', '682', '966', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('188', 'Senegal', 'SN', 'SEN', '686', '221', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('189', 'Serbia', 'CS', 'SCG', '891', '381', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('190', 'Seychelles', 'SC', 'SYC', '690', '248', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('191', 'Sierra Leone', 'SL', 'SLE', '694', '249', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('192', 'Slovakia', 'SK', 'SVK', '703', '421', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('193', 'Slovenia', 'SI', 'SVN', '705', '386', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('194', 'Solomon Islands', 'SB', 'SLB', '90', '677', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('195', 'Somalia', 'SO', 'SOM', '706', '252', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('196', 'South Africa', 'ZA', 'ZAF', '710', '027', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('197', 'Sri Lanka', 'LK', 'LKA', '144', '094', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('198', 'Sudan', 'SD', 'SDN', '736', '095', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('199', 'Suriname', 'SR', 'SUR', '740', '597', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('200', 'Swaziland', 'SZ', 'SWZ', '748', '268', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('201', 'Sweden', 'SE', 'SWE', '752', '046', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('202', 'Switzerland', 'CH', 'CHE', '756', '041', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('203', 'Syria', 'SY', 'SYR', '760', '963', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('204', 'Taiwan', 'TW', 'TWN', '158', '886', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('205', 'Tajikistan', 'TJ', 'TJK', '762', '992', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('206', 'Tanzania', 'TZ', 'TZA', '834', '255', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('207', 'Thailand', 'TH', 'THA', '764', '066', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('208', 'Togo', 'TG', 'TGO', '768', '228', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('209', 'Tonga', 'TO', 'TON', '776', '676', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('210', 'Trinidad & Tobago', 'TT', 'TTO', '780', '868', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('211', 'Tunisia', 'TN', 'TUN', '788', '216', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('212', 'Turkmenistan', 'TM', 'TKM', '795', '993', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('213', 'Turks & Caicos', 'TC', 'TCA', '796', '649', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('214', 'Tuvalu', 'TV', 'TUV', '798', '688', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('215', 'Uganda', 'UG', 'UGA', '800', '256', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('216', 'Ukraine', 'UA', 'UKR', '804', '380', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('217', 'United Arab Emirates', 'AE', 'ARE', '784', '971', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('218', 'Uruguay', 'UY', 'URY', '858', '598', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('219', 'Uzbekistan', 'UZ', 'UZB', '860', '998', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('220', 'Vanuatu', 'VU', 'VUT', '548', '678', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('221', 'Vatican City', 'VA', 'VAT', '336', '039', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('222', 'Venezuela', 'VE', 'VEN', '862', '058', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('223', 'Wallis & Futuna', 'WF', 'WLF', '876', '681', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('224', 'Western Samoa', 'EH', 'ESH', '732', '685', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('225', 'Yemen', 'YE', 'YEM', '887', '967', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('226', 'Yugoslavia', 'MK', 'MKD', '446', '381', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('227', 'Zambia', 'ZM', 'ZMB', '894', '260', true);
insert into Country (countryId, name, a2, a3, number_, idd_, active_) values ('228', 'Zimbabwe', 'ZW', 'ZWE', '716', '263', true);

insert into Region (regionId, countryId, regionCode, name, active_) values ('1', '19', 'AL', 'Alabama', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('2', '19', 'AK', 'Alaska', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('3', '19', 'AZ', 'Arizona', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('4', '19', 'AR', 'Arkansas', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('5', '19', 'CA', 'California', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('6', '19', 'CO', 'Colorado', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('7', '19', 'CT', 'Connecticut', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('8', '19', 'DC', 'District of Columbia', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('9', '19', 'DE', 'Delaware', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('10', '19', 'FL', 'Florida', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('11', '19', 'GA', 'Georgia', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('12', '19', 'HI', 'Hawaii', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('13', '19', 'ID', 'Idaho', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('14', '19', 'IL', 'Illinois', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('15', '19', 'IN', 'Indiana', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('16', '19', 'IA', 'Iowa', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('17', '19', 'KS', 'Kansas', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('18', '19', 'KY', 'Kentucky ', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('19', '19', 'LA', 'Louisiana ', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('20', '19', 'ME', 'Maine', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('21', '19', 'MD', 'Maryland', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('22', '19', 'MA', 'Massachusetts', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('23', '19', 'MI', 'Michigan', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('24', '19', 'MN', 'Minnesota', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('25', '19', 'MS', 'Mississippi', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('26', '19', 'MO', 'Missouri', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('27', '19', 'MT', 'Montana', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('28', '19', 'NE', 'Nebraska', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('29', '19', 'NV', 'Nevada', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('30', '19', 'NH', 'New Hampshire', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('31', '19', 'NJ', 'New Jersey', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('32', '19', 'NM', 'New Mexico', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('33', '19', 'NY', 'New York', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('34', '19', 'NC', 'North Carolina', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('35', '19', 'ND', 'North Dakota', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('36', '19', 'OH', 'Ohio', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('37', '19', 'OK', 'Oklahoma ', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('38', '19', 'OR', 'Oregon', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('39', '19', 'PA', 'Pennsylvania', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('40', '19', 'PR', 'Puerto Rico', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('41', '19', 'RI', 'Rhode Island', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('42', '19', 'SC', 'South Carolina', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('43', '19', 'SD', 'South Dakota', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('44', '19', 'TN', 'Tennessee', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('45', '19', 'TX', 'Texas', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('46', '19', 'UT', 'Utah', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('47', '19', 'VT', 'Vermont', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('48', '19', 'VA', 'Virginia', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('49', '19', 'WA', 'Washington', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('50', '19', 'WV', 'West Virginia', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('51', '19', 'WI', 'Wisconsin', true);
insert into Region (regionId, countryId, regionCode, name, active_) values ('52', '19', 'WY', 'Wyoming', true);

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


insert into Release_ (releaseId, createDate, modifiedDate, buildNumber, verified) values ('1', current_timestamp, current_timestamp, 3901, true);


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


