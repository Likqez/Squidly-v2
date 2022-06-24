package dev.dotspace.squidly.slash;

import dev.dotspace.squidly.util.EmbedFactory;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class AdvancedSlashCommand {

  private final SlashCommandData slashCommandData;
  private final EmbedFactory<?> embedFactory;

  public AdvancedSlashCommand(SlashCommandData slashCommandData, EmbedFactory<?> embedFactory) {
    this.slashCommandData = slashCommandData;
    this.embedFactory = embedFactory;
  }

  public SlashCommandData slashCommandData() {
    return slashCommandData;
  }

  public EmbedFactory<?> embedFactory() {
    return embedFactory;
  }
}
