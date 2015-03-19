package org.msh.etbm.services.adminunit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.AdministrativeUnit;
import org.msh.etbm.entities.CountryStructure;
import org.msh.etbm.entities.UserWorkspace;
import org.msh.etbm.entities.enums.UserView;

/**
 * Administrative unit utilities functions
 * @author Ricardo
 *
 */
public class AdminUnitsUtils {

	/**
	 * Check if a given administrative unit level is read-only or selectable by the user
	 * @param level
	 * @return
	 */
	public boolean isLevelReadOnly(int level) {
		return level < getSelectableLevel();
	}


	/**
	 * Return the label to be displayed to the user based on the list of administrative units 
	 * in a given list. Just the country structures of the administrative units are considered.
	 * @param lst
	 * @return
	 */
	public static String getLabel(List<AdministrativeUnit> lst) {
		List<String> names = new ArrayList<String>();
		
		for (AdministrativeUnit adm: lst) {
			String s = adm.getCountryStructure().getName().toString();
			
			if (!names.contains(s))
				names.add(s);
		}

		String txt = "";
		for (String name: names) {
			if (txt.length() > 0)
				txt += " / ";
			txt += name;
		}
		
		return txt;
	}


	/**
	 * Returns the minimum level where user can select an administrative unit,
	 * depending on his user view
	 * @return
	 */
	public static int getSelectableLevel() {
		UserWorkspace userWorkspace = (UserWorkspace)App.getComponent("userWorkspace");
		if (userWorkspace.getView() == UserView.COUNTRY)
			return 0;

		if (userWorkspace.getView() == UserView.ADMINUNIT) 
			return userWorkspace.getAdminUnit().getLevel();
		
		if (userWorkspace.getView() == UserView.TBUNIT)
			return userWorkspace.getTbunit().getAdminUnit().getLevel();
		return 0;
	}

	
	/**
	 * Return the list of {@link AdministrativeUnit} entities from its parent. If parent is null, 
	 * all administrative units level 0 will be returned
	 * @param parent
	 * @return
	 */
	public static List<AdministrativeUnit> getAdminUnits(AdministrativeUnit parent) {
		EntityManager em = App.getEntityManager();

		List<AdministrativeUnit> lst;
		if (parent != null)
			lst = em.createQuery("from AdministrativeUnit a where a.parent.id = :id")
				.setParameter("id", parent.getId())
				.getResultList();
		else lst = em
				.createQuery("from AdministrativeUnit a where a.workspace.id = #{defaultWorkspace.id} and a.parent.id is null")
				.getResultList();
		
		Collections.sort(lst, new Comparator<AdministrativeUnit>() {
			@Override
			public int compare(AdministrativeUnit au1, AdministrativeUnit au2) {
				return au1.getName().getDefaultName().compareTo(au2.getName().getDefaultName());
			}
		});
		
		return lst;
	}
	
	/**
	 * Generate HQL instruction to return all children ids of a parent administrative unit
	 * @param parent
	 * @return
	 */
	static public String childrenHQLCriteria(AdministrativeUnit parent, boolean includeSelf) {
		if (parent.getLevel() == 5) {
			if (includeSelf)
				 return "select au0.id from AdministrativeUnit au0 where au0.id = " + parent.getId().toString();
			else return "";
		}
		
		String parentId = parent.getId().toString();
		
		String crit = " where au1.id = " + parentId;
		String join = "from AdministrativeUnit au0 left join au0.parent au1";
		
		if (includeSelf)
			crit = crit + " or au0.id = " + parentId;
		
		if (parent.getLevel() <= 3) {
			join += " left join au1.parent au2";
			crit += " or au2.id = " + parentId;
		}

		if (parent.getLevel() <= 2) {
			join += " left join au2.parent au3";
			crit += " or au3.id = " + parentId;
		}

		if (parent.getLevel() == 1) {
			crit += " or au4.id = " + parentId;
			join += " left join au3.parent au4";
		}

		return "select au0.id " + join + crit;
	}
	
	
	/**
	 * Return an array with the names of the country structure levels
	 * @return
	 */
	static public String[] getCountryStructureLabels() {
		EntityManager em = App.getEntityManager();
		List<CountryStructure> lst = em.createQuery("from CountryStructure c where c.workspace.id = #{defaultWorkspace.id}")
			.getResultList();
		
		// calculate maximum level
		int level = 0;
		for (CountryStructure cs: lst)
			if (cs.getLevel() > level)
				level = cs.getLevel();

		// create names
		String[] names = new String[level];
		for (CountryStructure cs: lst) {
			String s = cs.getName().getDefaultName();
			int index = cs.getLevel() - 1;
			
			if (names[index] == null)
				 names[index] = s;
			else names[index] += "/" + s;
		}
		
		return names;
	}
}
