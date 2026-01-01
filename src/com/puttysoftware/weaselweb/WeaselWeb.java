/*  WeaselWeb: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.weaselweb;

import java.util.Vector;

import org.retropipes.diane.Diane;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.diane.integration.Integration;

import com.puttysoftware.weaselweb.maze.TempDirCleanup;
import com.puttysoftware.weaselweb.pluginmanagers.PluginHooks;
import com.puttysoftware.weaselweb.pluginmanagers.PluginLoader;
import com.puttysoftware.weaselweb.prefs.PreferencesLauncher;
import com.puttysoftware.weaselweb.prefs.PreferencesManager;
import com.puttysoftware.weaselweb.resourcemanagers.LogoManager;
import com.puttysoftware.weaselweb.resourcemanagers.MusicManager;

public class WeaselWeb {
    // Constants
    private static Application application;
    private static final String PROGRAM_NAME = "WeaselWeb";
    private static final String ERROR_MESSAGE = "Perhaps a bug is to blame for this error message.\n"
	    + "Include the error log with your bug report.\n" + "Email bug reports to: products@puttysoftware.com\n"
	    + "Subject: WeaselWeb Bug Report";
    private static final String ERROR_TITLE = "WeaselWeb Error";
    private static boolean IN_MAZER5D = true;

    // Methods
    public static Application getApplication() {
	return WeaselWeb.application;
    }

    public static boolean inWeaselWeb() {
	return WeaselWeb.IN_MAZER5D;
    }

    public static void leaveWeaselWeb() {
	if (MusicManager.isMusicPlaying()) {
	    MusicManager.stopMusic();
	}
	WeaselWeb.application.getOutputFrame().setVisible(false);
	PreferencesManager.writePrefs();
	// Run cleanup task
	new TempDirCleanup().start();
	WeaselWeb.IN_MAZER5D = false;
    }

    public static void logError(final Throwable t) {
	CommonDialogs.showErrorDialog(WeaselWeb.ERROR_MESSAGE, WeaselWeb.ERROR_TITLE);
	Diane.handleError(t);
    }

    public static void main(final String[] args) {
	try {
	    Diane.installDefaultErrorHandler(PROGRAM_NAME);
	    // Integrate with host platform
	    Integration i = Integration.integrate();
	    // Load all registered plugins
	    final Vector<Object> plugins = PluginLoader.loadAllRegisteredPlugins();
	    // Inject early hooks
	    PluginHooks.injectEarlyHooks(plugins);
	    WeaselWeb.application = new Application();
	    WeaselWeb.application.postConstruct();
	    WeaselWeb.application.playLogoSound();
	    WeaselWeb.application.getGUIManager().showGUI();
	    // Register platform hooks
	    i.setAboutHandler(WeaselWeb.application.getAboutDialog());
	    i.setOpenFileHandler(WeaselWeb.application.getMazeManager());
	    i.setPreferencesHandler(new PreferencesLauncher());
	    i.setQuitHandler(WeaselWeb.application.getGUIManager());
	    // Inject late hooks
	    PluginHooks.injectLateHooks(plugins);
	    // Set up Common Dialogs
	    CommonDialogs.setDefaultTitle(WeaselWeb.PROGRAM_NAME);
	    CommonDialogs.setIcon(LogoManager.getMicroLogo());
	} catch (final Throwable t) {
	    WeaselWeb.logError(t);
	}
    }
}
