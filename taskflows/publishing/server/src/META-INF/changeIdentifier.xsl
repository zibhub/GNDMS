<xsl:stylesheet
        version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:gmd="http://www.isotc211.org/2005/gmd"
        xmlns:gco="http://www.isotc211.org/2005/gco"
        xmlns:gml="http://www.opengis.net/gml"
        xmlns:oai="http://www.openarchives.org/OAI/2.0/"
        xmlns:xlink="http://www.w3.org/1999/xlink"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.isotc211.org/2005/gmd http://www.isotc211.org/2005/gmd/metadataEntity.xsd">

    <!-- external parameters -->
    <xsl:param name="identifier"/>

    <xsl:output method="xml" indent="yes"/>

    <!-- Change fileIdentifier -->
    <!-- value taken from parameter 'identifier' -->
    <xsl:template match="gmd:fileIdentifier">
        <gmd:fileIdentifier>
            <gco:CharacterString><xsl:value-of select="$identifier"/></gco:CharacterString>
        </gmd:fileIdentifier>
    </xsl:template>

    <!-- recursively copy all other elements -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
