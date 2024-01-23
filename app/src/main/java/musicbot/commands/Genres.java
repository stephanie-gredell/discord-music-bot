package musicbot.commands;

import com.google.common.collect.Lists;
import musicbot.MCommand;
import musicbot.SpotifyClient;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Genres implements MCommand {

  @Override
  public String getName() {
    return "genres";
  }

  @Override
  public String getDescription() {
    return "find support genres";
  }

  @Override
  public List<OptionData> getOptions() {
    return null;
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    final SpotifyClient spotifyClient = new SpotifyClient();
    final List<String> genres = spotifyClient.findGenres();

    final List<List<String>> batches = Lists.partition(genres, 20);

    final List<MessageEmbed> embeds = batches.stream().map(genresList -> {
      final EmbedBuilder eb = new EmbedBuilder();
      genresList.forEach(genre -> eb.addField(genre, "", false));
      return eb.build();
    }).collect(Collectors.toList());

    event.getChannel().sendMessageEmbeds(embeds).queue();
  }
}
