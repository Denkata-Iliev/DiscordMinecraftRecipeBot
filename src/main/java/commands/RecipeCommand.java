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

import static commands.BotConstants.BASE_URL;
import static commands.BotConstants.TEMP_PREFIX;

public class RecipeCommand extends ListenerAdapter {

    private static final String RECIPE_MESSAGE = "You need to specify the item you're looking for";
    public static final String RECIPE = "Recipe";

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        List<String> message = Arrays.asList(event.getMessage().getContentRaw().split(" "));

        if (!event.getAuthor().isBot()) {
            if (message.get(0).equalsIgnoreCase(TEMP_PREFIX) && message.get(1).equalsIgnoreCase(RECIPE)) {
                String searchedRecipe = getSearchedRecipe(message).toLowerCase();
                String title = getTitle(message);
                List<String> imgURLs = getImgURLs(BotConstants.BASE_URL);
                for (String imgUrl : imgURLs) {
                    if (imgUrl.contains(searchedRecipe)) {
                        sendCraftingRecipe(event, imgUrl, title);
                        break;
                    }
                }
            }
        }
    }

    private String getTitle(List<String> message) {
        StringBuilder sb = new StringBuilder();
        for(int i = 2; i < message.size(); i++) {
            sb.append(message.get(i)).append(" ");
        }
        return sb.toString();
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
