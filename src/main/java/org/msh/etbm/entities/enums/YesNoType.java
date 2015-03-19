package org.msh.etbm.entities.enums;

import org.msh.core.Displayable;

public enum YesNoType implements Displayable {
	YES ("global.yes"),
	NO ("global.no");
	
	private final String messageKey;

	YesNoType(String msg) {
		messageKey = msg;
	}
	
	public String getKey() {
		return messageKey;
	}

	@Override
	public String getMessageKey() {
		return messageKey;
	}
}
