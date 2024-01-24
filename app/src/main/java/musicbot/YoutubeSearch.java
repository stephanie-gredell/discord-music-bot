package musicbot;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class YoutubeSearch {
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

  private static final long NUMBER_VIDEOS_RETURNED = 1;

  private static YouTube _youtube;

  public String searchForMusic(final String title, final String artist) {
    _youtube = get_youtube();

    final List<SearchResult> searchResults = doSearch(title, artist);

    final Optional<SearchResult> maybeResult = searchResults.stream().findFirst();

    return maybeResult.map(result -> "http://www.youtube.com/watch?v=" + result.getId().getVideoId())
        .orElse("No videos found.");
  }

  private YouTube get_youtube() {
    return new YouTube.Builder(
        HTTP_TRANSPORT,
        JSON_FACTORY,
        createRequestInitializer()
    ).setApplicationName("discord-musicbot").build();
  }

  private HttpRequestInitializer createRequestInitializer() {
    final HttpRequestInitializer initializer = new HttpRequestInitializer() {
      public void initialize(HttpRequest request) throws IOException {
      }
    };

    return initializer;
  }

  private List<SearchResult> doSearch(final String title, final String artist) {
    try {
      final String apiKey = Token.YOUTUBE_API_KEY;

      final String query = title + " by " + artist;

      final YouTube.Search.List search = _youtube.search().list("id,snippet")
          .setKey(apiKey)
          .setType("video")
          .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
          .setMaxResults(NUMBER_VIDEOS_RETURNED)
          .setQ(query);

      final SearchListResponse response = search.execute();
      return response.getItems();
    } catch (GoogleJsonResponseException e) {
      System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
          + e.getDetails().getMessage());
      return ImmutableList.of();
    } catch (IOException e) {
      System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
      return ImmutableList.of();
    } catch (Throwable t) {
      t.printStackTrace();
      return ImmutableList.of();
    }
  }
}
