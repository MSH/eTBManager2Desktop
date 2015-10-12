package org.msh.etbm.desktop.app;

import org.msh.eventbus.EventBusService;

/**
 * List of events thrown by the system using the {@link EventBusService} class,
 * implementing the model observer
 * @author Ricardo Memoria
 *
 */
public enum AppEvent {

	LOGGEDIN,			// User has logged in
	REQUEST_APP_EXIT,	// command to quit application
	LANGUAGE_CHANGED,	// Language was changed
	CASE_MODIFIED,		// Case was modified
	CASE_DELETED,		// Case was deleted
	NEW_CASE,			// A new case was included
	TREATMENT_REFRESH,  // the treatment for the patient must be refreshed
	CASES_REFRESH,		// refresh the list of cases in the main page
	SYNC_EXECUTED,		// The synchronization between client and server was just executed
	EXAMS_MODIFIED;		// Any kind of exam was added, edited or removed from a case
}
