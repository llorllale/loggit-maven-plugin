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
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.xembly.Directives;
import org.xembly.Xembler;

/**
 * Default impl of {@link Commit}.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 */
final class DefaultCommit implements Commit {
  private final Repository repo;
  private final RevCommit rev;

  /**
   * Ctor.
   * 
   * @param repo the git repository
   * @param rev the git rev
   * @since 0.5.0
   */
  DefaultCommit(Repository repo, RevCommit rev) {
    this.repo = repo;
    this.rev = rev;
  }

  @Override
  public XML asXml() {
    return new StrictXML(
      new XMLDocument(
        new Xembler(
          new Directives()
            .add("commit")
              .add("id").set(this.rev.getId().getName()).up()
              .add("author")
                .add("name").set(this.rev.getAuthorIdent().getName()).up()
                .add("email").set(this.rev.getAuthorIdent().getEmailAddress()).up()
                .add("date").set(this.rev.getAuthorIdent().getWhen().toInstant()).up()
                .up()
              .add("message")
                .add("short").set(this.rev.getShortMessage()).up()
                .add("full").set(this.rev.getFullMessage()).up()
                .up()
              .add("taggedAs")
              .append(new TagsOf(this.repo, this.rev))
        ).xmlQuietly()
      ),
      new Schema()
    );
  }
}
