package com.banepig.dcf4j;

import com.banepig.dcf4j.stringcaster.CastType;
import com.banepig.dcf4j.stringcaster.StringCaster;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandDispatcher {
    private IDiscordClient client;
    private StringCaster stringCaster;
    private HashMap<String, ArrayList<CommandExecutor>> registeredCommands = new HashMap<>();
    private String usageMessage = "Invalid usage! Usage: %USAGE%";

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
     * Initializes a CommandDispatcher object
     *
     * @param client The client this object should use to listen for events.
     * @param usageMessage The custom invalid usage message which dcf4j will send to users if they use a command incorrectly.
     */
    public CommandDispatcher(IDiscordClient client, String usageMessage) {
        this.client = client;
        this.stringCaster = new StringCaster(client);

        this.usageMessage = usageMessage;

        EventDispatcher dispatcher = client.getDispatcher();
        dispatcher.registerListener(this);
    }

    /**
     * Registers commands, and methods with the "Commands" annotation will attempt to be called.
     *
     * @param commands The commands to register.
     */
    public void registerCommands(Object... commands) throws IllegalArgumentException {
        for (Object commandClassInstance : commands) {
            Class commandClass = commandClassInstance.getClass();
            for (Method commandExecutorMethod : commandClass.getMethods()) {
                Command annotation = commandExecutorMethod.getAnnotation(Command.class);
                if (annotation == null) continue;
                String label = annotation.label().toLowerCase();
                CommandExecutor commandExecutor = new CommandExecutor(commandClassInstance, commandExecutorMethod);
                registeredCommands.computeIfAbsent(label, k -> new ArrayList<>());
                ArrayList<CommandExecutor> commandsWithLabel = registeredCommands.get(label);
                commandsWithLabel.add(commandExecutor);
                if (!commandExecutor.isAnnotationsValid()) {
                    throw new IllegalAnnotationException();

                }
                registeredCommands.put(label.toLowerCase(), commandsWithLabel);
            }
        }
    }

    /**
     * Unregisters commands, they will no longer be called.
     *
     * @param commands The commands to unregister.
     */
    public void unregisterCommands(Object... commands) {
        for (Object commandClassInstance : commands) {
            Class commandClass = commandClassInstance.getClass();
            for (Method commandExecutorMethod : commandClass.getMethods()) {
                Command annotation = commandExecutorMethod.getAnnotation(Command.class);
                if (annotation == null) continue;
                String label = annotation.label();
                CommandExecutor commandExecutor = new CommandExecutor(commandClassInstance, commandExecutorMethod);
                registeredCommands.computeIfAbsent(label, k -> new ArrayList<>());
                ArrayList<CommandExecutor> commandsWithLabel = registeredCommands.get(label);
                commandsWithLabel.remove(commandExecutor);
                registeredCommands.put(label, commandsWithLabel);
            }
        }
    }

    /**
     * Searches for commands with messages.
     *
     * @param event The message.
     */
    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) {
        List<String> args = Arrays.asList(event.getMessage().getContent().split(" "));

        String label = args.get(0).toLowerCase();
        args = args.subList(1, args.size());
        dispatchCommand(label, args.toArray(new String[args.size()]), event.getMessage());
    }

    /**
     * @param label The label of the command.
     * @param args  The command arguments.
     */
    private void dispatchCommand(String label, String[] args, IMessage message) {
        List<CommandExecutor> commandExecutors = registeredCommands.get(label);
        if (commandExecutors == null || commandExecutors.isEmpty()) return;

        Boolean commandExecuted = false;

        invokeLoop:
        for (CommandExecutor commandExecutor : commandExecutors) {
            Class<?>[] commandExecutorParams = commandExecutor.getCommandExecutor().getParameterTypes();
            Object[] castedArgs = new Object[commandExecutorParams.length];
            castedArgs[0] = message;
            int index = 1;
            for (String argument : args) {
                if (commandExecutorParams.length <= index) continue invokeLoop;
                CastType goalType = StringCaster.getCastType(commandExecutorParams[index]);
                castedArgs[index] = stringCaster.autoCast(goalType, argument);
                index++;
            }
            for (int i = args.length + 1; i < castedArgs.length - args.length; i++) castedArgs[i] = null;
            boolean isCorrectUsage = commandExecutor.tryInvoke(castedArgs);
            if (isCorrectUsage) commandExecuted = true;
        }

        if (!commandExecuted) {
            Command command = commandExecutors.get(0).getAnnotation();
            message.getChannel().sendMessage(usageMessage.replace("%USAGE%", command.usage()));
        }
    }

    public String getUsageMessage() {
        return usageMessage;
    }

    public void setUsageMessage(String usageMessage) {
        this.usageMessage = usageMessage;
    }
}
