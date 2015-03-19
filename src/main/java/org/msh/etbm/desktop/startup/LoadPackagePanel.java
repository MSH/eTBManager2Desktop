/**
 * 
 */
package org.msh.etbm.desktop.startup;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.common.CloseListener;
import org.msh.etbm.desktop.common.MigLayoutPanel;
import org.msh.etbm.desktop.common.Refreshable;
import org.msh.etbm.desktop.common.SystemLogo;
import org.msh.etbm.desktop.databases.PackageList;
import org.msh.etbm.sync.ImportProgressListener;
import org.msh.etbm.sync.IniFileImporter;
import org.msh.eventbus.EventBusListener;
import org.msh.eventbus.EventBusService;


/**
 * Window that displays a progress bar while the initialization file is being imported into
 * the system.
 *
 * @author Ricardo Memoria
 *
 */
public class LoadPackagePanel extends JPanel implements Refreshable, CloseListener, EventBusListener {
	private static final long serialVersionUID = -8048021521128566602L;

	private JLabel txtTitle;
	private JLabel txtFilename;
	private JProgressBar bar;
	private PackageList packages;
	private int fileIndex;
	private File file;
	private IniFileImporter dbstarter;
	private SystemLogo logo;

	/**
	 * Default constructor
	 */
	public LoadPackagePanel() {
		super();
		
		setSize(500, 230);
		setBorder(new EmptyBorder(10,10,10,10));
		
		MigLayoutPanel pnl = new MigLayoutPanel("wrap 1", "", "[]20[]15[][]");

		logo = new SystemLogo();
		pnl.add(logo);

		txtTitle = new JLabel(Messages.getString("desktop.startup.initdb"));
		txtTitle.setFont(UiConstants.h2Font);
		pnl.add(txtTitle);
		
		txtFilename = new JLabel();
		txtFilename.setMaximumSize(new Dimension(400, 200));
		pnl.add(txtFilename, "span 2");
		
		bar = new JProgressBar(0, 100);
		bar.setPreferredSize(new Dimension(400,20));
		bar.setStringPainted(true);
		bar.setValue(0);
		pnl.add(bar, "span 2");
		
//		JButton btn = new JButton(Messages.getString("form.cancel"));
//		pnl.add(btn, "align center");

		add(pnl);
		
		EventBusService.observeEvent(AppEvent.LANGUAGE_CHANGED, this);
	}
	
	

	/** {@inheritDoc}
	 */
	@Override
	public void refresh() {
		packages = PackageList.instance();

		if ((packages == null) || (packages.getPackages().size() == 0))
			return;

		fileIndex = 0;
		executeCurrentFile();
	}


	/**
	 * Execute the importing of the file in a separate thread, but just return
	 * when import is finished
	 */
	protected void executeCurrentFile() {
		file = packages.getPackages().get(fileIndex);

		bar.setValue(0);
		
		// create worker that will execute task in background
		final SwingWorker<Boolean, Object> worker = new SwingWorker<Boolean, Object>() {
			@Override
			protected Boolean doInBackground() throws Exception {
				IniFileImporter starter = new IniFileImporter();
				dbstarter = starter;
				String s = MessageFormat.format(Messages.getString("desktop.readfile"), file.getName());
				txtFilename.setText(s);
				starter.start(file, new ImportProgressListener() {
					@Override
					public void onUpdateProgress(double perc) {
						setProgress((int)perc);
					}
				}, true);
				return true;
			}
		};

		worker.execute();
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// is importing finished ?
				if ("state".equals(evt.getPropertyName())
		                 && SwingWorker.StateValue.DONE == evt.getNewValue()) {
					fileImportFinished();
				}
				else updateProgress(worker.getProgress());
			}
		});
	}

	
	/**
	 * Called when the importing is finished
	 */
	protected void fileImportFinished() {
		if (dbstarter.isErrorOcurred()) {
			JOptionPane.showMessageDialog(this, dbstarter.getErrorMessage());
			App.exit();
			return;
		}
		
		packages.archiveFile(packages.getPackages().get(fileIndex));

		fileIndex++;
		// no more file to import ?
		if ((fileIndex >= packages.getPackages().size()) || (dbstarter.isErrorOcurred())) {
			EventBusService.raiseEvent(StartupEvent.EXECUTE_INIFILE_FINISHED);
			return;
		}
		else executeCurrentFile();
	}
	
	/**
	 * @param perc
	 */
	protected void updateProgress(double perc) {
		bar.setValue((int)perc);
	}



	/**
	 * @return the packages
	 */
	public PackageList getPackages() {
		return packages;
	}


	/**
	 * @param packages the packages to set
	 */
	public void setPackages(PackageList packages) {
		this.packages = packages;
	}



	/** {@inheritDoc}
	 */
	@Override
	public void closeHandler() {
		EventBusService.removeObserverHandler(this);
	}



	/** {@inheritDoc}
	 */
	@Override
	public void handleEvent(Object event, Object... data) {
		if (event == AppEvent.LANGUAGE_CHANGED)
			languageChangeHandler();
	}


	/**
	 * Update the labels of the panel
	 */
	private void languageChangeHandler() {
		txtTitle.setText(Messages.getString("desktop.startup.initdb"));
		String s = MessageFormat.format(Messages.getString("desktop.readfile"), file.getName());
		txtFilename.setText(s);
		logo.updateLanguage();
	}

}
