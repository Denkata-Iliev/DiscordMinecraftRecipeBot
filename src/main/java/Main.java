import commands.BrewingCommand;
import commands.RecipeCommand;
import commands.RecipeWithCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {

    private static final String TOKEN = "NzQ1MjYxMzcwMDEzOTA5MDcy.XzvMvA.MlLSmRB3KavJyuEtQYR37hh3tpc";

    public static void main(String[] args) throws Exception {
        JDA jda = JDABuilder.createDefault(TOKEN).build();
        jda.addEventListener(new RecipeCommand());
        jda.addEventListener(new BrewingCommand());
        jda.addEventListener(new RecipeWithCommand());
    }
}
