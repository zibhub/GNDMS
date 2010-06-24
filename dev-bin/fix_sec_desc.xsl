<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" 
    xmlns="http://www.globus.org"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

<!-- the location of the gridmap file for all gndms-c3grid services-->
<xsl:variable name="GRID_MAP_PATH">etc/gndms_shared/grid-mapfile</xsl:variable>


<xsl:template match="/">
<securityConfig xmlns="http://www.globus.org">
    <xsl:apply-templates/>
</securityConfig>
</xsl:template>

<xsl:template match="/*" >
    <xsl:for-each select="child::*">
        <xsl:choose >
            <xsl:when test="name() = 'gridmap'">
                <xsl:element name="gridmap"> 
                    <xsl:attribute name="value"><xsl:value-of select="$GRID_MAP_PATH"/></xsl:attribute>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
              <xsl:copy-of select="." />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:for-each>
</xsl:template>

</xsl:stylesheet>
<!--vim:tw=0-->
