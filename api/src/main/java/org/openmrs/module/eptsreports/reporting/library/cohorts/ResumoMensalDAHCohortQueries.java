package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.metadata.HivMetadata;
import org.openmrs.module.eptsreports.metadata.TbMetadata;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalDAHQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalDAHCohortQueries {

  private HivMetadata hivMetadata;
  private TbMetadata tbMetadata;
  private GenericCohortQueries genericCohortQueries;
  @Autowired private TxNewCohortQueries txNewCohortQueries;
  @Autowired ResumoMensalCohortQueries resumoMensalCohortQueries;

  @Autowired
  public ResumoMensalDAHCohortQueries(
      HivMetadata hivMetadata, TbMetadata tbMetadata, GenericCohortQueries genericCohortQueries) {
    this.hivMetadata = hivMetadata;
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
                ResumoMensalDAHQueries
                    .getNumberOfPatientsRegisteredAsNewEnrolledARTDuringReportPeriod1()),
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
                ResumoMensalDAHQueries
                    .getNumberOfPatientsRegisteredAsReinitiatedARTDuringReportPeriod2()),
            mappings));

    definition.addSearch("B3", EptsReportUtils.map(resumoMensalCohortQueries.getSumB3(), mappings));

    definition.setCompositionString("DAHPERIOD AND (INICIOTARV OR B3)");

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
                ResumoMensalDAHQueries
                    .getNumberOfPatientsRegisteredAsActiveInARTDuringReportPeriod3()),
            mappings));

    definition.addSearch(
        "B12",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHLastMonthB12(),
            mappings));

    definition.setCompositionString("DAHPERIOD AND (EMTARV OR B12)");

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
  public CohortDefinition getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getTotalNumberPatientsInDAHSixMonthsCoorteI7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CD4",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsRequestingCD4ScreeningDuringThePeriodI8",
                ResumoMensalDAHQueries.getNumberOfPatientsRequestingCD4ScreeningDuringThePeriod8()),
            mappings));

    definition.setCompositionString("CD4");

    return definition;
  }

  /** 9 - Número de utentes com resultado de CD4 de rastreio disponivel durante o mês */
  public CohortDefinition getNumberOfPatientWithCD4ResultDuringThePeriodI9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithCD4ResultDuringThePeriodI9");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientWithCD4ResultDuringThePeriodI9");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CD4-RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithCD4ResultDuringThePeriod9",
                ResumoMensalDAHQueries.getNumberOfPatientWithCD4ResultDuringThePeriod9()),
            mappings));

    definition.setCompositionString("CD4-RESULT");

    return definition;
  }

  /** 10 - Número de utentes com resultado de CD4 baixo* durante o mês */
  public CohortDefinition getNumberOfPatientWithLowCD4ResultDuringThePeriodI10() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CD4-BAIXO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientWithLowCD4ResultDuringThePeriod10",
                ResumoMensalDAHQueries.getNumberOfPatientWithLowCD4ResultDuringThePeriod10()),
            mappings));

    definition.setCompositionString("CD4-BAIXO");

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
                ResumoMensalDAHQueries
                    .getNumberOfPatientsElegibleToTBLAMWithTBLamResultDuringReportingPeriod11()),
            mappings));

    definition.setCompositionString("I10 AND CD4-WITH-TBLAM");

    return definition;
  }

  /** 12) Número de utentes com resultado de TB-LAM positivo durante o mês */
  public CohortDefinition
      getNumberOfPatientsElegibleToTBLAMWithTBLamResultPOSDuringReportingPeriod12() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfPatientWithLowCD4ResultDuringThePeriodI10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I11",
        EptsReportUtils.map(
            this.getNumberOfPatientsElegibleToTBLAMWithTBLamResultDuringReportingPeriodI11(),
            mappings));

    definition.addSearch(
        "CD4-WITH-TBLAM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsElegibleToTBLAMWithTBLamResultPOSDuringReportingPeriod12",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsElegibleToTBLAMWithTBLamResultPOSDuringReportingPeriod12()),
            mappings));

    definition.setCompositionString("I11 AND CD4-WITH-TBLAM");

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
                ResumoMensalDAHQueries
                    .getNumberOfPatientsWithCragSericoResultDuringReportingPeriod13()),
            mappings));

    definition.setCompositionString("I10 AND CD4-WITH-CRAG");

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
        "CD4-CRAG-POS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "getNumberOfPatientsWithCragSericoPOSResultDuringReportingPeriod14",
                ResumoMensalDAHQueries
                    .getNumberOfPatientsWithCragSericoPOSResultDuringReportingPeriod14()),
            mappings));

    definition.setCompositionString("I13 AND CD4-WITH-CRAG");

    return definition;
  }
}
