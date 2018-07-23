package com.banepig.dcf4j;

import org.jetbrains.annotations.NotNull;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandDispatcher {
    private IDiscordClient client;
    private StringCaster stringCaster;
    private ArrayList<Object> registeredCommands = new ArrayList<>();


    /**
     * Initializes a CommandDispatcher object
     *
     * @param client The client this object should use to listen for events.
     */
    public CommandDispatcher(IDiscordClient client) {
        this.client = client;
        this.stringCaster = new StringCaster(client);

        EventDispatcher dispatcher = client.getDispatcher();
        dispatcher.registerListener(this);
    }

    /**
     * Registers commands, and methods with the "Commands" annotation will attempt to be called.
     *
     * @param commands The commands to register.
     */
    public void registerCommands(Object... commands) {
        registeredCommands.addAll(Arrays.asList(commands));
    }

    /**
     * Unregisters commands, they will no longer be called.
     *
     * @param commands The commands to unregister.
     */
    public void unregisterCommands(Object... commands) {
        registeredCommands.removeAll(Arrays.asList(commands));
    }

    /**
     * Searches for commands with messages.
     *
     * @param event The message.
     */
    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) {
        List<String> args = Arrays.asList(event.getMessage().getContent().split(" "));

        String label = args.get(0);
        args = args.subList(1, args.size());
        ArrayList<Object> castedArgs = new ArrayList<>();
        castedArgs.add(event.getMessage());
        for (String arg : args) castedArgs.add(stringCaster.autoCast(arg));

        for (Object command : registeredCommands) {
            for (Method method : command.getClass().getDeclaredMethods()) {
                Command annotation = method.getAnnotation(Command.class);
                if(annotation == null) continue;
                if(!annotation.name().equals(label)) continue;
                try {
                    method.invoke(command, castedArgs.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
