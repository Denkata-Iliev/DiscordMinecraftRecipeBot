package commands;

import music.PlayerManager;
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
            PlayerManager manager = PlayerManager.getInstance();
            switch (message.get(1).toLowerCase()){
                case "join":
                    audioManager.openAudioConnection(memberVoiceState.getChannel());
                    event.getChannel().sendMessage("Joining...").queue();
                    break;
                case "leave": case "disconnect":
                    audioManager.closeAudioConnection();
                    event.getChannel().sendMessage("Leaving...").queue();
                    break;
                case "play":
                    switch (message.get(2).toLowerCase()){
                        case "cat":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.CAT);
                            break;
                        case "far":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.FAR);
                            break;
                        case "blocks":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.BLOCKS);
                            break;
                        case "chirp":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.CHIRP);
                            break;
                        case "mall":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.MALL);
                            break;
                        case "mellohi":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.MELLOHI);
                            break;
                        case "stall":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.STALL);
                            break;
                        case "strad":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.STRAD);
                            break;
                        case "ward":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.WARD);
                            break;
                        case "wait":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.WAIT);
                            break;
                        case "pigstep":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.PIGSTEP);
                            break;
                        case "11":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.DISC_11);
                            break;
                        case "13":
                            manager.loadAndPlay(event.getChannel(), MusicConstants.DISC_13);
                            break;
                    }
                    break;
            }
        }
    }
}
