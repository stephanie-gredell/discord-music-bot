package musicbot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

/**
 * Interface to build a command interaction to manage a button event.
 * This class is prefaced with an M (for MusicBot) as to not confuse with
 * JDA's Command class.
 */
public interface MCommand {
  String getName();

  String getDescription();
  
  List<OptionData> getOptions();

  void execute(SlashCommandInteractionEvent event);
}
