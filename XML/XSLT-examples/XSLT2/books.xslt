<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" indent="yes"/>
	<xsl:param name="color">red</xsl:param>
	<xsl:template match="/">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<title>Library</title>
			</head>
			<body style="font-weight: bold;">
				<xsl:apply-templates/>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="book">
		<p>
			<xsl:if test="count(author) > 1">
				<xsl:attribute name="style">color: <xsl:value-of select="$color"/>;</xsl:attribute>
			</xsl:if>
			<xsl:value-of select="title"/>
		</p>
	</xsl:template>
	<!-- Rgles implicites -->
	<xsl:template match="*">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="text()|@*">
		<xsl:value-of select="."/>
	</xsl:template>
</xsl:stylesheet>
