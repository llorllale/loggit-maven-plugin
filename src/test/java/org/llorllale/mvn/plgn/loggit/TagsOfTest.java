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

// @checkstyle AvoidStaticImport (3 lines)
import static com.jcabi.matchers.XhtmlMatchers.hasXPath;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;
import org.xembly.Directives;
import org.xembly.Xembler;

/**
 * Tests for {@link TagsOf}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.5.0
 */
@SuppressWarnings({"checkstyle:MethodName", "checkstyle:MultipleStringLiterals"})
public final class TagsOfTest {
  /**
   * The short name of a tag pointing to a commit must be included in {@code commit/taggedAs/tag}.
   * The "short name" is everything after the last forward slash in eg. {@code refs/tags/TAG}.
   * 
   * @throws Exception unexpected
   * @since 0.5.0
   */
  @Test
  public void includesTagsForAGivenCommit() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    final RevCommit first = this.addCommit(repo, "first", "first@gmail.com", "test");
    final Ref tag = this.tag(repo, "v1.0");
    final RevCommit second = this.addCommit(repo, "second", "second@gmail.com", "test");
    assertThat(
      new Xembler(
        new Directives().add("taggedAs").append(new TagsOf(repo.getRepository(), first))
      ).xml(),
      hasXPath("/taggedAs/tag[. = 'v1.0']")
    );
  }

  /**
   * No tags added if the given commit was not tagged.
   * 
   * @throws Exception unexpected
   * @since 0.5.0
   */
  @Test
  public void noTagsIfGivenCommitWasNotTagged() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    final RevCommit first = this.addCommit(repo, "first", "first@gmail.com", "test");
    final Ref tag = this.tag(repo, "v1.0");
    final RevCommit second = this.addCommit(repo, "second", "second@gmail.com", "test");
    assertThat(
      new Xembler(
        new Directives().add("taggedAs").append(new TagsOf(repo.getRepository(), second))
      ).xml(),
      not(hasXPath("/taggedAs[count(tag) > 0]"))
    );   
  }

  /**
   * No tags added when there are no tags. 
   * 
   * @throws Exception unexpected
   * @since 0.5.0
   */
  @Test
  public void emptyTags() throws Exception {
    final org.eclipse.jgit.api.Git repo = this.repo();
    final RevCommit first = this.addCommit(repo, "first", "first@gmail.com", "test");
    final RevCommit second = this.addCommit(repo, "second", "second@gmail.com", "test");
    assertThat(
      new Xembler(
        new Directives().add("taggedAs").append(new TagsOf(repo.getRepository(), second))
      ).xml(),
      not(hasXPath("/taggedAs[count(tag) > 0]"))
    );
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

  /**
   * Tags the repo.
   * 
   * @param repo the repo to tag
   * @param tag the tag's name
   * @return the tag
   * @throws GitAPIException unexpected
   */
  private Ref tag(org.eclipse.jgit.api.Git repo, String tag) throws GitAPIException {
    return repo.tag().setName(tag).setMessage(tag).call();
  }
}
