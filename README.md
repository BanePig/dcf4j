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
Getting started is easy, especially right now when there is only one feature.
Here is an example on how to create a simple "Ping!" command.

```
public void register() {
  CommandDispatcher dispatcher = new CommandDispatcher(client); //Where client is your IDiscordClient
  dispatcher.registerCommands(this);
}

@Command(label = "!ping", description = "Pongs your ping.", usage = "!ping")
public void handlePingCommand(IMessage message) {
  message.getChannel().sendMessage("Pong!");
}
```

All commands' first parameter must be an IMessage, if it is not, the command will not work:

```
@Command(label = "!ping", description = "Pongs your ping.", usage = "!ping")
public void handlePingCommand(IMessage message) { //Works!
  message.getChannel().sendMessage("Pong!");
}
```
```
@Command(label = "!ping", description = "Pongs your ping.", usage = "!ping")
public void handlePingCommand() { //Fails.
  message.getChannel().sendMessage("Pong!");
}
```

DCF4J also supports specific parameter, and will automatically check syntax, so you don't have to. For example:

```
@Command(label = "!ping", description = "Pings a user.", usage = "!ping [User]")
public void handleEatCommand(IMessage message, @Required IUser user) {
  message.getChannel().sendMessage("Ate " + user.getName() + "!");
}
```

Notice the @Required before the 2nd parameter, this is esentially DCF4J's equivalent of @NotNull. Normally, when a message is sent, DCF4J will invoke all commands which have the same label, replacing any missing arguments with null, this allows for more customization, but can also be annoying if you don't want null values, so I added @Required to fix this, any argument that has @Required will not accept a null value, and if DCF4J attempts to use one, it will skip the command entirely. Please note that you cannot have an optional parameter after a required one, because if you do DCF4J will not know which argument the user was trying to use. There are plans to fix this issue using something similar to user=@BanePig, however this are not high priority at the moment.

Types which can currently be used are:
* IUser
* IChannel
* Double
* Long
* String
Or any parent classes of the above.
