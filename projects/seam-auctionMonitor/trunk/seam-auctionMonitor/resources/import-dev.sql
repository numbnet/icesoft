insert into Bidder (username, password, name) values ('jguglielmin', 'jjjjj', 'Judy Guglielmin')
insert into Bidder (username, password, name) values ('demo', 'demo', 'Demo User')
insert into Bidder (username, password, name) values ('brad', 'demo', 'Brad Kroeger')
insert into Bidder (username, password, name) values ('yip', 'demo', 'Yip Ng')
insert into Bidder (username, password, name) values ('patrick', 'demo', 'Patrick Corless')
insert into auctionitem (itemId,bidCount,currency,description,imageFile,location,price,seller,site,title,expiresindays) values (4501921328,0,'USD','Used icebreaker with very few dents, comes with manual.','file:/images/icebreaker.jpg','Calgary, Alberta Canada',5.0,'ICEsoft Technologies, Inc.','http://www.icesoft.com','ICEsoft Icebreaker',3)
insert into auctionitem (itemId, bidCount,currency,description,imageFile,location,price,seller,site,title,expiresindays) values (4501908972,0,'USD','A single sharpened ice skate, size 7.','file:/images/iceskate.jpg','Calgary, Alberta Canada',100.0,'ICEsoft Technologies, Inc.','http://www.icesoft.com','ICEsoft Ice Skate',4)
insert into auctionitem (itemId, bidCount,currency,description,imageFile,location,price,seller,site,title,expiresindays) values (4501921327,0,'USD','Beautiful ice car with metal car filling.','file:/images/icecar.jpg','Calgary, Alberta Canada',10.0,'ICEsoft Technologies, Inc.','http://www.icesoft.com','ICEsoft Ice Car',12)
insert into auctionitem (itemId, bidCount,currency,description,imageFile,location,price,seller,site,title,expiresindays) values (4501921417,0,'USD','Put him on the ice and watch him go!  Requires food and water.', 'file:/images/icesailor.jpg','Calgary, Alberta Canada',10000.0,'ICEsoft Technologies, Inc.','http://www.icesoft.com','ICEsoft Ice Sailor',1)
insert into Bid (user_username, auctionItem_itemId, bidValue, timestamp, creditCard, creditCardName, creditCardExpiryMonth, creditCardExpiryYear) values ('jguglielmin', 4501921327, 12.34, '2007-06-08 15:13:03', '1234567890123456', 'Visa', 10, 2008)
insert into Bid (user_username, auctionItem_itemId, bidValue, timestamp, creditCard, creditCardName, creditCardExpiryMonth, creditCardExpiryYear) values ('demo', 4501921328, 88.88, '2007-04-29 19:38:29', '5896425631025489', 'Master Card', 4, 2010)
insert into Bid (user_username, auctionItem_itemId, bidValue, timestamp, creditCard, creditCardName, creditCardExpiryMonth, creditCardExpiryYear) values ('patrick', 4501921328, 107, '2007-04-30 19:38:29', '5896425631025489', 'Master Card', 4, 2010)
insert into Bid (user_username, auctionItem_itemId, bidValue, timestamp, creditCard, creditCardName, creditCardExpiryMonth, creditCardExpiryYear) values ('brad', 4501908972, 253.2, '1998-05-02 01:23:56.123', '6952145698745258', 'Amex', 6, 2009)
insert into Bid (user_username, auctionItem_itemId, bidValue, timestamp, creditCard, creditCardName, creditCardExpiryMonth, creditCardExpiryYear) values ('yip', 4501908972, 288, '2001-11-01 22:13:09.234', '3659487960215847', 'CapitalOne', 8, 2020)
insert into Bid (user_username, auctionItem_itemId, bidValue, timestamp, creditCard, creditCardName, creditCardExpiryMonth, creditCardExpiryYear) values ('demo', 4501908972, 568, '2001-10-30 22:13:09.234', '3659487960215847', 'CapitalOne', 8, 2020)
