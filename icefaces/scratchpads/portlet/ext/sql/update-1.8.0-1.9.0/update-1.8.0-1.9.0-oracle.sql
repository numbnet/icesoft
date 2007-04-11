alter table DLFileProfile add versionUserId varchar2(100) not null default '';
alter table DLFileProfile add versionUserName varchar2(100) null;

alter table DLFileVersion add userId varchar2(100) not null default '';
alter table DLFileVersion add userName varchar2(100) null;

alter table JournalArticle add internal_ number(1, 0);

alter table ShoppingItem add sku varchar2(100) null;
alter table ShoppingItem add minQuantity number(30,0);
alter table ShoppingItem add maxQuantity number(30,0);
alter table ShoppingItem add stockQuantity number(30,0);
alter table ShoppingItem add smallImageURL varchar2(100) null;
alter table ShoppingItem add mediumImageURL varchar2(100) null;
alter table ShoppingItem add largeImageURL varchar2(100) null;

create table ShoppingItemPrice (
	itemPriceId varchar2(100) not null primary key,
	itemId varchar2(100) null,
	minQuantity number(30,0),
	maxQuantity number(30,0),
	price number(30,20),
	discount number(30,20),
	taxable number(1, 0),
	shipping number(30,20),
	useShippingFormula number(1, 0),
	status number(30,0)
);

alter table ShoppingOrder add tax number(30,20);
alter table ShoppingOrder add shipping number(30,20);

alter table ShoppingOrderItem add sku varchar2(100) null;
alter table ShoppingOrderItem add name varchar2(100) null;
alter table ShoppingOrderItem add description varchar2(4000) null;
alter table ShoppingOrderItem add properties varchar2(4000) null;

drop table SSEntry;
