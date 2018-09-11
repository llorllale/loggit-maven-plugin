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

package org.llorllale.mvn.plgn.loggit.xsl.pre;

import org.cactoos.io.ResourceOf;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.llorllale.mvn.plgn.loggit.xsl.StylesheetEnvelope;

/**
 * Copies commits down until a given commit ID is found (inclusive).
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 1.1.0
 */
public final class StartCommit extends StylesheetEnvelope {
  /**
   * Ctor.
   * @param hash Commit id
   * @since 1.1.0
   */
  public StartCommit(String hash) {
    super(
      new ResourceOf("xsl/pre/start-commit.xsl"),
      new MapOf<>(new MapEntry<>("commitId", hash))
    );
  }
}