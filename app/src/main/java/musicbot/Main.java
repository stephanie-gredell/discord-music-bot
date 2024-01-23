package musicbot;

import musicbot.commands.Artist;
import musicbot.commands.Genres;
import musicbot.commands.MusicVideo;
import musicbot.commands.Recommend;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
public class Main {
  public static void main(final String[] args) {
    final JDA jda = JDABuilder
        .createDefault(Token.TOKEN)
        .build();
    final CommandManager manager = new CommandManager();
    manager.add(new Artist());
    manager.add(new Genres());
    manager.add(new MusicVideo());
    manager.add(new Recommend());
    jda.addEventListener(manager);
  }
}
