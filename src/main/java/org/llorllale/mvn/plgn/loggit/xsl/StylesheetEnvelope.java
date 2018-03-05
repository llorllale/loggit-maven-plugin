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

package org.llorllale.mvn.plgn.loggit.xsl;

import com.jcabi.xml.Sources;
import com.jcabi.xml.XML;
import com.jcabi.xml.XSL;
import com.jcabi.xml.XSLDocument;
import java.util.Collections;
import java.util.Map;
import org.cactoos.Input;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;
import org.cactoos.text.TextOf;

/**
 * Implements XSL functions.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.2.0
 */
public abstract class StylesheetEnvelope implements XSL {
  private final UncheckedScalar<XSL> stylesheet;

  /**
   * Ctor.
   * 
   * @param input the XSL file
   * @since 0.2.0
   */
  public StylesheetEnvelope(Input input) {
    this(input, Collections.emptyMap());
  }

  /**
   * Ctor.
   * 
   * @param xsl the XSL
   * @param params XSL params
   * @since 0.4.0
   */
  public StylesheetEnvelope(Input xsl, Map<String, Object> params) {
    this(
      new UncheckedScalar<>(
        new StickyScalar<>(
          () -> new XSLDocument(new TextOf(xsl).asString(), Sources.DUMMY, params)
        )
       )
    );
  }

  /**
   * Ctor.
   * 
   * @param xsl the XSL
   * @since 0.4.0
   */
  public StylesheetEnvelope(UncheckedScalar<XSL> xsl) {
    this.stylesheet = xsl;
  }

  @Override
  public final XML transform(XML xml) {
    return this.stylesheet.value().transform(xml);
  }

  @Override
  public final String applyTo(XML xml) {
    return this.stylesheet.value().applyTo(xml);
  }

  @Override
  public final XSL with(Sources src) {
    return this.stylesheet.value().with(src);
  }

  @Override
  public final XSL with(String name, Object value) {
    return this.stylesheet.value().with(name, value);
  }
}
