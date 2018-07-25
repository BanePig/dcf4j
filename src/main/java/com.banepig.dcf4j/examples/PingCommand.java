package com.banepig.dcf4j.examples;

import com.banepig.dcf4j.Command;
import com.banepig.dcf4j.Optional;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageHistory;

import java.util.Arrays;
import java.util.List;

public class PingCommand {
    //Called on "!ping", but not on "!ping [User]", because there are too many args.
    @Command(label = "!ping", description = "Pings me pong", usage = "!ping")
    public void commandHandler(IMessage message) {
        message.getChannel().sendMessage("Pong!");
    }

    //Not called on "!ping" because of @Optional, would be called without it.
    @Command(label = "!ping", description = "Pings a user.", usage = "!ping [User]")
    public void commandHandler(IMessage message, IUser user) {
        message.getChannel().sendMessage("Pinged " + user.getName());
        user.getOrCreatePMChannel().sendMessage("Pong! You've been pinged by " + message.getAuthor().getName());
        // May cause 403 forbidden error if client doesn't have permission to send PM.
    }

    @Command(label = "!bulkDelete", description = "Deletes some messages.", usage = "!bulkDelete (Amount)")
    public void commandHandler(IMessage message, @Optional Long amount) {
        if (amount == null) amount = (long) 5; // If no amount parameter was provided, set to a default of 5.
        MessageHistory history = message.getChannel().getMessageHistory(amount.intValue());
        List<IMessage> messages = Arrays.asList(history.asArray());
        message.getChannel().bulkDelete(messages);
    }
}
