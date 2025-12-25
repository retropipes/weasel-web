package com.puttysoftware.weaselweb.maze;

import java.io.File;

import org.retropipes.diane.fileio.utility.DirectoryUtilities;

public class TempDirCleanup extends Thread {
    @Override
    public void run() {
	try {
	    final File dirToDelete = new File(Maze.getMazeTempFolder());
	    DirectoryUtilities.removeDirectory(dirToDelete);
	} catch (final Throwable t) {
	    // Ignore
	}
    }
}
