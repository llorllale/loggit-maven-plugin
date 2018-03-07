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

package org.llorllale.mvn.plgn.loggit.xsl.pre;

// @checkstyle AvoidStaticImport (5 lines)
import static com.jcabi.matchers.XhtmlMatchers.hasXPath;
import static com.jcabi.matchers.XhtmlMatchers.hasXPaths;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.jcabi.xml.XMLDocument;
import org.junit.Test;

/**
 * Tests for {@link StartTag}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.5.0
 * @checkstyle MethodName (500 lines)
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public final class StartTagTest {
  private static final String LOG =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    + "<log>"
    + "  <commits>"
    + "    <commit>"
    + "      <id>b8ed3b6449289df8f5c9196492idj85613cefe96</id>"
    + "      <author>"
    + "        <name>fourth</name>"
    + "        <email>fourth@test.com</email>"
    + "        <date>2018-02-26T16:42:00Z</date>"
    + "      </author>"
    + "      <message>"
    + "        <short>Fourth commit</short>"
    + "        <full>Fourth commit</full>"
    + "      </message>"
    + "      <taggedAs/>"
    + "    </commit>"
    + "    <commit>"
    + "      <id>b8ed3b6449289df8f5c9196489dce85613cefe96</id>"
    + "      <author>"
    + "        <name>third</name>"
    + "        <email>third@test.com</email>"
    + "        <date>2018-02-26T16:42:00Z</date>"
    + "      </author>"
    + "      <message>"
    + "        <short>Third commit</short>"
    + "        <full>Third commit</full>"
    + "      </message>"
    + "      <taggedAs/>"
    + "    </commit>"
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
    + "      <taggedAs>"
    + "        <tag>1.0.0</tag>"
    + "      </taggedAs>"
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
    + "      <taggedAs/>"
    + "    </commit>"
    + "  </commits>"
    + "</log>";

  /**
   * No commit should be excluded if no tag/empty tag is specified.
   * 
   * @since 0.5.0
   */
  @Test
  public void allCommitsIncludedIfNoTagIsGiven() {
    assertThat(new StartTag("").applyTo(new XMLDocument(StartTagTest.LOG)),
      hasXPaths(
        "/log/commits/commit[id = 'b8ed3b6449289df8f5c9196492idj85613cefe96']",
        "/log/commits/commit[id = 'b8ed3b6449289df8f5c9196489dce85613cefe96']",
        "/log/commits/commit[id = 'fcc814a658aea3537ad5182ff211ed8c58479fb9']",
        "/log/commits/commit[id = 'b8ed3b64435525f8f5c9196489dce85613cefe96']"
      )
    );
  }

  /**
   * Do not include commits starting from the one with the given tag.
   * 
   * @since 0.5.0
   */
  @Test
  public void truncateCommitsStartingFromTag() {
    assertThat(new StartTag("1.0.0").applyTo(new XMLDocument(StartTagTest.LOG)),
      allOf(
        hasXPath("/log/commits/commit[id = 'b8ed3b6449289df8f5c9196492idj85613cefe96']"),
        hasXPath("/log/commits/commit[id = 'b8ed3b6449289df8f5c9196489dce85613cefe96']"),
        not(hasXPath("/log/commits/commit[id = 'fcc814a658aea3537ad5182ff211ed8c58479fb9']")),
        not(hasXPath("/log/commits/commit[id = 'b8ed3b64435525f8f5c9196489dce85613cefe96']"))
      )
    );
  }
}
