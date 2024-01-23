package musicbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public class Listeners extends ListenerAdapter {
    @Override
    public void onReady(@NotNull final ReadyEvent event) {
        final JDA jda = event.getJDA();

        jda.upsertCommand("music", "find music to play").addOptions(
                new OptionData(
                        OptionType.STRING,
                        "title",
                        "The name of the song",
                        true),
                new OptionData(
                        OptionType.STRING,
                        "artist",
                        "The artist of the song",
                        true)
        ).queue();

        jda.upsertCommand("recommend", "find recommendations for music").addOptions(
                new OptionData(
                        OptionType.STRING,
                        "artist",
                        "Find other songs and artists based on an artist you like"
                ),
                new OptionData(
                        OptionType.STRING,
                        "genre",
                        "Find other songs and artists based on a genre you like"
                )
        ).queue();

        jda.upsertCommand("genres", "find recommendations for music").queue();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        final String artist = event.getComponent().getId();
        final String title = event.getComponent().getLabel();

        final YoutubeSearch youtubeSearch = new YoutubeSearch();
        final String result = youtubeSearch.searchForMusic(title, artist);

        event.reply(result).queue();
    }
}
