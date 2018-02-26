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

import com.jcabi.xml.StrictXML;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import java.io.IOException;
import java.util.Objects;
import org.cactoos.iterable.Mapped;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.xembly.Directives;
import org.xembly.Xembler;

/**
 * Default impl of {@link Log}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 */
final class DefaultLog implements Log {
  private final Repository repo;
  private final Ref ref;

  /**
   * Ctor.
   * 
   * @param repo the repo
   * @param ref the ref for which to get the commits for
   * @since 0.1.0
   */
  DefaultLog(Repository repo, Ref ref) {
    this.repo = repo;
    this.ref = ref;
  }

  @Override
  public Iterable<Commit> commits() throws IOException {
    try {
      final RevWalk walk = new RevWalk(this.repo);
      walk.markStart(
        walk.parseCommit(
          Objects.requireNonNull(this.ref, "null ref!").getObjectId()
        )
      );
      return new Mapped<>(
        DefaultCommit::new,
        walk
      );
    } catch (NullPointerException e) {
      throw new IOException("Invalid ref provided", e);
    }
  }

  @Override
  public XML asXml() throws IOException {
    final Directives dirs = new Directives().add("log").add("commits");
    this.commits().forEach(commit ->
      dirs.append(Directives.copyOf(commit.asXml().node()))
    );
    return new StrictXML(
      new XMLDocument(
        new Xembler(dirs).xmlQuietly()
      ),
      new Schema()
    );
  }
}
