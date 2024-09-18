package org.openmrs.module.eptsreports.reporting.library.datasets.mqdatasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory13P3CohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory13P3DataSet extends MQAbstractDataSet {

  @Autowired private MQCategory13P3CohortQueries mqCohortQueries13_3;
  private final boolean EXCLUDE_TB_ACTIVE_DIAGNOSTIC = true;

  public void constructTMqDatset(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_2_DENOMINATOR",
        "13.2: Adultos (15/+anos) na 1a linha de TARV que receberam o resultado da CV entre o sexto e o nono mês após início do TARV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13Denominador(),
                "CAT13_PART_3_13_2_DENOMINATOR",
                mappings),
            mappings),
        "ageMqEndRevisionDate=15+");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_2_NUMERATOR",
        "13.2: Adultos (15/+anos) na 1a linha de TARV que receberam o resultado da CV entre o sexto e o nono mês após início do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13Numerador(),
                "CAT13_PART_3_13_2_NUMERATOR",
                mappings),
            mappings),
        "ageMqEndRevisionDate=15+");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_9_DENOMINATOR",
        "13.9: Crianças  (0-4 anos de idade) na 1a linha de TARV que receberam o resultado da "
            + "Carga Viral entre o sexto e o nono mês após o início do TARV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13DenominadorChildrens(),
                "CAT13_PART_3_13_9_DENOMINATOR",
                mappings),
            mappings),
        "ageMqEndRevisionDate=0-4");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_9_NUMERATOR",
        "13.9: Crianças  (0-4 anos de idade) na 1a linha de TARV que receberam o resultado da "
            + "Carga Viral entre o sexto e o nono mês após o início do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13NumeradorChildrens(),
                "CAT13_PART_3_13_9_NUMERATOR",
                mappings),
            mappings),
        "ageMqEndRevisionDate=0-4");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_10_DENOMINATOR",
        "13.10: Crianças  (5-9 anos de idade) na 1a linha de TARV que receberam o resultado da "
            + "Carga Viral entre o sexto e o nono mês após o início do TARV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13DenominadorChildrens(),
                "CAT13_PART_3_13_10_DENOMINATOR",
                mappings),
            mappings),
        "ageMqEndRevisionDate=5-9");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_10_NUMERATOR",
        "13.10: Crianças  (5-9 anos de idade) na 1a linha de TARV que receberam o resultado da "
            + "Carga Viral entre o sexto e o nono mês após o início do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13NumeradorChildrens(),
                "CAT13_PART_3_13_10_NUMERATOR",
                mappings),
            mappings),
        "ageMqEndRevisionDate=5-9");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_11_DENOMINATOR",
        "13.11: Crianças  (10-14 anos de idade) na 1a linha de TARV que receberam o resultado da "
            + "Carga Viral entre o sexto e o nono mês após o início do TARV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13DenominadorChildrens(),
                "CAT13_PART_3_13_11_DENOMINATOR",
                mappings),
            mappings),
        "ageMqEndRevisionDate=10-14");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_11_NUMERATOR",
        "13.11: Crianças  (10-14 anos de idade) na 1a linha de TARV que receberam o resultado da"
            + " Carga Viral entre o sexto e o nono mês após o início do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13NumeradorChildrens(),
                "CAT13_PART_3_13_11_NUMERATOR",
                mappings),
            mappings),
        "ageMqEndRevisionDate=10-14");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_5_DENOMINATOR",
        "13.5: Adultos (15/+anos) na 2a linha de TARV que receberam o resultado da CV entre o "
            + "sexto e o nono mês após o início da 2a linha de TARV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInSecondLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_3_Denominador_13_5(
                        EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13_PART_3_13_5_DENOMINATOR",
                mappings),
            mappings),
        "ageOnEndInclusionDate=15PlusSecondLine");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_5_NUMERATOR",
        "13.5: Adultos (15/+anos) na 2a linha de TARV que receberam o resultado da CV entre "
            + "o sexto e o nono mês após o início da 2a linha de TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInSecondLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_3_Numerador_13_5(
                        EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13_PART_3_13_5_NUMERATOR",
                mappings),
            mappings),
        "ageOnEndInclusionDate=15PlusSecondLine");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_14_DENOMINATOR",
        "13.14: Crianças  na 2a linha de TARV que receberam o resultado da Carga Viral entre o "
            + "sexto e o nono mês após o início da 2a linha de TARV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInSecondLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_3_Denominador_13_5(
                        EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13_PART_3_13_14_DENOMINATOR",
                mappings),
            mappings),
        "ageOnEndInclusionDate=2-14SL");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_14_NUMERATOR",
        "13.14: Crianças  na 2a linha de TARV que receberam o resultado da Carga Viral entre o "
            + "sexto e o nono mês após o início da 2a linha de TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3
                    .findPatientsInSecondLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_3_Numerador_13_5(
                        EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13_PART_3_13_14_NUMERATOR",
                mappings),
            mappings),
        "ageOnEndInclusionDate=2-14SL");

    dataSetDefinition.addColumn(
        "CAT13_5_TBHIV_DENOMINATOR",
        "13.5: % de adultos (15/+anos) coinfectados TB/HIV com resultado de CV registado na FM - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3.findDenominatorTBHIVIndicator13_5(),
                "CAT13_5_TBHIV_DENOMINATOR",
                mappings),
            mappings),
        "ageOnEndInclusionDate=AdultsOr15PlusSecondLine");

    dataSetDefinition.addColumn(
        "CAT13_5_TBHIV_NUMERATOR",
        "13.5: % de adultos (15/+anos) coinfectados TB/HIV com resultado de CV registado na FM - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3.findNumeratorTBHIVIndicator13_5(),
                "CAT13_5_TBHIV_NUMERATOR",
                mappings),
            mappings),
        "ageOnEndInclusionDate=AdultsOr15PlusSecondLine");

    dataSetDefinition.addColumn(
        "CAT13_14_TBHIV_DENOMINATOR",
        "13.14: % de crianças (0-14 anos) coinfectados TB/HIV com resultado de CV registado na FM - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3.findDenominatorTBHIVIndicator13_5(),
                "CAT13_14_TBHIV_DENOMINATOR",
                mappings),
            mappings),
        "ageOnEndInclusionDate=0-14SL");

    dataSetDefinition.addColumn(
        "CAT13_14_TBHIV_NUMERATOR",
        "13.14: % de crianças (0-14 anos) coinfectados TB/HIV com resultado de CV registado na FM - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3.findNumeratorTBHIVIndicator13_5(),
                "CAT13_14_TBHIV_NUMERATOR",
                mappings),
            mappings),
        "ageOnEndInclusionDate=0-14SL");

    dataSetDefinition.addColumn(
        "CAT13_2RESULTADOCV_DENOMINATOR",
        "13.2 (13.2-1ª Linha + 13.5-2ª Linha): # de adultos (15/+anos) na 1a ou 2ª linha de TARV ou mudança de regime de 1ª linha - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3.findDenominatorResultadoCVRF34_Indicator_13_2(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13_2RESULTADOCV_DENOMINATOR",
                mappings),
            mappings),
        "ageMqEndRevisionDate=15+");

    dataSetDefinition.addColumn(
        "CAT13_2RESULTADOCV_NUMERATOR",
        "13.2 (13.2-1ª Linha + 13.5-2ª Linha): # de adultos (15/+anos) na 1a ou 2ª linha de TARV ou mudança de regime de 1ª linha - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3.findNumeratorResultadoCVRF34_Indicator_13_2(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13_2RESULTADOCV_NUMERATOR",
                mappings),
            mappings),
        "ageMqEndRevisionDate=15+");

    dataSetDefinition.addColumn(
        "CAT13_11RESULTADOCV_DENOMINATOR",
        "13.11 (13.11-1ª Linha + 13.14-2ª Linha): # de crianças na 1a linha de TARV ou mudança de regime de 1ª linha (10-14 anos de idade) ou 2ª Linha TARV (0-14 anos de idade) que receberam o resultado da CV entre o sexto e o nono mês após início do TARV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3.findDenominatorResultadoCVRF40_Indicator_13_11(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13_11RESULTADOCV_DENOMINATOR",
                mappings),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "CAT13_11RESULTADOCV_NUMERATOR",
        "13.11 (13.11-1ª Linha + 13.14-2ª Linha): # de crianças na 1a linha de TARV ou mudança de regime de 1ª linha (10-14 anos de idade) ou 2ª Linha TARV (0-14 anos de idade) que receberam o resultado da CV entre o sexto e o nono mês após início do TARV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCohortQueries13_3.findNumeratorResultadoCVRF40_Indicator_13_11(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13_11RESULTADOCV_NUMERATOR",
                mappings),
            mappings),
        "");
  }
}
