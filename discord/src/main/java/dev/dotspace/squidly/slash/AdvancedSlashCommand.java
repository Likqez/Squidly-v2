package dev.dotspace.squidly.slash;

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class AdvancedSlashCommand {

  private final SlashCommandData slashCommandData;

  public AdvancedSlashCommand(SlashCommandData slashCommandData) {
    this.slashCommandData = slashCommandData;
  }

  public SlashCommandData slashCommandData() {
    return slashCommandData;
  }
}
