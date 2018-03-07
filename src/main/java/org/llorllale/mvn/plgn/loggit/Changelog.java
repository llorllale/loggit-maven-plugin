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
import org.llorllale.mvn.plgn.loggit.xsl.post.Custom;
import org.llorllale.mvn.plgn.loggit.xsl.post.Identity;
import org.llorllale.mvn.plgn.loggit.xsl.post.Markdown;
import org.llorllale.mvn.plgn.loggit.xsl.pre.EndTag;
import org.llorllale.mvn.plgn.loggit.xsl.pre.Limit;
import org.llorllale.mvn.plgn.loggit.xsl.pre.Pattern;
import org.llorllale.mvn.plgn.loggit.xsl.pre.StartTag;

/**
 * Changelog.
 *
 * @author George Aristy (george.aristy@gmail.com)
 * @since 0.2.0
 */
@Mojo(name = "changelog")
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public final class Changelog extends AbstractMojo {
  @Parameter(name = "repo", defaultValue = "${basedir}")
  private File repo;

  @Parameter(name = "outputFile", defaultValue = "${project.build.directory}/gitlog.xml")
  private File outputFile;

  @Parameter(name = "format", defaultValue = "default")
  private String format;

  @Parameter(name = "customFormatFile")
  private File customFormatFile;

  @Parameter(name = "ref", defaultValue = Constants.MASTER)
  private String ref;

  @Parameter(name = "maxEntries", defaultValue = "2147483647")
  private int maxEntries;

  @Parameter(name = "startTag", defaultValue = "")
  private String startTag;

  @Parameter(name = "endTag", defaultValue = "")
  private String endTag;

  @Parameter(name = "includeRegex", defaultValue = ".*")
  private String includeRegex;

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
    this(repo, output, "default");
  }

  /**
   * Ctor.
   * 
   * @param repo path to git repo
   * @param output file to which to save the XML
   * @param format the format for the output
   * @since 0.2.0
   */
  public Changelog(File repo, File output, String format) {
    this(repo, output, format, null);
  }

  /**
   * Ctor.
   * 
   * @param repo path to git repo
   * @param output file to which to save the XML
   * @param format the format for the output
   * @param customFormat path to customFormat
   * @since 0.2.0
   */
  public Changelog(File repo, File output, String format, File customFormat) {
    this(repo, output, format, customFormat, Constants.MASTER);
  }

  /**
   * Ctor.
   * 
   * @param repo path to git repo
   * @param output file to which to save the XML
   * @param format the format for the output
   * @param customFormat path to customFormat
   * @param ref the ref to point to in order to fetch the log
   * @since 0.3.0
   */
  @SuppressWarnings("checkstyle:ParameterNumber")
  public Changelog(File repo, File output, String format, File customFormat, String ref) {
    this(repo, output, format, customFormat, ref, Integer.MAX_VALUE);
  }

  /**
   * Ctor.
   * 
   * @param repo path to git repo
   * @param output file to which to save the XML
   * @param format the format for the output
   * @param customFormat path to customFormat
   * @param ref the ref to point to in order to fetch the log
   * @param maxEntries max number of entries to include in the log
   * @since 0.4.0
   */
  @SuppressWarnings("checkstyle:ParameterNumber")
  public Changelog(
    File repo, File output, String format,
    File customFormat, String ref, int maxEntries
  ) {
    this(repo, output, format, customFormat, ref, maxEntries, "");
  }

  /**
   * Ctor.
   * 
   * @param repo path to git repo
   * @param output file to which to save the XML
   * @param format the format for the output
   * @param customFormat path to customFormat
   * @param ref the ref to point to in order to fetch the log
   * @param maxEntries max number of entries to include in the log
   * @param startTag starting tag
   * @since 0.5.0
   */
  @SuppressWarnings("checkstyle:ParameterNumber")
  public Changelog(
    File repo, File output, String format,
    File customFormat, String ref, int maxEntries,
    String startTag
  ) {
    this(repo, output, format, customFormat, ref, maxEntries, startTag, ".*");
  }

  /**
   * Ctor.
   * 
   * @param repo path to git repo
   * @param output file to which to save the XML
   * @param format the format for the output
   * @param customFormat path to customFormat
   * @param ref the ref to point to in order to fetch the log
   * @param maxEntries max number of entries to include in the log
   * @param startTag starting tag
   * @param includeRegex the regular expression that commit messages must match
   * @since 0.6.0
   */
  @SuppressWarnings("checkstyle:ParameterNumber")
  public Changelog(
    File repo, File output, String format,
    File customFormat, String ref, int maxEntries,
    String startTag, String includeRegex
  ) {
    this(
      repo, output, format,
      customFormat, ref, maxEntries,
      startTag, includeRegex, ""
    );
  }

  /**
   * Ctor.
   * 
   * @param repo path to git repo
   * @param output file to which to save the XML
   * @param format the format for the output
   * @param customFormat path to customFormat
   * @param ref the ref to point to in order to fetch the log
   * @param maxEntries max number of entries to include in the log
   * @param startTag starting tag
   * @param includeRegex the regular expression that commit messages must match
   * @param endTag the ending tag
   * @since 0.7.0
   */
  @SuppressWarnings("checkstyle:ParameterNumber")
  public Changelog(
    File repo, File output, String format,
    File customFormat, String ref, int maxEntries,
    String startTag, String includeRegex, String endTag
  ) {
    this.repo = repo;
    this.outputFile = output;
    this.format = format;
    this.customFormatFile = customFormat;
    this.ref = ref;
    this.maxEntries = maxEntries;
    this.startTag = startTag;
    this.includeRegex = includeRegex;
    this.endTag = endTag;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      new IoCheckedScalar<>(
        new LengthOf(
          new TeeInput(
            new InputOf(
              this.postprocess(
                this.preprocess(
                  new DefaultGit(
                    this.repo.toPath().resolve(Constants.DOT_GIT), this.ref
                  ).log().asXml()
                )
              )
            ),
            new OutputTo(this.outputFile)
          )
        )
      ).value();
    } catch (IOException e) {
      throw new MojoFailureException(
        String.format("Cannot save XML from repo %s to file %s", this.repo, this.outputFile),
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
  private String postprocess(XML original) throws IOException {
    final String output;
    if ("markdown".equals(this.format)) {
      output = new Markdown().applyTo(original);
    } else if ("custom".equals(this.format)) {
      output = new Custom(new InputOf(this.customFormatFile)).applyTo(original);
    } else {
      output = new Identity().applyTo(original);
    }
    return output;
  }

  /**
   * Pre-processes the XML log.
   * 
   * @param xml original xml
   * @return the preprocessed XML
   * @throws IOException if there's an error during the transformation
   */
  private XML preprocess(XML xml) throws IOException {
    return new Pattern(this.includeRegex).transform(
      new EndTag(this.endTag).transform(
        new StartTag(this.startTag).transform(
          new Limit(this.maxEntries).transform(xml)
        )
      )
    );
  }
}
