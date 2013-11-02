# Korsakow

Korsakow is an open-source application for creating web docs and other kinds of nonlinear, interactive narratives. For more information please visit [http://korsakow.org]()


## The Korsakow Editor

The editor is a cross-platform (Windows and OS X) desktop application for the creation of Korsakow films. The editor allows you to organize media assets as well as their presentation and export a web-playable bundle called a Korsakow film.

## Getting started on Windows 7

### Summary
1. Install 32 bit JRE 6
1. Install Quicktime
1. You may need to manually add QTJava.zip to the classpath in Eclipse (Java Build Path -> Add External Jars...)
1. Use the launch configurations in the /launch directory to run the editor from Eclipse

### Details
Korsakow uses [Quicktime for Java](http://en.wikipedia.org/wiki/QuickTime_for_Java) to preview media in the editor. QTJava is installed as part of the regular [Quicktime](http://www.apple.com/ca/quicktime/download/) installation, during which a file QTJava.zip is placed in the lib/ext folder of existing JRE - this means that installing a JRE/JDK after Quicktime will not work so either Quicktime must be installed after or QTJava.zip must be manually copied to any new JRE installations. Furthermore QTJava has only 32 bit support so a 32 bit JRE is required. Finally, due to some legacy code, Korsakow does not yet support Java7.

## Getting started on OS X

### Summary
1. Install JRE 6
1. Use the launch configurations in the /launch directory to run the editor from Eclipse
