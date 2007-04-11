create table Release_ (
	releaseId varchar2(100) not null primary key,
	createDate timestamp null,
	modifiedDate timestamp null,
	buildNumber number(30,0) null,
	buildDate timestamp null
);

alter table ShoppingCart add altShipping number(30,0) null;

alter table ShoppingOrder add altShipping varchar2(100) null;
