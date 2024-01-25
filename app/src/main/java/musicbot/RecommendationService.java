package musicbot;

import com.google.common.collect.ImmutableList;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;
import java.util.Optional;

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
