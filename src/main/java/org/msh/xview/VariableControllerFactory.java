package org.msh.xview;

import org.msh.etbm.services.core.EntityServices;

public interface VariableControllerFactory {

	EntityServices createControllerInstance(String varname);
}
