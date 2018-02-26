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

import com.jcabi.xml.XML;
import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.cactoos.io.InputOf;
import org.cactoos.io.LengthOf;
import org.cactoos.io.OutputTo;
import org.cactoos.io.TeeInput;
import org.cactoos.scalar.IoCheckedScalar;
import org.eclipse.jgit.lib.Constants;
import org.llorllale.mvn.plgn.loggit.xsl.Identity;

/**
 * Changelog.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.2.0
 */
@Mojo(name = "changelog")
public final class Changelog extends AbstractMojo {
  @Parameter(name = "repo", defaultValue = "${basedir}")
  private File repo;

  @Parameter(name = "outputFile", defaultValue = "gitlog.xml")
  private File xml;

  /**
   * Ctor.
   * 
   * @since 0.2.0
   */
  public Changelog() {
    //intentional
  }

  /**
   * Ctor.
   * 
   * @param repo path to git repo
   * @param output file to which to save the XML
   * @since 0.2.0
   */
  public Changelog(File repo, File output) {
    this.repo = repo;
    this.xml = output;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      new IoCheckedScalar<>(
        new LengthOf(
          new TeeInput(
            new InputOf(
              this.transform(
                new DefaultGit(this.repo.toPath().resolve(Constants.DOT_GIT)).log().asXml()
              )
            ),
            new OutputTo(this.xml)
          )
        )
      ).value();
    } catch (IOException e) {
      throw new MojoFailureException(
        String.format("Cannot save XML from repo %s to file %s", this.repo, this.xml),
        e
      );
    }
  }

  /**
   * Transforms the XML using a stylesheet.
   * 
   * @param original the original XML
   * @return the transformed XML
   * @throws IOException if there's an issue reading the stylesheet
   */
  private String transform(XML original) throws IOException {
    return new Identity().applyTo(original);
  }
}