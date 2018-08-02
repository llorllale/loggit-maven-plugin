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
 * Includes commits with messages that match a given regular expression.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @see <a href="https://www.w3.org/TR/xpath-functions-30/#func-matches">fn:matches</a>
 * @see <a href="https://www.w3.org/TR/xpath-functions-30/#flags">Flags</a>
 * @since 0.6.0
 */
public final class Pattern extends StylesheetEnvelope {
  /**
   * Ctor.
   * 
   * @param regex the regular expression
   * @param flags regex flags
   * @since 0.6.0
   */
  public Pattern(String regex, String flags) {
    super(
      new ResourceOf("xsl/pre/pattern.xsl"),
      new MapOf<>(
          new MapEntry<>("regex", regex),
          new MapEntry<>("flags", flags)
      )
    );
  }
}
