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

package org.llorllale.mvn.plgn.loggit.mock;

import java.io.IOException;
import java.util.Collections;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * Mock LogCommand for tests.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 */
public final class MockLogCommand extends LogCommand {

  /**
   * Ctor.
   * 
   * @throws java.io.IOException if an I/O error occurs
   * @since 0.1.0
   */
  public MockLogCommand() throws IOException {
    super(new FileRepositoryBuilder().findGitDir().build());
  }

  @Override
  public Iterable<RevCommit> call() throws GitAPIException, NoHeadException {
    return Collections.emptyList();
  }
}
