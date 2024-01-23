package musicbot.commands;

import musicbot.YoutubeSearch;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MusicVideo extends ListenerAdapter {
  @Override
  public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
    if (!event.getName().equals("music")) return;

    final String title = Objects.requireNonNull(event.getOption("title")).getAsString();
    final String artist = Objects.requireNonNull(event.getOption("artist")).getAsString();

    final YoutubeSearch youtubeSearch = new YoutubeSearch();
    final String result = youtubeSearch.searchForMusic(title, artist);

    event.reply(result).queue();
  }
}
