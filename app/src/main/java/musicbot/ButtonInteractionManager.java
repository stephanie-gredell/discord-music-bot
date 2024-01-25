package musicbot;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension of the Listener Adaptor that manages all the interactions. Because
 * JDA doesn't have a class to specifically build button events, this is a custom way
 * of doing so.
 */
public class ButtonInteractionManager extends ListenerAdapter {
  private final List<ButtonInteraction> buttonInteractions = new ArrayList<>();

  /**
   * Listener for the button interactions.
   * @param event the {@code ButtonInteractionEvent}
   */
  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    final String id = event.getComponent().getId();

    final String[] args = id.split("_", 0);

    for (final ButtonInteraction buttonInteraction : buttonInteractions) {
      if (args[0].equals(buttonInteraction.getName())) {
        buttonInteraction.execute(event);
      }
    }
  }

  /**
   * Add a ButtonInteraction to the ButtonInteractionManager
   * @param buttonInteraction the ButtonInteractiont to be added
   */
  public void add(@NotNull final ButtonInteraction buttonInteraction) {
    buttonInteractions.add(buttonInteraction);
  }
}
