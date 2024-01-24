package musicbot;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

/**
 * Interface to build a button interaction to manage a button event
 */
public interface ButtonInteraction {
  /**
   * A unique name that identifies the button interaction
   * @return String the name of the button
   */
  String getName();

  /**
   * Executes the interaction logic of the button
   * @param event
   */
  void execute(ButtonInteractionEvent event);
}
