package org.msh.utils;

import java.sql.Types;

import org.hibernate.dialect.HSQLDialect;

public class CustomHSQLDialect extends HSQLDialect {

	public CustomHSQLDialect() {
		super();
        registerColumnType(Types.BOOLEAN, "boolean");
        registerHibernateType(Types.BOOLEAN, "boolean");
	}

}
