package musicbot;

import com.google.common.collect.ImmutableList;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpotifyClient {
  private static final SpotifyApi SPOTIFY_API = new SpotifyApi.Builder()
      .setClientId(Token.SPOTIFY_CLIENT_ID)
      .setClientSecret(Token.SPOTIFY_CLIENT_SECRET)
      .build();

  private static final ClientCredentialsRequest CLIENT_CREDENTIALS_REQUEST = SPOTIFY_API
      .clientCredentials()
      .build();

  public SpotifyClient() {
    clientCredentialsSync();
  }

  public Optional<Artist> findArtist(final String artist) {
    try {
      final SearchArtistsRequest searchArtistsRequest = SPOTIFY_API.searchArtists(artist)
          .limit(1)
          .build();

      final Paging<Artist> artists = searchArtistsRequest.execute();

      return Arrays.stream(artists.getItems()).findFirst();
    } catch (SpotifyWebApiException | ParseException | IOException exception) {
      return Optional.empty();
    }
  }

  public List<Track> findTracks(final String query) {
    try {
      final SearchTracksRequest searchTracksRequest = SPOTIFY_API.searchTracks(query).build();

      final Paging<Track> tracks = searchTracksRequest.execute();

      return Arrays.stream(tracks.getItems()).collect(Collectors.toList());
    } catch (SpotifyWebApiException | ParseException | IOException exception) {
      return List.of();
    }
  }

  public List<Track> findRecommendations(final String artistId, final String genres) {
    final GetRecommendationsRequest.Builder recommendationsRequestBuilder = SPOTIFY_API.getRecommendations();

    if (!artistId.isEmpty()) {
      recommendationsRequestBuilder
          .seed_artists(artistId);
    }

    if (!genres.isEmpty()) {
      recommendationsRequestBuilder
          .seed_genres(genres);
    }

    final GetRecommendationsRequest recommendationsRequest = recommendationsRequestBuilder.build();

    try {
      final Recommendations recommendations = recommendationsRequest.execute();

      return Arrays.stream(recommendations.getTracks()).collect(Collectors.toList());
    } catch (IOException | SpotifyWebApiException | ParseException e) {
      System.out.println("Error: " + e.getMessage());
      return ImmutableList.of();
    }
  }

  public List<String> findGenres() {
    final GetAvailableGenreSeedsRequest request = SPOTIFY_API.getAvailableGenreSeeds().build();

    try {
      final String[] genres = request.execute();
      return Arrays.stream(genres).collect(Collectors.toList());
    } catch (IOException | SpotifyWebApiException | ParseException e) {
      System.out.println("Error: " + e.getMessage());
      return ImmutableList.of();
    }
  }

  private static void clientCredentialsSync() {
    try {
      final ClientCredentials clientCredentials = CLIENT_CREDENTIALS_REQUEST.execute();

      // Set access token for further "spotifyApi" object usage
      SPOTIFY_API.setAccessToken(clientCredentials.getAccessToken());

      System.out.println("Expires in: " + clientCredentials.getExpiresIn());
    } catch (final IOException | SpotifyWebApiException | ParseException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}
