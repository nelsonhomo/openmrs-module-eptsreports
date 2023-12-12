package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalDAHQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalDAHQueries.ARTSituation;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalDAHQueries.MCCTreatment;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.TypesOfExams;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalDAHCohortQueries {

  private GenericCohortQueries genericCohortQueries;
  @Autowired ResumoMensalCohortQueries resumoMensalCohortQueries;

  @Autowired
  public ResumoMensalDAHCohortQueries(GenericCohortQueries genericCohortQueries) {
    this.genericCohortQueries = genericCohortQueries;
  }

  /** 0 - Número total de activos em DAH em TARV, até ao fim do mês anterior */
  public CohortDefinition
      getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicatorI0() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "ACTIVEDAH",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0()),
            mappings));

    definition.setCompositionString("ACTIVEDAH");

    return definition;
  }

  /**
   * 1 - Número de utentes novos inícios em TARV que iniciaram seguimento para Doença Avançada por
   * HIV durante o mês
   */
  public CohortDefinition getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriodI1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAHPERIOD",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1()),
            mappings));

    definition.addSearch(
        "INICIOTARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1",
                ResumoMensalDAHQueries.findPatientsARTSituation(ARTSituation.NEW_ENROLLED)),
            mappings));

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            resumoMensalCohortQueries
                .getPatientsWhoInitiatedTarvAtThisFacilityDuringCurrentMonthB1(),
            mappings));

    definition.setCompositionString("DAHPERIOD AND (INICIOTARV OR B1)");

    return definition;
  }

  /**
   * 2 - Número de utentes reinícios em TARV que iniciaram seguimento para Doença Avançada por HIV
   * durante o mês
   */
  public CohortDefinition
      getNumberOfPatientsWhoReinitiatedARTWhoInitiatedDAHDuringReportPeriodI2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAHPERIOD",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1()),
            mappings));

    definition.addSearch(
        "REINICIOTARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsRegisteredAsReinitiatedARTDuringReportPeriod2",
                ResumoMensalDAHQueries.findPatientsARTSituation(ARTSituation.RESTART)),
            mappings));

    definition.addSearch(
        "NEW-ENROLLED",
        EptsReportUtils.map(
            this.getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriodI1(),
            mappings));

    definition.addSearch("B3", EptsReportUtils.map(this.getSumB3(), mappings));

    definition.setCompositionString("DAHPERIOD AND ((REINICIOTARV OR B3) NOT NEW-ENROLLED)");

    return definition;
  }

  /**
   * 3 - Número de utentes activos em TARV que iniciaram seguimento para Doença Avançada por HIV
   * durante o mês
   */
  public CohortDefinition getNumberOfPatientsCurrentInARTWhoInitiatedDAHDuringReportPeriodI3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAHPERIOD",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1()),
            mappings));

    definition.addSearch(
        "EMTARV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsRegisteredAsActiveInARTDuringReportPeriod3",
                ResumoMensalDAHQueries.findPatientsARTSituation(ARTSituation.ACTIVE)),
            mappings));

    definition.addSearch(
        "RESTART",
        EptsReportUtils.map(
            this.getNumberOfPatientsWhoReinitiatedARTWhoInitiatedDAHDuringReportPeriodI2(),
            mappings));

    definition.addSearch(
        "NEW-ENROLLED",
        EptsReportUtils.map(
            this.getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriodI1(),
            mappings));

    definition.addSearch(
        "B12",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12(),
            mappings));

    definition.setCompositionString(
        "DAHPERIOD AND ((EMTARV OR B12) NOT (RESTART OR NEW-ENROLLED))");

    return definition;
  }

  /**
   * 4 - Número de utentes em seguimento para Doença Avançada que sairam da abordagem durante o mês
   * por qualquer motivo (saída por alta, óbito, transferido, abandono)
   */
  public CohortDefinition getNumberOfPatientsWhoLeftDAHDuringReportPeriodI4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWhoLeftDAHDuringReportPeriodI4");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsWhoLeftDAHDuringReportPeriodI4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "LEFTDAH",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1",
                ResumoMensalDAHQueries.getNumberOfPatientsWhoLeftDAHDuringReportPeriod4()),
            mappings));

    definition.setCompositionString("LEFTDAH");

    return definition;
  }

  /**
   * Número total de utentes em seguimento para Doença Avançada por HIV no fim do mês
   * (5=(0+1+2+3)-4)
   */
  public CohortDefinition getTotalNumberOfPatientsInDAHAtTheEndOfReportingPeriodI5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWhoLeftDAHDuringReportPeriodI4");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsWhoLeftDAHDuringReportPeriodI4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I0",
        EptsReportUtils.map(
            this.getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicatorI0(),
            mappings));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriodI1(),
            mappings));

    definition.addSearch(
        "I2",
        EptsReportUtils.map(
            this.getNumberOfPatientsWhoReinitiatedARTWhoInitiatedDAHDuringReportPeriodI2(),
            mappings));

    definition.addSearch(
        "I3",
        EptsReportUtils.map(
            this.getNumberOfPatientsCurrentInARTWhoInitiatedDAHDuringReportPeriodI3(), mappings));

    definition.addSearch(
        "I4",
        EptsReportUtils.map(this.getNumberOfPatientsWhoLeftDAHDuringReportPeriodI4(), mappings));

    definition.setCompositionString("(I0 OR I1 OR I2 OR I3) NOT I4");

    return definition;
  }

  /** 6 - Número total de óbitos nos utentes com diagnóstico de Doença Avançada na coorte */
  public CohortDefinition getTotalNumberOfPatientsWhoAreDeadWithDAHDiagnosticI6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TotalNumberOfPatientsWhoAreDeadWithDAHDiagnosticI6");
    final String mappings6Months =
        "startDate=${startDate-7m},endDate=${startDate-6m-1d},location=${location}";
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsDiedWithDAHSixMonthsAfterDAH6");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DEADDAH",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsDiedWithDAHSixMonthsAfterDAH6",
                ResumoMensalDAHQueries.getNumberOfPatientsDiedWithDAHSixMonthsAfterDAH6()),
            mappings6Months));

    definition.addSearch(
        "DAHIN6MONTHSCOORTE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithDAHInSixMonthsCoorte",
                ResumoMensalDAHQueries.getNumberOfPatientsWithDAHInSixMonthsCoorte()),
            mappings6Months));

    definition.addSearch(
        "B8",
        EptsReportUtils.map(resumoMensalCohortQueries.getPatientsWhoDiedTratmentB8(), mappings));

    definition.setCompositionString("DEADDAH OR (DAHIN6MONTHSCOORTE AND B8)");

    return definition;
  }

  /** 7 - Número total de novos inscritos em seguimento de DAH na coorte */
  public CohortDefinition getTotalNumberPatientsInDAHSixMonthsCoorteI7() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWhoLeftDAHDuringReportPeriodI4");
    final String mappings6Months =
        "startDate=${startDate-7m},endDate=${startDate-6m-1d},location=${location}";

    definition.setName("getTotalNumberPatientsInDAHSixMonthsCoorteI7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAH6MONTHS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithDAHInSixMonthsCoorte",
                ResumoMensalDAHQueries.getNumberOfPatientsWithDAHInSixMonthsCoorte()),
            mappings6Months));

    definition.setCompositionString("DAH6MONTHS");

    return definition;
  }

  /** 8 - Número de utentes com pedido de CD4 de rastreio durante o mês */
  public CohortDefinition getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    String composition = "";

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8",
                ResumoMensalDAHQueries.getNumberOfPatientsRequestingCD4ScreeningDuringThePeriod8()),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "CD4 AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "CD4 AND RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "CD4 AND ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "CD4 AND PREGNANT";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 9 - Número de utentes com resultado de CD4 de rastreio disponivel durante o mês */
  public CohortDefinition getNumberOfPatientWithCD4ResultDuringThePeriodI9(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithCD4ResultDuringThePeriodI9");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientWithCD4ResultDuringThePeriodI9");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    String composition = "";

    definition.addSearch(
        "CD4-RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithCD4ResultDuringThePeriod9",
                ResumoMensalDAHQueries.getNumberOfPatientWithCD4ResultDuringThePeriod9()),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "CD4-RESULT AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "CD4-RESULT AND RESTART";
    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "CD4-RESULT AND ACTIVE";
    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "CD4-RESULT AND PREGNANT";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 10 - Número de utentes com resultado de CD4 baixo* durante o mês */
  public CohortDefinition getNumberOfPatientWithLowCD4ResultDuringThePeriodI10(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    String composition = "";

    definition.addSearch(
        "CD4-BAIXO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithLowCD4ResultDuringThePeriod10",
                ResumoMensalDAHQueries.getNumberOfPatientWithLowCD4ResultDuringThePeriod10()),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "CD4-BAIXO AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "CD4-BAIXO AND RESTART";
    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "CD4-BAIXO AND ACTIVE";
    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "CD4-BAIXO AND PREGNANT";
    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      definition.addSearch(
          "SEGUIMENTO",
          EptsReportUtils.map(
              this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

      composition = "CD4-BAIXO AND SEGUIMENTO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 11 - Número de utentes elegíveis a pedido de TB-LAM com resultado de TB-LAM durante o mês */
  public CohortDefinition getNumberOfPatientsElegibleToTBLAMWithTBLamResultDuringReportingPeriodI11(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCd4Baixo =
        "startDate=${startDate-1m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I10",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithLowCD4ResultDuringThePeriod10",
                ResumoMensalDAHQueries.getNumberOfPatientWithLowCD4ResultDuringThePeriod10()),
            mappingsCd4Baixo));

    definition.addSearch(
        "CD4-WITH-TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsElegibleToTBLAMWithTBLamResultDuringReportingPeriod11",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.TBLAM)),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "I10 AND CD4-WITH-TBLAM AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "I10 AND CD4-WITH-TBLAM AND RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "I10 AND CD4-WITH-TBLAM AND ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I10 AND CD4-WITH-TBLAM AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      definition.addSearch(
          "SEGUIMENTO",
          EptsReportUtils.map(
              this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

      composition = "I10 AND CD4-WITH-TBLAM AND SEGUIMENTO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 11 - Número de utentes elegíveis a pedido de TB-LAM com resultado de TB-LAM durante o mês */
  public CohortDefinition
      getNumberOfPatientsElegibleToTBLAMWithTBLamResultDuringReportingPeriodI11() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCd4Baixo =
        "startDate=${startDate-1m},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I10",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithLowCD4ResultDuringThePeriod10",
                ResumoMensalDAHQueries.getNumberOfPatientWithLowCD4ResultDuringThePeriod10()),
            mappingsCd4Baixo));

    definition.addSearch(
        "CD4-WITH-TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsElegibleToTBLAMWithTBLamResultDuringReportingPeriod11",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.TBLAM)),
            mappings));

    definition.setCompositionString("I10 AND CD4-WITH-TBLAM");

    return definition;
  }

  /** 12) Número de utentes com resultado de TB-LAM positivo durante o mês */
  public CohortDefinition
      getNumberOfPatientsElegibleToTBLAMWithTBLamResultPOSDuringReportingPeriod12(
          ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsElegibleToTBLAMWithTBLamResultPOSDuringReportingPeriod12");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName(
        "getNumberOfPatientsElegibleToTBLAMWithTBLamResultPOSDuringReportingPeriod12");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I11",
        EptsReportUtils.map(
            this.getNumberOfPatientsElegibleToTBLAMWithTBLamResultDuringReportingPeriodI11(),
            mappings));

    definition.addSearch(
        "CD4-TBLAM-POS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsElegibleToTBLAMWithTBLamResultPOSDuringReportingPeriod12",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.TBLAM_POS)),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "I11 AND CD4-TBLAM-POS AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "I11 AND CD4-TBLAM-POS AND RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "I11 AND CD4-TBLAM-POS AND ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I11 AND CD4-TBLAM-POS AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      definition.addSearch(
          "SEGUIMENTO",
          EptsReportUtils.map(
              this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

      composition = "I11 AND CD4-TBLAM-POS AND SEGUIMENTO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 13 - Número de utentes com CD4 baixo* com resultado de CrAg sérico durante o mês */
  public CohortDefinition getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI13(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCd4Baixo =
        "startDate=${startDate-1m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I10",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithLowCD4ResultDuringThePeriod10",
                ResumoMensalDAHQueries.getNumberOfPatientWithLowCD4ResultDuringThePeriod10()),
            mappingsCd4Baixo));

    definition.addSearch(
        "CD4-WITH-CRAG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithCragSericoResultDuringReportingPeriod13",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.CRAG_SORO)),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "I10 AND CD4-WITH-CRAG AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "I10 AND CD4-WITH-CRAG AND RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "I10 AND CD4-WITH-CRAG AND ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I10 AND CD4-WITH-CRAG AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      definition.addSearch(
          "SEGUIMENTO",
          EptsReportUtils.map(
              this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

      composition = "I10 AND CD4-WITH-CRAG AND SEGUIMENTO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 13 - Número de utentes com CD4 baixo* com resultado de CrAg sérico durante o mês */
  public CohortDefinition getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI13() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCd4Baixo =
        "startDate=${startDate-1m},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I10",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithLowCD4ResultDuringThePeriod10",
                ResumoMensalDAHQueries.getNumberOfPatientWithLowCD4ResultDuringThePeriod10()),
            mappingsCd4Baixo));

    definition.addSearch(
        "CD4-WITH-CRAG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithCragSericoResultDuringReportingPeriod13",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.CRAG_SORO)),
            mappings));

    definition.setCompositionString("I10 AND CD4-WITH-CRAG");

    return definition;
  }

  /** 14 - Número de utentes com CD4 baixo* com resultado de CrAg sérico positivo durante o mês */
  public CohortDefinition getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI14(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I13",
        EptsReportUtils.map(
            this.getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI13(), mappings));

    definition.addSearch(
        "CD4-SORO-POS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithCragSericoPOSResultDuringReportingPeriod14",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.CRAG_SORO_POS)),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "I13 AND CD4-SORO-POS AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "I13 AND CD4-SORO-POS AND RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "I13 AND CD4-SORO-POS AND ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I13 AND CD4-SORO-POS AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      definition.addSearch(
          "SEGUIMENTO",
          EptsReportUtils.map(
              this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

      composition = "I13 AND CD4-SORO-POS AND SEGUIMENTO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /** 14 - Número de utentes com CD4 baixo* com resultado de CrAg sérico positivo durante o mês */
  public CohortDefinition getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI14() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I13",
        EptsReportUtils.map(
            this.getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI13(), mappings));

    definition.addSearch(
        "CD4-SORO-POS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithCragSericoPOSResultDuringReportingPeriod14",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.CRAG_SORO_POS)),
            mappings));

    definition.setCompositionString("I13 AND CD4-SORO-POS");

    return definition;
  }

  /**
   * 15) Número de utentes com CrAg sérico positivo e com o resultado de CrAg no LCR registrado
   * durante o mês
   */
  public CohortDefinition getNumberOfPatientsWithCragLCRResultDuringReportingPeriodI15(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWithCragLCRResultDuringReportingPeriodI15");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientsWithCragLCRResultDuringReportingPeriodI15");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I14",
        EptsReportUtils.map(
            this.getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI14(), mappings));

    definition.addSearch(
        "CD4-LCR",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithCragLCRResultDuringReportingPeriodI15",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.CRAG_LCR)),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "I14 AND CD4-LCR AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "I14 AND CD4-LCR AND RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "I14 AND CD4-LCR AND ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I14 AND CD4-LCR AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      definition.addSearch(
          "SEGUIMENTO",
          EptsReportUtils.map(
              this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

      composition = "I14 AND CD4-LCR AND SEGUIMENTO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /**
   * 16) Número de utentes com CrAg sérico positivo que iniciaram tratamento preventivo de MCC
   * durante o mês
   */
  public CohortDefinition getNumberOfPatientsWithMCCTreatmentResultDuringReportingPeriodI16(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWithMCCTreatmentResultDuringReportingPeriodI16");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientsWithMCCTreatmentResultDuringReportingPeriodI16");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I14",
        EptsReportUtils.map(
            this.getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI14(), mappings));

    definition.addSearch(
        "MCC-PREVENTIVE-TREATMENT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithMCCTreatmentResultDuringReportingPeriodI16",
                ResumoMensalDAHQueries.findPatientsWhoInMCCTreatment(
                    MCCTreatment.MCC_PREVENTIVE_TREATMENT)),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "I14 AND MCC-PREVENTIVE-TREATMENT AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "I14 AND MCC-PREVENTIVE-TREATMENT AND RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "I14 AND MCC-PREVENTIVE-TREATMENT AND ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I14 AND MCC-PREVENTIVE-TREATMENT AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      definition.addSearch(
          "SEGUIMENTO",
          EptsReportUtils.map(
              this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

      composition = "I14 AND MCC-PREVENTIVE-TREATMENT AND SEGUIMENTO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /**
   * 17) Número de utentes com CrAg no LCR positivo que iniciaram tratamento de 1a escolha de MCC
   * durante o mês
   */
  public CohortDefinition
      getNumberOfPatientsWithCragLcrPosInitiatedMCCTreatmentResultDuringReportingPeriodI17(
          ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "NumberOfPatientsWithCragLcrPosInitiatedMCCTreatmentResultDuringReportingPeriodI17");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName(
        "getNumberOfPatientsWithCragLcrPosInitiatedMCCTreatmentResultDuringReportingPeriodI17");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I14",
        EptsReportUtils.map(
            this.getNumberOfPatientsWithCragSericoResultDuringReportingPeriodI14(), mappings));

    definition.addSearch(
        "CRAG-LCR-POS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithCragLcrPosInitiatedMCCTreatmentResultDuringReportingPeriodI17",
                ResumoMensalDAHQueries.findPatientsWhoHaveSpecificExamTestResults(
                    TypesOfExams.CRAG_LCR_POS)),
            mappings));

    definition.addSearch(
        "MCC-TREATMENT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithMCCTreatmentResultDuringReportingPeriodI16",
                ResumoMensalDAHQueries.findPatientsWhoInMCCTreatment(MCCTreatment.MCC_TREATMENT)),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "I14 AND CRAG-LCR-POS AND MCC-TREATMENT AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "I14 AND CRAG-LCR-POS AND MCC-TREATMENT AND RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "I14 AND CRAG-LCR-POS AND MCC-TREATMENT AND ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "I14 AND CRAG-LCR-POS AND MCC-TREATMENT AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      definition.addSearch(
          "SEGUIMENTO",
          EptsReportUtils.map(
              this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

      composition = "I14 AND CRAG-LCR-POS AND MCC-TREATMENT AND SEGUIMENTO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /**
   * 18) Número de utentes com Sarcoma de Kaposi e com indicação para o tratamento de quimioterapia
   * durante o mês
   */
  public CohortDefinition
      getNumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriodI18(
          ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "NumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriodI18");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName(
        "getNumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriodI18");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "SK",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriodI18",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriod18()),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "SK AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "SK AND RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "SK AND ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "SK AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      definition.addSearch(
          "SEGUIMENTO",
          EptsReportUtils.map(
              this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

      composition = "SK AND SEGUIMENTO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /**
   * 19) Número de utentes com sarcoma de kaposi que iniciaram Ciclo 1 de quimioterapia durante o
   * mês
   */
  public CohortDefinition getNumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19(
      ARTSituation disagregationType) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("getNumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "QUIMIOTHERAPHY",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19",
                ResumoMensalDAHQueries.findPatientsWhoInMCCTreatment(
                    MCCTreatment.QUIMIOTHERAPHY_CICLE_ONE)),
            mappings));

    if (disagregationType == ARTSituation.NEW_ENROLLED) {

      definition.addSearch(
          "NEW-ART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.NEW_ENROLLED),
              mappings));

      composition = "QUIMIOTHERAPHY AND NEW-ART";

    } else if (disagregationType == ARTSituation.RESTART) {

      definition.addSearch(
          "RESTART",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.RESTART),
              mappings));

      composition = "QUIMIOTHERAPHY AND RESTART";

    } else if (disagregationType == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.findPatientsNewlyEnrolledOnARTDisagregationRF26(ARTSituation.ACTIVE), mappings));

      composition = "QUIMIOTHERAPHY AND ACTIVE";

    } else if (disagregationType == ARTSituation.PREGNANT) {

      definition.addSearch(
          "PREGNANT", EptsReportUtils.map(this.getNumberOfPatientsWhoArePregnantRF29(), mappings));

      composition = "QUIMIOTHERAPHY AND PREGNANT";

    } else if (disagregationType == ARTSituation.SEGUIMENTO_DAH) {

      definition.addSearch(
          "SEGUIMENTO",
          EptsReportUtils.map(
              this.getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30(), mappings));

      composition = "QUIMIOTHERAPHY AND SEGUIMENTO";
    }

    definition.setCompositionString(composition);

    return definition;
  }

  /**
   * RF26, RF27, RF28 O sistema irá identificar todos os utentes novos inícios de TARV, para
   * desagregação dos indicadores 8 a 19
   */
  public CohortDefinition findPatientsNewlyEnrolledOnARTDisagregationRF26(
      ARTSituation artSituation) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsNewlyEnrolledOnARTDisagregationRF26");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsBack2Months =
        "startDate=${startDate-2m},endDate=${endDate},location=${location}";
    String composition = "";

    definition.setName("findPatientsNewlyEnrolledOnARTDisagregationRF26");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsMarkedAsPregnantInTheLast6MonthsBeforeEndDate",
                ResumoMensalDAHQueries.findPatientsMarkedAsPregnantInTheLast6MonthsBeforeEndDate()),
            mappings));

    definition.addSearch(
        "NEW-ON-ART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsRegisteredAsNewEnrolledARTDuringReportPeriod1",
                ResumoMensalDAHQueries.findPatientsARTSituation(ARTSituation.NEW_ENROLLED)),
            mappings));

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            resumoMensalCohortQueries
                .getPatientsWhoInitiatedTarvAtThisFacilityDuringCurrentMonthB1(),
            mappingsBack2Months));

    definition.addSearch(
        "RESTART-ART",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsRegisteredAsNewEnrolledARTDuringReportPeriod1",
                ResumoMensalDAHQueries.findPatientsARTSituation(ARTSituation.RESTART)),
            mappings));

    definition.addSearch("B3", EptsReportUtils.map(this.getSumB3(), mappingsBack2Months));

    if (artSituation == ARTSituation.NEW_ENROLLED) {

      composition = "(NEW-ON-ART OR B1) NOT PREGNANT";

    } else if (artSituation == ARTSituation.RESTART) {

      composition = "((RESTART-ART OR B3) NOT PREGNANT) NOT ((NEW-ON-ART OR B1) NOT PREGNANT)";

    } else if (artSituation == ARTSituation.ACTIVE) {

      definition.addSearch(
          "ACTIVE",
          EptsReportUtils.map(
              this.genericCohortQueries.generalSql(
                  "getNumberOfPatientsRegisteredAsNewEnrolledARTDuringReportPeriod1",
                  ResumoMensalDAHQueries.findPatientsARTSituation(ARTSituation.ACTIVE)),
              mappings));

      definition.addSearch(
          "B12",
          EptsReportUtils.map(
              resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12(),
              mappings));

      composition =
          "((ACTIVE OR B12) NOT (((RESTART-ART OR B3) NOT (NEW-ON-ART OR B1)) OR (NEW-ON-ART OR B1))) NOT PREGNANT";
    }
    definition.setCompositionString(composition);

    return definition;
  }

  /** Relatório Desagregação Utentes em Seguimento de DAH */
  public CohortDefinition getNumberOfPatientsInDAHExcludingPatientsMarkedAsSaidaRF30() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF30",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsInDAHExcludingPatientsWithSaidaDAH",
                ResumoMensalDAHQueries.findPatientsInDAHExcludingPatientsWithSaidaDAH()),
            mappings));

    definition.setCompositionString("RF30");

    return definition;
  }

  /** RF29 Relatório Desagregação Mulheres Grávidas HIV+ */
  public CohortDefinition getNumberOfPatientsWhoArePregnantRF29() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientsWithQuimiotheraphyCicleOneDuringPeriodI19");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF29",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "findPatientsMarkedAsPregnantInTheLast6MonthsBeforeEndDate",
                ResumoMensalDAHQueries.findPatientsMarkedAsPregnantInTheLast6MonthsBeforeEndDate()),
            mappings));

    definition.setCompositionString("RF29");

    return definition;
  }

  public CohortDefinition getSumB3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    final String mappingsBackThreeMonths = "endDate=${endDate-3m+1d},location=${location}";

    definition.addSearch("ABANDONED", EptsReportUtils.map(this.getAbandonedPatient(), mappings));

    definition.addSearch(
        "SUSPEND",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "SUSPEND", ResumoMensalDAHQueries.getPatientsWhoSuspendTratmentLastMonth()),
            mappingsBackThreeMonths));

    definition.addSearch(
        "PICKUP",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PICKUP", ResumoMensalQueries.getPatientsWhoHaveDrugPickup()),
            mappings));

    definition.setCompositionString("(ABANDONED OR SUSPEND) AND PICKUP ");

    return definition;
  }

  public CohortDefinition getAbandonedPatient() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Number Of Patients Transferred In From Other Health Facilities");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate-3m+1d},location=${location}";

    definition.addSearch(
        "ABANDONED",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "ABANDONED", ResumoMensalDAHQueries.getPatientsWhoAbandonedTratmentToBeExclude()),
            mappings));

    definition.addSearch(
        "SUSPEND",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "SUSPEND", ResumoMensalDAHQueries.getPatientsWhoSuspendTratmentLastMonth()),
            mappings));

    definition.addSearch(
        "DIED",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "DIED", ResumoMensalDAHQueries.getPatientsWhoDiedTratmentLastMonth()),
            mappings));

    definition.addSearch(
        "TRFOUT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TRFOUT",
                ResumoMensalDAHQueries.getPatientsTransferredFromAnotherHealthFacilityLastMonth()),
            mappings));

    definition.setCompositionString("ABANDONED NOT (SUSPEND OR DIED OR TRFOUT)");

    return definition;
  }
}
