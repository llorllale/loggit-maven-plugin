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

// @checkstyle AvoidStaticImport (3 lines)
import static com.jcabi.matchers.XhtmlMatchers.hasXPath;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

/**
 * Tests for {@link DefaultCommit}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 * @checkstyle MultipleStringLiterals (500 lines)
 */
public final class DefaultCommitTest {
  /**
   * Check for expected field values.
   * 
   * @throws Exception unexpected
   * @since 0.1.0
   */
  @Test
  public void readsValues() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    repo.add().addFilepattern(".").call();
    final RevCommit commit = repo.commit()
      .setAuthor("gitlog test", "test@gitlog.com")
      .setMessage("This is a test.\n\nTest of DefaultCommit.asXml")
      .call();
    assertThat(
      new DefaultCommit(commit).asXml(),
      allOf(
        hasXPath(String.format("/commit[id = '%s']", commit.getId())),
        hasXPath(String.format("/commit/author[name = '%s']", commit.getAuthorIdent().getName())),
        // @checkstyle LineLength (2 lines)
        hasXPath(String.format("/commit/author[email = '%s']", commit.getAuthorIdent().getEmailAddress())),
        hasXPath(String.format("/commit/author[date = '%s']", commit.getAuthorIdent().getWhen().toInstant())),
        hasXPath(String.format("/commit/message[short = '%s']", commit.getShortMessage())),
        hasXPath(String.format("/commit/message[full = '%s']", commit.getFullMessage()))
      )
    );
  }

  /**
   * Initializes a git repo in a temp directory.
   * 
   * @return the repo
   * @throws IOException unexpected
   * @throws GitAPIException unexpected
   * @todo #29:30min Checkstyle: enforce use of the javadoc tag @since.
   *  Also enforce the semantic version format. Useful to know when something was
   *  added.
   */
  private org.eclipse.jgit.api.Git repo() throws IOException, GitAPIException {
    final Path dir = Files.createTempDirectory("");
    Files.createFile(dir.resolve("test.txt"));
    return org.eclipse.jgit.api.Git.init()
      .setDirectory(dir.toFile())
      .call();
  }
}
