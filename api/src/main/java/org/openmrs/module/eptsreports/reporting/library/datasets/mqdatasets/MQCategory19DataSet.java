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
        "19.1: % pedido de teste molecular Xpert para os adultos HIV+ (≥ 15 anos): Denominador",
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
        "19.1: % pedido de teste molecular Xpert para os adultos HIV+ (≥ 15 anos): Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findPatientsWithGeneXpertRequestExcludingTransferedOutNumerator(),
                "CAT191TOTALNUMERATORADULT",
                mappings),
            mappings),
        "ageOnPresuntiveTB=15+");

    // 19.2:pedido de teste molecular Xpert para crianças HIV+ (0-14 anos)

    dataSetDefinition.addColumn(
        "CAT192TOTALDENOMINATORCHILDREN",
        "19.2: % pedido de teste molecular Xpert para crianças HIV+ (0-14 anos): Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findPatientsWithGeneXpertRequestExcludingTransferedOutDenominator(),
                "CAT192TOTALDENOMINATORCHILDREN",
                mappings),
            mappings),
        "ageOnPresuntiveTB=0-14");

    dataSetDefinition.addColumn(
        "CAT192TOTALNUMERATORCHILDREN",
        "19.2: % pedido de teste molecular Xpert para crianças HIV+ (0-14 anos): Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findPatientsWithGeneXpertRequestExcludingTransferedOutNumerator(),
                "CAT192TOTALNUMERATORCHILDREN",
                mappings),
            mappings),
        "ageOnPresuntiveTB=0-14");

    // 19.3: % entrega do resultado do teste molecular Xpert para os adultos HIV+ (≥ 15 anos):

    dataSetDefinition.addColumn(
        "CAT193TOTALDENOMINATORADULT",
        "19.3: % entrega do resultado do teste molecular Xpert para os adultos HIV+ (≥ 15 anos): Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findAllPatientsWithGeneXpertResultOnTheSameDateGeneXpertRequestDenominator(),
                "CAT192TOTALDENOMINATORADULT",
                mappings),
            mappings),
        "ageOnGeneXpertRequest=15+");

    dataSetDefinition.addColumn(
        "CAT193TOTALNUMERATORADULT",
        "19.3: % entrega do resultado do teste molecular Xpert para os adultos HIV+ (≥ 15 anos): Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findAllPatientsWithGeneXpertResultOnTheSameDateGeneXpertRequestNumerator(),
                "CAT192TOTALNUMERATORADULT",
                mappings),
            mappings),
        "ageOnGeneXpertRequest=15+");

    // 19.4: % entrega do resultado do teste molecular Xpert para crianças HIV+ (0-14 anos):

    dataSetDefinition.addColumn(
        "CAT194TOTALDENOMINATORCHILDREN",
        "19.4: % entrega do resultado do teste molecular Xpert para crianças HIV+ (0-14 anos):  Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findAllPatientsWithGeneXpertResultOnTheSameDateGeneXpertRequestDenominator(),
                "CAT194TOTALDENOMINATORCHILDREN",
                mappings),
            mappings),
        "ageOnGeneXpertRequest=0-14");

    dataSetDefinition.addColumn(
        "CAT194TOTALNUMERATORCHILDREN",
        "19.4: % entrega do resultado do teste molecular Xpert para crianças HIV+ (0-14 anos):  Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findAllPatientsWithGeneXpertResultOnTheSameDateGeneXpertRequestNumerator(),
                "CAT194TOTALNUMERATORCHILDREN",
                mappings),
            mappings),
        "ageOnGeneXpertRequest=0-14");

    // 19.5: % avaliação do início atempado do Tratamento de TB para os adultos HIV+ (≥ 15 anos):

    dataSetDefinition.addColumn(
        "CAT195TOTALDENOMINATORADULT",
        "19.5: % avaliação do início atempado do Tratamento de TB para os adultos HIV+ (≥ 15 anos):  Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries.findAllPatientsWhoHaveTBDiagnosticActiveDenominator(),
                "CAT195TOTALDENOMINATORADULT",
                mappings),
            mappings),
        "ageOnTBDiagnostic=15+");

    dataSetDefinition.addColumn(
        "CAT195TOTALNUMERATORADULT",
        "19.5: % avaliação do início atempado do Tratamento de TB para os adultos HIV+ (≥ 15 anos):  Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries.findAllPatientsWhoHaveTBDiagnosticActiveNumerator(),
                "CAT195TOTALNUMERATORADULT",
                mappings),
            mappings),
        "ageOnTBDiagnostic=15+");

    // 19.6:  % avaliação do início atempado do Tratamento de TB para crianças HIV+ (0-14 anos):

    dataSetDefinition.addColumn(
        "CAT196TOTALDENOMINATORCHILDREN",
        "19.6: % avaliação do início atempado do Tratamento de TB para crianças HIV+ (0-14 anos):   Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findAllPatientsWithGeneXpertResultOnTheSameDateGeneXpertRequestDenominator(),
                "CAT196TOTALDENOMINATORCHILDREN",
                mappings),
            mappings),
        "ageOnTBDiagnostic=0-14");

    dataSetDefinition.addColumn(
        "CAT196TOTALNUMERATORCHILDREN",
        "19.6: % avaliação do início atempado do Tratamento de TB para crianças HIV+ (0-14 anos):   Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findAllPatientsWithGeneXpertResultOnTheSameDateGeneXpertRequestNumerator(),
                "CAT196TOTALNUMERATORCHILDREN",
                mappings),
            mappings),
        "ageOnTBDiagnostic=0-14");
  }
}
