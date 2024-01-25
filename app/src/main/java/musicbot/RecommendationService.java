package musicbot;

import com.google.common.collect.ImmutableList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecommendationService {

  public List<Track> getRecommendations(final String artist, final String genre) {

    if (artist.isEmpty() && genre.isEmpty()) {
      return ImmutableList.of();
    }
    final SpotifyClient spotifyClient = new SpotifyClient();
    final Optional<String> maybeArtistId = findArtistId(artist);
    final String artistId = maybeArtistId.orElse("");
    final List<Track> tracks = spotifyClient.findRecommendations(artistId, genre);

    return tracks;
  }

  private Optional<String> findArtistId(final String artist) {
    final SpotifyClient spotifyClient = new SpotifyClient();
    final Optional<Artist> maybeArtist = spotifyClient.findArtist(artist);

    return maybeArtist.map(Artist::getId);
  }
}
