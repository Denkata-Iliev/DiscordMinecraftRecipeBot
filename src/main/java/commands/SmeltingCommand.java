package commands;

import java.awt.Color;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Nonnull;

import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import constants.BotConstants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static constants.BotConstants.BASE_URL;

public class SmeltingCommand extends ListenerAdapter {

    private final static String SMELTING_ENDING = "-smelting.png";

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().indexOf(BotConstants.PREFIX) == 0) {
            String[] message = event.getMessage().getContentRaw().substring(BotConstants.PREFIX.length()).split(" ");
            if (message[0].equalsIgnoreCase("smelting")) {
                String recipeName = getRecipeName(message);
                String imgURL = BotConstants.SMELTING_BASE_URL + recipeName;
                String title = getTitle(message);
                if (isValidImage(imgURL))
                    sendEmbedMessage(event, title, imgURL);
                else
                    event.getChannel().sendMessage("No smelting recipe.").queue();

            }
        }
    }

    private String getTitle(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < message.length; i++) {
            sb.append(StringUtils.capitalize(message[i])).append(" ");
        }
        return sb.toString();
    }

    private String getRecipeName(String[] message) {
        String recipeName = "";
        if (message.length == 2) {
            recipeName += message[1] + SMELTING_ENDING;
        } else {
            for (int i = 1; i < message.length; i++) {
                if (i == message.length - 1) {
                    recipeName += message[i] + SMELTING_ENDING;
                } else {
                    recipeName += message[i] + "-";
                }
            }
        }
        return recipeName;
    }

    private void sendEmbedMessage(GuildMessageReceivedEvent event, String title, String imgURL) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setColor(Color.GRAY);
        builder.setImage(imgURL);
        builder.setFooter("Requested by: " + event.getAuthor().getName(), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(builder.build()).queue();
    }

    private boolean isValidImage(String imgURL) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(imgURL);
            connection = (HttpURLConnection) url.openConnection();
            return connection.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
