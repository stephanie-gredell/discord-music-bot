package musicbot.commands;

import musicbot.MCommand;
import musicbot.SpotifyClient;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import se.michaelthelin.spotify.model_objects.specification.Artist;
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

    final OptionMapping artistInput = event.getOption("artist");
    final OptionMapping genreInput = event.getOption("genre");

    final Optional<String> maybeArtistId = findArtistId(artistInput);
    final Optional<String> maybeGenre = findGenre(genreInput);

    final SpotifyClient spotifyClient = new SpotifyClient();

    final String artistId = maybeArtistId.orElse("");
    final String genre = maybeGenre.orElse("");
    final List<Track> tracks = spotifyClient.findRecommendations(artistId, genre);

    final EmbedBuilder eb = new EmbedBuilder();
    if (!tracks.isEmpty()) {

      eb
          .setTitle("Recommendations")
          .setDescription("Here are a list of recommendations based on your input. " +
              "It will help you find new music to listen to.")
          .setFooter("To find a song, select a button below.");

      tracks.forEach(track -> {
        final List<String> trackArtists = Arrays.stream(track.getArtists())
            .map(ArtistSimplified::getName)
            .collect(Collectors.toList());
        final String artists = String.join(", ", trackArtists);

        eb.addField(track.getName(), artists, false);
      });

      final MessageEmbed embed = eb.build();

      final List<Button> buttons = tracks.stream().map(track ->
          Button.primary(track.getName(), track.getName())).toList();

      event.getHook().sendMessageEmbeds(embed).addActionRow(buttons).queue();
    } else {
      eb.setTitle("No recommendations found");
      final MessageEmbed embed = eb.build();

      event.getHook().sendMessageEmbeds(embed).queue();
    }
  }

  private Optional<String> findArtistId(final OptionMapping artistInput) {
    if (artistInput == null) {
      return Optional.empty();
    }

    final SpotifyClient spotifyClient = new SpotifyClient();
    final Optional<Artist> maybeArtist = spotifyClient.findArtist(artistInput.getAsString());

    return maybeArtist.map(Artist::getId);
  }

  private Optional<String> findGenre(final OptionMapping genreInput) {
    if (genreInput == null) {
      return Optional.empty();
    }

    return Optional.of(genreInput.getAsString());
  }
}
