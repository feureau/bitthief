package ws.moor.bt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class BitThiefCliCharacterizationTest {

  @Test
  void parsesLegacyAndLongOptionsWithoutChangingNames() throws Exception {
    Method parse = BitThief.class.getDeclaredMethod("parseCommandLine", String[].class);
    parse.setAccessible(true);

    Object commandLine = parse.invoke(null, (Object) new String[] {
        "-m", "a.torrent",
        "-o", "out",
        "--port", "7000",
        "--uploadrate", "12",
        "--uploadslots", "3",
        "--denypercentage", "5",
        "--upload",
        "--uploadreal"
    });

    Class<?> type = commandLine.getClass();
    Method hasOption = type.getMethod("hasOption", String.class);
    Method getOptionValue = type.getMethod("getOptionValue", String.class);

    Assertions.assertTrue((Boolean) hasOption.invoke(commandLine, "m"));
    Assertions.assertTrue((Boolean) hasOption.invoke(commandLine, "o"));
    Assertions.assertTrue((Boolean) hasOption.invoke(commandLine, "upload"));
    Assertions.assertTrue((Boolean) hasOption.invoke(commandLine, "uploadreal"));
    Assertions.assertEquals("7000", getOptionValue.invoke(commandLine, "p"));
    Assertions.assertEquals("12", getOptionValue.invoke(commandLine, "uploadrate"));
    Assertions.assertEquals("3", getOptionValue.invoke(commandLine, "uploadslots"));
    Assertions.assertEquals("5", getOptionValue.invoke(commandLine, "denypercentage"));
  }
}
