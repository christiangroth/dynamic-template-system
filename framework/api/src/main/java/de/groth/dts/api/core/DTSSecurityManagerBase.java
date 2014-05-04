package de.groth.dts.api.core;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

/**
 * TODO comment
 * 
 * @author Christian Groth
 * @since 0.99
 */
public abstract class DTSSecurityManagerBase extends SecurityManager {

    @Override
    public void checkPropertiesAccess() {
        this.checkPropertyAccess("*");
    }

    @Override
    public void checkPropertyAccess(String key) {
        // TODO so far every property access is okay
    }

    @Override
    public void checkDelete(String file) {
        checkDeleteAccess(file);
    }

    @Override
    public void checkRead(String file, Object context) {
        checkReadAccess(file);
    }

    @Override
    public void checkRead(String file) {
        checkReadAccess(file);
    }

    @Override
    public void checkWrite(String file) {
        checkWriteAccess(file);
    }

    protected abstract void checkReadAccess(String file);

    protected abstract void checkWriteAccess(String file);

    protected abstract void checkDeleteAccess(String file);

    @Override
    public void checkAccept(String host, int port) {
        // TODO Auto-generated method stub
        super.checkAccept(host, port);
    }

    @Override
    public void checkAccess(Thread t) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkAccess(ThreadGroup g) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkAwtEventQueueAccess() {
        // TODO so far everything is okay here
    }

    @Override
    public void checkConnect(String host, int port, Object context) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkConnect(String host, int port) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkCreateClassLoader() {
        // TODO so far everything is okay here
    }

    @Override
    public void checkExec(String cmd) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkExit(int status) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkLink(String lib) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkListen(int port) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkMemberAccess(Class<?> clazz, int which) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkMulticast(InetAddress maddr) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkPackageAccess(String pkg) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkPackageDefinition(String pkg) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        // TODO so far everything is okay here
        System.out.println("permitting: " + perm.toString());
    }

    @Override
    public void checkPermission(Permission perm) {
        // TODO so far everything is okay here
        System.out.println("permitting: " + perm.toString());
    }

    @Override
    public void checkPrintJobAccess() {
        // TODO so far everything is okay here
    }

    @Override
    public void checkRead(FileDescriptor fd) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkSecurityAccess(String target) {
        // TODO so far everything is okay here
    }

    @Override
    public void checkSetFactory() {
        // TODO so far everything is okay here
    }

    @Override
    public void checkSystemClipboardAccess() {
        // TODO so far everything is okay here
    }

    @Override
    public boolean checkTopLevelWindow(Object window) {
        // TODO so far everything is okay here
        return true;
    }

    @Override
    public void checkWrite(FileDescriptor fd) {
        // TODO so far everything is okay here
    }
}
