<?xml version='1.0' encoding='ISO-8859-1'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes" omit-xml-declaration="yes" />
	
	<xsl:template match="roadmap">
		<div id="roadmap">
			<xsl:apply-templates select="currentRelease" />
			<xsl:apply-templates select="futureReleases/release" />
			<xsl:apply-templates select="futureReleases/unscheduled" />
		</div>
	</xsl:template>
	
	<xsl:template match="currentRelease">
		<h1>Current Release Todos (build <xsl:value-of select="@build" />):</h1>
		<div class="roadmapCurrentRelease">
			<xsl:if test="count(documentation/todo) &gt; 0">
				<p class="title">Documentation</p>
				<xsl:apply-templates select="documentation/todo" mode="recursive" />
			</xsl:if>
			<xsl:if test="count(misc/todo) &gt; 0">
				<p class="title">Misc</p>
				<xsl:apply-templates select="misc/todo" mode="recursive" />
			</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template match="release">
		<h1>Release <xsl:value-of select="@build" />:</h1>
		<xsl:apply-templates select="todo" mode="div" />
	</xsl:template>
	
	<xsl:template match="unscheduled">
		<h1>Unscheduled Features:</h1>
		<xsl:apply-templates select="todo" mode="div" />
	</xsl:template>
	
	<xsl:template match="todo" mode="recursive">
		<ul class="roadmapTodoList">
			<li><xsl:value-of select="@title" /></li>
			<xsl:if test="count(todo) &gt; 0">
				<xsl:apply-templates select="todo" mode="recursive" />
			</xsl:if>
		</ul>
	</xsl:template>
	
	<xsl:template match="todo" mode="div">
		<div class="roadmapTodo">
			<xsl:apply-templates select="." mode="normal" />
		</div>
	</xsl:template>
	
	<xsl:template match="todo" mode="normal">
		<p class="title"><xsl:value-of select="@title" /> (<xsl:value-of select="@weight" />)</p>
		<span class="roadmapTodoBody">
			<xsl:copy-of select="./*" />  
		</span>
	</xsl:template>
</xsl:stylesheet>