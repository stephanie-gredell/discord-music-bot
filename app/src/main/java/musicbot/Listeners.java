package musicbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Listeners extends ListenerAdapter {

    @Override
    public void onReady(@NotNull final ReadyEvent event) {
        //final Guild guildId = event.getJDA().getGuildById(1197770092243599431L);

        event.getJDA().upsertCommand("music", "find music to play").addOptions(
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

        event.getJDA().upsertCommand("recommend", "find recommendations for music").addOptions(
                new OptionData(
                        OptionType.STRING,
                        "artist",
                        "The name of artist you like",
                        true
                )
        ).queue();
    }
}
