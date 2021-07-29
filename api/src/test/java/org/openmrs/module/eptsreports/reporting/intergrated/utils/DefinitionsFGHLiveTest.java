package org.openmrs.module.eptsreports.reporting.intergrated.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import org.junit.Before;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.util.DatabaseUtil;

public abstract class DefinitionsFGHLiveTest extends DefinitionsTest {
	/** @see BaseContextSensitiveTest#useInMemoryDatabase() */
	@Override
	public Boolean useInMemoryDatabase() {
		/*
		 * ensure ~/.OpenMRS/openmrs-runtime.properties exists with your properties such
		 * as; connection.username=openmrs
		 * connection.url=jdbc:mysql://127.0.0.1:3316/openmrs
		 * connection.password=wTV.Tpp0|Q&c
		 */
		return false;
	}

	protected abstract String username();

	protected abstract String password();

	@Before
	public void initialize() throws ContextAuthenticationException {
		Context.authenticate(username(), password());
	}

	@Override
	protected Date getStartDate() {
		return DateUtil.getDateTime(2013, 2, 6);
	}

	@Override
	protected Date getEndDate() {
		return DateUtil.getDateTime(2019, 3, 6);
	}

	@Override
	protected Location getLocation() {
		return Context.getLocationService().getLocation(103);
	}
	
	protected void setupWithStandardDataAndAuthentication() throws Exception {
			String constraintsOffSql = "SET FOREIGN_KEY_CHECKS=0;";
			PreparedStatement ps = this.getConnection().prepareStatement(constraintsOffSql);
			ps.execute();
			executeDataSet(INITIAL_XML_DATASET_PACKAGE_PATH);
			constraintsOffSql = "SET FOREIGN_KEY_CHECKS=1;";
			ps = this.getConnection().prepareStatement(constraintsOffSql);
			ps.execute();
			authenticate();
	}
}
