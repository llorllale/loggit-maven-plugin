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

// @checkstyle AvoidStaticImport (1 line)
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;

/**
 * Unit tests for {@link DefaultCommits}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
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
}
