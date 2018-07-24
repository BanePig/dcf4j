package com.banepig.dcf4j.examples;

import com.banepig.dcf4j.Command;
import com.banepig.dcf4j.Required;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class PingCommand {
    //Called on "!ping", but not on "!ping [User]", because there are too many args.
    @Command(label = "!ping", description = "Pings me pong", usage = "!ping")
    public void commandHandler(IMessage message) {
        message.getChannel().sendMessage("Pong!");
    }

    //Not called on "!ping" because of @Required, would be called without it.
    @Command(label = "!ping", description = "Pings a user.", usage = "!ping [User]")
    public void commandHandler(IMessage message, @Required IUser user) {
        message.getChannel().sendMessage("Pinged " + user.getName());
        user.getOrCreatePMChannel().sendMessage("Pong! You've been pinged by " + message.getAuthor().getName());
        // May cause 403 forbidden error if client doesn't have permission to send PM.
    }
}
