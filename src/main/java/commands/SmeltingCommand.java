package commands;

import javax.annotation.Nonnull;

import constants.BotConstants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.util.Scanner;
import static constants.BotConstants.REQUESTED_BY;


public class SmeltingCommand extends ListenerAdapter {

    private final static String SMELTING = "smelting";

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().indexOf(BotConstants.PREFIX) == 0) {
            String[] message = event.getMessage().getContentRaw().substring(1).split(" ");
            if (message[0].equalsIgnoreCase(SMELTING)) {
                String ingredient = getIngredient(message);
                JSONArray productDetails = productDetails(ingredient);
                if(productDetails == null){
                    event.getChannel().sendMessage("No smelting recipe for this item.").queue();
                }else {
                    sendEmbedMessage(productDetails,ingredient,event);
                }
            }
        }
    }

    private void sendEmbedMessage(JSONArray productDetails, String ingredient, GuildMessageReceivedEvent event){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle((String)productDetails.get(1));
        builder.setDescription(String.format("Recipe with requested ingredient **\"%s\"**",StringUtils.capitalize(ingredient)));
        builder.setImage((String) productDetails.get(0));
        builder.setColor(Color.WHITE);
        builder.setFooter(REQUESTED_BY + event.getAuthor().getName(), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(builder.build()).queue();
    }

    private JSONArray productDetails(String ingredient){
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        try {
            Scanner scanner = new Scanner(new File("src/main/java/static/smelting.json"));
            String jsonString = "";
            while (scanner.hasNext()) {
                jsonString += scanner.nextLine();
            }

            Object obj = parser.parse(jsonString);
            JSONObject jsonObject = (JSONObject) obj;
            jsonArray = (JSONArray) jsonObject.get(ingredient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private String getIngredient(String[] message) {
        String ingredient = "";
        for (int i = 1; i < message.length; i++) {
            if (i == message.length - 1) {
                ingredient += message[i].toLowerCase();
            } else ingredient += message[i].toLowerCase() + " ";
        }
        return ingredient;
    }
}
