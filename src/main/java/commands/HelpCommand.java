package commands;

import constants.BotConstants;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class HelpCommand extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().substring(0, BotConstants.PREFIX.length()).equals(BotConstants.PREFIX)) {
            String[] message = event.getMessage().getContentRaw().substring(BotConstants.PREFIX.length()).split(" ");
            if (message[0].equalsIgnoreCase("help")) {
                event.getChannel().sendMessage(BotConstants.HELP_TEXT).queue();
            }
        }
    }
}
