package client.filters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class XMLFileFilter extends FileFilter {
	@Override
	public boolean accept(File f) {
		return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
	}

	@Override
	public String getDescription() {
		return "XML-files (*.xml)";
	}
}
