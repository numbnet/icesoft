alter table DLFileProfile add versionUserId varchar(100) not null default '';
alter table DLFileProfile add versionUserName varchar(100);

alter table DLFileVersion add userId varchar(100) not null default '';
alter table DLFileVersion add userName varchar(100);

alter table JournalArticle add internal_ smallint;

alter table ShoppingItem add sku varchar(100);
alter table ShoppingItem add minQuantity integer;
alter table ShoppingItem add maxQuantity integer;
alter table ShoppingItem add stockQuantity integer;
alter table ShoppingItem add smallImageURL varchar(100);
alter table ShoppingItem add mediumImageURL varchar(100);
alter table ShoppingItem add largeImageURL varchar(100);

create table ShoppingItemPrice (
	itemPriceId varchar(100) not null primary key,
	itemId varchar(100),
	minQuantity integer,
	maxQuantity integer,
	price double precision,
	discount double precision,
	taxable smallint,
	shipping double precision,
	useShippingFormula smallint,
	status integer
);

alter table ShoppingOrder add tax double precision;
alter table ShoppingOrder add shipping double precision;

alter table ShoppingOrderItem add sku varchar(100);
alter table ShoppingOrderItem add name varchar(100);
alter table ShoppingOrderItem add description varchar(4000);
alter table ShoppingOrderItem add properties varchar(4000);

drop table SSEntry;
