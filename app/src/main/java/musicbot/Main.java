package musicbot;

import musicbot.buttonInteractions.MoreGenre;
import musicbot.buttonInteractions.MoreRecommendation;
import musicbot.buttonInteractions.VideoRecommendation;
import musicbot.commands.Artist;
import musicbot.commands.Genres;
import musicbot.commands.MusicVideo;
import musicbot.commands.Recommend;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
public class Main {
  public static void main(final String[] args) {
    final JDA jda = JDABuilder
        .createDefault(Token.DEV_TOKEN)
        .build();
    final CommandManager commandManager = new CommandManager();
    final ButtonInteractionManager buttonInteractionManager = new ButtonInteractionManager();

    // commands
    commandManager.add(new Artist());
    commandManager.add(new Genres());
    commandManager.add(new MusicVideo());
    commandManager.add(new Recommend());

    // button interactions
    buttonInteractionManager.add(new MoreRecommendation());
    buttonInteractionManager.add(new MoreGenre());
    buttonInteractionManager.add(new VideoRecommendation());

    // add the managers
    jda.addEventListener(commandManager);
    jda.addEventListener(buttonInteractionManager);
  }
}
