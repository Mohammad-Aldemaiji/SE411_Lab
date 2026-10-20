# Lab04 Maven Intro

SE411 Lab 04 — introduction to Maven.

## What this project does

- Imports **JavaFX 23** (`org.openjfx:javafx-controls`) as a Maven dependency.
- Uses the **javafx-maven-plugin** so the app runs with `mvn clean javafx:run`.
- Generates this documentation website with the **Maven Site Plugin** (`mvn clean site`).

## Project structure

```
Lab04/
├── pom.xml
└── src/
    ├── main/java/edu/psu/se411/lab04/MainClass.java
    └── site/            (site.xml + markdown pages for this website)
```

See [Getting Started](getting-started.html) for build instructions, and the
*Project Reports* menu for the team, license, dependencies and Javadoc.
