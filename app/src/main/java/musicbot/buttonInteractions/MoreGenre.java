package musicbot.buttonInteractions;

import musicbot.ButtonInteraction;
import musicbot.Paginator;
import musicbot.SpotifyClient;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class MoreGenre implements ButtonInteraction {
  @Override
  public String getName() {
    return "genre";
  }

  @Override
  public void execute(ButtonInteractionEvent event) {
    final String id = event.getComponent().getId();
    final Paginator paginator = new Paginator();

    final String[] args = id.split("_", 0);
    event.deferReply().queue();

    final int page = Integer.parseInt(args[1]);

    final SpotifyClient spotifyClient = new SpotifyClient();
    final List<String> genres = spotifyClient.findGenres();
    final Paginator.PaginatedItems<String> paginatedGenre = paginator.paginate(genres, 10, page);

    final EmbedBuilder eb = new EmbedBuilder();

    paginatedGenre.get().forEach(genreItem -> eb.addField(genreItem, "", false));
    if (!paginatedGenre.isIsLastPage()) {
      event.getHook().sendMessageEmbeds(eb.build()).addActionRow(
          Button.primary("genre_" + paginatedGenre.getNextPageNum(), "More genres")
      ).queue();
    } else {
      event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
  }
}
