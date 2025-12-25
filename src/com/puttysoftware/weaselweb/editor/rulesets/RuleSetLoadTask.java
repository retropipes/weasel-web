/*  WeaselWeb: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.weaselweb.editor.rulesets;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.retropipes.diane.fileio.DataIOFactory;
import org.retropipes.diane.fileio.XDataReader;
import org.retropipes.diane.gui.dialog.CommonDialogs;

import com.puttysoftware.weaselweb.Application;
import com.puttysoftware.weaselweb.WeaselWeb;

public class RuleSetLoadTask extends Thread {
    // Fields
    private final String filename;

    // Constructors
    public RuleSetLoadTask(final String file) {
	this.filename = file;
	this.setName(" Rule Set File Reader");
    }

    // Methods
    @Override
    public void run() {
	final Application app = WeaselWeb.getApplication();
	final String sg = "Rule Set";
	try (final XDataReader ruleSetFile = DataIOFactory.createTagReader(this.filename, "ruleset")) {
	    try {
		final int magic = ruleSetFile.readInt();
		if (magic == RuleSetConstants.MAGIC_NUMBER_2) {
		    // Format 2 file
		    app.getObjects().readRuleSet(ruleSetFile, RuleSetConstants.FORMAT_2);
		}
		CommonDialogs.showTitledDialog(sg + " file loaded.", "Rule Set Picker");
	    } catch (final FileNotFoundException fnfe) {
		CommonDialogs.showDialog("Loading the " + sg.toLowerCase()
			+ " file failed, probably due to illegal characters in the file name.");
		app.getMazeManager().handleDeferredSuccess(false);
	    } catch (final IOException ie) {
		throw new InvalidRuleSetException("Error loading " + sg.toLowerCase() + " file.");
	    }
	} catch (final InvalidRuleSetException irse) {
	    CommonDialogs.showDialog(irse.getMessage());
	} catch (final Exception ex) {
	    WeaselWeb.logError(ex);
	}
    }
}
