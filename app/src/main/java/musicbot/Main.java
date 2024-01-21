package musicbot;

import musicbot.commands.MusicVideo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {
    public static void main(final String[] args) {
        final JDA jda = JDABuilder
                .createDefault(Token.TOKEN)
                .build();
        jda.addEventListener(new Listeners());
        jda.addEventListener(new MusicVideo());
    }
}