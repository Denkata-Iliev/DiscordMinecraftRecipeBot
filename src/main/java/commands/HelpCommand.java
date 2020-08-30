package commands;

import constants.BotConstants;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class HelpCommand extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if(message[0].equalsIgnoreCase(BotConstants.TEMP_PREFIX) && message[1].equalsIgnoreCase("help")){
            event.getChannel().sendMessage(BotConstants.HELP_TEXT).queue();
        }
    }
}
