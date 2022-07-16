package dev.dotspace.squidly.slash;

import dev.dotspace.squidly.util.EmbedFactory;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicSlashCommand {

  private final String name;
  private final String description;
  private final List<OptionData> optionData = new ArrayList<>();

  public BasicSlashCommand(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public void addOption(OptionData... option) {
    optionData.addAll(List.of(option));
  }

  public String name() {
    return name;
  }

  public String description() {
    return description;
  }

  public List<OptionData> optionData() {
    return optionData;
  }

  public SlashCommandData toCommandData() {
    return new CommandDataImpl(name, description).addOptions(optionData);
  }
}
