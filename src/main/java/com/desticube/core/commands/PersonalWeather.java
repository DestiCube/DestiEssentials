package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "PersonalWeather",
        description = "Sets your personal weather",
        aliases = {"playerweather", "pw"},
        usageARGS = "(rain | sun | clear)",
        permissions = {"desticore.personalweather", "desticore.personalweather.other"})
public class PersonalWeather extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.personalweather")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) return p.sendMessage(USAGE);
        else {
            if (args[0].equalsIgnoreCase("reset")) p.player().resetPlayerWeather();
            switch (args[0]) {
                case "rain" -> p.player().setPlayerWeather(WeatherType.DOWNFALL);
                case "sun" -> p.player().setPlayerWeather(WeatherType.CLEAR);
                case "clear" -> p.player().setPlayerWeather(WeatherType.CLEAR);
            }
        }
        return false;
    }
}
	