<?xml version="1.0"?>
<!--
  ~ Version: MPL 1.1
  ~
  ~ "The contents of this file are subject to the Mozilla Public License
  ~ Version 1.1 (the "License"); you may not use this file except in
  ~ compliance with the License. You may obtain a copy of the License at
  ~ http://www.mozilla.org/MPL/
  ~
  ~ Software distributed under the License is distributed on an "AS IS"
  ~ basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
  ~ License for the specific language governing rights and limitations under
  ~ the License.
  ~
  ~ The Original Code is ICEfaces 1.5 open source software code, released
  ~ November 5, 2006. The Initial Developer of the Original Code is ICEsoft
  ~ Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
  ~ 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
  ~
  ~ Contributor(s): _____________________.
  ~
  -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output
		doctype-public="-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN"
		doctype-system="dtd/web-facesconfig_1_1.dtd" />
		
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
