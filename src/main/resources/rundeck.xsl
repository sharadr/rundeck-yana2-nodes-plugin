<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- Receives the parameters  -->
<xsl:param name="nodeType" />
<xsl:param name="url" />

<xsl:output method="xml"/>


<xsl:template match="/nodes">
<project>
<xsl:apply-templates select="node[@type=$nodeType]"/>
</project>
</xsl:template>


<xsl:template match="node">
  <node name="{@name}" description="{description}" 
	type="{@type}" id="{@id}" tags="{@tags}" 
	hostname="{@name}"
	editUrl="{$url}/node/show/{@id}">
    <xsl:apply-templates select="attributes/attribute"/>
  </node>
</xsl:template>


<xsl:template match="attribute">
  <attribute name="{@name}" value="{@value}"/>
</xsl:template>

</xsl:stylesheet>

