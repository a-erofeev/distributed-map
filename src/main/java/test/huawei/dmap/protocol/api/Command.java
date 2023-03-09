package test.huawei.dmap.protocol.api;

/**
 * Command to be processed in client channel
 * @author aerofeev
 * @since 1.0
 */
public interface Command {
  /**
   * Execute command
   * @return string result
   */
  String execute();

  /**
   * @return command string representation
   */
  String getCommandString();
}
