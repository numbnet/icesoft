alter table DLFileProfile add versionUserId varchar(100) not null default '';
alter table DLFileProfile add versionUserName varchar(100) null;

alter table DLFileVersion add userId varchar(100) not null default '';
alter table DLFileVersion add userName varchar(100) null;

alter table JournalArticle add internal_ boolean;

alter table ShoppingItem add sku varchar(100) null;
alter table ShoppingItem add minQuantity int;
alter table ShoppingItem add maxQuantity int;
alter table ShoppingItem add stockQuantity int;
alter table ShoppingItem add smallImageURL varchar(100) null;
alter table ShoppingItem add mediumImageURL varchar(100) null;
alter table ShoppingItem add largeImageURL varchar(100) null;

create table ShoppingItemPrice (
	itemPriceId varchar(100) not null primary key,
	itemId varchar(100) null,
	minQuantity int,
	maxQuantity int,
	price float,
	discount float,
	taxable boolean,
	shipping float,
	useShippingFormula boolean,
	status int
);

alter table ShoppingOrder add tax float;
alter table ShoppingOrder add shipping float;

alter table ShoppingOrderItem add sku varchar(100) null;
alter table ShoppingOrderItem add name varchar(100) null;
alter table ShoppingOrderItem add description varchar null;
alter table ShoppingOrderItem add properties varchar null;

drop table SSEntry;
