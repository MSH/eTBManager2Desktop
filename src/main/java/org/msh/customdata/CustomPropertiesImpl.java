/**
 * 
 */
package org.msh.customdata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the {@link CustomProperties} interface
 * @author Ricardo Memoria
 *
 */
public class CustomPropertiesImpl implements CustomProperties, StateControl {

	private Map<String, Object> data = new HashMap<String, Object>();
	private boolean changed;
	private boolean newRecord = true;

	
	/** {@inheritDoc}
	 */
	@Override
	public void setValue(String propertyName, Object value) {
		data.put(propertyName, value);
		changed = true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Object getValue(String propertyName) {
		return data.get(propertyName);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Set<String> getProperties() {
		return data.keySet();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void deleteProperty(String propertyName) {
		if (data.remove(propertyName) != null) {
			changed = true;
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return changed;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void commitChanges() {
		changed = false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void clear() {
		changed = true;
		data.clear();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isNew() {
		return newRecord;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setNew(boolean value) {
		this.newRecord = value;
	}

}
