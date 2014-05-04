package de.groth.dts.api.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Helper for necessary file and/or path operations.
 * 
 * @author Christian Groth
 */
public final class FileHelper {
    private static final Logger LOGGER = Logger.getLogger(FileHelper.class);

    private static final int BUFFER_SIZE = 1024;

    private FileHelper() {
    }

    /**
     * Gets the file content as string.
     * 
     * @param path
     *                the path
     * 
     * @return the file content
     * 
     * @throws IOException
     *                 Signals that an I/O exception has occurred.
     */
    public static String getFileContent(final String path) throws IOException {
        FileHelper.LOGGER.debug("reading fileContent for file " + path);
        BufferedReader reader = null;

        try {
            final File file = new File(path);
            if (file.exists() && file.canRead()) {
                FileHelper.LOGGER.debug("file found and is readable");
                final StringBuffer fileData = new StringBuffer(
                        FileHelper.BUFFER_SIZE);
                reader = new BufferedReader(new FileReader(file));

                int numRead = 0;
                char[] buf = new char[FileHelper.BUFFER_SIZE];
                FileHelper.LOGGER.debug("starting to read file");
                while ((numRead = reader.read(buf)) != -1) {
                    final String readData = String.valueOf(buf, 0, numRead);
                    fileData.append(readData);
                    buf = new char[FileHelper.BUFFER_SIZE];
                }
                reader.close();
                FileHelper.LOGGER.debug("reading done, returning content");

                return fileData.toString();
            } else {
                throw new IOException("file does not exist or can't be read: "
                        + path + " !!");
            }
        } finally {
            if (reader != null) {
                FileHelper.LOGGER.debug("closing BufferedReader for " + path);
                reader.close();
            }
        }
    }

    /**
     * Combines path-elements with System.getProperty("file.separator").
     * 
     * @param parent
     *                the parent
     * @param childs
     *                the childs
     * 
     * @return the string
     */
    public static String combinePath(final String parent,
            final String... childs) {
        FileHelper.LOGGER.debug("combining path elements");

        if (childs == null || childs.length < 1) {
            FileHelper.LOGGER.debug("got only one element " + parent);
            return parent;
        }

        String result = FileHelper.combinePath(parent, childs[0]);

        if (childs.length > 1) {
            for (int i = 1; i < childs.length; i++) {
                result = FileHelper.combinePath(result, childs[i]);
            }
        }

        return result;
    }

    /**
     * Combine two path elements.
     * 
     * @param parent
     *                the parent
     * @param child
     *                the child
     * 
     * @return the string
     */
    public static String combinePath(final String parent, final String child) {
        if (child == null || child.trim().equals("")) {
            FileHelper.LOGGER.debug("got only one element " + parent);
            return parent;
        }

        FileHelper.LOGGER.debug("combining " + parent + " and " + child
                + " with " + System.getProperty("file.separator"));
        return parent + System.getProperty("file.separator") + child;
    }

    /**
     * Calculates the depth of a file with the given path.
     * 
     * @param path
     * @return the depth
     */
    public static int getFileDepth(final String path) {
        String workPath = path;

        FileHelper.LOGGER.debug("calculating fileDepth for " + workPath);
        if (path.indexOf('/') == 0 || path.indexOf('\\') == 0) {
            workPath = path.substring(1);
            FileHelper.LOGGER
                    .debug("stripped first position so we have a relative path "
                            + workPath);
        }

        final int slashCount = workPath.split("/").length - 1;
        final int backslashCount = workPath.split("\\\\").length - 1;

        FileHelper.LOGGER.debug("slashCount=" + slashCount
                + ", backslachCount=" + backslashCount + ", depth="
                + (slashCount + backslashCount));
        return slashCount + backslashCount;
    }
}
