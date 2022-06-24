package dev.dotspace.squidly.slash;

import dev.dotspace.squidly.slash.impl.profile.ProfileSlashCommand;
import dev.dotspace.squidly.slash.impl.settings.SettingsSlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.Arrays;

public class SlashCommandList extends ArrayList<SlashCommandData> {

  public SlashCommandList() {
    super();
    this.addAll(
        new ProfileSlashCommand().toCommandData(),
        new SettingsSlashCommand().slashCommandData()
    );
  }

  public void addAll(SlashCommandData... data) {
    Arrays.stream(data).forEach(super::add);
  }
}
