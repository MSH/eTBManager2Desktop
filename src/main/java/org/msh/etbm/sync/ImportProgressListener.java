/**
 * 
 */
package org.msh.etbm.sync;

/**
 * Listener to receive notifications about the progress of importing operations
 * @author Ricardo Memoria
 *
 */
public interface ImportProgressListener {

	/**
	 * Called to update the progress of database importing
	 * @param perc is the percentage indicator, ranging from 0 to 100
	 */
	void onUpdateProgress(double perc);
}
