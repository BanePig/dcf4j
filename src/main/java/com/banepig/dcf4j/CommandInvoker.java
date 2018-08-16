package com.banepig.dcf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


public class CommandInvoker {
    public static boolean tryDispatch(CommandHandler commandHandler, List<Object> args) throws InvocationTargetException, IllegalAccessException {
        try {
            Object methodReturn = commandHandler.getMethod().invoke(commandHandler.getInstance(), args.toArray());
            if (methodReturn instanceof Boolean) {
                return (Boolean) methodReturn;
            } else return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}