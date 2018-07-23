# DCF4J
A discord command framework for discord. I got sick of constantly having to check syntax every time I wanted to make a command that I should be able to make in one line. This framework was made in 1 hour, so it still has lots of work needed. I would be greatful if you could report any issues you find. At the moment, this framework is only available for Discord4J, however JDA and Javacord versions are planned.

## Installation
To install DCF4J follow the instructions found on [jithub.io](https://jitpack.io/private#BanePig/dcf4j)

## Usage
Getting started is easy, especially right now when there is only one feature.
Here is an example on how to create a simple "Ping! Pong!" command.

```
public void register() {
  CommandDispatcher dispatcher = new CommandDispatcher(client); //Where client is your IDiscordClient
  dispatcher.registerCommands(this);
}

@Command(name = "!ping", description = "Pongs your ping.", usage = "!ping")
public void handlePingCommand(IMessage message) {
  message.getChannel().sendMessage("Pong!");
}
```

All commands' first argument must be an IMessage, if it is not, the command will not work:

```
@Command(name = "!ping", description = "Pongs your ping.", usage = "!ping")
public void handlePingCommand(IMessage message) { //Works!
  message.getChannel().sendMessage("Pong!");
}
```
```
@Command(name = "!ping", description = "Pongs your ping.", usage = "!ping")
public void handlePingCommand() { //Fails.
  message.getChannel().sendMessage("Pong!");
}
```

dcf4j also supports arguments, and will automatically check syntax, so you don't have to. For example:

```
@Command(name = "!eat", description = "Eats a user.", usage = "!eat [User]")
public void handleEatCommand(IMessage message, IUser user) {
  message.getChannel().sendMessage("Ate " + user.getName() + "!");
}
```

This command will be activated if a user types "!eat @BanePig", however if the user types "!eat @59fja5jga", and 59fja5jga is not a user, it will fail to invoke the command.
