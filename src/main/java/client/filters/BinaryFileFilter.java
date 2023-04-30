package client.filters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class BinaryFileFilter extends FileFilter {
	@Override
	public boolean accept(File f) {
		return f.getName().toLowerCase().endsWith(".dat") || f.isDirectory();
	}

	@Override
	public String getDescription() {
		return "Binary files (*.dat)";
	}
}
