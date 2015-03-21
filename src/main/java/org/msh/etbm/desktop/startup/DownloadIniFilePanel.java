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
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.msh.etbm.desktop.app.*;
import org.msh.etbm.desktop.common.CloseListener;
import org.msh.etbm.desktop.common.MigLayoutPanel;
import org.msh.etbm.desktop.common.Refreshable;
import org.msh.etbm.desktop.common.SystemLogo;
import org.msh.etbm.sync.DownloadProgressListener;
import org.msh.etbm.sync.ServerServices;
import org.msh.eventbus.EventBusListener;
import org.msh.eventbus.EventBusService;


/**
 * @author Ricardo Memoria
 *
 */
public class DownloadIniFilePanel extends JPanel implements Refreshable, CloseListener, EventBusListener {
	private static final long serialVersionUID = -8048021521128566602L;

	private JLabel txtTitle;
	private JLabel txtFilename;
	private File downloadedFile;
	private JProgressBar bar;
	private SystemLogo logo;
	private ServerCredentials credentials;

	/**
	 * Default constructor
	 */
	public DownloadIniFilePanel() {
		super();
		
		setSize(500, 230);
		setBorder(new EmptyBorder(10,10,10,10));
		
		MigLayoutPanel pnl = new MigLayoutPanel("wrap 1", "", "[]20[]15[][]");

		logo = new SystemLogo();
		pnl.add(logo);

		txtTitle = new JLabel(Messages.getString("desktop.startup.download"));
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
	
	
	public void execute(ServerCredentials credent) {
		this.credentials = credent;

		bar.setValue(0);
		
		// create worker that will execute task in background
		final SwingWorker<Boolean, Object> worker = new SwingWorker<Boolean, Object>() {
			@Override
			protected Boolean doInBackground() throws Exception {
				ServerServices conn = App.getComponent(ServerServices.class);

				String s = MessageFormat.format(Messages.getString("desktop.startup.connecting"), credentials.getServer());
				txtFilename.setText(s);

                try {
                    conn.downloadIniFile(credentials.getServer(), credentials.getToken(), null, new DownloadProgressListener() {
                        @Override
                        public void onUpdateProgress(double perc) {
                            setProgress((int)perc);
                        }

                        @Override
                        public void onInitDownload(File file) {
                            downloadedFile = file;
                        }
                    });
                    return true;
                }
                catch (Exception e) {
                    firePropertyChange("error", null, e);
                    return false;
                }
			}
		};

        // monitor property changes
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
                if ("error".equals(evt.getPropertyName())) {
                    throw new AppInitializationException(((RuntimeException)evt.getNewValue()).getMessage());
                }
				// is importing finished ?
				if ("state".equals(evt.getPropertyName())
		                 && SwingWorker.StateValue.DONE == evt.getNewValue()) {
					downloadFinished();
				}
				else updateProgress(worker.getProgress());
			}
		});
        worker.execute();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void refresh() {
	}


	
	/**
	 * Called when the importing is finished
	 */
	protected void downloadFinished() {
//		if (dbstarter.isErrorOcurred()) 
//			 JOptionPane.showMessageDialog(this, dbstarter.getErrorMessage());
		
		EventBusService.raiseEvent(StartupEvent.EXECUTE_INIFILE, downloadedFile);
	}
	
	/**
	 * @param perc
	 */
	protected void updateProgress(double perc) {
		bar.setValue((int)perc);
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
//		String s = MessageFormat.format(Messages.getString("desktop.readfile"), file.getName());
//		txtFilename.setText(s);
		logo.updateLanguage();
	}

}
