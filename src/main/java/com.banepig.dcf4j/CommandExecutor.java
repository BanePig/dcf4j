package com.banepig.dcf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * A class which makes it easier to invoke commands.
 */
class CommandExecutor {
    private Object commandClassInstance;
    private Method commandExecutor;

    CommandExecutor(Object commandClassInstance, Method commandMethod) {

        this.commandClassInstance = commandClassInstance;
        this.commandExecutor = commandMethod;
    }

    Object getCommandClassInstance() {
        return commandClassInstance;
    }

    void setCommandClassInstance(Object commandClassInstance) {
        this.commandClassInstance = commandClassInstance;
    }

    Method getCommandExecutor() {
        return commandExecutor;
    }

    void setCommandExecutor(Method commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    Command getAnnotation() {
        return commandExecutor.getAnnotation(Command.class);
    }

    /**
     * Invokes the commandExecutor.
     * @param args The arguments to call the method with.
     * @return True, if the command was used correctly, else false.
     */
    boolean invoke(Object... args) throws InvocationTargetException, IllegalAccessException {
        Object isCorrectUsage = commandExecutor.invoke(commandClassInstance, args);
        if (isCorrectUsage == null) return true;
        return (Boolean) isCorrectUsage;
    }

    /**
     * Attempts to invoke the commandExecutor.
     *
     * @param args The arguments to call the method with.
     * @return True, if the command was used correctly, else false.
     */
    boolean tryInvoke(Object... args) {
        Parameter[] parameters = commandExecutor.getParameters();
        int index = 0;
        for (Object argument : args) {
            if (parameters[index].getAnnotation(Optional.class) == null && argument == null) return false;
            index++;
        }
        try {
            return invoke(args);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            if (e.getClass() == InvocationTargetException.class) {
                System.out.println(e.getCause().toString());
                return true; // This is a programming error.
            }
            return false;
        }
    }

    boolean isAnnotationsValid() {
        Boolean optionalParameterFound = false;
        for (Parameter param : commandExecutor.getParameters()) {
            if (param.getAnnotation(Optional.class) == null && optionalParameterFound) {
                return false;
            } else if (param.getAnnotation(Optional.class) != null) optionalParameterFound = true;
        }
        return true;
    }
}
