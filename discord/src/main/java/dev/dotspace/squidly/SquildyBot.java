package dev.dotspace.squidly;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class SquildyBot {

  private JDA jda;

  public SquildyBot(JDABuilder jdaBuilder) {
    assert jdaBuilder != null;
    this.login(jdaBuilder);
  }


  public JDA jda() {
    return this.jda;
  }

  private void login(JDABuilder jdaBuilder) {
    try {
      this.jda = jdaBuilder.build();
      this.jda.awaitReady();
      System.out.println("Squildy is ready for operation");
    } catch (LoginException e) {
      e.printStackTrace();
      throw new RuntimeException("Could create JDA instance!");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
