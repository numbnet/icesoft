create index IX_93D5AD4E on Address (companyId)
go
create index IX_4F4BDD05 on Address (companyId, className)
go
create index IX_DB84CC7E on Address (companyId, className, classPK)
go
create index IX_5BC8B0D4 on Address (userId)
go

create index IX_C49DD10C on BlogsCategory (parentCategoryId)
go

create index IX_B0608DF4 on BlogsEntry (categoryId)
go
create index IX_72EF6041 on BlogsEntry (companyId)
go
create index IX_81A50303 on BlogsEntry (groupId)
go

create index IX_443BDC38 on BookmarksEntry (folderId)
go

create index IX_7F703619 on BookmarksFolder (groupId)
go
create index IX_967799C0 on BookmarksFolder (groupId, parentFolderId)
go

create index IX_12EE4898 on CalEvent (groupId)
go
create index IX_FCD7C63D on CalEvent (groupId, type_)
go

create index IX_66D496A3 on Contact_ (companyId)
go


create index IX_24A846D1 on DLFileEntry (folderId)
go

create index IX_40B56512 on DLFileRank (folderId, name)
go
create index IX_EED06670 on DLFileRank (userId)
go

create index IX_E56EC6AD on DLFileShortcut (folderId)
go
create index IX_CA2708A2 on DLFileShortcut (toFolderId, toName)
go

create index IX_9CD91DB6 on DLFileVersion (folderId, name)
go

create index IX_A74DB14C on DLFolder (companyId)
go
create index IX_F2EA1ACE on DLFolder (groupId)
go
create index IX_49C37475 on DLFolder (groupId, parentFolderId)
go
create index IX_51556082 on DLFolder (parentFolderId, name)
go

create index IX_1BB072CA on EmailAddress (companyId)
go
create index IX_A9801209 on EmailAddress (companyId, className)
go
create index IX_C161FBFA on EmailAddress (companyId, className, classPK)
go
create index IX_7B43CD8 on EmailAddress (userId)
go

create index IX_5849ABF2 on Group_ (companyId, className, classPK)
go
create index IX_5BDDB872 on Group_ (companyId, friendlyURL)
go
create index IX_5AA68501 on Group_ (companyId, name)
go
create index IX_16218A38 on Group_ (liveGroupId)
go

create index LIFERAY_001 on Groups_Permissions (permissionId)
go

create index IX_206498F8 on IGFolder (groupId)
go
create index IX_1A605E9F on IGFolder (groupId, parentFolderId)
go

create index IX_4438CA80 on IGImage (folderId)
go

create index IX_DFF98523 on JournalArticle (companyId)
go
create index IX_29BD22DA on JournalArticle (companyId, groupId, articleId)
go
create index IX_A0C28B17 on JournalArticle (companyId, groupId, structureId)
go
create index IX_EC743CD0 on JournalArticle (companyId, groupId, templateId)
go
create index IX_9356F865 on JournalArticle (groupId)
go

create index IX_4D73E06F on JournalContentSearch (companyId, groupId, articleId)
go
create index IX_972C13BA on JournalContentSearch (groupId)
go
create index IX_ABEEA675 on JournalContentSearch (layoutId, ownerId)
go
create index IX_F09DD5EE on JournalContentSearch (ownerId)
go
create index IX_4A642025 on JournalContentSearch (ownerId, groupId, articleId)
go

create index IX_47EF5658 on JournalStructure (companyId, structureId)
go
create index IX_B97F5608 on JournalStructure (groupId)
go

create index IX_AB3E5F05 on JournalTemplate (companyId, groupId, structureId)
go
create index IX_D7A3867A on JournalTemplate (companyId, templateId)
go
create index IX_77923653 on JournalTemplate (groupId)
go

create index IX_1A0B984E on Layout (ownerId)
go
create index IX_E230D266 on Layout (ownerId, friendlyURL)
go
create index IX_9AF212B1 on Layout (ownerId, parentLayoutId)
go

