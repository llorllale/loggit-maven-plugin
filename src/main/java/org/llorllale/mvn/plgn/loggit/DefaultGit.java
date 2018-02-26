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

import java.io.IOException;
import java.nio.file.Path;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;

/**
 * Default impl of {@link Git}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 */
final class DefaultGit implements Git {
  private final Path path;

  /**
   * Ctor.
   * 
   * @param path path to the repo's dir
   * @since 0.1.0
   */
  DefaultGit(Path path) {
    this.path = path;
  }

  @Override
  public Log log() throws IOException {
    final Repository repo = new FileRepository(this.path.toFile());
    return new DefaultLog(
      repo,
      repo.findRef(Constants.MASTER)
    );
  }
}
