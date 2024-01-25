package musicbot.buttonInteractions;

import musicbot.ButtonInteraction;
import musicbot.YoutubeSearch;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class VideoRecommendation implements ButtonInteraction {

  @Override
  public String getName() {
    return "recommend";
  }

  @Override
  public void execute(ButtonInteractionEvent event) {
    final String id = event.getComponent().getId();
    final String label = event.getComponent().getLabel();
    final String[] args = id.split("_");

    event.deferReply().queue();

    final YoutubeSearch youtubeSearch = new YoutubeSearch();
    final String result = youtubeSearch.searchForMusic(label, args[1]);

    event.getHook().sendMessage(result).queue();
  }
}
