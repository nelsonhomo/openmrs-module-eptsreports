package org.openmrs.module.eptsreports.reporting.library.datasets.mqdatasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory19CohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory19DataSet extends MQAbstractDataSet {
  @Autowired private MQCategory19CohortQueries category19CohortQueries;

  public void constructTMqDatset(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    // 19.1:pedido de teste molecular Xpert para os adultos HIV+ (≥ 15 anos)
    dataSetDefinition.addColumn(
        "CAT191TOTALDENOMINATORADULT",
        "19.1: %  de adultos (>=15 anos) presuntivos de TB com pedido de teste molecular (Xpert/Truenat) na data da 1ª consulta: Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findPatientsWithGeneXpertRequestExcludingTransferedOutDenominator(),
                "CAT191TOTALDENOMINATORADULT",
                mappings),
            mappings),
        "ageOnPresuntiveTB=15+");

    dataSetDefinition.addColumn(
        "CAT191TOTALNUMERATORADULT",
        "19.1: % de adultos (>=15 anos) presuntivos de TB com pedido de teste molecular (Xpert/Truenat) na data da 1ª consulta: Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findPatientsWithGeneXpertRequestExcludingTransferedOutNumerator(),
                "CAT191TOTALNUMERATORADULT",
                mappings),
            mappings),
        "ageOnPresuntiveTB=15+");

    dataSetDefinition.addColumn(
        "CAT194TOTALDENOMINATORCHILDREN",
        "19.4: %  de crianças (0-14 anos) presuntivos de TB com pedido de teste molecular (Xpert/Truenat) na data da 1ª consulta:  Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findPatientsWithGeneXpertRequestExcludingTransferedOutDenominator(),
                "CAT194TOTALDENOMINATORCHILDREN",
                mappings),
            mappings),
        "ageOnPresuntiveTB=0-14");
    dataSetDefinition.addColumn(
        "CAT194TOTALNUMERATORCHILDREN",
        "19.4: %  de crianças (0-14 anos) presuntivos de TB com pedido de teste molecular (Xpert/Truenat) na data da 1ª consulta:  Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findPatientsWithGeneXpertRequestExcludingTransferedOutNumerator(),
                "CAT194TOTALNUMERATORCHILDREN",
                mappings),
            mappings),
        "ageOnPresuntiveTB=0-14");

    dataSetDefinition.addColumn(
        "CAT192TOTALDENOMINATORADULT",
        "19.2: % de adultos (>=15 anos) HIV+ presuntivos de TB que receberam resultado do teste molecular (Xpert/Truenat) dentro de 7 dias após o pedido: Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findAllPatientsWithGeneXpertResultOnTheSameDateGeneXpertRequestDenominator(),
                "CAT192TOTALDENOMINATORADULT",
                mappings),
            mappings),
        "ageOnGeneXpertRequest=15+");

    dataSetDefinition.addColumn(
        "CAT192TOTALNUMERATORADULT",
        "19.2: % de adultos (>=15 anos) HIV+ presuntivos de TB que receberam resultado do teste molecular (Xpert/Truenat) dentro de 7 dias após o pedido: Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findAllPatientsWithGeneXpertResultOnTheSameDateGeneXpertRequestNumerator(),
                "CAT192TOTALNUMERATORADULT",
                mappings),
            mappings),
        "ageOnGeneXpertRequest=15+");

    dataSetDefinition.addColumn(
        "CAT195TOTALDENOMINATORCHILDREN",
        "19.5: % de crianças (0-14 anos) HIV+ presuntivos de TB que receberam resultado do teste molecular (Xpert/Truenat) dentro de 7 dias após o pedido:  Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findAllPatientsWithGeneXpertResultOnTheSameDateGeneXpertRequestDenominator(),
                "CAT195TOTALDENOMINATORCHILDREN",
                mappings),
            mappings),
        "ageOnGeneXpertRequest=0-14");

    dataSetDefinition.addColumn(
        "CAT195TOTALNUMERATORCHILDREN",
        "19.5: % de crianças (0-14 anos) HIV+ presuntivos de TB que receberam resultado do teste molecular (Xpert/Truenat) dentro de 7 dias após o pedido:  Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findAllPatientsWithGeneXpertResultOnTheSameDateGeneXpertRequestNumerator(),
                "CAT195TOTALNUMERATORADULT",
                mappings),
            mappings),
        "ageOnGeneXpertRequest=0-14");

    dataSetDefinition.addColumn(
        "CAT193TOTALDENOMINATORADULT",
        "19.3: % de adultos (>=15 anos) HIV+ diagnosticados com TB e que iniciaram tratamento de TB na data do diagnóstico de TB: Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries.findAllPatientsWhoHaveTBDiagnosticActiveDenominator(),
                "CAT192TOTALDENOMINATORADULT",
                mappings),
            mappings),
        "ageOnTBDiagnostic=15+");

    dataSetDefinition.addColumn(
        "CAT193TOTALNUMERATORADULT",
        "19.3: % de adultos (>=15 anos) HIV+ diagnosticados com TB e que iniciaram tratamento de TB na data do diagnóstico de TB: Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries.findAllPatientsWhoHaveTBDiagnosticActiveNumerator(),
                "CAT192TOTALNUMERATORADULT",
                mappings),
            mappings),
        "ageOnTBDiagnostic=15+");

    dataSetDefinition.addColumn(
        "CAT196TOTALDENOMINATORCHILDREN",
        "19.6: % de crianças (0-14 anos) HIV+ diagnosticados com TB e que iniciaram tratamento de TB na data do diagnóstico de TB:   Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries.findAllPatientsWhoHaveTBDiagnosticActiveDenominator(),
                "CAT196TOTALDENOMINATORCHILDREN",
                mappings),
            mappings),
        "ageOnTBDiagnostic=0-14");

    dataSetDefinition.addColumn(
        "CAT196TOTALNUMERATORCHILDREN",
        "19.6: % de crianças (0-14 anos) HIV+ diagnosticados com TB e que iniciaram tratamento de TB na data do diagnóstico de TB:   Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries.findAllPatientsWhoHaveTBDiagnosticActiveNumerator(),
                "CAT196TOTALNUMERATORCHILDREN",
                mappings),
            mappings),
        "ageOnTBDiagnostic=0-14");
  }
}
