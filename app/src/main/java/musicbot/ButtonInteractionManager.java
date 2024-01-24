package musicbot;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ButtonInteractionManager extends ListenerAdapter {
  private final List<ButtonInteraction> buttonInteractions = new ArrayList<>();

  @Override
  public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    final String id = event.getComponent().getId();

    final String[] args = id.split("_");

    for (final ButtonInteraction buttonInteraction : buttonInteractions) {
      if (args[0].equals(buttonInteraction.getName())) {
        buttonInteraction.execute(event);
      }
    }
  }

  public void add(@NotNull final ButtonInteraction buttonInteraction) {
    buttonInteractions.add(buttonInteraction);
  }
}
