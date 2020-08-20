package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static commands.BotConstants.*;

public class RecipeCommand extends ListenerAdapter {

    private static final String RECIPE_MESSAGE = "You need to specify the item you're looking for";
    private static final String RECIPE = "Recipe";

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        List<String> message = Arrays.asList(event.getMessage().getContentRaw().split(" "));
        List<String> imgURLs = getImgURLs(BotConstants.BASE_URL);

        if (!event.getAuthor().isBot()) {
            if (message.get(0).equalsIgnoreCase(TEMP_PREFIX) && message.get(1).equalsIgnoreCase(RECIPE)) {
                String searchedRecipe = getSearchedRecipe(message).toLowerCase();
                if (searchedRecipe.contains("block")) {
                    event.getChannel().sendMessage("You'll have to be a bit more specific").queue();
                    return;
                }
                if (message.size() <= 2) {
                    event.getChannel().sendMessage(RECIPE_MESSAGE).queue();
                    return;
                }
                String title = getTitle(message);
                specialCases(searchedRecipe, title, event);
                for (String imgUrl : imgURLs) {
                    if (imgUrl.contains(searchedRecipe)) {
                        sendCraftingRecipe(event, imgUrl, title);
                        break;
                    }
                }
            }
        }
    }

    private void specialCases(String searchedRecipe, String title, GuildMessageReceivedEvent event) {
        if (searchedRecipe.equalsIgnoreCase("wood")) {
            sendCraftingRecipe(event, WOOD_URL, title);
        }
        if (searchedRecipe.equalsIgnoreCase("sticks")) {
            sendCraftingRecipe(event, STICKS_URL, title);
        }
        if (searchedRecipe.contains("planks")) {
            sendCraftingRecipe(event, WOODEN_PLANKS_URL, title);
        }
        if (searchedRecipe.contains("woodendoor") || searchedRecipe.contains("irondoor")) {
            sendCraftingRecipe(event, DOORS_URL, title);
        }
        if (searchedRecipe.contains("pickaxe")) {
            sendCraftingRecipe(event, PICKAXES_URL, title);
        }
        if (searchedRecipe.contains("hoes")) {
            sendCraftingRecipe(event, HOES_URL, title);
        }
        if (searchedRecipe.contains("SHOVELS")) {
            sendCraftingRecipe(event, SHOVELS_URL, title);
        }
        if (searchedRecipe.equalsIgnoreCase("woodenaxe")
                || searchedRecipe.equalsIgnoreCase("stoneaxe")
                || searchedRecipe.equalsIgnoreCase("ironaxe")
                || searchedRecipe.equalsIgnoreCase("goldenaxe")
                || searchedRecipe.equalsIgnoreCase("diamondaxe")
                || searchedRecipe.equalsIgnoreCase("netheriteaxe")) {
            sendCraftingRecipe(event, AXES_URL, title);
        }
        if (searchedRecipe.contains("sword")) {
            sendCraftingRecipe(event, SWORDS_URL, title);
        }
        if (searchedRecipe.contains("helmet")) {
            sendCraftingRecipe(event, HELMETS_URL, title);
        }
        if (searchedRecipe.contains("chestplate")) {
            sendCraftingRecipe(event, CHESTPLATES_URL, title);
        }
        if (searchedRecipe.contains("leggings")) {
            sendCraftingRecipe(event, LEGGINGS_URL, title);
        }
        if (searchedRecipe.contains("boots")) {
            sendCraftingRecipe(event, BOOTS_URL, title);
        }
    }

    private String getTitle(List<String> message) {
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < message.size(); i++) {
            sb.append(message.get(i)).append(" ");
        }
        return sb.toString().toUpperCase();
    }

    private void sendCraftingRecipe(GuildMessageReceivedEvent event, String imgUrl, String title) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setImage(BASE_URL + imgUrl);
        eb.setColor(Color.RED);
        event.getChannel().sendMessage(eb.build()).queue();
    }

    private String getSearchedRecipe(List<String> message) {
        StringBuilder searchedRecipe = new StringBuilder();
        for (int i = 2; i < message.size(); i++) {
            searchedRecipe.append(message.get(i));
        }
        return searchedRecipe.toString();
    }

    private List<String> getImgURLs(String baseUrl) {
        Document doc = null;
        try {
            doc = Jsoup.connect(baseUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = doc.select("img[src]");
        List<String> htmlStrings = new ArrayList<>();
        for (Element e : elements) {
            htmlStrings.add(e.toString());
        }

        List<String> imgURLs = new ArrayList<>();
        for (String htmlString : htmlStrings) {
            int beginIndex = getFirstQuotationMarks(htmlString);
            int endIndex = getImgExtensionEnding(htmlString);
            imgURLs.add(htmlString.substring(beginIndex, endIndex));
        }

        return imgURLs;
    }

    private static int getFirstQuotationMarks(String string) {
        return string.indexOf("\"") + 1;
    }

    private static int getImgExtensionEnding(String string) {
        return string.indexOf(" ", getFirstQuotationMarks(string)) - 1;
    }
}
