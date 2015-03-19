/**
 * 
 */
package org.msh.etbm.desktop.databases;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.msh.etbm.desktop.app.App;
import org.springframework.stereotype.Component;

/**
 * Manage the list of initialization files available in the working
 * directory
 * 
 * @author Ricardo Memoria
 *
 */
@Component
public class PackageList {

	private List<File> packages;
	
	/**
	 * Return the list of packages available in the working directory
	 * @return {@link List} of {@link File} instances
	 */
	public List<File> getPackages() {
		if (packages == null)
			createPackageList();
		return packages;
	}

	
	/**
	 * Add an external file to the list of files to be imported
	 * @param file instance of the {@link File} class
	 */
	public void addPackageFile(File file) {
		createPackageList();
		if (!packages.contains(file))
			packages.add(file);
	}
	
	/**
	 * Return true if there is any package available
	 * @return boolean value
	 */
	public boolean isAnyPackageAvailable() {
		return getPackages().size() > 0;
	}
	
	/**
	 * Archive the package file not to be read again when system is executed
	 * @param file
	 */
	public void archiveFile(File file) {
		// get the archive directory
		File archDir = new File(App.getWorkingDirectory(), "archives");
		archDir.mkdirs();

		// move file to the archive directory
		File destFile = new File(archDir, file.getName());
		if (destFile.exists())
			destFile.delete();
		file.renameTo(destFile);
	}
	
	/**
	 * Scan the working directory and create a list of files that with pkg extension 
	 */
	private void createPackageList() {
		File workDir = App.getWorkingDirectory();
		// search for files with pkg extension in the working directory 
		String[] files = workDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".pkg");
			}
		});

		// create list of files
		packages = new ArrayList<File>();
		for (String fname: files)
			packages.add(new File(workDir, fname));
	}
	
	/**
	 * Return the singleton instance of the component
	 * @return instance of {@link PackageList}
	 */
	public static PackageList instance() {
		return App.getComponent(PackageList.class);
	}
}
