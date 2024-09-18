package org.openmrs.module.eptsreports.reporting.library.datasets.mqdatasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory13Section1CohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory13DataSetSectionI extends MQAbstractDataSet {

  @Autowired private MQCategory13Section1CohortQueries mQCategory13Section1CohortQueries;
  private final boolean EXCLUDE_TB_ACTIVE_DIAGNOSTIC = true;

  public void constructTMqDatset(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    dataSetDefinition.addColumn(
        "CAT13CV15PLUSDENOMINATOR",
        "13.1: Adultos (15/+anos) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Denominador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory13Section1CohortQueries.findDenominatorCategory13SectionIB(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13CV15PLUSDENOMINATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13CV15PLUSNUMERATOR",
        "13.1: Adultos (15/+anos) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory13Section1CohortQueries.findFinalNumeratorCategory13SectionIC(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13CV15PLUSNUMERATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13CV04DENOMINATOR",
        "13.6: Crianças (0-4 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory13Section1CohortQueries
                    .findDenominatorCategory13SectionIBChildrens(),
                "CAT13CV04DENOMINATOR",
                mappings),
            mappings),
        "age=0-4");

    dataSetDefinition.addColumn(
        "CAT13CV04NUMERATOR",
        "13.6: Crianças (0-4 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory13Section1CohortQueries
                    .findFinalNumeratorCategory13SectionICChildrens(),
                "CAT13CV04NUMERATOR",
                mappings),
            mappings),
        "age=0-4");

    dataSetDefinition.addColumn(
        "CAT13CV59DENOMINATOR",
        "13.7: Crianças (5-9 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory13Section1CohortQueries
                    .findDenominatorCategory13SectionIBChildrens(),
                "CAT13CV59DENOMINATOR",
                mappings),
            mappings),
        "age=5-9");

    dataSetDefinition.addColumn(
        "CAT13CV59NUMERATOR",
        "13.7: Crianças (5-9 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory13Section1CohortQueries
                    .findFinalNumeratorCategory13SectionICChildrens(),
                "CAT13CV59NUMERATOR",
                mappings),
            mappings),
        "age=5-9");

    dataSetDefinition.addColumn(
        "CAT13CV1014DENOMINATOR",
        "13.8: Crianças (10-14 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                mQCategory13Section1CohortQueries.findDenominatorCategory13SectionIBChildrens(),
                "CAT13CV1014DENOMINATOR",
                mappings),
            mappings),
        "age=10-14");

    dataSetDefinition.addColumn(
        "CAT13CV1014NUMERATOR",
        "13.8: Crianças (10-14 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory13Section1CohortQueries
                    .findFinalNumeratorCategory13SectionICChildrens(),
                "CAT13CV1014NUMERATOR",
                mappings),
            mappings),
        "age=10-14");

    dataSetDefinition.addColumn(
        "CAT13_1_PEDIDOCV_DENOMINATOR",
        "13.1 (13.1-1ª Linha + 13.4-2ª Linha): # de adultos (15/+anos) na 1ª ou 2ª linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Denominador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory13Section1CohortQueries.findDenominatorPedidoCVRF16_Indicator_13_1(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13_1_PEDIDOCV_DENOMINATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13_1_PEDIDOCV_NUMERATOR",
        "13.1 (13.1-1ª Linha + 13.4-2ª Linha): # de adultos (15/+anos) na 1ª ou 2ª linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Numerador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory13Section1CohortQueries.findNumeratorPedidoCVRF16_Indicator_13_1(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13_1_PEDIDOCV_NUMERATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13_8_PEDIDOCV_DENOMINATOR",
        "13.8 (13.8-1ª Linha + 13.13-2ª Linha): # de crianças na 1a linha (10-14 anos de idade) ou 2ª linha (0-14 anos) de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                mQCategory13Section1CohortQueries.findDenominatorPedidoCVRF24_Indicator_13_8(),
                "CAT13_8_PEDIDOCV_DENOMINATOR",
                mappings),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "CAT13_8_PEDIDOCV_NUMERATOR",
        "13.8 (13.8-1ª Linha + 13.13-2ª Linha): # de crianças na 1a linha (10-14 anos de idade) ou 2ª linha (0-14 anos) de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mQCategory13Section1CohortQueries.findNumeratorPedidoCVRF25_Indicator_13_8(),
                "CAT13_8_PEDIDOCV_NUMERATOR",
                mappings),
            mappings),
        "");
  }
}
