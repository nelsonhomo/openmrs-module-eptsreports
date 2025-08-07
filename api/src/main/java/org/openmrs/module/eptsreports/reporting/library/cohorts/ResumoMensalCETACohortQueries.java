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
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalCETAQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalCETACohortQueries {

  @Autowired private GenericCohortQueries genericCohortQueries;

  /** Indicador 0: Nr de Pacientes que reunem critérios para o rastreio FICA-BEM (RF6) */
  public CohortDefinition getPatientsIndicator0() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithCriteriasForTheFICABEMScreening");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithCriteriasForTheFICABEMScreening");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF7",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF7", ResumoMensalCETAQueries.findPatientsWhoWereTrackedUsingTheFICABEMForm()),
            mappings));

    definition.addSearch("RF28", EptsReportUtils.map(getPatientsRF28(), mappings));

    definition.addSearch("RF29", EptsReportUtils.map(getPatientsRF29(), mappings));

    definition.addSearch("RF30", EptsReportUtils.map(getPatientsRF30(), mappings));

    definition.addSearch("RF31", EptsReportUtils.map(getPatientsRF31(), mappings));

    definition.setCompositionString("RF7 OR RF28 OR RF29 OR RF30 OR RF31");

    return definition;
  }

  /** Indicador 1: Nr de Pacientes rastreados usando o FICA-BEM (RF7) */
  public CohortDefinition getPatientsIndicator1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF7",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF7", ResumoMensalCETAQueries.findPatientsWhoWereTrackedUsingTheFICABEMForm()),
            mappings));

    definition.setCompositionString("RF7");

    return definition;
  }

  /** Indicador 2 - Nr de Pacientes com resultado Positivo no FICA-BEM */
  public CohortDefinition getPatientsIndicator2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithPositiveResultInFICABEMForm");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithPositiveResultInFICABEMForm");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I1", EptsReportUtils.map(getPatientsIndicator1(), mappings));

    definition.addSearch(
        "DMC-DMG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "DMC-DMG", ResumoMensalCETAQueries.findPatientsWithPositiveResultInFICABEMForm()),
            mappings));

    definition.setCompositionString("I1 AND DMC-DMG");

    return definition;
  }

  /**
   * RF9 - Indicador 3 – Nr de Pacientes referidos para o seguimento de Doença Mental Grave
   * (psiquiatria/fluxo normal)
   */
  public CohortDefinition getPatientsIndicator3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsReferredForFollowUpSevereMentalIllness");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsReferredForFollowUpSevereMentalIllness");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I2", EptsReportUtils.map(getPatientsIndicator2(), mappings));

    definition.addSearch(
        "DMG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "DMG", ResumoMensalCETAQueries.findPatientsWithDMGOrDMCResultInFICABEMForm(165466)),
            mappings));

    definition.setCompositionString("I2 AND DMG");

    return definition;
  }

  /**
   * RF10 - Indicador 4 – Nr de Pacientes referidos para o seguimento de Doença Mental Comum
   * (Psicólogo)
   */
  public CohortDefinition getPatientsIndicator4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsReferredForFollowUpCommonMentalIllness");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsReferredForFollowUpCommonMentalIllness");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I2", EptsReportUtils.map(getPatientsIndicator2(), mappings));

    definition.addSearch(
        "DMC",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "DMC", ResumoMensalCETAQueries.findPatientsWithDMGOrDMCResultInFICABEMForm(165465)),
            mappings));

    definition.setCompositionString("I2 AND DMC");

    return definition;
  }

  /** RF11 - Indicador 5 - Nr de Pacientes que iniciaram o tratamento de SM - CETA */
  public CohortDefinition getPatientsIndicator5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoInitiatedTreatmentCETA");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoInitiatedTreatmentCETA");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I5",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I5", ResumoMensalCETAQueries.findPatientsWhoInitiatedSMTreatment()),
            mappings));

    definition.setCompositionString("I5");

    return definition;
  }

  /** RF12 - Indicador 6: Nr de Pacientes em seguimento no CETA até ao final do mês */
  public CohortDefinition getPatientsIndicator6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsInFollowUpCETA");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsInFollowUpCETA");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I6",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I6", ResumoMensalCETAQueries.findPatientsInFollowUpCETAUntilThEndOfTheMonth()),
            mappings));

    definition.setCompositionString("I6");

    return definition;
  }

  /** RF13 - Indicador 7 - Nr de Pacientes com, pelo menos, uma Tentativa de Suicídio a entrada */
  public CohortDefinition getPatientsIndicator7() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithAtLeastOneSuicideAttempt");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithAtLeastOneSuicideAttempt");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I5", EptsReportUtils.map(getPatientsIndicator5(), mappings));

    definition.addSearch(
        "I7",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I7", ResumoMensalCETAQueries.findPatientsWithAtLeastOneSuicideAttempt()),
            mappings));

    definition.setCompositionString("I5 AND I7");

    return definition;
  }

  /** RF14 - Indicador 8 – Nr de Pacientes com Tentativa de Homicídio a entrada */
  public CohortDefinition getPatientsIndicator8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithAtLeastOneHomicideAttempt");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithAtLeastOneHomicideAttempt");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I5", EptsReportUtils.map(getPatientsIndicator5(), mappings));

    definition.addSearch(
        "I8",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I8", ResumoMensalCETAQueries.findPatientsWithAtLeastOneHomicideAttempt()),
            mappings));

    definition.setCompositionString("I5 AND I8");

    return definition;
  }

  /** RF15 - Indicador 9 – Nr de pacientes com sintomas de ansiedade/ depressão */
  public CohortDefinition getPatientsIndicator9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithSymptomsOfDepressionAndAnxiety");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithSymptomsOfDepressionAndAnxiety");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I5", EptsReportUtils.map(getPatientsIndicator5(), mappings));

    definition.addSearch(
        "I9",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I9", ResumoMensalCETAQueries.findPatientsWithSymptomsOfDepressionAndAnxiety()),
            mappings));

    definition.setCompositionString("I5 AND I9");

    return definition;
  }

  /** RF16 - Indicador 10 - Nr de pacientes com trauma */
  public CohortDefinition getPatientsIndicator10() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithTrauma");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithTrauma");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I5", EptsReportUtils.map(getPatientsIndicator5(), mappings));

    definition.addSearch(
        "I10",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I10", ResumoMensalCETAQueries.findPatientsWithTrauma()),
            mappings));

    definition.setCompositionString("I5 AND I10");

    return definition;
  }

  /** RF17 - Indicador 11 - Nr de pacientes que consomem abusivamente bebidas alcoólicas */
  public CohortDefinition getPatientsIndicator11() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoAbuseAlcohol");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoAbuseAlcohol");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I5", EptsReportUtils.map(getPatientsIndicator5(), mappings));

    definition.addSearch(
        "I11",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I11", ResumoMensalCETAQueries.findPatientsWhoAbuseAlcohol()),
            mappings));

    definition.setCompositionString("I5 AND I11");

    return definition;
  }

  /**
   * RF18 - Indicador 12 - Nr de pacientes que consomem outras substâncias psicoactivas (ex.
   * Canabis, marijuana, etc)
   */
  public CohortDefinition getPatientsIndicator12() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I5", EptsReportUtils.map(getPatientsIndicator5(), mappings));

    definition.addSearch(
        "I12",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I12", ResumoMensalCETAQueries.findPatientsWhoConsumeOtherPsychoactiveSubstances()),
            mappings));

    definition.setCompositionString("I5 AND I12");

    return definition;
  }

  /** RF19 - Indicador 13- Nr de pacientes que interromperam o tratamento */
  public CohortDefinition getPatientsIndicator13() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CETA-89",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CETA-89",
                ResumoMensalCETAQueries.findPatientsWithFichaSeguimentoCETA89DaysBeforeEndDate()),
            mappings));

    definition.addSearch(
        "CETA-59",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CETA-59",
                ResumoMensalCETAQueries
                    .findPatientsWithoutFichaSeguimentoCETA59DaysBeforeEndDate()),
            mappings));

    definition.addSearch(
        "TRATAMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TRATAMENTO",
                ResumoMensalCETAQueries
                    .findPatientsWithTreatmentPlannedRegisteredInSection7ByReportEndDate()),
            mappings));

    definition.setCompositionString("(CETA-89 NOT CETA-59) NOT TRATAMENTO");

    return definition;
  }

  /** RF20 - Indicador 14 - Nr de pacientes referidos */
  public CohortDefinition getPatientsIndicator14() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoWereReferred");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoWereReferred");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I13", EptsReportUtils.map(getPatientsIndicator13(), mappings));

    definition.addSearch(
        "I14",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I14", ResumoMensalCETAQueries.findPatientsWhoWereReferred()),
            mappings));

    definition.setCompositionString("I13 AND I14");

    return definition;
  }

  /** RF21 - Indicador 15 - Nr de pacientes transferidos */
  public CohortDefinition getPatientsIndicator15() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoHaveTransferredAsAreasonOfInterruption");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoHaveTransferredAsAreasonOfInterruption");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I13", EptsReportUtils.map(getPatientsIndicator13(), mappings));

    definition.addSearch(
        "I15",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I15",
                ResumoMensalCETAQueries.findPatientsWhoHaveTransferredAsAreasonOfInterruption()),
            mappings));

    definition.setCompositionString("I13 AND I15");

    return definition;
  }

  /** RF22 - Indicador 16 - Nr de pacientes reintegrados */
  public CohortDefinition getPatientsIndicator16() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I6", EptsReportUtils.map(getPatientsIndicator6(), mappings));

    definition.addSearch(
        "I16",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I16", ResumoMensalCETAQueries.findPatientsWhoWereReintegrated()),
            mappings));

    definition.setCompositionString("I6 NOT I16");

    return definition;
  }

  /** RF23 - Indicador 17 - Nr de óbitos */
  public CohortDefinition getPatientsIndicator17() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsDeaths");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsDeaths");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I13", EptsReportUtils.map(getPatientsIndicator13(), mappings));

    definition.addSearch(
        "I17",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I17", ResumoMensalCETAQueries.findPatientsDeaths()),
            mappings));

    definition.setCompositionString("I13 AND I17");

    return definition;
  }

  /** RF24 - Indicador 18 - Nr de abandonos */
  public CohortDefinition getPatientsIndicator18() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoAbandonedTreatment");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoAbandonedTreatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I13", EptsReportUtils.map(getPatientsIndicator13(), mappings));

    definition.addSearch("I14", EptsReportUtils.map(getPatientsIndicator14(), mappings));

    definition.addSearch("I15", EptsReportUtils.map(getPatientsIndicator15(), mappings));

    definition.addSearch("I16", EptsReportUtils.map(getPatientsIndicator16(), mappings));

    definition.addSearch("I17", EptsReportUtils.map(getPatientsIndicator17(), mappings));

    definition.setCompositionString("I13 NOT (I14 OR I15 OR I16 OR I17)");

    return definition;
  }

  /** RF25 - Indicador 19: Nr de Pacientes que terminaram o tratamento */
  public CohortDefinition getPatientsIndicator19() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoFinishedTreatment");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoFinishedTreatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CETA-AFTER-89",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CETA-AFTER-89",
                ResumoMensalCETAQueries
                    .findPatientsWithFichaSeguimentoCETAAfter89DaysMinusEndDate()),
            mappings));

    definition.addSearch(
        "CETA-AFTER-59",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CETA-AFTER-59",
                ResumoMensalCETAQueries
                    .findPatientsWithoutFichaSeguimentoCETA59DaysBeforeEndDate()),
            mappings));

    definition.addSearch(
        "TRATAMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TRATAMENTO",
                ResumoMensalCETAQueries
                    .findPatientsWithTreatmentPlannedRegisteredInSection7ByReportEndDate()),
            mappings));

    definition.setCompositionString("(CETA-AFTER-89 NOT CETA-AFTER-59) AND TRATAMENTO");

    return definition;
  }

  /** RF26.1: Relatório Desagregação – Tipo de paciente: 2ª consulta TARV */
  public CohortDefinition getPatientsDisagregationsSecondLineTARV26_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "INICIAL-CETA",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "INICIAL-CETA",
                ResumoMensalCETAQueries
                    .findPatientsWhoHaveScreeningCriteriaSecondConsultationInFichaInicialCETA()),
            mappings));

    definition.addSearch("RF28", EptsReportUtils.map(getPatientsRF28(), mappings));

    definition.setCompositionString("INICIAL-CETA OR RF28");

    return definition;
  }

  /** RF26.2: Relatório Desagregação – Tipo de paciente: CV>1000 cp/ml */
  public CohortDefinition getPatientsDisagregationsCVGreaterThan1000Copies26_2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("RF29", EptsReportUtils.map(getPatientsRF29(), mappings));

    definition.addSearch(
        "CV-1000",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CV-1000",
                ResumoMensalCETAQueries
                    .findPatientsWhoHaveScreeningCriteriaCVGreaterThan1000CopiesInFichaInicialCETA()),
            mappings));

    definition.addSearch(
        "DISAG1", EptsReportUtils.map(getPatientsDisagregationsSecondLineTARV26_1(), mappings));

    definition.setCompositionString("(RF29 OR CV-1000) NOT DISAG1");

    return definition;
  }

  /** RF26.3: Relatório Desagregação – Tipo de paciente: Reintegrado / Ma Adesao */
  public CohortDefinition getPatientsDisagregationsPatientsReintegreted26_3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsDisagregationsPatientsReintegreted26_3");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsDisagregationsPatientsReintegreted26_3");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "REINTEGRADO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "REINTEGRADO",
                ResumoMensalCETAQueries
                    .findPatientsWhoHaveScreeningCriteriaReintegretedInFichaInicialCETA()),
            mappings));

    definition.addSearch("RF30", EptsReportUtils.map(getPatientsRF30(), mappings));

    definition.addSearch(
        "DISAG1", EptsReportUtils.map(getPatientsDisagregationsSecondLineTARV26_1(), mappings));

    definition.addSearch(
        "DISAG2",
        EptsReportUtils.map(getPatientsDisagregationsCVGreaterThan1000Copies26_2(), mappings));

    definition.setCompositionString("(REINTEGRADO OR RF30) NOT (DISAG1 OR DISAG2)");

    return definition;
  }

  /** RF26.4: Relatório Desagregação – Tipo de paciente: Factores Psicossociais */
  public CohortDefinition getPatientsDisagregationsPsychosocialFactors26_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsDisagregationsPsychosocialFactors26_4");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsDisagregationsPsychosocialFactors26_4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "PSICOSSOCIAIS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PSICOSSOCIAIS",
                ResumoMensalCETAQueries
                    .findPatientsWhoHaveScreeningCriteriaPsychosocialFactorsInFichaInicialCETA()),
            mappings));

    definition.addSearch("RF31", EptsReportUtils.map(getPatientsRF31(), mappings));

    definition.addSearch(
        "DISAG1", EptsReportUtils.map(getPatientsDisagregationsSecondLineTARV26_1(), mappings));

    definition.addSearch(
        "DISAG2",
        EptsReportUtils.map(getPatientsDisagregationsCVGreaterThan1000Copies26_2(), mappings));

    definition.addSearch(
        "DISAG3",
        EptsReportUtils.map(getPatientsDisagregationsPatientsReintegreted26_3(), mappings));

    definition.setCompositionString("(PSICOSSOCIAIS OR RF31) NOT (DISAG1 OR DISAG2 OR DISAG3)");

    return definition;
  }

  /** RF26.5 - All Indicators: Tipo de paciente: Sem Informação */
  public CohortDefinition getPatientsIndicatorWithoutInformationDisag(CohortDefinition indicator) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsIndicator0WithoutInformationDisag");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsIndicator0WithoutInformationDisag");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I", EptsReportUtils.map(indicator, mappings));

    definition.addSearch(
        "DISAG1", EptsReportUtils.map(getPatientsDisagregationsSecondLineTARV26_1(), mappings));

    definition.addSearch(
        "DISAG2",
        EptsReportUtils.map(getPatientsDisagregationsCVGreaterThan1000Copies26_2(), mappings));

    definition.addSearch(
        "DISAG3",
        EptsReportUtils.map(getPatientsDisagregationsPatientsReintegreted26_3(), mappings));

    definition.addSearch(
        "DISAG4",
        EptsReportUtils.map(getPatientsDisagregationsPsychosocialFactors26_4(), mappings));

    definition.setCompositionString("I NOT (DISAG1 OR DISAG2 OR DISAG3 OR DISAG4)");

    return definition;
  }

  /** RF28: Outras Fontes: 2ª Consulta TARV */
  public CohortDefinition getPatientsRF28() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsRF28");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsRF28");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF28-SEGUNDA-CONSULTA",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF28-SEGUNDA-CONSULTA",
                ResumoMensalCETAQueries.findPatientsWithSecondFichaClinicaDuringPeriod()),
            mappings));

    definition.addSearch(
        "RF28-TRANSFERIDOS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF28-TRANSFERIDOS", ResumoMensalCETAQueries.findPatientsWhoWereTransferredIn()),
            mappings));

    definition.setCompositionString("RF28-SEGUNDA-CONSULTA NOT RF28-TRANSFERIDOS");

    return definition;
  }

  /** RF29 - Outras Fontes: CV > 1000 cps */
  public CohortDefinition getPatientsRF29() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsRF29");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsRF29");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF29",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF29", ResumoMensalCETAQueries.findPatientsWithCVGreaterThan1000Copies()),
            mappings));

    definition.setCompositionString("RF29");

    return definition;
  }

  /** RF30 - Outras Fontes: Reintegrado / Ma Adesao */
  public CohortDefinition getPatientsRF30() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsRF30");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsRF30");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF30-REINTEGRADO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF30-REINTEGRADO",
                ResumoMensalCETAQueries.findPatientsWithReinicioFichaClinicaAndMaAdesaoAPSS()),
            mappings));

    definition.setCompositionString("RF30-REINTEGRADO");

    return definition;
  }

  /** RF30 - Outras Fontes: Reintegrado / Ma Adesao */
  public CohortDefinition getPatientsRF31() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsRF31");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsRF31");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF31-PSICOSSOCIAIS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF31-PSICOSSOCIAIS",
                ResumoMensalCETAQueries.findPatientsRegisteredInPsychosocialFactorsInAPSSForm()),
            mappings));

    definition.addSearch(
        "CUIDADO-FRESUMO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CUIDADO-FRESUMO",
                ResumoMensalCETAQueries
                    .findPatientsRegisteredInTARVCuidadosOrHaveMasterCardFichaResumo()),
            mappings));

    definition.addSearch(
        "RF31-FILA-RL",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF31-FILA-RL", ResumoMensalCETAQueries.findPatientsWhoPickedUpARV()),
            mappings));

    definition.setCompositionString("RF31-PSICOSSOCIAIS OR  (CUIDADO-FRESUMO NOT RF31-FILA-RL)");

    return definition;
  }
}
