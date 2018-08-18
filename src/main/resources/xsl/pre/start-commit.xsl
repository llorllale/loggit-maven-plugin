<?xml version="1.0"?>
<!--

    Copyright 2018 George Aristy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <xsl:param name="commitId"/>
  <xsl:template match="commits">
    <commits>
      <xsl:call-template name="loop">
        <xsl:with-param name="commits" select="//commit"/>
      </xsl:call-template>
    </commits>
  </xsl:template>
  <xsl:template name="loop">
    <xsl:param name="commits"/>
    <xsl:if test="count($commits) > 0">
      <xsl:variable name="commit" select="$commits[1]"/>
      <xsl:choose>
        <xsl:when test="string-length($commitId) = 0">
          <xsl:copy-of select="$commit"/>
          <xsl:call-template name="loop">
            <xsl:with-param name="commits" select="$commit/following-sibling::commit"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:when test="$commit/id != $commitId">
          <xsl:copy-of select="$commit"/>
          <xsl:call-template name="loop">
            <xsl:with-param name="commits" select="$commit/following-sibling::commit"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:when test="$commit/id = $commitId">
          <xsl:copy-of select="$commit"/>
        </xsl:when>
      </xsl:choose>
    </xsl:if>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
