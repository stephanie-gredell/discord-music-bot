package musicbot;

import com.google.common.collect.ImmutableList;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.special.SearchResult;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;
import se.michaelthelin.spotify.requests.data.search.SearchItemRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

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
        final SearchArtistsRequest searchItemRequest = SPOTIFY_API.searchArtists(artist)
                .market(CountryCode.US)
                .limit(1)
                .build();

        try {
            final SearchArtistsRequest searchArtistsRequest = SPOTIFY_API.searchArtists(artist)
                    .limit(1)
                    .build();

            final Paging<Artist> artists  = searchArtistsRequest.execute();
            return Arrays.stream(artists.getItems()).findFirst();
        } catch (SpotifyWebApiException | ParseException | IOException exception) {
            return Optional.empty();
        }
    }

    public List<Track> findRecommendations(final String artistId) {
        System.out.println("getting recommendations");
        final GetRecommendationsRequest recommendationsRequest = SPOTIFY_API.getRecommendations()
                .seed_artists(artistId)
                .limit(5)
                .build();

        try {
            final Recommendations recommendations = recommendationsRequest.execute();
            System.out.println("Length: " + recommendations.getTracks().length);

            return Arrays.stream(recommendations.getTracks()).collect(Collectors.toList());
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
