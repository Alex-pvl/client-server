package client.filters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class TXTFileFilter extends FileFilter {
	@Override
	public boolean accept(File f) {
		return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
	}

	@Override
	public String getDescription() {
		return "Text files (*.txt)";
	}
}
