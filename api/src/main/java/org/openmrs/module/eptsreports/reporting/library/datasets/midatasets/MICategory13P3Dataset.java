package org.openmrs.module.eptsreports.reporting.library.datasets.midatasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.mi.MICategory13P3CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.mqdatasets.MQAbstractDataSet;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MICategory13P3Dataset extends MQAbstractDataSet {

  @Autowired private MICategory13P3CohortQueries mICategory13P3CohortQueries;
  private final boolean EXCLUDE_TB_ACTIVE_DIAGNOSTIC = true;

  public void constructTMiDatset(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    dataSetDefinition.addColumn(
        "CAT13_CV_13-2_13-5_DENOMINATOR",
        "13.2: # de adultos (15/+anos) em TARV (1a ou 2ª linha) 13.2-1ª Linha + 13.5-2ª Linha Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findAdultsPatientsWithViralLoadResultInFirstOrSecondLineTARVDenominator13_2Plus13_5Denominator(),
                "CAT13_CV_13-2_13-5_DENOMINATOR",
                mappings),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "CAT13_CV_13-2_13-5_NUMERATOR",
        "13.2: # de adultos (15/+anos) em TARV (1a ou 2ª linha) 13.2-1ª Linha + 13.5-2ª Linha Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findAdultsPatientsWithViralLoadResultInFirstOrSecondLineTARVDenominator13_2Plus13_5Numerator(),
                "CAT13_CV_13-2_13-5_NUMERATOR",
                mappings),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "CAT13_CV_13-11_13-14_DENOMINATOR",
        "13.11: # de crianças (10-14 anos de idade) na 1ª(13.11) ou 2ª(13.14) linha de TARV que recebeu o resultado da Carga Viral entre o sexto e o nono mês após início do TARV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findAdultsPatientsWithViralLoadResultInFirstOrSecondLineTARVDenominator13_11Plus13_14Denominator(),
                "CAT13_CV_13-11_13-14_DENOMINATOR",
                mappings),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "CAT13_CV_13-11_13-14_NUMERATOR",
        "13.11: # de crianças (10-14 anos de idade) na 1ª(13.11) ou 2ª(13.14) linha de TARV que recebeu o resultado da Carga Viral entre o sexto e o nono mês após início do TARV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findAdultsPatientsWithViralLoadResultInFirstOrSecondLineTARVDenominator13_11Plus13_14Numerator(),
                "CAT13_CV_13-11_13-14_NUMERATOR",
                mappings),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_2_DENOMINATOR",
        "13.2: Adultos (15/+anos) na 1a linha de TARV que receberam o resultado da CV entre o sexto e o nono mês após início do TARV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13Denominador(
                        EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13_PART_3_13_2_DENOMINATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=15+");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_2_NUMERATOR",
        "13.2: Adultos (15/+anos) na 1a linha de TARV que receberam o resultado da CV entre o sexto e o nono mês após início do TARV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13Numerador(),
                "CAT13_PART_3_13_2_NUMERATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=15+");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_9_DENOMINATOR",
        "13.9: Crianças  (0-4 anos de idade) na 1a linha de TARV que receberam o resultado da "
            + "Carga Viral entre o sexto e o nono mês após o início do TARV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_9Denominador(),
                "CAT13_PART_3_13_9_DENOMINATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=0-4");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_9_NUMERATOR",
        "13.9: Crianças  (0-4 anos de idade) na 1a linha de TARV que receberam o resultado da "
            + "Carga Viral entre o sexto e o nono mês após o início do TARV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_9Numerador(),
                "CAT13_PART_3_13_9_NUMERATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=0-4");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_10_DENOMINATOR",
        "13.10: Crianças  (5-9 anos de idade) na 1a linha de TARV que receberam o resultado da "
            + "Carga Viral entre o sexto e o nono mês após o início do TARV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_10Denominador(),
                "CAT13_PART_3_13_10_DENOMINATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=5-9");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_10_NUMERATOR",
        "13.10: Crianças  (5-9 anos de idade) na 1a linha de TARV que receberam o resultado da "
            + "Carga Viral entre o sexto e o nono mês após o início do TARV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_10Numerador(),
                "CAT13_PART_3_13_10_NUMERATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=5-9");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_11_DENOMINATOR",
        "13.11: Crianças  (10-14 anos de idade) na 1a linha de TARV que receberam o resultado da "
            + "Carga Viral entre o sexto e o nono mês após o início do TARV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_11Denominador(),
                "CAT13_PART_3_13_11_DENOMINATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=10-14");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_11_NUMERATOR",
        "13.11: Crianças  (10-14 anos de idade) na 1a linha de TARV que receberam o resultado da"
            + " Carga Viral entre o sexto e o nono mês após o início do TARV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInFirstLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_11Numerador(),
                "CAT13_PART_3_13_11_NUMERATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=10-14");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_5_DENOMINATOR",
        "13.5: Adultos (15/+anos) na 2a linha de TARV que receberam o resultado da CV entre o "
            + "sexto e o nono mês após o início da 2a linha de TARV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInSecondLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_3_Denominador_13_5(),
                "CAT13_PART_3_13_5_DENOMINATOR",
                mappings),
            mappings),
        "ageOnB2NEW=15+10MONTHS");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_5_NUMERATOR",
        "13.5: Adultos (15/+anos) na 2a linha de TARV que receberam o resultado da CV entre "
            + "o sexto e o nono mês após o início da 2a linha de TARV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInSecondLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_3_Numerador_13_5(),
                "CAT13_PART_3_13_5_NUMERATOR",
                mappings),
            mappings),
        "ageOnB2NEW=15+10MONTHS");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_14_DENOMINATOR",
        "13.14: Crianças  na 2a linha de TARV que receberam o resultado da Carga Viral entre o "
            + "sexto e o nono mês após o início da 2a linha de TARV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInSecondLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_3_Denominador_13_5(),
                "CAT13_PART_3_13_14_DENOMINATOR",
                mappings),
            mappings),
        "ageOnB2NEW=2-14-10MONTHS");

    dataSetDefinition.addColumn(
        "CAT13_PART_3_13_14_NUMERATOR",
        "13.14: Crianças  na 2a linha de TARV que receberam o resultado da Carga Viral entre o "
            + "sexto e o nono mês após o início da 2a linha de TARV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findPatientsInSecondLineTherapheuticWhoReceivedViralChargeBetweenSixthAndNinthMonthAfterARTStartCategory13_3_Numerador_13_5(),
                "CAT13_PART_3_13_14_NUMERATOR",
                mappings),
            mappings),
        "ageOnB2NEW=2-14-10MONTHS");

    dataSetDefinition.addColumn(
        "CAT13_TB_HIV_13_5_DENOMINATOR",
        "13.5: % de adultos (15/+anos) coinfectados TB/HIV com resultado de CV registado na FM Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findCoinfectedWithTBAndHIVAdultsPatientsDenominator13_5(),
                "CAT13_TB_HIV_13_5_DENOMINATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=15+");

    dataSetDefinition.addColumn(
        "CAT13_TB_HIV_13_5_NUMERATOR",
        "13.5: % de adultos (15/+anos) coinfectados TB/HIV com resultado de CV registado na FM Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findCoinfectedWithTBAndHIVAdultsPatientsNumerator13_5(),
                "CAT13_TB_HIV_13_5_NUMERATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=15+");

    dataSetDefinition.addColumn(
        "CAT13_TB_HIV_13_14_DENOMINATOR",
        "13.14: % de crianças (0-14 anos) coinfectados TB/HIV com resultado de CV registado na FM Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findCoinfectedWithTBAndHIVAdultsPatientsDenominator13_5(),
                "CAT13_TB_HIV_13_14_DENOMINATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=0-14");

    dataSetDefinition.addColumn(
        "CAT13_TB_HIV_13_14_NUMERATOR",
        "13.14: % de crianças (0-14 anos) coinfectados TB/HIV com resultado de CV registado na FM Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P3CohortQueries
                    .findCoinfectedWithTBAndHIVAdultsPatientsNumerator13_5(),
                "CAT13_TB_HIV_13_14_NUMERATOR",
                mappings),
            mappings),
        "ageMiEndRevisionDate=0-14");
  }
}
