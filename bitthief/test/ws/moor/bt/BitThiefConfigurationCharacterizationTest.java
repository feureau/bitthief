package ws.moor.bt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BitThiefConfigurationCharacterizationTest {

  @Test
  void loadsDefaultPropertiesFile() {
    System.clearProperty("ws.moor.bt.mainproperty");
    BitThiefConfiguration configuration = BitThiefConfiguration.fromPropertiesFile();

    Assertions.assertNotNull(configuration);
    Assertions.assertFalse(configuration.doLogStats());
    Assertions.assertFalse(configuration.isETEnabled());
    Assertions.assertEquals("logback.xml", configuration.getLoggingPropertyFile());
  }

  @Test
  void honorsPropertyFileOverride() {
    String key = "ws.moor.bt.mainproperty";
    String previous = System.getProperty(key);
    try {
      System.setProperty(key, "bitthief-debug.properties");
      BitThiefConfiguration configuration = BitThiefConfiguration.fromPropertiesFile();
      Assertions.assertNotNull(configuration);
      Assertions.assertTrue(configuration.doLogStats());
      Assertions.assertEquals("logback-debug.xml", configuration.getLoggingPropertyFile());
    } finally {
      if (previous == null) {
        System.clearProperty(key);
      } else {
        System.setProperty(key, previous);
      }
    }
  }
}
