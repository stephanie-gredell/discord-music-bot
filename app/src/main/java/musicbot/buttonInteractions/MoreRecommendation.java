package musicbot.buttonInteractions;

import musicbot.ButtonInteraction;
import musicbot.Paginator;
import musicbot.RecommendationService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This interaction happens when a user requests more recommendations
 * after initial /recommend.
 */
public class MoreRecommendation implements ButtonInteraction {
  private static final int INCREMENT = 5;
  private static final String ARGS_DELIMITER = "_";
  private static final String ARTIST_GENRE_DELIMITER = "--";

  @Override
  public String getName() {
    return "more-recommend";
  }

  @Override
  public void execute(final ButtonInteractionEvent event) {
    event.deferReply().queue();
    final String id = event.getComponent().getId();
    final String[] args = id.split(ARGS_DELIMITER);

    final String[] artistGenre = args[1].split(ARTIST_GENRE_DELIMITER);
    final String artist = artistGenre[0];
    final String genre = artistGenre.length < 2 ? "" : artistGenre[1];

    final int pageNumber = Integer.parseInt(args[2]);
    final List<Track> tracks = new RecommendationService().getRecommendations(artist, genre);

    final Paginator paginator = new Paginator();
    final Paginator.PaginatedItems<Track> paginatedTracks = paginator.paginate(tracks, 5, pageNumber);

    final EmbedBuilder eb = new EmbedBuilder();
    final List<Track> trackList = paginatedTracks.get();
    if (!trackList.isEmpty()) {
      eb
          .setTitle("More recommendations based on \"" + artist + "\"")
          .setDescription("Here are more recommendations based on your input.");

      trackList.forEach(track -> {
        final List<String> trackArtists = Arrays.stream(track.getArtists())
            .map(ArtistSimplified::getName)
            .collect(Collectors.toList());
        final String artists = String.join(", ", trackArtists);

        eb.addField(track.getName(), artists, false);
      });

      final MessageEmbed embed = eb.build();

      final List<Button> buttons = trackList.stream().map(track -> {
        final String artists = Arrays.stream(track.getArtists()).map(ArtistSimplified::getName)
            .collect(Collectors.joining(", "));

        return Button.primary("recommend_" + artists, track.getName());
      }).collect(Collectors.toList());

      if (!paginatedTracks.isIsLastPage()) {
        final String buttonId = "more-recommend_" + artist + "--" + genre + "_" + paginatedTracks.getNextPageNum();

        event
            .getHook()
            .sendMessageEmbeds(embed)
            .addActionRow(buttons)
            .addActionRow(Button.secondary(buttonId, "Get more recommendations"))
            .queue();
      } else {
        event
            .getHook()
            .sendMessageEmbeds(embed)
            .addActionRow(buttons)
            .queue();
      }
    }
  }
}
