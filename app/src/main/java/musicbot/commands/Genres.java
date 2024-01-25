package musicbot.commands;

import musicbot.MCommand;
import musicbot.Paginator;
import musicbot.SpotifyClient;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

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
    event.deferReply().queue();
    final SpotifyClient spotifyClient = new SpotifyClient();
    final List<String> genres = spotifyClient.findGenres();
    final Paginator paginator = new Paginator();

    final EmbedBuilder eb = new EmbedBuilder();
    final Paginator.PaginatedItems<String> paginatedGenre = paginator.paginate(genres, 10, 1);

    paginatedGenre.get().forEach(genre -> eb.addField(genre, "", false));

    event.getHook().sendMessageEmbeds(eb.build()).addActionRow(
        Button.primary("genre_" + paginatedGenre.getNextPageNum(), "More genres")
    ).queue();
  }
}