create index IX_A34FBC19 on LayoutSet (companyId, virtualHost)
go
create index IX_A40B8BEC on LayoutSet (groupId)
go

create index IX_2932DD37 on ListType (type_)
go

create index IX_69951A25 on MBBan (banUserId)
go
create index IX_5C3FF12A on MBBan (groupId)
go
create index IX_8ABC4E3B on MBBan (groupId, banUserId)
go
create index IX_48814BBA on MBBan (userId)
go

create index IX_BC735DCF on MBCategory (companyId)
go
create index IX_BB870C11 on MBCategory (groupId)
go
create index IX_ED292508 on MBCategory (groupId, parentCategoryId)
go

create index IX_B628DAD3 on MBDiscussion (className, classPK)
go

create index IX_3C865EE5 on MBMessage (categoryId)
go
create index IX_138C7F1E on MBMessage (categoryId, threadId)
go
create index IX_75B95071 on MBMessage (threadId)
go
create index IX_A7038CD7 on MBMessage (threadId, parentMessageId)
go

create index IX_EE1CA456 on MBMessageFlag (topicId)
go
create index IX_93BF5C9C on MBMessageFlag (topicId, messageId)
go
create index IX_E1F34690 on MBMessageFlag (topicId, userId)
go
create index IX_7B2917BE on MBMessageFlag (userId)
go

create index IX_A00A898F on MBStatsUser (groupId)
go
create index IX_FAB5A88B on MBStatsUser (groupId, messageCount)
go
create index IX_847F92B5 on MBStatsUser (userId)
go

create index IX_CB854772 on MBThread (categoryId)
go

create index IX_A425F71A on OrgGroupPermission (groupId)
go
create index IX_6C53DA4E on OrgGroupPermission (permissionId)
go

create index IX_4A527DD3 on OrgGroupRole (groupId)
go
create index IX_AB044D1C on OrgGroupRole (roleId)
go

create index IX_6AF0D434 on OrgLabor (organizationId)
go

create index IX_834BCEB6 on Organization_ (companyId)
go
create index IX_E301BDF5 on Organization_ (companyId, name)
go
create index IX_418E4522 on Organization_ (companyId, parentOrganizationId)
go

create index IX_326F75BD on PasswordTracker (userId)
go

create index IX_4D19C2B8 on Permission_ (actionId, resourceId)
go
create index IX_F090C113 on Permission_ (resourceId)
go

create index IX_9F704A14 on Phone (companyId)
go
create index IX_139DA87F on Phone (companyId, className)
go
create index IX_A074A44 on Phone (companyId, className, classPK)
go
create index IX_F202B9CE on Phone (userId)
go

create index IX_B9746445 on PluginSetting (companyId)
go
create index IX_7171B2E8 on PluginSetting (companyId, pluginId, pluginType)
go

create index IX_EC370F10 on PollsChoice (questionId)
go

create index IX_9FF342EA on PollsQuestion (groupId)
go

create index IX_12112599 on PollsVote (questionId)
go
create index IX_FE3220E9 on PollsVote (questionId, choiceId)
go

create index IX_80CC9508 on Portlet (companyId)
go

create index IX_8B1E639D on PortletPreferences (layoutId)
go
create index IX_4A6293E1 on PortletPreferences (layoutId, ownerId)
go
create index IX_3EAB5A5A on PortletPreferences (ownerId)
go
create index IX_8E6DA3A1 on PortletPreferences (portletId)
go

create index IX_EA9B85B2 on RatingsEntry (className, classPK)
go
create index IX_9941DAEC on RatingsEntry (userId, className, classPK)
go

create index IX_8366321F on RatingsStats (className, classPK)
go

create index IX_16D87CA7 on Region (countryId)
go

create index IX_717FDD47 on ResourceCode (companyId)
go
create index IX_A32C097E on ResourceCode (companyId, name, scope)
go
create index IX_AACAFF40 on ResourceCode (name)
go

