@echo off
call ant build
call configureClasspath.bat
java de.groth.dts.impl.core.BatchInvoker . sample\helloWorld.dts.xml export
pause