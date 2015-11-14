************
Installation
************

This section describes how to install and run MONGKIE. Installation instructions are provided for ``Linux`` , ``OS X``, and ``Windows``.

.. important::
	If you have an older version on your computer, you should uninstall it and remove the user directory.
	
	**User directories**
	
	* On Windows 2K/XP, ``C:\Documents and Settings\username\Application Data\.mongkie``.
	* On Windows Vista or later, ``C:\Users\username\AppData\Roaming\.mongkie``.
	* On OS X, ``/Users/username/Library/Application Support/mongkie``
	* On Linux, ``/home/username/.mongkie``
	
	Whenever you encounter some troubles in running MONGKIE, you can clean running environments by deleting user directory, then rerun it.

System Requirements
===================

Recommended hardware requirements
---------------------------------

========== ============================= ============================ ========================
OS         Processor                     Memory                       Disk space              
========== ============================= ============================ ========================
Linux      Intel Core i5 or equivalent   2GB (32-bit), 4GB (64-bit)   1.5GB of free disk space
Windows    Intel Core i5 or equivalent   2GB (32-bit), 4GB (64-bit)   1.5GB of free disk space
OS X       Dual-Core Intel               4GB                          1.5GB of free disk space
========== ============================= ============================ ========================

Java
----

MONGKIE is written in Java, and runs on the Java Runtime Environment. Therefore, the Java runtime (7 or 8) is required to install and run it. You can download the latest version of Java runtime from `here <http://www.oracle.com/technetwork/java/javase/downloads/index.html>`_ for Windows and Linux, and OS X Lion (10.7), Mountain Lion (10.8), or Mavericks (10.9).

The tested Java versions are Java ``8`` and ``7u67`` for Windows, Linux, and OS X.

.. caution::
	MONGKIE cannot be installed or run using Java 6.0, and `OpenJDK <http://openjdk.java.net/>`_ is not supported, be sure to run the official Java version from `Oracle’s website <http://www.oracle.com/technetwork/java/javase/downloads/index.html>`_.

ZIP Distributions
================
Download the `latest release <https://github.com/yjjang/mongkie/releases/latest>`_ of ZIP distribution for your OS, then

1. Unzip it to any directory on your system.
2. Run the executable file located in the ``bin`` directory
	
	* On Linux,
		``mongkie/bin/mongkie``
	* On Windows,
		``mongkie/bin/mongkie.exe``
	* On OS X,
		``mongkie.app/Contents/MacOS/mongkie``

Installable Packages
====================

