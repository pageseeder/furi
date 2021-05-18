[![Maven Central](https://img.shields.io/maven-central/v/org.pageseeder.furi/pso-furi.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.pageseeder.furi%22%20AND%20a:%22pso-furi%22)

# FURI

This project is a Java implementation of URI templates and follows as close as possible the URI template notation 
defined in http://code.google.com/p/uri-templates/ and was inspired by Joe Gregorio's URI Template idea.

## URI Templates

URI templates can be used to produce URI given a set of parameters.

For example:
```
  /{username}/home
```

Becomes...
```
  /jsmith/home
```

When supplied with a username which value is jsmith.

## URI Patterns

This library also uses the URI template notation for pattern matching.

The idea stemmed from when we started using the URI template notation in the configuration of our server. 

We found it useful to be able to use the same notation for matching URIs and finding out possible parameters 
as for generating URIs.

This library provides additional methods for binding variables to data objects.
