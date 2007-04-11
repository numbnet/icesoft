create table Release_ (
	releaseId varchar(100) not null primary key,
	createDate datetime null,
	modifiedDate datetime null,
	buildNumber int null,
	buildDate datetime null
)
go

alter table ShoppingCart add altShipping int null;

alter table ShoppingOrder add altShipping varchar(100) null;
