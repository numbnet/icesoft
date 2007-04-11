create table ComicEntry (
	entryId varchar(100) not null primary key,
	companyId varchar(100) not null,
	name varchar(100) not null,
	url varchar(100) not null,
	imageSearchKey varchar(100) not null,
	active_ tinyint not null
);

insert into Counter values ('com.liferay.portlet.comic.model.ComicEntry', 5);

insert into ComicEntry (entryId, companyId, name, url, imageSearchKey, active_) values ('1', 'liferay.com', 'FoxTrot', 'http://www.ucomics.com/foxtrot/', '<img src="http://images.ucomics.com/comics/ft/', 1);
insert into ComicEntry (entryId, companyId, name, url, imageSearchKey, active_) values ('2', 'liferay.com', 'Calvin and Hobbes', 'http://www.ucomics.com/calvinandhobbes/', '<img src="http://images.ucomics.com/comics/ch/', 1);
insert into ComicEntry (entryId, companyId, name, url, imageSearchKey, active_) values ('3', 'liferay.com', 'Dilbert', 'http://news.yahoo.com/news?tmpl=story&u=/umedia/cx_dilbert_umedia/latest', '<img src="http://us.news1.yimg.com/us.yimg.com/p/umedia/', 1);
insert into ComicEntry (entryId, companyId, name, url, imageSearchKey, active_) values ('4', 'liferay.com', 'Garfield', 'http://www.ucomics.com/garfield/', '<img src="http://images.ucomics.com/comics/ga/', 1);
insert into ComicEntry (entryId, companyId, name, url, imageSearchKey, active_) values ('5', 'liferay.com', 'Get Fuzzy', 'http://news.yahoo.com/news?tmpl=story&u=/umedia/cx_getfuzzy_umedia/latest', '<img src="http://us.news1.yimg.com/us.yimg.com/p/umedia/', 1);

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
	description longtext,
	externalUserId varchar(100),
	primary key (userId, type_)
);

create table Users_ComicEntries (
	userId varchar(100) not null,
	entryId varchar(100) not null
);
