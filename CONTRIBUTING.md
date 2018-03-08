## How to contribute?
Fork the repository, code your changes, then submit a pull request. We ask 
several things:

* Small changes! Break up bugs/features into chunks that can be reasonably
reviewed in 15 minutes
* Ensure your contribution meets the project's styleguides by running 
the following and making sure there are no errors: 
`mvn -P release-profile clean install -DskipTests`
* Your code contribution must include tests
* Make sure `mvn test` runs with no errors

If your PR is accepted it will be merged into `master` immediately and released to sonatype.

### Code Coverage
Coverage is reported via [CodeCov.io](https://codecov.io/gh/llorllale/loggit-maven-plugin)
and also the [project's site](https://llorllale.github.io/loggit-maven-plugin/cobertura/).

**The current minimum target coverage is 85%.**

### Feedback
Please direct any questions, feature requests or bugs to the 
[issue tracker](https://github.com/llorllale/loggit-maven-plugin/issues/).
