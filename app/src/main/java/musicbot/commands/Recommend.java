package musicbot.commands;

import musicbot.SpotifyClient;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.MiscUtil;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Recommend extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
        if (!event.getName().equals("recommend")) return;

        final String artist = Objects.requireNonNull(event.getOption("artist")).getAsString();
        final SpotifyClient spotifyClient = new SpotifyClient();
        final Optional<Artist> maybeArtist = spotifyClient.findArtist(artist);

        if (maybeArtist.isPresent()) {
            final String artistId = maybeArtist.get().getId();
            final List<String> tracks = spotifyClient.findRecommendations(artistId).stream().map(track -> track.getName()).collect(Collectors.toList());
            final String message = String.join(", ", tracks);/**/

            event.reply(message).queue();
        } else {
            event.reply("Sorry, I can't find any recommendations.").queue();
        }
    }
}
