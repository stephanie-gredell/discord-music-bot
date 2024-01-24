package musicbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class Listeners extends ListenerAdapter {

  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    final String id = event.getComponent().getId();
    final String label = event.getComponent().getLabel();

    final String[] args = id.split("_");
    event.deferReply().queue();

    if (args[0].equalsIgnoreCase("recommend")) {

      final YoutubeSearch youtubeSearch = new YoutubeSearch();
      final String result = youtubeSearch.searchForMusic(label, args[1]);

      event.getHook().sendMessage(result).queue();
    }

    if (args[0].equalsIgnoreCase("genre")) {
      final int page = Integer.parseInt(args[1]);
      final int offset = page * 10;
      final int index = offset + 10;
      final int nextPage = page + 1;

      final SpotifyClient spotifyClient = new SpotifyClient();
      final List<String> genres = spotifyClient.findGenres();
      final int maxSize = genres.size();
      final int maxPage = (int) Math.floor(maxSize / 10);

      final EmbedBuilder eb = new EmbedBuilder();

      if (nextPage <= maxPage) {
        genres.subList(offset, index).forEach(genreItem -> eb.addField(genreItem, "", false));

        event.getHook().sendMessageEmbeds(eb.build()).addActionRow(
            Button.primary("genre_" + nextPage, "More")
        ).queue();
      } else {
        genres.subList(offset, maxSize).forEach(genreItem -> eb.addField(genreItem, "", false));
        event.getHook().sendMessageEmbeds(eb.build()).queue();
      }
    }
  }
}
