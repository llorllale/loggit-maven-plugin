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

import com.jcabi.xml.XSD;
import com.jcabi.xml.XSDDocument;
import java.util.Collection;
import javax.xml.transform.Source;
import org.xml.sax.SAXParseException;

/**
 * The XML schema for the intermediate transformation stage.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.1.0
 */
public final class Schema implements XSD {
  private final String schemaFile;

  /**
   * Ctor.
   * 
   * @since 0.1.0
   */
  Schema() {
    this.schemaFile = "/xsd/schema.xsd";
  }

  @Override
  public Collection<SAXParseException> validate(Source xml) {
    return XSDDocument.make(
      Schema.class.getResourceAsStream(this.schemaFile)
    ).validate(xml);
  }
}
