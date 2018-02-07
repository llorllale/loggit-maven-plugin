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
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.jcabi.xml.XML;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;

/**
 * Unit tests for {@link DefaultCommits}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 */
@SuppressWarnings({"checkstyle:MultipleStringLiterals", "checkstyle:MethodName"})
public final class DefaultCommitsTest {

  /**
   * Returns a reference to commits.
   * 
   * @throws IOException unexpected
   * @since 0.1.0
   */
  @Test
  public void testIterator() throws IOException {
    assertNotNull(
      new DefaultCommits(
        new org.eclipse.jgit.api.Git(
          new FileRepositoryBuilder()
            .findGitDir(Paths.get(".").toFile())
            .build()
        ).log()
      ).iterator()
    );
  }

  /**
   * Iterator is not empty.
   * 
   * @throws IOException unexpected
   * @since 0.1.0
   */
  @Test
  public void test() throws IOException {
    new DefaultCommits(
      new org.eclipse.jgit.api.Git(
        new FileRepositoryBuilder()
          .findGitDir(Paths.get(".").toFile())
          .build()
      ).log()
    ).iterator().next();
  }

  /**
   * IOExceptions are unchecked.
   * 
   * @throws IOException unexpected
   * @since 0.1.0
   */
  @Test(expected = UncheckedIOException.class)
  public void uncheckedException() throws IOException {
    new DefaultCommits(
      new org.eclipse.jgit.api.Git(
        new FileRepositoryBuilder()
          .setGitDir(Paths.get(".").toFile())
          .build()
      ).log()
    ).iterator();
  }

  /**
   * All commits must be read from the {@link LogCommand}.
   * 
   * @throws Exception unexpected
   * @since 0.1.0
   * @todo #35:30min Implement DefaultCommit.equals in order to facilidate testing. Then come back
   *  and stop @Ignoring DefaultCommitsTest.allComitsAreRead() and make sure it works.
   */
  @Test
  public void allCommitsAreRead() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    final RevCommit first = this.addCommit(
      repo, "test1", "test1@gitlog.com",
      "This is a test.\n\nTest of DefaultCommits to make sure it reads all commits."
    );
    final RevCommit second = this.addCommit(
      repo, "test2", "test2@gitlog.com",
      "This is a test.\n\nTest of DefaultCommits to make sure it reads all commits."
    );
    assertThat(
      new DefaultCommits(
        repo.log()
      ),
      hasItems(new SmartCommit(second), new SmartCommit(first))
    );
  }

  /**
   * Adds a commit to the repo.
   * 
   * @param repo the repo
   * @param author the author name
   * @param email the author email
   * @param msg the commit msg
   * @return the revcommit
   * @throws GitAPIException unexpected
   * @throws IOException unexpected
   */
  private RevCommit addCommit(
    org.eclipse.jgit.api.Git repo, String author, String email, String msg
  ) throws GitAPIException, IOException {
    Files.createFile(
      repo.getRepository().getWorkTree().toPath().resolve(System.nanoTime() + "test.txt")
    );
    repo.add().addFilepattern(".").call();
    return repo.commit()
      .setAuthor(author, email)
      .setMessage(msg)
      .call();
  }

  /**
   * Initializes a git repo in a temp directory.
   * 
   * @return the repo
   * @throws IOException unexpected
   * @throws GitAPIException unexpected
   */
  private org.eclipse.jgit.api.Git repo() throws IOException, GitAPIException {
    final Path dir = Files.createTempDirectory("");
    return org.eclipse.jgit.api.Git.init()
      .setDirectory(dir.toFile())
      .call();
  }

  /**
   * A commit that implements Object.equals().
   */
  private static final class SmartCommit implements Commit {
    private final Commit origin;

    /**
     * Ctor.
     * @param commit the rev commit
     */
    SmartCommit(RevCommit commit) {
      this.origin = new DefaultCommit(commit);
    }

    @Override
    public XML asXml() {
      return this.origin.asXml();
    }

    @Override
    public boolean equals(Object object) {
      if (!(object instanceof Commit)) {
        return false;
      }
      final Commit other = (Commit) object;
      return this.asXml().node().isEqualNode(other.asXml().node());
    }

    @Override
    public int hashCode() {
      return this.origin.asXml().node().hashCode();
    }
  }
}
