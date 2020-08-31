package commands;

import constants.BotConstants;
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
import java.util.List;

import static constants.BotConstants.*;
import static constants.SpecialCasesConstants.*;

public class RecipeCommand extends ListenerAdapter {

    private static final String BE_MORE_SPECIFIC = "You'll have to be a bit more specific!";
    public static final String SPECIFY_ITEM = "You need to specify the item you're looking for!";
    private static final String RECIPE = "Recipe";
    private static final String BLOCK = "block";
    private static final String NO_RECIPE_FOR_YOUR_ITEM = "There's no recipe for your item!";
    private static final String CSS_QUERY = "img[src]";
    public static final String AT_LEAST_FOUR_CHARACTERS_LONG = "Item has to be at least 4 characters long!";


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        List<String> imgURLs = getImgURLs();

        if (event.getMessage().getContentRaw().substring(0, BotConstants.PREFIX.length()).equals(BotConstants.PREFIX)) {
            String[] message = event.getMessage().getContentRaw().substring(BotConstants.PREFIX.length()).split(" ");
            if (!event.getAuthor().isBot() && message.length > 0) {
                if (message[0].equalsIgnoreCase(RECIPE)) {
                    String searchedRecipe = getSearchedRecipe(message).toLowerCase();
                    if (message.length <= 1) {
                        event.getChannel().sendMessage(SPECIFY_ITEM).queue();
                        return;
                    }
                    if (searchedRecipe.length() <= 3) {
                        event.getChannel().sendMessage(AT_LEAST_FOUR_CHARACTERS_LONG).queue();
                        return;
                    }
                    if (searchedRecipe.equalsIgnoreCase(BLOCK)) {
                        event.getChannel().sendMessage(BE_MORE_SPECIFIC).queue();
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
    }

    private boolean specialCases(String searchedRecipe, String title, GuildMessageReceivedEvent event) {
        if (searchedRecipe.equalsIgnoreCase(CRAFTING_TABLE)) {
            sendCraftingRecipe(event, CRAFTING_TABLE_URL, title);
            return true;
        }
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

    private String getTitle(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < message.length; i++) {
            sb.append(StringUtils.capitalize(message[i])).append(" ");
        }
        return sb.toString();
    }

    private void sendCraftingRecipe(GuildMessageReceivedEvent event, String imgUrl, String title) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setImage(BASE_URL + imgUrl);
        eb.setColor(Color.RED);
        eb.setFooter(REQUESTED_BY + event.getAuthor().getName(), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(eb.build()).queue();
    }

    private String getSearchedRecipe(String[] message) {
        StringBuilder searchedRecipe = new StringBuilder();
        for (int i = 1; i < message.length; i++) {
            searchedRecipe.append(message[i]);
        }
        return searchedRecipe.toString();
    }

    private List<String> getImgURLs() {
        Document doc = null;
        try {
            doc = Jsoup.connect(BASE_URL).get();
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
