package de.groth.dts.api.core.dao;

import de.groth.dts.api.core.util.DateHelper;

/**
 * General context holding relevant path information.
 * 
 * @author Christian Groth
 * 
 */
public class PathContext {

    /**
     * Time of context creation
     */
    protected final long timeId;

    private final String dtsFilePath;
    private final String baseDtsPath;
    private final String baseExportPath;

    /**
     * Creates a new instance
     * 
     * @param baseDtsPath
     *                the base dts path
     * @param baseExportPath
     *                the base export path
     * @param dtsFilePath
     *                the dts file path (relative to base dts path)
     */
    public PathContext(final String baseDtsPath, final String baseExportPath,
            final String dtsFilePath) {
        super();
        this.timeId = DateHelper.now().getTime();
        this.dtsFilePath = dtsFilePath;
        this.baseDtsPath = baseDtsPath;
        this.baseExportPath = baseExportPath;
    }

    /**
     * Gets the dtsFilePath (path and filename).
     * 
     * @return dts file path
     */
    public String getDtsFilePath() {
        return this.dtsFilePath;
    }

    /**
     * Gets the base dts path.
     * 
     * @return base dts path
     */
    public String getBaseDtsPath() {
        return this.baseDtsPath;
    }

    /**
     * Gets the base export path.
     * 
     * @return base export path
     */
    public String getBaseExportPath() {
        return this.baseExportPath;
    }
}
