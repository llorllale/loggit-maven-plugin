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

package org.llorllale.mvn.plgn.gitlog;

import static com.jcabi.matchers.XhtmlMatchers.hasXPath;
import com.jcabi.xml.StrictXML;
import java.io.IOException;
import java.nio.file.Paths;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.llorllale.mvn.plgn.gitlog.mock.MockLogCommand;

/**
 * Unit tests of {@link DefaultLog}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 */
public class DefaultLogTest {

  /**
   * Returns a reference to commits.
   * 
   * @since 0.1.0
   */
  @Test
  public void returnsCommits() throws IOException {
    assertNotNull(
      new DefaultLog(
        new org.eclipse.jgit.api.Git(
          new FileRepositoryBuilder()
            .findGitDir(Paths.get(".").toFile())
            .build()
        ).log()
      ).commits()
    );
  }

  /**
   * The XML will have no commit nodes when there are no commits.
   * 
   * @since 0.1.0
   */
  @Test
  public void asXmlNoCommits() throws IOException {
    assertThat(
      new DefaultLog(new MockLogCommand()).asXml().toString(),
      hasXPath("/log/commits[count(commit) = 0]")
    );
  }

  /**
   * The XML complies with the schema.
   * 
   * @since 0.1.0
   */
  @Test
  public void asXmlIsValidAgainstSchema() throws IOException {
    new StrictXML(
      new DefaultLog(new MockLogCommand()).asXml(),
      new Schema()
    ).toString();
  }
}
