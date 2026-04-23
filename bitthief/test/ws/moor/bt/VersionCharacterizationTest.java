package ws.moor.bt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VersionCharacterizationTest {

  @Test
  void exposesStableVersionFields() {
    Assertions.assertEquals(0, Version.getMajor());
    Assertions.assertEquals(1, Version.getMinor());
    Assertions.assertEquals(7, Version.getTiny());
    Assertions.assertEquals("0.1.7", Version.getShortVersionString());
    Assertions.assertTrue(Version.getLongVersionString().startsWith("0.1.7"));
  }
}
