package dev.dotspace.squidly;

import dev.dotspace.squidly.listener.SlashCommandListener;
import dev.dotspace.squidly.slash.SlashCommandList;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class SquildyBot {

  private JDA jda;

  public SquildyBot(JDABuilder jdaBuilder) {
    assert jdaBuilder != null;
    this.login(jdaBuilder);
    this.initializeCommands();
    this.iniializeListeners();
  }

  private void iniializeListeners() {
    this.jda.addEventListener(
        new SlashCommandListener()
    );
  }

  private void initializeCommands() {
    var commands = this.jda.updateCommands();
    commands.addCommands(new SlashCommandList()).queue(success -> System.out.printf("Slashcommands successfully registered! %s%n", success));
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

  public JDA jda() {
    return this.jda;
  }

}
