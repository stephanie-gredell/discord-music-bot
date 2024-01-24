package musicbot;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonInteraction {
  String getName();

  void execute(ButtonInteractionEvent event);
}
