package com.puttysoftware.weaselweb.maze;

import java.io.IOException;

import org.retropipes.diane.fileio.XDataReader;
import org.retropipes.diane.fileio.XDataWriter;

import com.puttysoftware.weaselweb.WeaselWeb;

public class SuffixHandler implements SuffixIO {
    @Override
    public void readSuffix(final XDataReader reader, final int formatVersion) throws IOException {
	WeaselWeb.getApplication().getGameManager().loadGameHook(reader, formatVersion);
    }

    @Override
    public void writeSuffix(final XDataWriter writer) throws IOException {
	WeaselWeb.getApplication().getGameManager().saveGameHook(writer);
    }
}
