package de.groth.dts.api.core.dto.plugins;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.groth.dts.api.core.util.plugins.AbstractPluginFactory;
import de.groth.dts.api.core.util.plugins.GenericPluginFactory;
import de.groth.dts.api.core.util.plugins.ParameterPluginFactory;
import de.groth.dts.api.core.util.plugins.ProcessingPluginFactory;

/**
 * Defines a constructor as pluginConstructor, so a concrete PluginFactory can
 * use this constructor to create plugins during runtime.
 * 
 * @see AbstractPluginFactory
 * @see GenericPluginFactory
 * @see ParameterPluginFactory
 * @see ProcessingPluginFactory
 * 
 * @author Christian Groth
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.CONSTRUCTOR)
public @interface PluginConstructor {
    /* marker annotation */
}
