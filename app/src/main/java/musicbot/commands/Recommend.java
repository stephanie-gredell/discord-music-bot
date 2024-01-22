package musicbot.commands;

import musicbot.SpotifyClient;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.*;
import java.util.stream.Collectors;

public class Recommend extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
        if (!event.getName().equals("recommend")) return;

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
                            "It will help you find new music to listen to.");

            tracks.forEach(track -> {
                List<String> trackArtists = Arrays.stream(track.getArtists())
                        .map(ArtistSimplified::getName)
                        .collect(Collectors.toList());
                String artists = String.join(", ", trackArtists);

                eb.addField(track.getName(), artists, false);
            });

            MessageEmbed embed = eb.build();

            event.getHook().sendMessageEmbeds(embed).queue();
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
