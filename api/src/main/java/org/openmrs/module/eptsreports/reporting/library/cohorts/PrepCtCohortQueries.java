/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.PrepCtQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** DataSet for PREP CT */
@Component
public class PrepCtCohortQueries {

  @Autowired private GenericCohortQueries genericCohorts;

  /**
   * Build PrepCt composition cohort definition
   *
   * @param cohortName Cohort name
   * @return CompositionQuery
   */
  public CohortDefinition getClientsNewlyEnrolledInPrep() {
    final CompositionCohortDefinition txNewCompositionCohort = new CompositionCohortDefinition();

    txNewCompositionCohort.setName("PREP CT");
    txNewCompositionCohort.addParameter(new Parameter("startDate", "Start Date", Date.class));
    txNewCompositionCohort.addParameter(new Parameter("endDate", "End Date", Date.class));
    txNewCompositionCohort.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    txNewCompositionCohort.addSearch(
        "START-PREP",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsNewlyEnrolledInPrep",
                PrepCtQueries.QUERY.findClientsNewlyEnrolledInPrep),
            mappings));

    txNewCompositionCohort.addSearch(
        "TRANSFERED-IN-BEFORE",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWhoWhereTransferredInBeforeReportingPeriod",
                PrepCtQueries.QUERY.findClientsWhoWhereTransferredInBeforeReportingPeriod),
            mappings));

    txNewCompositionCohort.addSearch(
        "TRANSFERED-IN-DURING",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWhoWhereTransferredInDuringReportingPeriod",
                PrepCtQueries.QUERY.findClientsWhoWhereTransferredInDuringReportingPeriod),
            mappings));

    txNewCompositionCohort.addSearch(
        "REINITIATED-PREP",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWhoReinitiatedPrep", PrepCtQueries.QUERY.findClientsWhoReinitiatedPrep),
            mappings));

    txNewCompositionCohort.addSearch(
        "CONTINUE-PREP",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "findClientsWhoReinitiatedPrep", PrepCtQueries.QUERY.findClientsWhoContinuePrep),
            mappings));

    txNewCompositionCohort.setCompositionString(
        "(START-PREP OR TRANSFERED-IN-BEFORE) OR (TRANSFERED-IN-DURING OR REINITIATED-PREP OR CONTINUE-PREP)");

    return txNewCompositionCohort;
  }
}
