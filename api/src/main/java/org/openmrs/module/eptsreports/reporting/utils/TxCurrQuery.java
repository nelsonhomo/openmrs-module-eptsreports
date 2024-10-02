package org.openmrs.module.eptsreports.reporting.utils;

public class TxCurrQuery {

  private static final String TXCURR = "TX_CURR/PATIENTS_WHO_ARE_CURRENTLY_ENROLLED_ON_ART.sql";

  public static String findPatientsInTxCurr() {

    return EptsQuerysUtils.loadQuery(TXCURR);
  }
}
