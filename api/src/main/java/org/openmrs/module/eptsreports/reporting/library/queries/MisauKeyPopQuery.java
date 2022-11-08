package org.openmrs.module.eptsreports.reporting.library.queries;

import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;

public class MisauKeyPopQuery {

  private static final String KP = "KP/KEY_POP.sql";

  public static final String findPatientsWhoAreKeyPop(final KeyPopType keyPopType) {

    String query = EptsQuerysUtils.loadQuery(KP);

    switch (keyPopType) {
      case HOMOSEXUAL:
        query = query + "where value_coded=1377 ";
        break;

      case PRISIONER:
        query = query + "where value_coded=20426 ";
        break;

      case SEXWORKER:
        query = query + "where value_coded=1901 ";
        break;

      case DRUGUSER:
        query = query + "where value_coded=20454 ";
        break;

      default:
        break;
    }

    return query;
  }
}
