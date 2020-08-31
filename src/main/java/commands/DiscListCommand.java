package commands;

import constants.BotConstants;
import constants.MusicConstants;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class DiscListCommand extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().indexOf(BotConstants.PREFIX) == 0) {
            String[] message = event.getMessage().getContentRaw().substring(BotConstants.PREFIX.length()).split(" ");
            if (message[0].equalsIgnoreCase("discs")) {
                event.getChannel().sendMessage(MusicConstants.DISC_LIST).queue();
            }
        }
    }
}