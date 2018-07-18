<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="1.0"
>
    <xsl:strip-space elements="*"/>
    <xsl:output method="text"/>

    <xsl:template match="entry">
        <xsl:if test="descendant::gloss[@lang='swe']">
            <xsl:text>&#xa;</xsl:text>
        </xsl:if>
        <xsl:apply-templates select="sense"/>
    </xsl:template>

    <xsl:template match="sense">
        <xsl:if test="child::gloss[@lang='swe']">
            <xsl:apply-templates select="../k_ele"/>
            <xsl:text>€</xsl:text>
            <xsl:apply-templates select="../r_ele"/>
            <xsl:text>€</xsl:text>
            <xsl:apply-templates select="gloss[@lang='swe']"/>
            <xsl:text>€</xsl:text>
            <xsl:apply-templates select="../sense/gloss[@lang='eng']"/>
            <xsl:text>€</xsl:text>
            <xsl:apply-templates select="../sense/pos"/>
            <xsl:text>€</xsl:text>
            <xsl:apply-templates select="../ent_seq"/>
            <xsl:text>€</xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template match="gloss">
        <xsl:value-of select="text()"/>
        <xsl:text>§</xsl:text>
    </xsl:template>

    <xsl:template match="pos">
        <xsl:value-of select="text()"/>
        <xsl:text>§</xsl:text>
    </xsl:template>

    <xsl:template match="ent_seq">
        <xsl:value-of select="text()"/>
    </xsl:template>

    <xsl:template match="r_ele">
        <xsl:apply-templates select="reb"/>
    </xsl:template>

    <xsl:template match="k_ele">
        <xsl:apply-templates select="keb"/>
    </xsl:template>

    <xsl:template match="reb">
        <xsl:value-of select="text()"/>
        <xsl:text>§</xsl:text>
    </xsl:template>

    <xsl:template match="keb">
        <xsl:value-of select="text()"/>
        <xsl:text>§</xsl:text>
    </xsl:template>

</xsl:stylesheet>
