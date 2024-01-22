package musicbot.commands;

import com.google.common.collect.Lists;
import musicbot.SpotifyClient;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class Genres extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("genres")) return;

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
