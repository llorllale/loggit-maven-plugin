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

// @checkstyle AvoidStaticImport (4 lines)
import static com.jcabi.matchers.XhtmlMatchers.hasXPath;
import static com.jcabi.matchers.XhtmlMatchers.hasXPaths;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.jcabi.xml.StrictXML;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit tests of {@link DefaultLog}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 * @todo #36:30min Handle NullPointerException thrown when the given ref is null. This will
 *  happen if the git repo has been initialized but no commits have been added. Then, stop
 *  ignoring the 'asXmlNoCommits' and 'asXmlIsValidAgainstSchema' tests and refactor as necessary.
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
        repo.findRef("master")
      ).commits()
    );
  }

  /**
   * The XML will have no commit nodes when there are no commits.
   * 
   * @throws Exception unexpected
   * @since 0.1.0
   */
  @Ignore
  @Test
  public void asXmlNoCommits() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    assertThat(
      new DefaultLog(repo.getRepository(), repo.getRepository().findRef("master")).asXml(),
      hasXPath("/log/commits[count(commit) = 0]")
    );
  }

  /**
   * Returns all commits in the log.
   * 
   * @throws Exception unexpected
   * @since 0.1.0
   * @todo #36:30min The commit XML should be setting the id to the hexadecimal form of
   *  the commit's hash (in string form). Currently it's just setting it to whatever
   *  ObjectId.toString() returns. After fixing that, return to this test and adjust
   *  accordingly.
   */
  @Test
  public void asXmlAllCommits() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    this.addCommit(repo, "first", "first@test.com", "First commit");
    this.addCommit(repo, "second", "second@test.com", "Second commit");
    assertThat(
      new DefaultLog(repo.getRepository(), repo.getRepository().findRef("master")).asXml(),
      hasXPaths(
        "//commit//author[name = 'first']",
        "//commit//author[email = 'first@test.com']",
        "//commit//message[short = 'First commit']",
        "//commit//message[full = 'First commit']",
        "//commit//author[name = 'second']",
        "//commit//author[email = 'second@test.com']",
        "//commit//message[short = 'Second commit']",
        "//commit//message[full = 'Second commit']"
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
      new DefaultLog(repo.getRepository(), repo.getRepository().findRef("master")).asXml(),
      hasXPaths(
        "//commit[1]//author[name = 'second']",
        "//commit[2]//author[name = 'first']"
      )
    );
  }

  /**
   * The XML complies with the schema.
   * 
   * @throws Exception unexpected
   * @since 0.1.0
   */
  @Ignore
  @Test
  public void asXmlIsValidAgainstSchema() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    new StrictXML(
      new DefaultLog(repo.getRepository(), repo.getRepository().findRef("master")).asXml(),
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
