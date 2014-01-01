package net.escapecraft.component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Provides metadata on a component.
 *
 * @author james
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentDescriptor {
    String slug();
    String name();
    String version();
}
