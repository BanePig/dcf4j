# DCF4J [![](https://jitpack.io/v/BanePig/dcf4j.svg)](https://jitpack.io/#BanePig/dcf4j)

A java command framework for discord. This framework automatically checks syntax, and has many options for customization to write more with less.

## Requirements

dcf4j requires Discord4J to be installed. Discord4J can be found at its [official github repo](https://github.com/Discord4J/Discord4J).
After installing Discord4J follow the instructions below.

## Maven

Add the following to your pom.xml, substituting %version% for your desired version.
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

```xml
<dependency>
  <groupId>com.github.BanePig</groupId>
  <artifactId>dcf4j</artifactId>
  <version>%version%</version>
</dependency>
```

## Gradle

Add the following to your build.gradle, substituting %version% for your desired version.
```gradle
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```
  
```gradle
dependencies {
  implementation 'com.github.BanePig:dcf4j:%version%'
}
```

## Usage

#### Information on usage can be found in the wiki.
