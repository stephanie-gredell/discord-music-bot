package musicbot.commands;

import musicbot.MCommand;
import musicbot.YoutubeSearch;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MusicVideo implements MCommand {

  @Override
  public String getName() {
    return "music";
  }

  @Override
  public String getDescription() {
    return "find music to play";
  }

  @Override
  public List<OptionData> getOptions() {
    final List<OptionData> options = new ArrayList<>();
    options.add(new OptionData(
        OptionType.STRING,
        "title",
        "The name of the song",
        true));
    options.add(new OptionData(
        OptionType.STRING,
        "artist",
        "The artist of the song",
        true));
    return options;
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    final String title = Objects.requireNonNull(event.getOption("title")).getAsString();
    final String artist = Objects.requireNonNull(event.getOption("artist")).getAsString();

    final YoutubeSearch youtubeSearch = new YoutubeSearch();
    final String result = youtubeSearch.searchForMusic(title, artist);

    event.reply(result).queue();
  }
}
