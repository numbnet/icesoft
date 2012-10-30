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
			    <mobi:fieldsetGroup  style="margin:5px;">
			        <mobi:fieldsetRow>Item 1</mobi:fieldsetRow>
			        <mobi:fieldsetRow>Item 2</mobi:fieldsetRow>
			        <mobi:fieldsetRow>Item 3</mobi:fieldsetRow>
			    </mobi:fieldsetGroup>
			</mobi:pagePanelBody>
            <mobi:pagePanelFooter>
               &copy; Acme Inc 2012
            </mobi:pagePanelFooter>
        </mobi:pagePanel>
    </body>
</html>