create index IX_2578FBD3 on Resource_ (codeId)
go
create index IX_67DE7856 on Resource_ (codeId, primKey)
go

create index IX_449A10B9 on Role_ (companyId)
go
create index IX_ED284C69 on Role_ (companyId, className, classPK)
go
create index IX_EBC931B8 on Role_ (companyId, name)
go

create index LIFERAY_002 on Roles_Permissions (permissionId)
go

create index IX_C98C0D78 on SCFrameworkVersion (companyId)
go
create index IX_272991FA on SCFrameworkVersion (groupId)
go


create index IX_5D25244F on SCProductEntry (companyId)
go
create index IX_72F87291 on SCProductEntry (groupId)
go
create index IX_98E6A9CB on SCProductEntry (groupId, userId)
go

create index IX_8377A211 on SCProductVersion (productEntryId)
go

create index IX_C28B41DC on ShoppingCart (groupId)
go
create index IX_54101CC8 on ShoppingCart (userId)
go

create index IX_5F615D3E on ShoppingCategory (groupId)
go
create index IX_1E6464F5 on ShoppingCategory (groupId, parentCategoryId)
go

create index IX_3251AF16 on ShoppingCoupon (groupId)
go

create index IX_C8EACF2E on ShoppingItem (categoryId)
go
create index IX_1C717CA6 on ShoppingItem (companyId, sku)
go

create index IX_6D5F9B87 on ShoppingItemField (itemId)
go

create index IX_EA6FD516 on ShoppingItemPrice (itemId)
go

create index IX_119B5630 on ShoppingOrder (groupId, userId, ppPaymentStatus)
go

create index IX_B5F82C7A on ShoppingOrderItem (orderId)
go

create index IX_E00DE435 on Subscription (companyId, className, classPK)
go
create index IX_FC7B066F on Subscription (companyId, userId, className, classPK)
go
create index IX_54243AFD on Subscription (userId)
go

create index IX_92B431ED on TagsAsset (className, classPK)
go

create index IX_10563688 on TagsEntry (companyId, name)
go

create index IX_C134234 on TagsProperty (companyId)
go
create index IX_EB974D08 on TagsProperty (companyId, key_)
go
create index IX_5200A629 on TagsProperty (entryId)
go
create index IX_F505253D on TagsProperty (entryId, key_)
go

create index IX_524FEFCE on UserGroup (companyId)
go
create index IX_23EAD0D on UserGroup (companyId, name)
go
create index IX_69771487 on UserGroup (companyId, parentUserGroupId)
go

create index IX_1B988D7A on UserGroupRole (groupId)
go
create index IX_887A2C95 on UserGroupRole (roleId)
go
create index IX_887BE56A on UserGroupRole (userId)
go
create index IX_4D040680 on UserGroupRole (userId, groupId)
go

create index IX_E60EA987 on UserIdMapper (userId)
go

create index IX_29BA1CF5 on UserTracker (companyId)
go
create index IX_E4EFBA8D on UserTracker (userId)
go

create index IX_14D8BCC0 on UserTrackerPath (userTrackerId)
go

create index IX_3A1E834E on User_ (companyId)
go
create index IX_615E9F7A on User_ (companyId, emailAddress)
go
create index IX_765A87C6 on User_ (companyId, password_)
go
create index IX_9782AD88 on User_ (companyId, userId)
go
create index IX_5ADBE171 on User_ (contactId)
go
create index IX_480DC765 on User_ (screenName)
go

create index LIFERAY_003 on Users_Permissions (permissionId)
go

create index IX_96F07007 on Website (companyId)
go
create index IX_66A45CAC on Website (companyId, className)
go
create index IX_5233F8B7 on Website (companyId, className, classPK)
go
create index IX_F75690BB on Website (userId)
go

create index IX_5D6FE3F0 on WikiNode (companyId)
go
create index IX_B480A672 on WikiNode (groupId)
go

create index IX_C8A9C476 on WikiPage (nodeId)
go
create index IX_997EEDD2 on WikiPage (nodeId, title)
go
