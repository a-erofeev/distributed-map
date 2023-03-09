package test.huawei.dmap.protocol.api;

/**
 * Command that executed by worker remotely and asynchronously
 * @author aerofeev
 * @since 1.0
 */
public interface AsyncCommand {
  /**
   * @return command instance id
   */
  long getId();

  /**
   * Execute command
   */
  void executeAsync();

  /**
   * Process worker response
   * @param response worker response
   */
  void processResponse(String response);

}
