package commands;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class MusicCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        List<String> message = Arrays.asList(event.getMessage().getContentRaw().split(" "));
        if(message.get(0).equalsIgnoreCase(BotConstants.TEMP_PREFIX)){
            AudioManager audioManager = event.getGuild().getAudioManager();
            GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
            switch (message.get(1).toLowerCase()){
                case "join":
                    audioManager.openAudioConnection(memberVoiceState.getChannel());
                    event.getChannel().sendMessage("Joining...").queue();
                    break;
                case "leave": case "disconnect":
                    audioManager.closeAudioConnection();
                    event.getChannel().sendMessage("Leaving...").queue();
                    break;
            }
        }
    }
}
