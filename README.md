# DCF4J [![](https://jitpack.io/v/BanePig/dcf4j.svg)](https://jitpack.io/#BanePig/dcf4j)

NOTE: At DCF4J's current state, it is not recommended you use this framework. I would suggest you use a more mature command framework, such as SDCF4J

A discord command framework for java. I got sick of constantly having to check syntax every time I wanted to make a command that I should be able to make in one line. This framework was made in 1 hour, so it still has lots of work needed. I would be greatful if you could report any issues you find. At the moment, this framework is only available for Discord4J, however JDA and Javacord versions are planned. 

## Maven

Add the following to your pom.xml, substituting %version% for your desired version.
```
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

```
<dependency>
  <groupId>com.github.BanePig</groupId>
  <artifactId>dcf4j</artifactId>
  <version>%version%</version>
</dependency>
```

## Gradle

Add the following to your build.gradle, substituting %version% for your desired version.
```
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```
  
```
dependencies {
  implementation 'com.github.BanePig:dcf4j:%version%'
}
```

## Usage

#### Information on usage can be found in the wiki.
