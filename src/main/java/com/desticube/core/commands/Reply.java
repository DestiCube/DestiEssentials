package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.function.Consumer;

import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

@DuckCommand(
        command = "r",
        description = "Reply to a message",
        aliases = {"reply"},
        usageARGS = "(message)",
        permissions = {})
public class Reply extends DestiCommand {

    private DestiPlayer tempPlayer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (args.length == 0) return p.sendMessage(USAGE);
        else {
            String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
            switch (p.reply(message, new Consumer<DestiPlayer>() {
                public void accept(DestiPlayer result) {
                    tempPlayer = (DestiPlayer) result;
                }
            })) {
                case PLAYER_NOT_ONLINE -> p.sendMessage(tl("noOneToReplyToo"));
                case SUCCESS -> {
                    p.sendMessage(tl("messageFormatSender", p.getNickName(), tempPlayer.getNickName(), message));
                    tempPlayer.sendMessage(tl("messageFormatReciever", p.getNickName(), tempPlayer.getNickName(), message));
                    Bukkit.getLogger().info(gson().serialize(
                            tl("messageFormatConsole", p.getNickName(), tempPlayer.getNickName(), message)));
                }
                case EVENT_CANCELLED -> {
                    break;
                }
                default -> {
                    break;
                }
            }
        }
        return false;
    }

}
