package musicbot.commands;

import com.google.common.collect.ImmutableList;
import musicbot.MCommand;
import musicbot.Paginator;
import musicbot.RecommendationService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.*;
import java.util.stream.Collectors;

public class Recommend implements MCommand {

  @Override
  public String getName() {
    return "recommend";
  }

  @Override
  public String getDescription() {
    return "find recommendations for music";
  }

  @Override
  public List<OptionData> getOptions() {
    final List<OptionData> options = new ArrayList<>();
    options.add(new OptionData(
        OptionType.STRING,
        "artist",
        "Find other songs and artists based on an artist you like"
    ));
    options.add(new OptionData(
        OptionType.STRING,
        "genre",
        "Find other songs and artists based on a genre you like"
    ));
    return options;
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    event.deferReply().queue();

    final String artistInput = event.getOption("artist") != null ? event.getOption("artist").getAsString() : "";
    final String genreInput = event.getOption("genre") != null ? event.getOption("genre").getAsString() : "";
    final List<Track> tracks = new RecommendationService().getRecommendations(artistInput, genreInput);
    final Paginator paginator = new Paginator();
    final Paginator.PaginatedItems<Track> paginatedTracks = paginator.paginate(tracks, 5, 1);
    final EmbedBuilder eb = new EmbedBuilder();
    final List<Track> trackList = paginatedTracks.get();

    if (!trackList.isEmpty()) {
      eb
          .setTitle("Recommendations based on \"" + artistInput + "\"")
          .setDescription("Here are a list of recommendations based on your input. " +
              "It will help you find new music to listen to.")
          .setFooter("To find a song, select a button below.");

      trackList.forEach(track -> {
        final List<String> trackArtists = Arrays.stream(track.getArtists())
            .map(ArtistSimplified::getName)
            .collect(ImmutableList.toImmutableList());
        final String artists = String.join(", ", trackArtists);

        eb.addField(track.getName(), artists, false);
      });

      final MessageEmbed embed = eb.build();
      final List<Button> buttons = trackList.stream().map(track -> {
        final String artists = Arrays.stream(track.getArtists()).map(ArtistSimplified::getName)
            .collect(Collectors.joining(", "));

        return Button.primary("recommend_" + artists, track.getName());
      }).collect(ImmutableList.toImmutableList());

      final String buttonId = "more-recommend_" + artistInput + "--" + genreInput + "_" + paginatedTracks.getNextPageNum();

      event
          .getHook()
          .sendMessageEmbeds(embed)
          .addActionRow(buttons)
          .addActionRow(Button.secondary(buttonId, "Get more recommendations"))
          .queue();
    } else {
      eb.setTitle("No recommendations found");
      final MessageEmbed embed = eb.build();

      event.getHook().sendMessageEmbeds(embed).queue();
    }
  }
}
