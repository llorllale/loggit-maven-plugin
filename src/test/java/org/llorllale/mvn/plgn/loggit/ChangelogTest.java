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

// @checkstyle AvoidStaticImport (2 lines)
import static com.jcabi.matchers.XhtmlMatchers.hasXPaths;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.maven.plugin.MojoFailureException;
import org.cactoos.text.TextOf;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

/**
 * Tests for {@link Changelog}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.2.0
 */
@SuppressWarnings({"checkstyle:MethodName", "checkstyle:MultipleStringLiterals"})
public final class ChangelogTest {
  /**
   * Writes git log to XML. The file should have all entries from the git log.
   * 
   * @throws Exception unexpected
   * @since 0.2.0
   */
  @Test
  public void writeLogToFileAndReadItBack() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    final RevCommit first = this.addCommit(repo, "first", "first@test.com", "First commit");
    final RevCommit second = this.addCommit(repo, "second", "second@test.com", "Second commit");
    final File output = repo.getRepository().getWorkTree().toPath().resolve("log.xml").toFile();
    new Changelog(
      repo.getRepository().getWorkTree(),
      output
    ).execute();
    assertThat(
      new TextOf(output).asString(),
      hasXPaths(
        // @checkstyle LineLength (8 lines)
        String.format("//commit[id = '%s']/author[name = 'first']", first.getId().getName()),
        String.format("//commit[id = '%s']/author[email = 'first@test.com']", first.getId().getName()),
        String.format("//commit[id = '%s']/message[short = 'First commit']", first.getId().getName()),
        String.format("//commit[id = '%s']/message[full = 'First commit']", first.getId().getName()),
        String.format("//commit[id = '%s']/author[name = 'second']", second.getId().getName()),
        String.format("//commit[id = '%s']/author[email = 'second@test.com']", second.getId().getName()),
        String.format("//commit[id = '%s']/message[short = 'Second commit']", second.getId().getName()),
        String.format("//commit[id = '%s']/message[full = 'Second commit']", second.getId().getName())
      )
    );
  }

  /**
   * Must fail if a path is given for a directory that is not a git repo.
   * 
   * @throws Exception the expected error
   * @since 0.2.0
   */
  @Test(expected = MojoFailureException.class)
  public void failsWithInvalidPathToRepo() throws Exception {
    new Changelog(Files.createTempDirectory("").toFile(), new File("log.xml")).execute();
  }

  /**
   * Must fail if a directory path is given as the output xml.
   * 
   * @throws Exception the expected error
   * @since 0.2.0
   */
  @Test(expected = MojoFailureException.class)
  public void failsWithInvalidPathToOutputXml() throws Exception {
    final File dir = Files.createTempDirectory("").toFile();
    final File xml = Files.createTempDirectory("").toFile();
    new Changelog(dir, xml).execute();
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
