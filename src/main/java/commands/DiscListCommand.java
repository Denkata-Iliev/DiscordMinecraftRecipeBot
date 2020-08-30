package commands;

import constants.BotConstants;
import constants.MusicConstants;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class DiscListCommand extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if(message[0].equalsIgnoreCase(BotConstants.TEMP_PREFIX) && message[1].equalsIgnoreCase("discs")){
            event.getChannel().sendMessage(MusicConstants.DISC_LIST).queue();
        }
    }
}