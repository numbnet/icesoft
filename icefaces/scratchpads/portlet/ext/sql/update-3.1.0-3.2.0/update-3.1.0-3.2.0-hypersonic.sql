create table Release_ (
	releaseId varchar(100) not null primary key,
	createDate timestamp null,
	modifiedDate timestamp null,
	buildNumber int null,
	buildDate timestamp null
);

alter table ShoppingCart add altShipping int null;

alter table ShoppingOrder add altShipping varchar(100) null;
