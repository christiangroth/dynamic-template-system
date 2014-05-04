package de.groth.dts.api.core.dto.plugins;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.groth.dts.api.core.util.plugins.AbstractPluginFactory;

/**
 * Defines a method as capable of converting the current plugin instance to a
 * representation in xml. This method is used for generating the configuration
 * at runtime.
 * 
 * @see AbstractPluginFactory
 * 
 * @author Christian Groth
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface PluginToXml {
    /* marker annotation */
}
