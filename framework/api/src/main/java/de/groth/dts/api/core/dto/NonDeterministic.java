package de.groth.dts.api.core.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.groth.dts.api.core.dto.generics.IGeneric;
import de.groth.dts.api.core.dto.parameters.IParameter;

/**
 * Marks an {@link IParameter} or {@link IGeneric} as non deterministic, meaning
 * two instances with the same setup executed at different times will show up
 * different results.
 * 
 * @author Christian Groth
 * 
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface NonDeterministic {
    /* marker annotation */
}
