package dev.dotspace.squidly.slash;

import dev.dotspace.squidly.slash.impl.match.MatchSlashCommand;
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
        new SettingsSlashCommand().slashCommandData(),
        new MatchSlashCommand().toCommandData()
    );
  }

  public void addAll(SlashCommandData... data) {
    Arrays.stream(data).forEach(super::add);
  }
}
