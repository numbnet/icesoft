create table Release_ (
	releaseId varchar(100) not null primary key,
	createDate timestamp,
	modifiedDate timestamp,
	buildNumber integer,
	buildDate timestamp
);

alter table ShoppingCart add altShipping integer;

alter table ShoppingOrder add altShipping varchar(100);
