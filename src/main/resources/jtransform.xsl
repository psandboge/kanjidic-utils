<?xml version="1.0" encoding="ISO-8859-1"?>
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
            <xsl:apply-templates select="../r_ele"/>
            <xsl:text>sv:</xsl:text>
            <xsl:apply-templates select="gloss[@lang='swe']"/>
            <xsl:text>en:</xsl:text>
            <xsl:apply-templates select="../sense/gloss[@lang='eng']"/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="gloss">
        <xsl:value-of select="text()"/>
        <xsl:text>,</xsl:text>
    </xsl:template>

    <xsl:template match="r_ele">
        <xsl:text>r-ele:</xsl:text>
        <xsl:apply-templates select="reb"/>
    </xsl:template>

    <xsl:template match="k_ele">
        <xsl:text>k-ele:</xsl:text>
        <xsl:apply-templates select="keb"/>
    </xsl:template>

    <xsl:template match="reb">
        <xsl:value-of select="text()"/>
        <xsl:text>,</xsl:text>
    </xsl:template>

    <xsl:template match="keb">
        <xsl:value-of select="text()"/>
        <xsl:text>,</xsl:text>
    </xsl:template>

    <xsl:template match="character">
        <xsl:apply-templates select="literal"/>
        <xsl:text>,</xsl:text>
        <xsl:apply-templates select="codepoint"/>
        <xsl:text>,</xsl:text>
        <xsl:apply-templates select="misc"/>
        <xsl:apply-templates select="reading_meaning"/>
        <xsl:text>&#xa;</xsl:text>
    </xsl:template>

    <xsl:template match="reading_meaning">
        <xsl:apply-templates select="rmgroup"/>
    </xsl:template>

    <xsl:template match="rmgroup">
        <xsl:apply-templates select="reading"/>
        <xsl:text>,</xsl:text>
        <xsl:apply-templates select="meaning"/>
    </xsl:template>

    <xsl:template match="reading[@r_type='ja_on']">
        <xsl:value-of select="."/>
        <xsl:text>/</xsl:text>
    </xsl:template>

    <xsl:template match="reading[@r_type='ja_kun']">
        <xsl:value-of select="."/>
        <xsl:text>/</xsl:text>
    </xsl:template>

    <xsl:template match="reading"/>

    <xsl:template match="meaning[not(@m_lang)]">
        <xsl:value-of select="."/>
        <xsl:text>/</xsl:text>
    </xsl:template>

    <xsl:template match="meaning"/>

    <xsl:template match="meaning"/>

    <xsl:template match="misc">
        <xsl:apply-templates select="grade"/>
        <xsl:text>,</xsl:text>
        <xsl:apply-templates select="stroke_count[position()=1]"/>
        <xsl:text>,</xsl:text>
        <xsl:apply-templates select="freq"/>
        <xsl:text>,</xsl:text>
        <xsl:apply-templates select="jlpt"/>
        <xsl:text>,</xsl:text>
    </xsl:template>

    <xsl:template match="literal">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="codepoint">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="cp_value[not(@cp_type='ucs')]"/>

    <xsl:template match="path[not(position()=last())]">
        <xsl:text>"</xsl:text>
        <xsl:value-of select="attribute::d"/>
        <xsl:text>",&#xa;</xsl:text>
    </xsl:template>

    <xsl:template match="path[position()=last()]">
        <xsl:text>"</xsl:text>
        <xsl:value-of select="attribute::d"/>
        <xsl:text>"&#xa;</xsl:text>
    </xsl:template>

    <xsl:template match="text">
    </xsl:template>
</xsl:stylesheet>
