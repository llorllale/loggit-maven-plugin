/*
 * Copyright 2018 George Aristy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.llorllale.mvn.plgn.loggit.xsl.post;

// @checkstyle AvoidStaticImport (2 lines)
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import com.jcabi.xml.XMLDocument;
import org.cactoos.io.InputOf;
import org.junit.Test;

/**
 * Tests for {@link Custom}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.2.0
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public final class CustomTest {
  private static final String LOG =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    + "<log>"
    + "  <commits>"
    + "    <commit>"
    + "      <id>fcc814a658aea3537ad5182ff211ed8c58479fb9</id>"
    + "      <author>"
    + "        <name>second</name>"
    + "        <email>second@test.com</email>"
    + "        <date>2018-02-26T16:42:00Z</date>"
    + "      </author>"
    + "      <message>"
    + "        <short>Second commit</short>"
    + "        <full>Second commit</full>"
    + "      </message>"
    + "    </commit>"
    + "    <commit>"
    + "      <id>b8ed3b64435525f8f5c9196489dce85613cefe96</id>"
    + "      <author>"
    + "        <name>first</name>"
    + "        <email>first@test.com</email>"
    + "        <date>2018-02-26T16:42:00Z</date>"
    + "      </author>"
    + "      <message>"
    + "        <short>First commit</short>"
    + "        <full>First commit</full>"
    + "      </message>"
    + "    </commit>"
    + "  </commits>"
    + "</log>";

  /**
   * Applies any custom XSLT provided.
   * 
   * @since 0.2.0
   */
  @Test
  public void appliesCustomXslt() {
    assertThat(
      new Custom(new InputOf(
        "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"2.0\">"
        + "  <xsl:output method=\"text\"/>"
        + "  <xsl:template match=\"commits\">"
        + "    <xsl:for-each select=\"commit\"><xsl:value-of select=\"id\"/>,</xsl:for-each>"
        + "  </xsl:template>"
        + "</xsl:stylesheet>"
      )).applyTo(new XMLDocument(CustomTest.LOG)),
      containsString(
        "fcc814a658aea3537ad5182ff211ed8c58479fb9,b8ed3b64435525f8f5c9196489dce85613cefe96,"
      )
    );
  }
}
