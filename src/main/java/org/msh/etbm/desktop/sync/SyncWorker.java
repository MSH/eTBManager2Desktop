/**
 * 
 */
package org.msh.etbm.desktop.sync;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import javax.swing.SwingWorker;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.sync.IniFileImporter;
import org.msh.etbm.sync.ServerFileGenerator;
import org.msh.etbm.sync.SyncFileDeliverer;
import org.msh.etbm.sync.SyncFileDeliverer.SyncFileDeliverEvent;
import org.msh.eventbus.EventBusListener;
import org.msh.eventbus.EventBusService;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;

/**
 * @author Ricardo Memoria
 *
 */
public class SyncWorker extends SwingWorker<Boolean, Object> implements EventBusListener {

	public static final String EVENT_NEXTSTEP = "nextstep";
	public static final String EVENT_SHOWMSG = "showmsg";
	public static final String EVENT_EXEC_SUCCESS = "exec_success";
	public static final String EVENT_ADDLOG = "addlog";

	public static final String SYNC_FILE_NAME = "syncfile.etbm";
	
	// the client file to be sent to the server
	private File clientFile;
	private File answerFile;
	private SyncFileDeliverer deliverer;
	private boolean firstTitle;


	/** {@inheritDoc}
	 */
	@Override
	protected Boolean doInBackground() throws Exception {
		// map the event handlers
		EventBusService.observeEvent(SyncFileDeliverEvent.CONNECTED, this);
		EventBusService.observeEvent(SyncFileDeliverEvent.CONNECTING, this);
		EventBusService.observeEvent(SyncFileDeliverEvent.TRANSMITTED, this);
		EventBusService.observeEvent(SyncFileDeliverEvent.TRANSMITTING, this);
		EventBusService.observeEvent(SyncFileDeliverEvent.WAITING_ANSWER, this);

		try {
			try {
				notifyNextStep();
				initStep1();

				notifyNextStep();
				initStep2();
				
				notifyNextStep();
				initStep3();
				
				notifyNextStep();
				initStep4();

			} catch (Exception e) {
				showMessage("Error: " + e.getMessage());
				e.printStackTrace();
				throw e;
			}
			
		} finally {
			EventBusService.removeObserverHandler(this);
		}

		return null;
	}

	/**
	 * 
	 */
	protected void notifyNextStep() {
		firePropertyChange(EVENT_NEXTSTEP, null, null);
	}
	
	/**
	 * Request parent to display a message on the screen
	 * @param msg
	 */
	protected void showMessage(String msg) {
		firePropertyChange(EVENT_SHOWMSG, msg, null);
	}

	/**
	 * Request parent to add a log message
	 * @param msg message to be displayed in the log screen
	 */
	protected void addLog(String msg) {
		firePropertyChange(EVENT_ADDLOG, msg, null);
	}

	
	/**
	 * Add a title message to the log
	 * @param s title to be displayed in the log
	 */
	protected void addTitle(String s) {
		s = s.toUpperCase();
		if (!firstTitle) {
			s = "\n\n" + s;
		}
		firstTitle = false;
		addLog(s);
		char[] underline = new char[s.length()];
		Arrays.fill(underline, '-');
		addLog(new String(underline));
	}
	
	/**
	 * Generate the file to be sent to the server
	 */
	public void initStep1() {
		firstTitle = true;
		addTitle(Messages.getString("desktop.sync.step1"));

		EntityManagerUtils.doInTransaction(new ActionCallback() {
			@Override
			public void execute(Object data) {
				ServerFileGenerator gen = new ServerFileGenerator();
				
				clientFile = new File(App.getWorkingDirectory(), SYNC_FILE_NAME);
				addLog("Generated file: " + SYNC_FILE_NAME);

				try {
					FileOutputStream fout = new FileOutputStream(clientFile);
					try {
						gen.generateFile(fout);
					}
					finally {
						fout.close();
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
				addLog("Number of objects to transmit: " + gen.getObjectCount());
			}
		});
	}

	/**
	 * Send file to the server and get the token id
	 */
	private void initStep2() {
		addTitle(Messages.getString("desktop.sync.step2"));

		if (clientFile == null)
			throw new RuntimeException("No file to send!");
		deliverer = new SyncFileDeliverer();
		addLog("Trying to connect to: " + deliverer.getServerURL());

		String fileToken = deliverer.sendToServer(clientFile);
//		clientFile.delete();
		if (fileToken == null)
			throw new RuntimeException("Error sending file");
	}


	/**
	 * Update information from the answer file downloaded from the server
	 */
	private void initStep4() {
		addTitle(Messages.getString("desktop.sync.step4"));

		IniFileImporter importer = new IniFileImporter();
		importer.start(answerFile, null, true);
		addLog("Server answered back");
		firePropertyChange(EVENT_EXEC_SUCCESS, null, null);
	}


	/**
	 * Wait for the server answer about the file snchronization process 
	 */
	private void initStep3() {
		addTitle(Messages.getString("desktop.sync.step3"));

		if (!deliverer.waitServerAnswer())
			throw new RuntimeException("The server answered with an error.\n" + deliverer.getErrorMessage());
		
		answerFile = deliverer.downloadAnswerFile(null);
//		showMessage("Sync executed in server\nAnswer = " + file);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void handleEvent(Object event, Object... data) {
		if (event == SyncFileDeliverEvent.CONNECTED) {
			addLog("Connected.");
			return;
		}
		
		if (event == SyncFileDeliverEvent.TRANSMITTING) {
			addLog("Transmitting data to the server...");
			return;
		}
		
		if (event == SyncFileDeliverEvent.TRANSMITTED) {
			addLog("... Data transmitted");
			return;
		}
		
		if (event == SyncFileDeliverEvent.WAITING_ANSWER) {
			addLog("Server is processing the data. Wait...");
			return;
		}
	}

}
