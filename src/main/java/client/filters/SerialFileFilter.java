package client.filters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class SerialFileFilter extends FileFilter {
	@Override
	public boolean accept(File f) {
		return f.getName().toLowerCase().endsWith(".serial") || f.isDirectory();
	}

	@Override
	public String getDescription() {
		return "Serial files (*.serial)";
	}
}
