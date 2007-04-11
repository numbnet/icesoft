create table ComicEntry (
	entryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	name varchar(100) not null,
	url varchar(100) not null,
	imageSearchKey varchar(100) not null,
	active_ smallint not null
);



create table JournalContentSearch (
	layoutId varchar(100) not null,
	userId varchar(100) not null,
	portletId varchar(100) not null,
	articleId varchar(100),
	primary key (layoutId, userId, portletId)
);

create table UserIdMapper (
	userId varchar(100) not null,
	type_ varchar(100) not null,
	description varchar(4000),
	externalUserId varchar(100),
	primary key (userId, type_)
);

create table Users_ComicEntries (
	userId varchar(100) not null,
	entryId varchar(100) not null
);
