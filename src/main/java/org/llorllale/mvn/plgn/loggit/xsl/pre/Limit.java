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
 * Limits the number of commit entries.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.4.0
 */
public final class Limit extends StylesheetEnvelope {
  /**
   * Ctor.
   * 
   * @param max the max number of commit entries
   * @since 0.4.0
   */
  public Limit(int max) {
    super(
      new ResourceOf("xsl/pre/limit.xsl"),
      new MapOf<>(new MapEntry<>("max", String.valueOf(max)))
    );
  }
}
