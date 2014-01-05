package net.escapecraft.component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Provides schema metadata on a component's database.
 *
 * @author Tulonsae
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SchemaDescriptor {
    String version();
}
