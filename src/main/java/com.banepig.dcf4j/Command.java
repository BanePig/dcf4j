package com.banepig.dcf4j;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The annotations used to define commands.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String label();

    String description();

    String usage();
}
