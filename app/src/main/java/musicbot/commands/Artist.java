package musicbot.commands;

import musicbot.MCommand;
import musicbot.SpotifyClient;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.ArrayList;
import java.util.List;

public class Artist implements MCommand {

  @Override
  public String getName() {
    return "artist";
  }

  @Override
  public String getDescription() {
    return "Find songs by artist";
  }

  @Override
  public List<OptionData> getOptions() {
    final List<OptionData> options= new ArrayList<>();
    options.add(new OptionData(
        OptionType.STRING,
        "name",
        "Find tracks by an artist.",
        true
    ));

    return options;
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    event.deferReply().queue();

    final String artist = event.getOption("name").getAsString();

    final SpotifyClient spotifyClient = new SpotifyClient();
    final List<Track> tracks = spotifyClient.findTracks("artist:" + artist);

    final EmbedBuilder eb = new EmbedBuilder();
    eb
        .setTitle("Some tracks by " + artist);

    tracks.forEach(track ->
      eb.addField(track.getName(), "", false)
    );

    final MessageEmbed embed = eb.build();

    event.getHook().sendMessageEmbeds(embed).queue();
  }
}
