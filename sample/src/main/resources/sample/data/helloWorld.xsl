<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/hello-world">
		<div>
			<h1><xsl:value-of select="greeting"/></h1>
        	<xsl:apply-templates select="greeter"/>
		</div>
	</xsl:template>
  
	<xsl:template match="greeter">
		<p>from <xsl:value-of select="."/></p>
	</xsl:template>
</xsl:stylesheet>
