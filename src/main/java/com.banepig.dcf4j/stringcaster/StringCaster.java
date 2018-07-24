package com.banepig.dcf4j.stringcaster;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class StringCaster {
    /**
     * Returns a CastType enum from a class.
     *
     * @param clazz The class to base the CastType on.
     * @return A CastType if the conversion was successful, else null.
     */
    public static CastType getCastType(Class<?> clazz) {
        String className = clazz.getSimpleName().toUpperCase();
        try {
            return CastType.valueOf(className);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    private IDiscordClient client;


    public StringCaster(IDiscordClient client) {
        this.client = client;
    }

    /**
     * Attempts to cast a string to one of the following objects:
     * - String
     * - Long
     * - Double
     * - IUser
     * - IChannel
     * If multiple casts are successful, it will return the lowest object on the above list.
     *
     * @param string The string to attempt to cast.
     * @return The casted object.
     */
    public Object autoCast(String string) {
        IUser userCast = tryUserCast(string);
        if (userCast != null) return userCast;

        IChannel channelCast = tryChannelCast(string);
        if (channelCast != null) return channelCast;

        Long longCast = tryLongCast(string);
        if (longCast != null) return longCast;

        Double doubleCast = tryDoubleCast(string);
        if (doubleCast != null) return doubleCast;

        return string;
    }

    /**
     * Attempts to cast a string to the desired type.
     *
     * @param type   The type to cast the string to.
     * @param string The string to cast.
     * @return The desired type, derived from the provided string. Null if the string cannot be casted.
     */
    public Object autoCast(CastType type, String string) {
        if (type == null) return null;
        switch (type) {
            case LONG:
                return tryLongCast(string);
            case DOUBLE:
                return tryDoubleCast(string);
            case ICHANNEL:
                return tryChannelCast(string);
            case STRING:
                return string;
            case IUSER:
                return tryUserCast(string);
        }
        return null;
    }

    /**
     * Attempts to cast a string to a long.
     *
     * @param string The string to cast.
     * @return A long, if the cast was successful, otherwise null.
     */
    public Long tryLongCast(String string) {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Attempts to cast a string to a double.
     *
     * @param string The string to cast.
     * @return A double, if the cast was successful, otherwise null.
     */
    public Double tryDoubleCast(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Attempts to cast a string to an IUser.
     *
     * @param string The string to cast.
     * @return An IUser, if the cast was successful, otherwise null.
     */
    public IUser tryUserCast(String string) {
        try {
            String id;
            if (string.startsWith("<@!")) {
                id = string.substring(3, string.length() - 1);
            } else if (string.startsWith("<@")) {
                id = string.substring(2, string.length() - 1);
            } else return null;

            return client.getUserByID(Long.parseLong(id));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Attempts to cast a string to an IMessage.
     *
     * @param string The string to cast.
     * @return An IChannel, if the cast was successful, otherwise null.
     */
    public IChannel tryChannelCast(String string) {
        try {
            String id;
            if (string.startsWith("<#")) {
                id = string.substring(2, string.length() - 1);
            } else return null;

            return client.getChannelByID(Long.parseLong(id));
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
