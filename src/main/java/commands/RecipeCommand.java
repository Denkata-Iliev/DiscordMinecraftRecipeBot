package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;
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
import static commands.SpecialCasesConstants.*;

public class RecipeCommand extends ListenerAdapter {

    private static final String BE_MORE_SPECIFIC = "You'll have to be a bit more specific";
    private static final String RECIPE_MESSAGE = "You need to specify the item you're looking for";
    private static final String RECIPE = "Recipe";
    private static final String BLOCK = "block";
    private static final String NO_RECIPE_FOR_YOUR_ITEM = "There's no recipe for your item!";
    private static final String CSS_QUERY = "img[src]";


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        List<String> message = Arrays.asList(event.getMessage().getContentRaw().split(" "));
        List<String> imgURLs = getImgURLs(BotConstants.BASE_URL);

        if (!event.getAuthor().isBot()) {
            if (message.get(0).equalsIgnoreCase(TEMP_PREFIX) && message.get(1).equalsIgnoreCase(RECIPE)) {
                String searchedRecipe = getSearchedRecipe(message).toLowerCase();
                if (searchedRecipe.equalsIgnoreCase(BLOCK)) {
                    event.getChannel().sendMessage(BE_MORE_SPECIFIC).queue();
                    return;
                }
                if (message.size() <= 2) {
                    event.getChannel().sendMessage(RECIPE_MESSAGE).queue();
                    return;
                }
                String title = getTitle(message);
                for (int i = 0; i < imgURLs.size(); i++) {
                    String imgUrl = imgURLs.get(i);
                    if (specialCases(searchedRecipe, title, event)) {
                        break;
                    }
                    if (imgUrl.contains(searchedRecipe)) {
                        sendCraftingRecipe(event, imgUrl, title);
                        break;
                    }
                    if (i == imgURLs.size() - 1 && !imgUrl.contains(searchedRecipe)) {
                        event.getChannel().sendMessage(NO_RECIPE_FOR_YOUR_ITEM).queue();
                    }
                }
            }
        }
    }

    private boolean specialCases(String searchedRecipe, String title, GuildMessageReceivedEvent event) {
        if (searchedRecipe.equalsIgnoreCase(WOOD)) {
            sendCraftingRecipe(event, WOOD_URL, title);
            return true;
        }
        if (searchedRecipe.equalsIgnoreCase(STICKS)) {
            sendCraftingRecipe(event, STICKS_URL, title);
            return true;
        }
        if (searchedRecipe.contains(PLANKS)) {
            sendCraftingRecipe(event, WOODEN_PLANKS_URL, title);
            return true;
        }
        if (searchedRecipe.contains(WOODEN_DOOR) || searchedRecipe.contains(IRON_DOOR)) {
            sendCraftingRecipe(event, DOORS_URL, title);
            return true;
        }
        if (searchedRecipe.contains(PICKAXE)) {
            sendCraftingRecipe(event, PICKAXES_URL, title);
            return true;
        }
        if (searchedRecipe.contains(HOE)) {
            sendCraftingRecipe(event, HOES_URL, title);
            return true;
        }
        if (searchedRecipe.contains(SHOVEL)) {
            sendCraftingRecipe(event, SHOVELS_URL, title);
            return true;
        }
        if (searchedRecipe.equalsIgnoreCase(WOODEN_AXE)
                || searchedRecipe.equalsIgnoreCase(STONE_AXE)
                || searchedRecipe.equalsIgnoreCase(IRON_AXE)
                || searchedRecipe.equalsIgnoreCase(GOLDEN_AXE)
                || searchedRecipe.equalsIgnoreCase(DIAMOND_AXE)
                || searchedRecipe.equalsIgnoreCase(NETHERITE_AXE)) {
            sendCraftingRecipe(event, AXES_URL, title);
            return true;
        }
        if (searchedRecipe.contains(SWORD)) {
            sendCraftingRecipe(event, SWORDS_URL, title);
            return true;
        }
        if (searchedRecipe.contains(HELMET)) {
            sendCraftingRecipe(event, HELMETS_URL, title);
            return true;
        }
        if (searchedRecipe.contains(CHESTPLATE)) {
            sendCraftingRecipe(event, CHESTPLATES_URL, title);
            return true;
        }
        if (searchedRecipe.contains(LEGGINGS)) {
            sendCraftingRecipe(event, LEGGINGS_URL, title);
            return true;
        }
        if (searchedRecipe.contains(BOOTS)) {
            sendCraftingRecipe(event, BOOTS_URL, title);
            return true;
        }
        if (searchedRecipe.contains(COAL_BLOCK)) {
            sendCraftingRecipe(event, COAL_BLOCK_URL, title);
            return true;
        }
        if (searchedRecipe.equalsIgnoreCase(QUARTZ_BLOCK)) {
            sendCraftingRecipe(event, QUARTZ_BLOCK_URL, title);
            return true;
        }
        if (searchedRecipe.contains(REDSTONE_BLOCK)) {
            sendCraftingRecipe(event, REDSTONE_BLOCK_URL, title);
            return true;
        }
        return false;
    }

    private String getTitle(List<String> message) {
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < message.size(); i++) {
            sb.append(StringUtils.capitalize(message.get(i))).append(" ");
        }
        return sb.toString();
    }

    private void sendCraftingRecipe(GuildMessageReceivedEvent event, String imgUrl, String title) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setImage(BASE_URL + imgUrl);
        eb.setColor(Color.RED);
        eb.setFooter("Requested by " + event.getAuthor().getName(), event.getAuthor().getAvatarUrl());
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

        Elements elements = doc.select(CSS_QUERY);
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
