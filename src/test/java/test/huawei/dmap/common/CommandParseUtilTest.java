package test.huawei.dmap.common;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.huawei.dmap.common.CommandParseUtil.Pair;


import static org.testng.AssertJUnit.assertEquals;

/**
 * @author aerofeev
 * @since 1.0
 */
public class CommandParseUtilTest {

  @Test(dataProvider = "correctGetIdAndCommand")
  public void testGetIdAndCommandCorrect(Pair<Long, String> expectedResult, String commandWithId) {
    var result = CommandParseUtil.getIdAndCommand(commandWithId);
    assertEquals(expectedResult, result);
  }

  @DataProvider
  public static Object[][] correctGetIdAndCommand() {
    return new Object[][] {
      { new Pair<>(11L, "get 1"), "11 get 1" },
      { new Pair<>(21L, "put 17:val"), "21 put 17:val" },
    };
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "incorrectParseGetCommand")
  public void testParseGetCommandIncorrect(String commandStr) {
    CommandParseUtil.parseGetCommand(commandStr);
  }

  @DataProvider
  public static Object[][] incorrectParseGetCommand() {
    return new Object[][] {
      { "get1" },
      { "put 3:com" },
      { "get com" },
    };
  }

  @Test(dataProvider = "correctParseGetCommand")
  public void testParseGetCommandCorrect(int expectedResult, String commandStr) {
    int result = CommandParseUtil.parseGetCommand(commandStr);
    assertEquals(expectedResult, result);
  }

  @DataProvider
  public static Object[][] correctParseGetCommand() {
    return new Object[][] {
      { 1, "get 1" },
      { 21, "GET 21" },
    };
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "incorrectParsePutCommand")
  public void testParsePutCommandIncorrect(String commandStr) {
    CommandParseUtil.parsePutCommand(commandStr);
  }

  @DataProvider
  public static Object[][] incorrectParsePutCommand() {
    return new Object[][] {
      { "put1" },
      { "get 3" },
      { "put com" },
      { "put 11-com" },
    };
  }

  @Test(dataProvider = "correctParsePutCommand")
  public void testParsePutCommandCorrect(Pair<Integer, String> expectedResult, String commandStr) {
    var result = CommandParseUtil.parsePutCommand(commandStr);
    assertEquals(expectedResult, result);
  }

  @DataProvider
  public static Object[][] correctParsePutCommand() {
    return new Object[][] {
      { new Pair<>(1, "val1"), "put 1:val1" },
      { new Pair<>(21, "val2"), "PUT 21:val2" },
    };
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "incorrectGetIdAndCommand")
  public void testGetIdAndCommandIncorrect(String commandWithId) {
    CommandParseUtil.getIdAndCommand(commandWithId);
  }

  @DataProvider
  public Object[][] incorrectGetIdAndCommand() {
    return new Object[][] {
      { "11get1" },
      { "get 1" },
      { "11g et 1" },
      { "get1" },
    };
  }
}
