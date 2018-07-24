package com.banepig.dcf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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
     *
     * @param args The arguments to call the method with.
     * @return The methods return.
     */
    Object invoke(Object... args) throws InvocationTargetException, IllegalAccessException {
        return commandExecutor.invoke(commandClassInstance, args);
    }

    /**
     * Attempts to invoke the commandExecutor.
     *
     * @param args The arguments to call the method with.
     * @return The methods return, or null if the method could not be invoked.
     */
    Object tryInvoke(Object... args) {
        Parameter[] parameters = commandExecutor.getParameters();
        int index = 0;
        for (Object argument : args) {
            if (parameters[index].getAnnotation(Required.class) != null && argument == null) return null;
            index++;
        }
        try {
            return commandExecutor.invoke(commandClassInstance, args);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            if (e.getClass() == InvocationTargetException.class) System.out.println(e.getCause().toString());
            return null;
        }
    }

    Boolean isAnnotationsValid() {
        Boolean requiredParameterFound = false;
        for (Parameter param : commandExecutor.getParameters()) {
            if (param.getAnnotation(Required.class) == null && requiredParameterFound) {
                return false;
            } else if (param.getAnnotation(Required.class) != null) requiredParameterFound = true;
        }
        return true;
    }
}
