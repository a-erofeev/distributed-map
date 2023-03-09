package test.huawei.dmap.common;

import test.huawei.dmap.protocol.impl.GetCommand;
import test.huawei.dmap.protocol.impl.PutCommand;

/**
 * Utility class for parse command
 * @author aerofeev
 * @since 9.0.28
 */
public class CommandParseUtil {
  public static final String COMMAND_PARTS_DELIMITER = " ";
  private static final String KEY_VALUE_DELIMITER = ":";

  /**
   * Extract command id from string command
   * @param commandWithId string command with id
   * @return pair object command id and command string
   */
  public static Pair<Long, String> getIdAndCommand(String commandWithId) {
    String[] commandParts = commandWithId.split(COMMAND_PARTS_DELIMITER);
    if (commandParts.length < 1) {
      String message = "Invalid command format: " + commandWithId;
      throw new IllegalArgumentException(message);
    }
    try {
      long commandId = Long.parseLong(commandParts[0]);
      return new Pair<>(commandId,
        commandWithId.replace(commandId + COMMAND_PARTS_DELIMITER, ""));
    } catch (NumberFormatException e) {
      String message = "Incorrect command id: " + commandParts[0];
      throw new IllegalArgumentException(message);
    }
  }

  private static boolean isNotEmpty(String str) {
    return str != null && str.trim().length() != 0;
  }

  public static int parseGetCommand(String commandStr) {
    String[] commandParts = commandStr.split(" ");
    checkArgument(commandParts.length == 2, "Incorrect command format.");
    checkArgument(commandParts[0].equalsIgnoreCase(GetCommand.NAME), commandStr + " - is not put command.");
    String keyStr = commandParts[1];
    checkArgument(isNotEmpty(keyStr), "Key is not provided.");
    try {
      return Integer.parseInt(keyStr);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Key is not integer value.");
    }
  }

  public static Pair<Integer, String> parsePutCommand(String commandStr) {
    String[] commandParts = commandStr.split(" ");
    checkArgument(commandParts.length == 2, "Incorrect command format.");
    checkArgument(commandParts[0].equalsIgnoreCase(PutCommand.NAME), commandStr + " - is not put command.");
    String entryStr = commandParts[1];
    checkArgument(isNotEmpty(entryStr), "Entry is not provided.");
    String[] entryElements = entryStr.split(KEY_VALUE_DELIMITER);
    checkArgument(entryElements.length == 2, "Incorrect key:value format.");
    try {
      return new Pair<>(Integer.parseInt(entryElements[0]), entryElements[1]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Key is not integer value.");
    }
  }

  public static void checkArgument(boolean condition, String message) {
    if (!condition) {
      throw new IllegalArgumentException(message);
    }
  }


  public static class Pair<T1, T2> {
    private final T1 mObj1;
    private final T2 mObj2;

    public Pair(T1 obj1, T2 obj2) {
      mObj1 = obj1;
      mObj2 = obj2;
    }

    public T1 getmObj1() {
      return mObj1;
    }

    public T2 getmObj2() {
      return mObj2;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }

      if (!(obj instanceof Pair<?, ?> key)) {
        return false;
      }

      return
        (mObj1 == null ?
          key.mObj1 == null : mObj1.equals(key.mObj1)) &&
          (mObj2 == null ?
            key.mObj2 == null : mObj2.equals(key.mObj2));
    }

    @Override
    public int hashCode() {
      return
        (mObj1 == null ? 0 : mObj1.hashCode()) +
          (mObj2 == null ? 0 : mObj2.hashCode());
    }

    @Override
    public String toString() {
      return "[" + mObj1 + ':' + mObj2 + ']';
    }

  }
}
