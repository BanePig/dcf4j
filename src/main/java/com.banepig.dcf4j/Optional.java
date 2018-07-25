package com.banepig.dcf4j;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to denote parameters which may be null or not provided by the user.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Optional {
}
