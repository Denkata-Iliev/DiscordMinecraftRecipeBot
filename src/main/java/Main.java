import commands.*;
import constants.BotConstants;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class Main {

    private static final String TOKEN = "NzQ1MjYxMzcwMDEzOTA5MDcy.XzvMvA.MlLSmRB3KavJyuEtQYR37hh3tpc";

    public static void main(String[] args) throws Exception {
        JDABuilder jda = JDABuilder.createDefault(TOKEN);
        jda.setActivity(Activity.watching(BotConstants.PREFIX +"help"));
        jda.addEventListeners(new RecipeCommand());
        jda.addEventListeners(new MusicCommand());
        jda.addEventListeners(new BrewingCommand());
        jda.addEventListeners(new RecipeWithCommand());
        jda.addEventListeners(new HelpCommand());
        jda.addEventListeners(new DiscListCommand());
        jda.addEventListeners(new SmeltingCommand());
        jda.build();

    }
}
