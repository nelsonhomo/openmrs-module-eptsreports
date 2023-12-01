/*
 * The contents of this file are subject to the OpenMRS Public License Version
 * 1.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Arrays;
import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.metadata.HivMetadata;
import org.openmrs.module.eptsreports.metadata.TbMetadata;
import org.openmrs.module.eptsreports.reporting.library.queries.TXTBMontlyCascadeReportQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.TXTBMontlyCascadeReportQueries.QUERY.DiagnosticTestTypes;
import org.openmrs.module.eptsreports.reporting.library.queries.TXTBQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TXTBDenominatorForTBMontlyCascadeQueries {

  @Autowired private TbMetadata tbMetadata;

  @Autowired private HivMetadata hivMetadata;

  @Autowired private GenericCohortQueries genericCohortQueries;

  @Autowired private TXTBCohortQueries txtbCohortQueries;

  @Autowired private TxCurrCohortQueries txCurrCohortQueries;

  private String generalParameterMapping =
      "startDate=${endDate-6m+1d},endDate=${endDate},location=${location}";

  @DocumentedDefinition(value = "TxTBDenominatorPositiveScreening")
  public CohortDefinition getTxTBDenominatorAndPositiveScreening() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("TxTB - Denominator Positive Screening");
    definition.addSearch(
        "denominator",
        EptsReportUtils.map(this.txtbCohortQueries.getDenominator(), generalParameterMapping));

    definition.addSearch(
        "positive-screening",
        EptsReportUtils.map(this.getTxTBPPositiveScreening(), generalParameterMapping));
    this.addGeneralParameters(definition);
    definition.setCompositionString("denominator AND positive-screening");
    return definition;
  }

  @DocumentedDefinition(value = "TxTBDenominatorNegativeScreening")
  public CohortDefinition getTxTBDenominatorAndNegativeScreening() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB - Denominator  Negative Screening");

    definition.addSearch(
        "denominator",
        EptsReportUtils.map(this.txtbCohortQueries.getDenominator(), generalParameterMapping));

    definition.addSearch(
        "positive-screening",
        EptsReportUtils.map(this.getTxTBPPositiveScreening(), generalParameterMapping));

    definition.addSearch(
        "new-on-art", EptsReportUtils.map(this.getNewOnArt(), this.generalParameterMapping));

    this.addGeneralParameters(definition);
    definition.setCompositionString("denominator NOT positive-screening");
    return definition;
  }

  @DocumentedDefinition(value = "get Specimen Sent")
  public CohortDefinition getSpecimenSentCohortDefinition() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    this.addGeneralParameters(definition);
    definition.setName("TxTB -specimen-sent");

    CohortDefinition applicationForLaboratoryResearchDataset =
        this.genericCohortQueries.generalSql(
            "applicationForLaboratoryResearch",
            TXTBQueries.dateObsForEncounterAndQuestionAndAnswers(
                this.hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    this.hivMetadata.getApplicationForLaboratoryResearch().getConceptId()),
                Arrays.asList(
                    this.tbMetadata.getTbGenexpertTest().getConceptId(),
                    this.tbMetadata.getCultureTest().getConceptId(),
                    this.tbMetadata.getTbLam().getConceptId(),
                    this.tbMetadata.getSputumForAcidFastBacilli().getConceptId())));

    this.addGeneralParameters(applicationForLaboratoryResearchDataset);

    definition.addSearch(
        "application-for-laboratory-research",
        EptsReportUtils.map(applicationForLaboratoryResearchDataset, this.generalParameterMapping));
    definition.addSearch(
        "tb-genexpert-culture-lam-bk-test",
        EptsReportUtils.map(
            txtbCohortQueries.getTbGenExpertORCultureTestOrTbLamOrBk(),
            this.generalParameterMapping));
    definition.addSearch(
        "lab-results",
        EptsReportUtils.map(this.getResultsOnFichaLaboratorio(), this.generalParameterMapping));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(this.txtbCohortQueries.getDenominator(), this.generalParameterMapping));

    definition.setCompositionString(
        "(application-for-laboratory-research OR tb-genexpert-culture-lam-bk-test OR lab-results) AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "getTxTBPPositiveScreening")
  private CohortDefinition getTxTBPPositiveScreening() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();

    cd.setName("TxTB - positiveScreening");

    cd.addSearch(
        "A",
        EptsReportUtils.map(
            txtbCohortQueries.codedYesTbScreening(),
            "onOrAfter=${endDate-6m+1d},onOrBefore=${endDate},locationList=${location}"));
    cd.addSearch(
        "B",
        EptsReportUtils.map(
            txtbCohortQueries.positiveInvestigationResultComposition(), generalParameterMapping));
    cd.addSearch(
        "C",
        EptsReportUtils.map(
            txtbCohortQueries.negativeInvestigationResultAndAnyResultForTBScreeningComposition(),
            generalParameterMapping));
    cd.addSearch(
        "D",
        EptsReportUtils.map(
            txtbCohortQueries.getTbDrugTreatmentStartDateWithinReportingDate(),
            generalParameterMapping));
    cd.addSearch(
        "E", EptsReportUtils.map(txtbCohortQueries.getInTBProgram(), generalParameterMapping));
    cd.addSearch(
        "F",
        EptsReportUtils.map(
            txtbCohortQueries.getPulmonaryTBWithinReportingDate(), generalParameterMapping));
    cd.addSearch(
        "G",
        EptsReportUtils.map(
            txtbCohortQueries.getTuberculosisTreatmentPlanWithinReportingDate(),
            generalParameterMapping));
    cd.addSearch(
        "H",
        EptsReportUtils.map(
            txtbCohortQueries.getAllTBSymptomsForDisaggregationComposition(),
            generalParameterMapping));
    cd.addSearch(
        "I",
        EptsReportUtils.map(
            txtbCohortQueries.getSputumForAcidFastBacilliWithinReportingDate(),
            generalParameterMapping));

    cd.setCompositionString("A OR B OR C OR D OR E OR F OR G OR H OR I");
    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "get All TB Symptoms for Denominator")
  private CohortDefinition getAllTBSymptomsForDemoninatorComposition() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    this.addGeneralParameters(definition);
    definition.setName("TxTB - All TB Symptoms for Denominator");

    definition.addSearch(
        "tuberculosis-symptoms",
        EptsReportUtils.map(
            txtbCohortQueries.getTuberculosisSymptoms(
                this.hivMetadata.getYesConcept().getConceptId(),
                this.hivMetadata.getNoConcept().getConceptId()),
            generalParameterMapping));

    definition.addSearch(
        "active-tuberculosis",
        EptsReportUtils.map(txtbCohortQueries.getActiveTuberculosis(), generalParameterMapping));

    definition.addSearch(
        "tb-observations",
        EptsReportUtils.map(txtbCohortQueries.getTbObservations(), generalParameterMapping));

    definition.addSearch(
        "application-for-laboratory-research",
        EptsReportUtils.map(
            txtbCohortQueries.getApplicationForLaboratoryResearch(), generalParameterMapping));

    definition.addSearch(
        "tb-genexpert-or-culture-test-or-lam-or-bk-test",
        EptsReportUtils.map(
            txtbCohortQueries.getTbGenExpertORCultureTestOrTbLamOrBk(), generalParameterMapping));

    definition.addSearch(
        "tb-raioxtorax",
        EptsReportUtils.map(txtbCohortQueries.getTbRaioXTorax(), generalParameterMapping));

    definition.setCompositionString(
        "tuberculosis-symptoms OR active-tuberculosis OR tb-observations OR application-for-laboratory-research OR tb-genexpert-or-culture-test-or-lam-or-bk-test OR tb-raioxtorax");

    return definition;
  }

  @DocumentedDefinition(value = "get Diagnóstico Laboratorial para TB")
  private CohortDefinition getResultsOnFichaLaboratorio() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    this.addGeneralParameters(definition);
    definition.setName("TxTB -Diagnóstico Laboratorial para TB");

    definition.addSearch(
        "sputum-for-acid-fast-bacilli",
        EptsReportUtils.map(
            txtbCohortQueries.getSputumForAcidFastBacilliWithinReportingDate(),
            generalParameterMapping));

    definition.addSearch(
        "genexpert-culture",
        EptsReportUtils.map(
            txtbCohortQueries.getGenExpertOrCulturaOnFichaLaboratorio(), generalParameterMapping));

    definition.addSearch(
        "tblam",
        EptsReportUtils.map(
            txtbCohortQueries.getTbLamOnFichaLaboratorio(), generalParameterMapping));

    definition.addSearch(
        "xpert-mtb",
        EptsReportUtils.map(
            txtbCohortQueries.getXpertMTBOnFichaLaboratorio(), generalParameterMapping));

    definition.setCompositionString(
        "sputum-for-acid-fast-bacilli OR genexpert-culture OR tblam OR xpert-mtb");

    return definition;
  }

  @DocumentedDefinition(value = "get New on Art")
  public CohortDefinition getNewOnArt() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB - New on ART");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "started-on-period",
        EptsReportUtils.map(
            this.genericCohortQueries.getStartedArtOnPeriod(false, true),
            "onOrAfter=${endDate-6m+1d},onOrBefore=${endDate},location=${location}"));
    definition.setCompositionString("started-on-period");
    return definition;
  }

  @DocumentedDefinition(value = "GenExpertTests")
  public CohortDefinition getGenExpertTests() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("GeneXpert MTB/RIF");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "genexperts",
        EptsReportUtils.map(
            this.findDiagnostiTests(DiagnosticTestTypes.GENEXPERT), generalParameterMapping));
    definition.setCompositionString("genexperts");
    return definition;
  }

  @DocumentedDefinition(value = "getGenExpertPositiveTestResults")
  public CohortDefinition getGenExpertPositiveTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("GeneXpert MTB/RIF Posetive Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "genexperts",
        EptsReportUtils.map(
            this.findDiagnosticPositiveTestResults(DiagnosticTestTypes.GENEXPERT),
            generalParameterMapping));
    definition.setCompositionString("genexperts");
    return definition;
  }

  @DocumentedDefinition(value = "allPositiveTestResults")
  public CohortDefinition getAllPositiveTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Posetive Test Results");
    this.addGeneralParameters(definition);

    definition.addSearch(
        "genexpertsPositiveTestResults",
        EptsReportUtils.map(this.getGenExpertPositiveTestResults(), generalParameterMapping));

    definition.addSearch(
        "baciloscopiaPositiveTestResults",
        EptsReportUtils.map(this.getBaciloscopiaPositiveTestResults(), generalParameterMapping));

    definition.addSearch(
        "tblamPositiveTestResults",
        EptsReportUtils.map(this.getTBLAMPositiveTestResults(), generalParameterMapping));

    definition.addSearch(
        "otherAditionalPositiveTestResults",
        EptsReportUtils.map(this.getAdditionalPositiveTestResults(), generalParameterMapping));

    definition.setCompositionString(
        "genexpertsPositiveTestResults OR baciloscopiaPositiveTestResults OR tblamPositiveTestResults OR otherAditionalPositiveTestResults ");
    return definition;
  }

  @DocumentedDefinition(value = "allNegativeTestResults")
  public CohortDefinition getAllNegativeTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Posetive Test Results");
    this.addGeneralParameters(definition);

    definition.addSearch(
        "genexpertsNegativeTestResults",
        EptsReportUtils.map(this.getGenExpertNegativeTestResults(), generalParameterMapping));

    definition.addSearch(
        "baciloscopiaNegativeTestResults",
        EptsReportUtils.map(this.getBaciloscopiaNegativeTestResults(), generalParameterMapping));

    definition.addSearch(
        "tblamNegativeTestResults",
        EptsReportUtils.map(this.getTBLAMNegativeTestResults(), generalParameterMapping));

    definition.addSearch(
        "otherAditionalNegativeTestResults",
        EptsReportUtils.map(this.getAdditionalNegativeTestResults(), generalParameterMapping));

    definition.setCompositionString(
        "genexpertsNegativeTestResults OR baciloscopiaNegativeTestResults OR tblamNegativeTestResults OR otherAditionalNegativeTestResults ");
    return definition;
  }

  @DocumentedDefinition(value = "getGenExpertNegativeTestResults")
  public CohortDefinition getGenExpertNegativeTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("GeneXpert MTB/RIF Negative Test Results");
    this.addGeneralParameters(definition);

    definition.addSearch(
        "negativeGenexperts",
        EptsReportUtils.map(
            this.findDiagnosticNegativeTestResults(DiagnosticTestTypes.GENEXPERT),
            generalParameterMapping));

    definition.setCompositionString("negativeGenexperts");
    return definition;
  }

  @DocumentedDefinition(value = "BaciloscopiaTests")
  public CohortDefinition getBaciloscopiaTests() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Smear microscopy only");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "baciloscopia",
        EptsReportUtils.map(
            this.findDiagnostiTests(DiagnosticTestTypes.BACILOSCOPIA), generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));

    definition.setCompositionString("baciloscopia NOT genexperts");
    return definition;
  }

  @DocumentedDefinition(value = "getBaciloscopiaPositiveTestResults")
  public CohortDefinition getBaciloscopiaPositiveTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Smear microscopy only Positive Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "baciloscopia",
        EptsReportUtils.map(
            this.findDiagnosticPositiveTestResults(DiagnosticTestTypes.BACILOSCOPIA),
            generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));

    definition.setCompositionString("baciloscopia NOT genexperts");
    return definition;
  }

  @DocumentedDefinition(value = "getBaciloscopiaNegativeTestResults")
  public CohortDefinition getBaciloscopiaNegativeTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Smear microscopy only Negative Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "negativeBaciloscopia",
        EptsReportUtils.map(
            this.findDiagnosticNegativeTestResults(DiagnosticTestTypes.BACILOSCOPIA),
            generalParameterMapping));

    definition.addSearch(
        "negativeGenexpert",
        EptsReportUtils.map(this.getGenExpertNegativeTestResults(), generalParameterMapping));

    definition.setCompositionString("negativeBaciloscopia NOT negativeGenexpert");
    return definition;
  }

  @DocumentedDefinition(value = "TBLAM")
  public CohortDefinition getTBLAMTests() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB LAM");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "tblam",
        EptsReportUtils.map(
            this.findDiagnostiTests(DiagnosticTestTypes.TBLAM), generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));
    definition.addSearch(
        "baciloscopia", EptsReportUtils.map(this.getBaciloscopiaTests(), generalParameterMapping));

    definition.setCompositionString("tblam NOT (genexperts OR baciloscopia)");
    return definition;
  }

  @DocumentedDefinition(value = "getTBLAMPositiveTestResults")
  public CohortDefinition getTBLAMPositiveTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB LAM Positive Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "tblam",
        EptsReportUtils.map(
            this.findDiagnosticPositiveTestResults(DiagnosticTestTypes.TBLAM),
            generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));
    definition.addSearch(
        "baciloscopia", EptsReportUtils.map(this.getBaciloscopiaTests(), generalParameterMapping));

    definition.setCompositionString("tblam NOT (genexperts OR baciloscopia)");
    return definition;
  }

  @DocumentedDefinition(value = "getTBLAMNegativeTestResults")
  public CohortDefinition getTBLAMNegativeTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB LAM Negative Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "negativeTblam",
        EptsReportUtils.map(
            this.findDiagnosticNegativeTestResults(DiagnosticTestTypes.TBLAM),
            generalParameterMapping));

    definition.addSearch(
        "negativeGenexpert",
        EptsReportUtils.map(this.getGenExpertNegativeTestResults(), generalParameterMapping));

    definition.addSearch(
        "negativeBaciloscopia",
        EptsReportUtils.map(this.getBaciloscopiaNegativeTestResults(), generalParameterMapping));

    definition.setCompositionString(
        "negativeTblam NOT (negativeGenexpert OR negativeBaciloscopia)");
    return definition;
  }

  @DocumentedDefinition(value = "AdditionalTests")
  public CohortDefinition getAdditionalTests() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Additional test other than GeneXpert");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "additionalTests",
        EptsReportUtils.map(
            this.findDiagnostiTests(DiagnosticTestTypes.CULTURA), generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));
    definition.addSearch(
        "baciloscopia", EptsReportUtils.map(this.getBaciloscopiaTests(), generalParameterMapping));
    definition.addSearch(
        "tblam", EptsReportUtils.map(this.getTBLAMTests(), generalParameterMapping));

    definition.setCompositionString("additionalTests NOT (genexperts OR baciloscopia OR tblam)");
    return definition;
  }

  @DocumentedDefinition(value = "getAdditionalPositiveTestResults")
  public CohortDefinition getAdditionalPositiveTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Additional test other than GeneXpert Positive Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "additionalTests",
        EptsReportUtils.map(
            this.findDiagnosticPositiveTestResults(DiagnosticTestTypes.CULTURA),
            generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));
    definition.addSearch(
        "baciloscopia", EptsReportUtils.map(this.getBaciloscopiaTests(), generalParameterMapping));
    definition.addSearch(
        "tblam", EptsReportUtils.map(this.getTBLAMTests(), generalParameterMapping));

    definition.setCompositionString("additionalTests NOT (genexperts OR baciloscopia OR tblam)");
    return definition;
  }

  @DocumentedDefinition(value = "getAdditionalNegativeTestResults")
  public CohortDefinition getAdditionalNegativeTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB LAM Negative Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "negativeAdditionalTests",
        EptsReportUtils.map(
            this.findDiagnosticNegativeTestResults(DiagnosticTestTypes.CULTURA),
            generalParameterMapping));

    definition.addSearch(
        "negativeTBLAM",
        EptsReportUtils.map(this.getTBLAMNegativeTestResults(), generalParameterMapping));

    definition.addSearch(
        "negativeGenexpert",
        EptsReportUtils.map(this.getGenExpertNegativeTestResults(), generalParameterMapping));

    definition.addSearch(
        "negativeBaciloscopia",
        EptsReportUtils.map(this.getBaciloscopiaNegativeTestResults(), generalParameterMapping));

    definition.setCompositionString(
        "negativeAdditionalTests NOT (negativeTBLAM OR negativeGenexpert OR negativeBaciloscopia)");
    return definition;
  }

  @DocumentedDefinition(value = "txTbNumeratorA")
  private CohortDefinition txTbNumeratorA() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - txTbNumeratorA");
    final CohortDefinition i =
        this.genericCohortQueries.generalSql(
            "onTbTreatment",
            TXTBQueries.dateObs(
                this.tbMetadata.getTBDrugTreatmentStartDate().getConceptId(),
                Arrays.asList(
                    this.hivMetadata.getAdultoSeguimentoEncounterType().getId(),
                    this.hivMetadata.getARVPediatriaSeguimentoEncounterType().getId()),
                true));
    final CohortDefinition ii = txtbCohortQueries.getInTBProgram();

    this.addGeneralParameters(i);

    cd.addSearch("i", EptsReportUtils.map(i, generalParameterMapping));
    cd.addSearch("ii", EptsReportUtils.map(ii, generalParameterMapping));
    cd.addSearch(
        "iii",
        EptsReportUtils.map(
            txtbCohortQueries.getPulmonaryTBWithinReportingDate(), generalParameterMapping));
    cd.addSearch(
        "iv",
        EptsReportUtils.map(
            txtbCohortQueries.getTuberculosisTreatmentPlanWithinReportingDate(),
            generalParameterMapping));

    final CohortDefinition artList = txtbCohortQueries.artList();
    cd.addSearch("artList", EptsReportUtils.map(artList, generalParameterMapping));
    cd.setCompositionString("(i OR ii OR iii OR iv) AND artList");
    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "findDiagnostiTests")
  private CohortDefinition findDiagnostiTests(DiagnosticTestTypes diagnosticTestType) {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDiagnostiTests");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        TXTBMontlyCascadeReportQueries.QUERY.findDiagnosticTests(diagnosticTestType));

    return definition;
  }

  @DocumentedDefinition(value = "findDiagnosticPositiveTestResults")
  private CohortDefinition findDiagnosticPositiveTestResults(
      DiagnosticTestTypes diagnosticTestType) {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDiagnosticPositiveTestResults");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        TXTBMontlyCascadeReportQueries.QUERY.findDiagnosticTestsWithPositiveTestResults(
            diagnosticTestType));

    return definition;
  }

  @DocumentedDefinition(value = "findDiagnosticNegativeTestResults")
  private CohortDefinition findDiagnosticNegativeTestResults(
      DiagnosticTestTypes diagnosticTestType) {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDiagnosticNegativeTestResults");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        TXTBMontlyCascadeReportQueries.QUERY.findDiagnosticTestsWithNegativeTestResults(
            diagnosticTestType));

    return definition;
  }

  @DocumentedDefinition(value = "PatientsWithPositiveResultWhoStartedTBTreatment")
  public CohortDefinition getPositiveResultAndTXTBNumerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB -Denominator Positive Results Who Started TB Treatment");
    this.addGeneralParameters(definition);

    definition.addSearch(
        "posetiveResults",
        EptsReportUtils.map(
            txtbCohortQueries.getPositiveResultsForTXTBMontlyCascadeCohortDefinition(
                this.txtbCohortQueries.getDenominator(), generalParameterMapping),
            generalParameterMapping));
    definition.addSearch(
        "txtbNumerator",
        EptsReportUtils.map(txtbCohortQueries.txTbNumerator(), generalParameterMapping));

    definition.setCompositionString("posetiveResults AND txtbNumerator");
    return definition;
  }

  @DocumentedDefinition(value = "ScreenedPatientsWhoStartedTBTreatmentAndTXCurr")
  public CohortDefinition getScreenedPatientsWhoStartedTBTreatmentAndTXCurr() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB - Screened Patients Who Started TB Treatment and Are TX_CURR");
    this.addGeneralParameters(definition);

    definition.addSearch(
        "txTB", EptsReportUtils.map(txtbCohortQueries.txTbNumerator(), generalParameterMapping));
    definition.addSearch(
        "txCurr",
        EptsReportUtils.map(
            this.txCurrCohortQueries.findPatientsWhoAreActiveOnART(),
            "endDate=${endDate},location=${location}"));

    definition.setCompositionString("txTB AND txCurr");
    return definition;
  }

  @DocumentedDefinition(value = "get Negative Results")
  public CohortDefinition getNegativeResultCohortDefinition() {

    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    this.addGeneralParameters(cd);
    cd.setName("TxTB -Denominator Negative Results");

    cd.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(this.txtbCohortQueries.getDenominator(), this.generalParameterMapping));

    cd.addSearch(
        "all-negative-test-results",
        EptsReportUtils.map(this.getAllNegativeTestResults(), this.generalParameterMapping));

    cd.setCompositionString("DENOMINATOR AND all-negative-test-results ");

    return cd;
  }

  private void addGeneralParameters(final CohortDefinition cd) {
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));
  }
}
