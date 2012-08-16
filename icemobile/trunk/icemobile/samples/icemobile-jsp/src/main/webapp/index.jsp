<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>

<!DOCTYPE html >

<html>
    <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>Hello ICEmobile!</title>
		<meta name="viewport"
			  content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
		<meta name="apple-mobile-web-app-capable" content="yes"/>
		<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
		<mobi:deviceResource/>
    </head>
    <body>
    	<mobi:pagePanel>
            <mobi:pagePanelHeader >
                Hello ICEmobile!
            </mobi:pagePanelHeader>
			<mobi:pagePanelBody>
			    <mobi:fieldSetGroup  style="margin:5px;">
			        <mobi:fieldSetRow>Item 1</mobi:fieldSetRow>
			        <mobi:fieldSetRow>Item 2</mobi:fieldSetRow>
			        <mobi:fieldSetRow>Item 3</mobi:fieldSetRow>
			    </mobi:fieldSetGroup>
			</mobi:pagePanelBody>
            <mobi:pagePanelFooter>
               &copy; Acme Inc 2012
            </mobi:pagePanelFooter>
        </mobi:pagePanel>
    </body>
</html>