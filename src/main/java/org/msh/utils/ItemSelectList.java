package org.msh.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintain a list of {@link ItemSelect} objects used in UI for user selection 
 * @author Ricardo Memoria
 *
 * @param <E>
 */
public class ItemSelectList<E>  {
	
	private List<ItemSelect<E>> items = new ArrayList<ItemSelect<E>>();

	public ItemSelectList() {
		super();
	}
	
	public ItemSelectList(List<E> lst) {
		super();
		setItems(lst);
	}

	public void setItems(List<E> lst) {
		for (E item: lst) {
			items.add(new ItemSelect<E>(item));
		}
	}

	
	public void add(E obj) {
		items.add(new ItemSelect<E>(obj));
	}
	
	
	public void add(E obj, boolean selected) {
		items.add(new ItemSelect<E>(obj, selected));
	}
	
	
	/**
	 * Return a list of items
	 * @return
	 */
	public List<ItemSelect<E>> getList() {
		return items;
	}
	
	/**
	 * Create a list with all selected items
	 * @return
	 */
	public List<E> getSelectedItems() {
		return getItemsByState(true);
	}
	
	/**
	 * Create a list with all unselected items
	 * @return
	 */
	public List<E> getUnselectedItems() {
		return getItemsByState(false);
	}

	/**
	 * @param selected
	 * @return
	 */
	public List<E> getItemsByState(boolean selected) {
		List<E> lst = new ArrayList<E>();
		for (ItemSelect<E> itemSel: items) {
			if (selected == itemSel.isSelected())
			{
				lst.add(itemSel.getItem());
			}
		}
		return lst;
	}
	
	/**
	 * Select all items of the list.<br/>
	 * The property selected of all items is set to true
	 */
	public void selectAll() {
		for (ItemSelect<E> itemSel: items) {
			itemSel.setSelected(true);
		}
	}
	
	
	public boolean contains(E obj) {
		for (ItemSelect<E> item: items) {
			if (item.getItem().equals(obj)) {
				return true;
			}
		}
		return false;
	}
	
	
	public int indexOf(E obj) {
		for (int i = 0; i < items.size(); i++) {
			ItemSelect<E> item = items.get(i);
			if (item.getItem().equals(obj)) {
				return i;
			}
		}
		return -1;
	}

	
	public boolean isSelected(E obj) {
		for (ItemSelect<E> item: items) {
			if (item.getItem().equals(obj)) {
				return item.isSelected();
			}
		}
		throw new RuntimeException("Item not found");
	}
	
	/**
	 * Unselect all items of the list.<br/>
	 * The property selected of all items is set to false
	 */
	public void unselectAll() {
		for (ItemSelect<E> itemSel: items) {
			itemSel.setSelected(false);
		}
	}
}
