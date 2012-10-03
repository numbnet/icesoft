<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.icemobile.org/tags" prefix="mobi" %>

<!DOCTYPE html >

<html>
    <head>
    	<title>Hello ICEmobile!</title>
		<mobi:deviceResource />
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