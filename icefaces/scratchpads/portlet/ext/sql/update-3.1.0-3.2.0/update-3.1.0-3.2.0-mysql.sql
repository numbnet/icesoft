create table Release_ (
	releaseId varchar(100) not null primary key,
	createDate datetime null,
	modifiedDate datetime null,
	buildNumber integer null,
	buildDate datetime null
);

alter table ShoppingCart add altShipping integer null;

alter table ShoppingOrder add altShipping varchar(100) null;
