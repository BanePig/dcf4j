package com.banepig.dcf4j

import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IMessage

class CommandDispatcher(val client: IDiscordClient) {
    init {
        val dispatcher = client.dispatcher
        dispatcher.registerListener(this)
    }

    private val registeredCommands = HashMap<String /*Label*/, ArrayList<CommandHandler> /*CommandMethod*/>()

    /**
     * Registers all commands in a class.
     */
    fun registerCommand(commandInstance: Any) {
        for (method in commandInstance::class.java.methods) {
            if (method.getAnnotationsByType(Command::class.java).isNotEmpty()) registerCommand(CommandHandler(method, commandInstance))
        }
    }

    /**
     * Registers a command by method.
     */
    fun registerCommand(commandHandler: CommandHandler) {
        val commandAnnotation = commandHandler.method.getAnnotation(Command::class.java)
                ?: throw Exception("Command annotation not found")
        val label = commandAnnotation.label.toLowerCase()
        registeredCommands[label] = (registeredCommands[label] ?: ArrayList()).apply { add(commandHandler) }
    }

    @EventSubscriber
    fun onMessage(event: MessageReceivedEvent) {
        val args = ArrayList(event.message.content.split(" "))
        if (args.isEmpty()) return
        val label = args[0].toLowerCase()
        args.removeAt(0)

        var commandSuccessful = false

        for (commandHandler in registeredCommands[label] ?: return) {
            val commandMethod = commandHandler.method
            val parameterTypes = commandMethod.parameterTypes
            if (parameterTypes.isEmpty() || parameterTypes[0] != IMessage::class.java) throw Exception("Commands first parameter is not of IMessage type")
            val castedArgs = ArrayList<Any?>()
            castedArgs.add(event.message)
            for (arg in args.withIndex()) {
                if (parameterTypes.size < arg.index + 1) return
                castedArgs.add(arg.value.to(parameterTypes[arg.index + 1], client))
            }
            val wasDispatched = CommandInvoker.tryDispatch(commandHandler, castedArgs)
            if (wasDispatched) commandSuccessful = true
        }

        if (registeredCommands[label]?.isNotEmpty() == true && !commandSuccessful) {
            event.channel.sendMessage(registeredCommands[label]!![0].method.getAnnotation(Command::class.java).usage)
        }
    }
}