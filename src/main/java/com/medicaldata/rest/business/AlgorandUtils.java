package com.medicaldata.rest.business;

import org.apache.commons.lang3.ArrayUtils;

public class AlgorandUtils {

  public static final String[] headers = {"X-API-Key"};
  public static final String[] values = {""};/* Enter your purestake.io token*/
  public static final String[] txHeaders = ArrayUtils.add(headers, "Content-Type");
  public static final String[] txValues = ArrayUtils.add(values, "application/x-binary");

}
