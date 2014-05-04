package de.groth.dts.impl.core.security;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.groth.dts.api.core.DTSSecurityManagerBase;

/**
 * TODO comment
 * 
 * @author Christian Groth
 * @since 0.99
 */
public class DTSSecurityManager extends DTSSecurityManagerBase {

    private final String baseDtsPath;
    private final String baseExportPath;

    // TODO save JAVA_HOME here

    public DTSSecurityManager(String baseDtsPath, String baseExportPath) {
        this.baseDtsPath = handleRelativePaths(new File(baseDtsPath)
                .getAbsolutePath());
        this.baseExportPath = handleRelativePaths(new File(baseExportPath)
                .getAbsolutePath());

        System.out.println("*** dts: " + this.baseDtsPath);
        System.out.println("*** exp: " + this.baseExportPath);
    }

    @Override
    protected void checkDeleteAccess(String file) {
        System.out.println("checking delete for " + file);
        if (!isInExportPath(file)) {
            String error = "Illegal access: deleting only allowed for "
                    + this.baseExportPath + " (" + file + ")!!";
            System.out.println(error);
            throw new SecurityException(error);
        }
    }

    @Override
    protected void checkReadAccess(String file) {
        System.out.println("checking read for " + file);
        if (!isJavaStuff(file) && !isInSomePath(file)) {
            String error = "Illegal access: reading only allowed for "
                    + this.baseDtsPath + " and " + this.baseExportPath + " ("
                    + file + ")!!";
            System.out.println(error);
            throw new SecurityException(error);
        }
    }

    @Override
    protected void checkWriteAccess(String file) {
        System.out.println("checking write for " + file);
        if (!isInExportPath(file)) {
            String error = "Illegal access: writing only allowed for "
                    + this.baseExportPath + " (" + file + ")!!";
            System.out.println(error);
            throw new SecurityException(error);
        }
    }

    private boolean isInSomePath(String file) {
        return isInDtsPath(file) || isInExportPath(file);
    }

    private boolean isInDtsPath(String file) {
        if (file == null && "".equals(file.trim())) {
            return true;
        }

        return handleRelativePaths(new File(file).getAbsolutePath()).indexOf(
                this.baseDtsPath) >= 0;
    }

    private boolean isInExportPath(String file) {
        if (file == null && "".equals(file.trim())) {
            return true;
        }

        return handleRelativePaths(new File(file).getAbsolutePath()).indexOf(
                this.baseExportPath) >= 0;
    }

    private boolean isJavaStuff(String file) {
        if (file == null && "".equals(file.trim())) {
            return true;
        }

        String javaHomePath = System.getenv().get("JAVA_HOME");
        if (javaHomePath == null || "".equals(javaHomePath.trim())) {
            return false;
        }

        return handleRelativePaths(new File(file).getAbsolutePath()).indexOf(
                handleRelativePaths(new File(javaHomePath).getAbsolutePath())) >= 0;
    }

    // TODO this one still sucks somehow ...
    private String handleRelativePaths(final String input) {
        if (input == null || "".equals(input.trim())) {
            return "";
        }

        Pattern currentDirPattern = Pattern.compile("^.*"
                + Matcher.quoteReplacement(File.pathSeparator + "."
                        + File.pathSeparator) + ".*$");
        Matcher matcher = currentDirPattern.matcher(input);
        String output = matcher.replaceAll(File.pathSeparator);

        Pattern parentDirPattern = Pattern.compile("^.*"
                + Matcher.quoteReplacement(File.pathSeparator + ".."
                        + File.pathSeparator) + ".*$");
        matcher = parentDirPattern.matcher(output);
        output = matcher.replaceAll(File.pathSeparator);

        if (input.equals(output)) {
            System.out
                    .println("changed relatives: " + input + " --> " + output);
        }
        return output;
    }
}
