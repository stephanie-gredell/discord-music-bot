package musicbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension of the Listener Adaptor that manages all the commands.
 */
public class CommandManager extends ListenerAdapter {
  private final List<MCommand> commands = new ArrayList<>();

  @Override
  public void onReady(final ReadyEvent event) {
    for (final Guild guild : event.getJDA().getGuilds()) {
      for (MCommand command : commands) {
        if (command.getOptions() == null) {
          guild.upsertCommand(command.getName(), command.getDescription()).queue();
        } else {
          guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions()).queue();
        }
      }
    }
  }

  @Override
  public void onSlashCommandInteraction(final @NotNull SlashCommandInteractionEvent event) {
    for(final MCommand command : commands) {
      if(command.getName().equals(event.getName())) {
        command.execute(event);
        return;
      }
    }
  }

  public void add(final MCommand command) {
    commands.add(command);
  }
}
