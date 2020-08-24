package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

import java.awt.*;

import static commands.BotConstants.REQUESTED_BY;
import static commands.BotConstants.TEMP_PREFIX;

public class BrewingCommand extends ListenerAdapter {

    private static final String BREWING_RECIPES = "Brewing Recipes";
    private static final String THE_POTIONS_IN_GAME = "This picture shows how to make any of the potions in-game";
    private static final String BREWING_URL = "https://vignette.wikia.nocookie.net/minecraft_gamepedia/images/7/7b/Minecraft_brewing_en.png?version=54f768c316dee66e3735314e29687da6";
    private static final String BREWING = "brewing";
    private static final String BLUE = "#4287f5";

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");
        if (!event.getAuthor().isBot()) {
            if (message[0].equals(TEMP_PREFIX) && message[1].equalsIgnoreCase(BREWING)) {
                sendBrewingPicture(event);
            }
        }
    }

    private void sendBrewingPicture(GuildMessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.decode(BLUE));
        eb.setTitle(BREWING_RECIPES);
        eb.setDescription(THE_POTIONS_IN_GAME);
        eb.setImage(BREWING_URL);
        eb.setFooter(REQUESTED_BY + event.getAuthor().getName(), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
