/**
 * 
 */
package org.msh.etbm.services.cases.prevtreat;

import org.msh.etbm.entities.Substance;

/**
 * @author Mauricio Santos
 *
 */
public class SubstanceOption {

	private Substance substance;
	private boolean selected;

	public SubstanceOption(Substance substance, boolean selected) {
		this.substance = substance;
		this.selected = selected;
	}

	public Substance getSubstance() {
		return substance;
	}

	public void setSubstance(Substance substance) {
		this.substance = substance;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
