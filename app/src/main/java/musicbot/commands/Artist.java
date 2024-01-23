package musicbot.commands;

import musicbot.SpotifyClient;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

public class Artist extends ListenerAdapter {

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    if (!event.getName().equals("artist")) return;

    event.deferReply().queue();

    final String artist = event.getOption("name").getAsString();

    final SpotifyClient spotifyClient = new SpotifyClient();
    final List<Track> tracks = spotifyClient.findTracks("artist:" + artist);

    final EmbedBuilder eb = new EmbedBuilder();
    eb
        .setTitle("Some tracks by " + artist);

    tracks.forEach(track -> {
      eb.addField(track.getName(), "", false);
    });

    final MessageEmbed embed = eb.build();

    event.getHook().sendMessageEmbeds(embed).queue();
  }
}
