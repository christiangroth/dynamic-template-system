<?xml version='1.0' encoding='ISO-8859-1'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes" omit-xml-declaration="yes" />
	
	<xsl:template match="/">
		<xsl:value-of select="/changelog/release[1]/@build" />
	</xsl:template>
</xsl:stylesheet>