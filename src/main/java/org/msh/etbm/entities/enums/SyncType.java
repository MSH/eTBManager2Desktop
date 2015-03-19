/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.msh.etbm.entities.enums;

/**
 *
 * @author USrivastava
 */
public enum SyncType {
	UPLOAD,
	DOWNLOAD,
	PASS,
        FAIL;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}

}
