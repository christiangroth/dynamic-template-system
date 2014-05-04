package de.groth.dts.api.core.util;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Helper methods for date handling.
 * 
 * @author Christian Groth
 */
public final class DateHelper {
    private static final Logger LOGGER = Logger.getLogger(DateHelper.class);

    private DateHelper() {
    }

    /**
     * Now.
     * 
     * @return the date
     */
    public static Date now() {
        final Date now = new Date();
        DateHelper.LOGGER.debug("returning now " + now);
        return now;
    }
}
