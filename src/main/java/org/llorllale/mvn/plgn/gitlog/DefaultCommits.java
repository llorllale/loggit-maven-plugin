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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import org.cactoos.iterator.Mapped;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 * Allows iteration over commits.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 */
final class DefaultCommits implements Iterable<Commit> {
  private final LogCommand log;

  /**
   * Ctor.
   * 
   * @param log the git log
   * @since 0.1.0
   */
  DefaultCommits(LogCommand log) {
    this.log = log;
  }

  @Override
  public Iterator<Commit> iterator() {
    try {
      return new Mapped<>(
        DefaultCommit::new,
        this.log.call().iterator()
      );
    } catch (GitAPIException e) {
      throw new UncheckedIOException(
        "Error reading commits", 
        new IOException(e)
      );
    }
  }
}
