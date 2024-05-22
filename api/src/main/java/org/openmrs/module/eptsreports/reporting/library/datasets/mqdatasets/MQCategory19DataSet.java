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
        "CAT191TOTALDENOMINATOR",
        "19.1: % pedido de teste molecular Xpert para os adultos HIV+ (≥ 15 anos): Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findPatientsWithGeneXpertRequestExcludingTransferedOutDenominator(),
                "CAT191TOTALDENOMINATOR",
                mappings),
            mappings),
        "ageOnPresuntiveTB=15+");

    dataSetDefinition.addColumn(
        "CAT191TOTALNUMERATOR",
        "19.1: % pedido de teste molecular Xpert para os adultos HIV+ (≥ 15 anos): Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findPatientsWithGeneXpertRequestExcludingTransferedOutNumerator(),
                "CAT191TOTALNUMERATOR",
                mappings),
            mappings),
        "ageOnPresuntiveTB=15+");

    // 19.2:pedido de teste molecular Xpert para crianças HIV+ (0-14 anos)

    dataSetDefinition.addColumn(
        "CAT192TOTALDENOMINATOR",
        "19.2: % pedido de teste molecular Xpert para crianças HIV+ (0-14 anos): Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findPatientsWithGeneXpertRequestExcludingTransferedOutDenominator(),
                "CAT192TOTALDENOMINATOR",
                mappings),
            mappings),
        "ageOnPresuntiveTB=15-");

    dataSetDefinition.addColumn(
        "CAT192TOTALNUMERATOR",
        "19.2: % pedido de teste molecular Xpert para crianças HIV+ (0-14 anos): Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.category19CohortQueries
                    .findPatientsWithGeneXpertRequestExcludingTransferedOutNumerator(),
                "CAT192TOTALNUMERATOR",
                mappings),
            mappings),
        "ageOnPresuntiveTB=15-");
  }
}
