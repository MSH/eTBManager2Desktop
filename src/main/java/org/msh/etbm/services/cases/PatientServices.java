package org.msh.etbm.services.cases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.msh.etbm.entities.Patient;
import org.msh.etbm.entities.PersonNameComponent;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.services.core.EntityQuery;
import org.msh.etbm.services.core.EntityServicesImpl;
import org.springframework.stereotype.Component;

/**
 * Services for patient entity
 * @author Ricardo Memoria
 *
 */
@Component
public class PatientServices extends EntityServicesImpl<Patient> {

	
	/**
	 * Get list of patients from its partial name and birth date. All parameters are
	 * optional but <code>maxResult</code>, which must control the number of records found
	 * @param name
	 * @param middleName
	 * @param lastName
	 * @param birthDate
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public List<PatientData> getPatients(PersonNameComponent name, Date birthDate, 
			Integer firstResult, int maxResults, String orderBy) {
		String hql = "from Patient p left outer join p.cases c " +
			"where p.workspace.id = #{defaultWorkspace.id} ";

		EntityQuery<Object[]> qry = createEntityQuery(name, birthDate);

		qry.setHql(hql);
		qry.setOrderBy(orderBy);
		qry.setFirstResult(firstResult);
		qry.setMaxResults(maxResults);
		
		List<Object[]> lst = qry.getResults();

		// create list with patient data
		List<PatientData> result = new ArrayList<PatientData>();
		for (Object obj: lst) {
			Object[] vals = (Object[])obj;
			PatientData item = new PatientData((Patient)vals[0], (TbCase)vals[1]);
			item.getPatient();
			result.add(item);
		}

		return result;
	}

	
	/**
	 * Return the number of cases using the giving filters. All values are optional
	 * @param name is the (part of) first name of the patient
	 * @param middleName is the (part of) middle name of the patient
	 * @param lastName is the (part of) last name of the patient
	 * @param birthDate is the birth name of the patient
	 * @return Number of patients found
	 */
	public Long getPatientCount(PersonNameComponent name, Date birthDate) {
		String hql = "select count(*) from Patient p left outer join p.cases c " +
				"where p.workspace.id = #{defaultWorkspace.id} ";

		EntityQuery<Patient> qry = createEntityQuery(name, birthDate);

		qry.setHql(hql);
			
		return (Long)qry.getResultCount();
	}

	
	/**
	 * Prepare a new entity query with the given parameters
	 * @param name
	 * @param middleName
	 * @param lastName
	 * @param birthDate
	 * @return
	 */
	protected EntityQuery createEntityQuery(PersonNameComponent name, Date birthDate) {
		EntityQuery<Patient> qry = new EntityQuery<Patient>();
		StringBuilder s = new StringBuilder();
		// birth date was defined ?
		if (birthDate != null) {
			s.append("p.birthDate = :dt");
			qry.addParameter("dt", birthDate);
		}

		if (name != null) {
			// was the name given ?
			if ((name.getName() != null) && (!name.getName().isEmpty())) {
				if (s.length() > 0)
					s.append(" or ");
				s.append("upper(p.name.name) like :name");
				qry.addParameter("name", name.getName().toUpperCase() + "%");
			}
			
			// was the middle name given ?
			if ((name.getMiddleName() != null) && (!name.getMiddleName().isEmpty())) {
				if (s.length() > 0)
					s.append(" or ");
				s.append("upper(p.name.middleName) like :midname");
				qry.addParameter("midname", name.getMiddleName().toUpperCase() + "%");
			}

			// was the last name given ?
			if ((name.getLastName() != null) && (!name.getLastName().isEmpty())) {
				if (s.length() > 0)
					s.append(" or ");
				s.append("upper(p.name.lastName) like :lastname");
				qry.addParameter("lastname", name.getLastName().toUpperCase() + "%");
			}
		}
		
		if (s != null)
			qry.addCondition("(" + s + ")");
		
		return qry;
	}
}
