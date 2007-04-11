create table Release_ (
	releaseId varchar(100) not null primary key,
	createDate date null,
	modifiedDate date null,
	buildNumber integer null,
	buildDate date null
);

alter table ShoppingCart add altShipping integer null;

alter table ShoppingOrder add altShipping varchar(100) null;
