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

package org.llorllale.mvn.plgn.loggit.xsl;

// @checkstyle AvoidStaticImport (2 lines)
import static com.jcabi.matchers.XhtmlMatchers.hasXPaths;
import static org.junit.Assert.assertThat;

import com.jcabi.xml.XMLDocument;
import org.junit.Test;

/**
 * Tests for {@link Identity}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.2.0
 * @todo #45:30min Implement the markdown transform and add tests. Checkout the
 *  flexmark-java project in order to validate the output markdown
 *  (https://github.com/vsch/flexmark-java) or any other library.
 */
@SuppressWarnings("checkstyle:MethodName")
public final class IdentityTest {
  /**
   * Output must be the same as input.
   * 
   * @since 0.2.0
   */
  @Test
  public void noChangesToXml() {
    assertThat(
      new Identity().transform(
        new XMLDocument("<doc><id>1</id><author>George</author><email>email@test.com</email></doc>")
      ),
      hasXPaths(
        "/doc[id = 1]",
        "/doc[author = 'George']",
        "/doc[email = 'email@test.com']"
      )
    );
  }
}
