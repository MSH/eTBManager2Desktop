/*
 * UserRole.java
 *
 * Created on 31 de Janeiro de 2007, 15:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.msh.etbm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Store information about a system event
 * @author Ricardo Memoria
 */

@Entity
@Table(name="userrole")
public class UserRole implements java.io.Serializable, Comparable<UserRole> {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(length=80, nullable=false, name="Role_Name")
    private String name;

    @Column(length=50)
    private String code;

    @Column(length=100)
    private String messageKey;

    /**
     * Indicate if this role contains operations of read-write, like, for instance, insert-update-delete commands
     */
    private boolean changeable;
    
    /**
     * Indicate if the role is used internally by the system or is available to be assigned in a profile
     */
    private boolean internalUse;
    
    /**
     * Indicate if role is assigned to profile by case classification
     */
    private boolean byCaseClassification;

    
    /**
     * Return the parent code of the user role
     * @return
     */
    public String getParentCode() {
    	int level = getLevel();
    	switch (level) {
    	case 2: return code.substring(0, 2).concat("0000");
    	case 3: return code.substring(0, 4).concat("00");
    	default: return null;
    	}
    }


    /**
     * Check if user role is a child of the given role
     * @param role
     * @return
     */
    public boolean isChildOf(UserRole role) {
    	int parentLevel = role.getLevel();
    	if ((parentLevel >= getLevel()) || (parentLevel == 0))
    		return false;
    	
    	String parentCode = role.getCode().substring(0, parentLevel * 2);
    	return code.startsWith(parentCode);
    }

    public int getLevel() {
		if ((code == null) || (code.isEmpty()))
			 return 0;
		
		if (code.endsWith("0000"))
			 return 1;
		else
		if (code.endsWith("00"))
			 return 2;
		else
		if (code.length() == 6)
			return 3;
		else return 0;
	}

    public String getDisplayName() {
    	if (name.equals("-"))
    		return "";
    	
    	String msg = "userrole." + name;
    	return msg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int compareTo(UserRole userRole) {
		return code.compareTo(userRole.getCode());
	}

	/**
	 * @return the changeable
	 */
	public boolean isChangeable() {
		return changeable;
	}

	/**
	 * @param changeable the changeable to set
	 */
	public void setChangeable(boolean changeable) {
		this.changeable = changeable;
	}

	/**
	 * @param internalUse the internalUse to set
	 */
	public void setInternalUse(boolean internalUse) {
		this.internalUse = internalUse;
	}

	/**
	 * @return the internalUse
	 */
	public boolean isInternalUse() {
		return internalUse;
	}

	/**
	 * @return the byClassification
	 */
	public boolean isByCaseClassification() {
		return byCaseClassification;
	}

	/**
	 * @param byClassification the byClassification to set
	 */
	public void setByCaseClassification(boolean byCaseClassification) {
		this.byCaseClassification = byCaseClassification;
	}


	/**
	 * @return the messageKey
	 */
	public String getMessageKey() {
		return messageKey;
	}


	/**
	 * @param messageKey the messageKey to set
	 */
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
}
