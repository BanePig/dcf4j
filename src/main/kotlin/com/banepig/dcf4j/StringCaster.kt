package com.banepig.dcf4j

import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser

fun String.to(type: Class<*>, client: IDiscordClient): Any? {
    return when (type) {
        Int::class.java -> toIntOrNull()
        Long::class.java -> toLongOrNull()
        Float::class.java -> toFloatOrNull()
        Double::class.java -> toDoubleOrNull()
        IUser::class.java -> getUserByMention(client)
        IChannel::class.java -> getChannelByMention(client)
        IRole::class.java -> getRoleByMention(client)
        String::class.java -> this
        else -> null
    }
}

private fun String.getUserByMention(client: IDiscordClient): IUser? {
    val mention = this
    when {
        mention.startsWith("<@!") -> {
            val userID = mention.substring(3, mention.length - 1)
            return try {
                client.getUserByID(java.lang.Long.parseLong(userID))
            } catch (e: NumberFormatException) {
                null
            }

        }
        mention.startsWith("<@") -> {
            val userID = mention.substring(2, mention.length - 1)
            return try {
                client.getUserByID(java.lang.Long.parseLong(userID))
            } catch (e: NumberFormatException) {
                null
            }

        }
        else -> return null
    }
}

private fun String.getRoleByMention(client: IDiscordClient): IRole? {
    val mention = this
    return when {
        mention.startsWith("<@&") -> {
            val roleID = mention.substring(3, mention.length - 1)
            try {
                client.getRoleByID(java.lang.Long.parseLong(roleID))
            } catch (e: NumberFormatException) {
                null
            }

        }
        else -> null
    }
}

private fun String.getChannelByMention(client: IDiscordClient): IChannel? {
    val mention = this
    return when {
        mention.startsWith("<#") -> {
            val channelID = mention.substring(2, mention.length - 1)
            try {
                client.getChannelByID(java.lang.Long.parseLong(channelID))
            } catch (e: NumberFormatException) {
                null
            }

        }
        else -> null
    }
}