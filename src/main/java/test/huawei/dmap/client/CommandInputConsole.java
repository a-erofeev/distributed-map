package test.huawei.dmap.client;

import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import test.huawei.dmap.protocol.impl.QuitCommand;
import test.huawei.dmap.protocol.impl.ShutdownCommand;

/**
 * Console of client application
 * @author aerofeev
 * @since 9.0.28
 */
@Component
public class CommandInputConsole {
  @Autowired
  private ClientNode clientNode;

  @PostConstruct
  public void console() {
    Scanner in = new Scanner(System.in);
    String input;
    do {
      input = in.nextLine();
      clientNode.sendCommandToServer(input);
    } while (!input.equalsIgnoreCase(QuitCommand.NAME) && !input.equalsIgnoreCase(ShutdownCommand.NAME));

  }

}
