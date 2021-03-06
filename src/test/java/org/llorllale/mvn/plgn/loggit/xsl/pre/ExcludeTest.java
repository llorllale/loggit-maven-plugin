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

// @checkstyle AvoidStaticImport (4 lines)
import static com.jcabi.matchers.XhtmlMatchers.hasXPath;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.jcabi.xml.XMLDocument;
import org.junit.Test;

/**
 * Unit tests for {@link Exclude}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 1.1.0
 */
@SuppressWarnings({"checkstyle:MultipleStringLiterals", "checkstyle:MethodName"})
public final class ExcludeTest {
  /**
   * Excludes commits that match the regex.
   * @since 1.1.0
   */
  @Test
  public void excludeCommitsIfRegexMatches() {
    assertThat(
      new Exclude("First.*", "").transform(new XMLDocument(
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
        + "      <taggedAs/>"
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
        + "</log>"
      )),
      allOf(
        not(hasXPath("/log/commits/commit[id = 'b8ed3b64435525f8f5c9196489dce85613cefe96']")),
        hasXPath("/log/commits/commit[id = 'fcc814a658aea3537ad5182ff211ed8c58479fb9']")
      )
    );
  }

  /**
   * Does not exclude commits when regex doesn't match.
   * @since 1.1.0
   */
  @Test
  public void doesNotExcludeCommitsIfRegexDoesNotMatch() {
    assertThat(
      new Exclude("Some regex that doesn't match.*", "").transform(new XMLDocument(
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
        + "      <taggedAs/>"
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
        + "</log>"
      )),
      allOf(
        hasXPath("/log/commits/commit[id = 'b8ed3b64435525f8f5c9196489dce85613cefe96']"),
        hasXPath("/log/commits/commit[id = 'fcc814a658aea3537ad5182ff211ed8c58479fb9']")
      )
    );
  }
}
