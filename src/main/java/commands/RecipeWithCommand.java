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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static commands.BotConstants.*;

public class RecipeWithCommand extends ListenerAdapter {

    private static final String RECIPES_WITH_COMMAND = "recipes-with";
    private static final String TABLE = "table";
    private static final String NOT_THEAD_TR = ":not(thead) tr";
    private static final String SRC = "src";
    private static final String HEADER = "header";
    private static final String PAGEAD = "pagead";
    private static final String TD = "td";
    private static final String FILE_NAME = "ingredients.txt";
    private static final File INGREDIENTS = new File(FILE_NAME);
    private static final String UTF_8 = "UTF-8";
    private static final String RECIPES_WITH = "Recipes With ";
    private static final String NO_RECIPES_WITH_ITEM = "There are no recipes with this item!";
    private static final String DARK_PINK = "#d11d53";

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if (!event.getAuthor().isBot()) {
            if (message[0].equalsIgnoreCase(TEMP_PREFIX) && message[1].equalsIgnoreCase(RECIPES_WITH_COMMAND)) {
                String ingredient = getSearchedIngredient(message);
                if (message.length <= 2) {
                    event.getChannel().sendMessage(RecipeCommand.SPECIFY_ITEM).queue();
                    return;
                }
                if (ingredient.trim().length() <= 3) {
                    event.getChannel().sendMessage(RecipeCommand.AT_LEAST_FOUR_CHARACTERS_LONG).queue();
                    return;
                }
                try {
                    createIngredientsAndURLFile();
                    List<String> itemsWithIngredient = getItemsListWithIngredient(ingredient);
                    if (itemsWithIngredient.isEmpty()) {
                        event.getChannel().sendMessage(NO_RECIPES_WITH_ITEM).queue();
                        return;
                    }
                    sendList(event, itemsWithIngredient, ingredient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendList(GuildMessageReceivedEvent event, List<String> itemsWithIngredient, String ingredient) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(RECIPES_WITH + StringUtils.capitalize(ingredient));
        eb.setColor(Color.decode(DARK_PINK));
        itemsWithIngredient.forEach(item -> {
            String[] split = item.split(":");
            eb.addField(split[0], split[1], false);
        });
        eb.setFooter(REQUESTED_BY + event.getAuthor().getName(), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(eb.build()).queue();
    }

    private String getSearchedIngredient(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < message.length; i++) {
            sb.append(StringUtils.capitalize(message[i])).append(" ");
        }
        return sb.toString().trim();
    }

    private void createIngredientsAndURLFile() throws IOException {
        FileWriter writer = new FileWriter(FILE_NAME);
        Document doc = Jsoup.connect(BASE_URL).get();
        Element tableElement = doc.select(TABLE).first();

        Elements tableRowElements = tableElement.select(NOT_THEAD_TR);

        for (Element row : tableRowElements) {
            if (row.getElementsByAttribute(SRC).attr(SRC).contains(HEADER) || row.getElementsByAttribute(SRC).attr(SRC).contains(PAGEAD)) {
                continue;
            }
            Elements rowItems = row.select(TD);
            for (int j = 0; j < rowItems.size() - 1; j++) {
                String s = rowItems.get(j).getElementsByAttribute(SRC).attr(SRC);
                writer.append(rowItems.get(j).ownText().concat(s));

                if (rowItems.get(j).ownText().isEmpty()) continue;

                if (j != rowItems.size() - 2) {
                    writer.append(", ");
                }
            }
            writer.append('\n');
        }
        writer.close();
    }

    private List<String> getItemsListWithIngredient(String ingredient) {
        List<String> itemsWithIngredient = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(INGREDIENTS, UTF_8);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] ingredients = line.split(", ");
                if (ingredients.length < 3) {
                    continue;
                }
                if (ingredients[1].contains(ingredient)) {
                    itemsWithIngredient.add(ingredients[0] + ": " + ingredients[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return itemsWithIngredient;
    }
}
