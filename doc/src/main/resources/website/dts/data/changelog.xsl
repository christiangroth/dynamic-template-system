<?xml version='1.0' encoding='ISO-8859-1'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes" omit-xml-declaration="yes" />
	
	<xsl:template match="changelog">
		<div id="changelog">
			<xsl:apply-templates select="release">
				<xsl:sort select="@build" order="descending" />
			</xsl:apply-templates>
		</div>
	</xsl:template>
	
	<xsl:template match="release">
		<h1><xsl:value-of select="@releaseDate" /> - v<xsl:value-of select="@build" />:</h1>
		<xsl:if test="count(features) &gt; 0">
			<xsl:apply-templates select="features" />
		</xsl:if>
		
		<xsl:if test="count(fixes/fix) &gt; 0">
			<p class="title">Fixes</p>
			<ul>
				<xsl:apply-templates select="fixes/fix" />
			</ul>
		</xsl:if>
		<xsl:if test="count(refactored/refactor) &gt; 0">
			<p class="title">Refactored</p>
			<ul>
				<xsl:apply-templates select="refactored/refactor" />
			</ul>
		</xsl:if>
		<xsl:if test="count(removed/removed) &gt; 0">
			<p class="title">Removed</p>
			<ul>
				<xsl:apply-templates select="removed/removed" />
			</ul>
		</xsl:if>
		<xsl:if test="count(documentation/doc) &gt; 0">
			<p class="title">Documentation</p>
			<ul>
				<xsl:apply-templates select="documentation/change" mode="documentation" />
			</ul>
		</xsl:if>
		<xsl:if test="count(buildManagement/change) &gt; 0">
			<p class="title">Build Management</p>
			<ul>
				<xsl:apply-templates select="buildManagement/change" mode="buildManagement" />
			</ul>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="features">
		<xsl:if test="count(parameter) &gt; 0">
			<xsl:apply-templates select="parameter" />
		</xsl:if>
		<xsl:if test="count(generic) &gt; 0">
			<xsl:apply-templates select="generic" />
		</xsl:if>
		<xsl:if test="count(processing) &gt; 0">
			<xsl:apply-templates select="processing" />
		</xsl:if>
		<xsl:if test="count(dto) &gt; 0">
			<xsl:apply-templates select="dto" />
		</xsl:if>
		<xsl:if test="count(feature) &gt; 0">
			<xsl:apply-templates select="feature" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="parameter">
		<div class="changelogParameter">
			<p class="title"><xsl:value-of select="@title" /></p>
			<span class="changelogParameterBody">
				<xsl:copy-of select="./*" />
			</span>
		</div>
	</xsl:template>
	
	<xsl:template match="parameter" mode="nestedDto">
		<div class="changelogParameter">
			<p class="title"><xsl:value-of select="@title" /></p>
			<span class="changelogParameterBody">
				<xsl:copy-of select="./*" />
			</span>
		</div>
	</xsl:template>
	
	<xsl:template match="parameter" mode="nestedFeature">
		<div class="changelogParameter">
			<p class="title"><xsl:value-of select="@title" /></p>
			<span class="changelogParameterBody">
				<xsl:copy-of select="./*" />
			</span>
		</div>
	</xsl:template>
	
	<xsl:template match="generic">
		<div class="changelogGeneric">
			<p class="title"><xsl:value-of select="@title" /></p>
			<span class="changelogGenericBody">
				<xsl:copy-of select="./*" />
			</span>
		</div>
	</xsl:template>
	
	<xsl:template match="generic" mode="nestedDto">
		<div class="changelogGeneric">
			<p class="title"><xsl:value-of select="@title" /></p>
			<span class="changelogGenericBody">
				<xsl:copy-of select="./*" />
			</span>
		</div>
	</xsl:template>
	
	<xsl:template match="generic" mode="nestedFeature">
		<div class="changelogGeneric">
			<p class="title"><xsl:value-of select="@title" /></p>
			<span class="changelogGenericBody">
				<xsl:copy-of select="./*" />
			</span>
		</div>
	</xsl:template>
	
	<xsl:template match="processing">
		<div class="changelogProcessing">
			<p class="title"><xsl:value-of select="@title" /></p>
			<span class="changelogProcessingBody">
				<xsl:copy-of select="./*" />
			</span>
		</div>
	</xsl:template>
	
	<xsl:template match="processing" mode="nestedFeature">
		<div class="changelogProcessing">
			<p class="title"><xsl:value-of select="@title" /></p>
			<span class="changelogProcessingBody">
				<xsl:copy-of select="./*" />
			</span>
		</div>
	</xsl:template>
	
	<xsl:template match="dto">
		<div class="changelogDto">
			<p class="title"><xsl:value-of select="@title" /></p>
			<span class="changelogDtoBody">
				<xsl:copy-of select="./*[name() != 'parameter' and name() != 'generic']" />
			</span>
			
			<xsl:if test="count(parameter) &gt; 0">
				<xsl:apply-templates select="parameter" mode="nestedDto" />
			</xsl:if>
			<xsl:if test="count(generic) &gt; 0">
				<xsl:apply-templates select="generic" mode="nestedDto" />
			</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template match="feature">
		<div class="changelogFeature">
			<p class="title"><xsl:value-of select="@title" /></p>
			<span class="changelogFeatureBody">
				<xsl:copy-of select="./*[name() != 'feature' and name() != 'parameter' and name() != 'generic' and name() != 'processing']" />
			</span>
			
			<xsl:if test="count(feature) &gt; 0">
				<xsl:apply-templates select="feature" mode="nestedFeature" />
			</xsl:if>
			<xsl:if test="count(parameter) &gt; 0">
				<xsl:apply-templates select="parameter" mode="nestedFeature" />
			</xsl:if>
			<xsl:if test="count(generic) &gt; 0">
				<xsl:apply-templates select="generic" mode="nestedFeature" />
			</xsl:if>
			<xsl:if test="count(processing) &gt; 0">
				<xsl:apply-templates select="processing" mode="nestedFeature" />
			</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template match="feature" mode="nestedFeature">
		<div class="changelogFeature">
			<p class="title"><xsl:value-of select="@title" /></p>
			<span class="changelogFeatureBody">
				<xsl:copy-of select="./*[name() != 'feature' and name() != 'parameter' and name() != 'generic' and name() != 'processing']" />
			</span>
			
			<xsl:if test="count(feature) &gt; 0">
				<xsl:apply-templates select="feature" mode="nestedFeature" />
			</xsl:if>
			<xsl:if test="count(parameter) &gt; 0">
				<xsl:apply-templates select="parameter" mode="nestedFeature" />
			</xsl:if>
			<xsl:if test="count(genric) &gt; 0">
				<xsl:apply-templates select="generic" mode="nestedFeature" />
			</xsl:if>
			<xsl:if test="count(processing) &gt; 0">
				<xsl:apply-templates select="processing" mode="nestedFeature" />
			</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template match="fix">
		<li><xsl:value-of select="@title" /></li>
	</xsl:template>
	
	<xsl:template match="refactor">
		<li><xsl:value-of select="@title" /></li>
	</xsl:template>
	
	<xsl:template match="removed">
		<li><xsl:value-of select="@title" /></li>
	</xsl:template>
	
	<xsl:template match="change" mode="documentation">
		<li><xsl:value-of select="@title" /></li>
	</xsl:template>
	
	<xsl:template match="change" mode="buildManagement">
		<li><xsl:value-of select="@title" /></li>
	</xsl:template>
</xsl:stylesheet>