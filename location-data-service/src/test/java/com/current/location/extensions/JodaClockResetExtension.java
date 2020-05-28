package com.current.location.extensions;

import org.joda.time.DateTimeUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

public class JodaClockResetExtension implements AfterEachCallback, Extension {

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    DateTimeUtils.setCurrentMillisSystem();
  }
}
