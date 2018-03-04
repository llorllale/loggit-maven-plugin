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

package org.llorllale.mvn.plgn.loggit;

// @checkstyle AvoidStaticImport (4 lines)
import static com.jcabi.matchers.XhtmlMatchers.hasXPaths;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.jcabi.xml.StrictXML;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;

/**
 * Unit tests of {@link DefaultLog}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 */
@SuppressWarnings({"checkstyle:MethodName", "checkstyle:MultipleStringLiterals"})
public final class DefaultLogTest {

  /**
   * Returns a reference to commits.
   * 
   * @throws IOException unexpected
   * @since 0.1.0
   */
  @Test
  public void returnsCommits() throws IOException {
    final Repository repo = new FileRepositoryBuilder()
      .findGitDir(Paths.get(".").toFile())
      .build();
    assertNotNull(
      new DefaultLog(
        repo,
        () -> repo.findRef(Constants.MASTER)
      ).commits()
    );
  }

  /**
   * Returns all commits in the log.
   * 
   * @throws Exception unexpected
   * @since 0.1.0
   */
  @Test
  public void asXmlAllCommits() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    final RevCommit first = this.addCommit(repo, "first", "first@test.com", "First commit");
    final RevCommit second = this.addCommit(repo, "second", "second@test.com", "Second commit");
    assertThat(
      new DefaultLog(
        repo.getRepository(), () -> repo.getRepository().findRef(Constants.MASTER)
      ).asXml(),
      hasXPaths(
        // @checkstyle LineLength (8 lines)
        String.format("/log/commits/commit[id = '%s']//author[name = 'first']", first.getId().getName()),
        String.format("/log/commits/commit[id = '%s']//author[email = 'first@test.com']", first.getId().getName()),
        String.format("/log/commits/commit[id = '%s']//message[short = 'First commit']", first.getId().getName()),
        String.format("/log/commits/commit[id = '%s']//message[full = 'First commit']", first.getId().getName()),
        String.format("/log/commits/commit[id = '%s']//author[name = 'second']", second.getId().getName()),
        String.format("/log/commits/commit[id = '%s']//author[email = 'second@test.com']", second.getId().getName()),
        String.format("/log/commits/commit[id = '%s']//message[short = 'Second commit']", second.getId().getName()),
        String.format("/log/commits/commit[id = '%s']//message[full = 'Second commit']", second.getId().getName())
      )
    );
  }

  /**
   * All commits must be in order.
   * 
   * @throws Exception unexpected
   * @since 0.1.0
   */
  @Test
  public void asXmlAllCommitsInOrder() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    this.addCommit(repo, "first", "first@test.com", "First commit");
    this.addCommit(repo, "second", "second@test.com", "Second commit");
    assertThat(
      new DefaultLog(
        repo.getRepository(), () -> repo.getRepository().findRef(Constants.MASTER)
      ).asXml(),
      hasXPaths(
        "//commit[1]//author[name = 'second']",
        "//commit[2]//author[name = 'first']"
      )
    );
  }

  /**
   * {@link IOException} if the given branch does not exist. This situation occurs when the
   * repo is initialized and has not commits yet. This implies that if there IS a branch then
   * there must be at least one commit.
   * 
   * @throws Exception expected
   * @since 0.1.0
   */
  @Test(expected = IOException.class)
  public void errorIfNoBranch() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    new StrictXML(
      new DefaultLog(
        repo.getRepository(), () -> repo.getRepository().findRef(Constants.MASTER)
      ).asXml(),
      new Schema()
    ).toString();
  }

  /**
   * Initializes a git repo in a temp directory.
   * 
   * @return the repo
   * @throws IOException unexpected
   * @throws GitAPIException unexpected
   */
  private org.eclipse.jgit.api.Git repo() throws IOException, GitAPIException {
    final File dir = Files.createTempDirectory("").toFile();
    return org.eclipse.jgit.api.Git.init()
      .setDirectory(dir)
      .call();
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
}
