package commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import music.GuildMusicManager;
import music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class MusicCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        List<String> message = Arrays.asList(event.getMessage().getContentRaw().split(" "));
        if (message.get(0).equalsIgnoreCase(BotConstants.TEMP_PREFIX)) {
            AudioManager audioManager = event.getGuild().getAudioManager();
            GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
            PlayerManager manager = PlayerManager.getInstance();
            GuildMusicManager musicManager = manager.getGuildMusicManager(event.getGuild());
            BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
            TextChannel channel = event.getChannel();
            switch (message.get(1).toLowerCase()) {
                case "join":
                    audioManager.openAudioConnection(memberVoiceState.getChannel());
                    channel.sendMessage("Joining...").queue();
                    break;
                case "leave":
                case "disconnect":
                    audioManager.closeAudioConnection();
                    channel.sendMessage("Leaving...").queue();
                    break;
                case "play":
                    if(audioManager.getConnectedChannel() == null){
                        audioManager.openAudioConnection(memberVoiceState.getChannel());
                        channel.sendMessage("Joining...").queue();
                    }
                    switch (message.get(2).toLowerCase()) {
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
                case "pause":
                    musicManager.player.setPaused(true);
                    channel.sendMessage("Player paused!").queue();
                    break;
                case "resume":
                    musicManager.player.setPaused(false);
                    channel.sendMessage("Player resumed!").queue();
                    break;
                case "clear":
                    musicManager.scheduler.getQueue().clear();
                    musicManager.player.stopTrack();
                    musicManager.player.setPaused(false);
                    break;
                case "queue":
                case "q":
                    if (queue.isEmpty()) {
                        channel.sendMessage("The queue is empty!").queue();
                    } else {
                        int trackCount = Math.min(queue.size(), 10);
                        List<AudioTrack> tracks = new ArrayList<>(queue);
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setTitle("Current queue(Total: " + queue.size() + ")");
                        builder.setColor(Color.GREEN);
                        for (int i = 0; i < trackCount; i++) {
                            AudioTrack track = tracks.get(i);
                            AudioTrackInfo info = track.getInfo();
                            builder.appendDescription(String.format("%s - %s\n",
                                    info.title,
                                    info.author));
                        }
                        channel.sendMessage(builder.build()).queue();
                    }
                    break;
                case "next":
                case "skip":
                    musicManager.scheduler.nextTrack();
                    channel.sendMessage("Skipped!").queue();
                    break;
                case "np":
                    EmbedBuilder builder = new EmbedBuilder();
                    AudioTrackInfo trackInfo = musicManager.player.getPlayingTrack().getInfo();
                    if(musicManager.player.getPlayingTrack() == null){
                        channel.sendMessage("There is no song playing!").queue();
                        break;
                    }
                    else {
                        builder.setColor(Color.PINK);
                        builder.setTitle(String.format("**Playing** [%s] (%s)\n%s : %s",
                                trackInfo.title,
                                trackInfo.uri,
                                formatTime(musicManager.player.getPlayingTrack().getPosition()),
                                formatTime(musicManager.player.getPlayingTrack().getDuration())
                                ));
                        channel.sendMessage(builder.build()).queue();
                    }
            }
        }
    }
    private String formatTime(long milliseconds){
        long hours = milliseconds / TimeUnit.HOURS.toMillis(1);
        long minutes = milliseconds / TimeUnit.MINUTES.toMillis(1);
        long seconds = milliseconds % TimeUnit.MINUTES.toMillis(1)/TimeUnit.SECONDS.toMillis(1);
        return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }
}
