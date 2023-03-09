package test.huawei.dmap.server.api;

/**
 * Type of clients connected to master node
 * @author aerofeev
 * @since 9.0.28
 */
public enum ClientType {
  WORKER("WORKER"), CLIENT("CLIENT");
  private final String name;

  ClientType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
