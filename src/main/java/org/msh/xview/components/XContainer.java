package org.msh.xview.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Define a view that can store other views
 * @author Ricardo Memoria
 *
 */
public abstract class XContainer extends XView {

	private List<XView> views;

	/**
	 * Add a component to the list of components
	 * @param comp
	 */
	public void add(XView comp) {
		getViews().add(comp);
	}

	/**
	 * Add a component to the list of components in the specified position in the list
	 * @param index
	 * @param comp
	 */
	public void add(int index, XView comp) {
		getViews().add(index, comp);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void refreshDependencies() {
		super.refreshDependencies();
		if (views != null) {
			for (XView view: views) {
				view.refreshDependencies();
			}
		}
	}

	/**
	 * Find recursively a view by its name
	 * @param viewname
	 * @return
	 */
	public XView findView(String viewname) {
		if (viewname.equals(getId()))
			return this;

		for (XView view: getViews()) {
			if (view instanceof XContainer) {
				XView res = ((XContainer)view).findView(viewname);
				if (res != null)
					return res;
			}
			else
				if (viewname.equals(view.getId()))
					return view;
		}
		
		return null;
	}
	
	
	/**
	 * Clear the list of views inside this view
	 */
	public void removeAllViews() {
		if (views != null)
			views.clear();
	}
	
	/**
	 * Return the list of views inside this view
	 * @return an instance of {@link List} class containing views
	 */
	public List<XView> getViews() {
		if (views == null)
			views = createViewsList();
		return views;
	}
	
	/**
	 * Create a list to store the components
	 * @return a create list of views of instance {@link ComponentList}
	 */
	protected List<XView> createViewsList() {
		return new ComponentList(this);
	}

	
	/**
	 * Get list of views that are dependent of the given field name
	 * @param fieldName
	 * @return
	 */
	public List<XView> getDependencies(String fieldName) {
		List<XView> deps = new ArrayList<XView>();
		if (isDependentOf(fieldName))
			deps.add(this);
		mountDependenciesList(deps, fieldName);
		return deps;
	}
	
	/**
	 * @param comps
	 * @param fieldName
	 */
	protected void mountDependenciesList(List<XView> comps, String fieldName) {
		for (XView view: getViews()) {
			if (view.isDependentOf(fieldName))
				comps.add(view);

			if (view instanceof XContainer) {
				((XContainer)view).mountDependenciesList(comps, fieldName);
			}
		}
	}
	
	/**
	 * Define a list of components
	 * @author Ricardo Memoria
	 *
	 */
	protected class ComponentList extends ArrayList<XView> {
		private static final long serialVersionUID = -5086335945080340944L;

		private XView owner;

		public ComponentList(XView owner) {
			super();
			this.owner = owner;
		}
		/** {@inheritDoc}
		 * @see java.util.ArrayList#add(java.lang.Object)
		 */
		@Override
		public boolean add(XView comp) {
			comp.setParent(owner);
			return super.add(comp);
		}
		/** {@inheritDoc}
		 * @see java.util.ArrayList#addAll(java.util.Collection)
		 */
		@Override
		public boolean addAll(Collection<? extends XView> c) {
			for (XView comp: c)
				comp.setParent(owner);
			return super.addAll(c);
		}
		/** {@inheritDoc}
		 * @see java.util.ArrayList#addAll(int, java.util.Collection)
		 */
		@Override
		public boolean addAll(int index, Collection<? extends XView> c) {
			for (XView comp: c)
				comp.setParent(owner);
			return super.addAll(index, c);
		}
		
	}
}
