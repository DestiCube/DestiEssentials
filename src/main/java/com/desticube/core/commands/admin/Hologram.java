package com.desticube.core.commands.admin;//package com.desticube.core.com.desticube.core.commands.admin;
//
//import com.desticube.core.api.objects.DestiPlayer;
//import com.desticube.core.com.desticube.core.commands.handlers.DestiCommand;
//import com.gamerduck.commons.com.desticube.core.commands.DuckCommand;
//import com.gamerduck.commons.general.Numbers;
//import com.google.common.collect.Maps;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//import java.util.Arrays;
//import java.util.HashMap;
//
//import static net.kyori.adventure.text.Component.text;
//
//@DuckCommand(
//        command = "hologram",
//        description = "",
//        aliases = {},
//        usageARGS = "",
//        permissions = {"desticore.hologram"})
//public class Hologram extends DestiCommand {
//
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        DestiPlayer p = player(((Player) sender));
//        if (!p.hasPermission("desticore.hologram")) return p.sendMessage(NO_PERMISSIONS);
//        if (args.length < 1) {
//            p.sendMessage(text("/hologram create (id) (text)"));
//            p.sendMessage(text("/hologram delete (id)"));
//            p.sendMessage(text("/hologram edit (id) (line) (text)"));
//            p.sendMessage(text("/hologram add (id) (text)"));
//            p.sendMessage(text("/hologram setrefresh (id) (refreshRate)"));
//            p.sendMessage(text("/hologram movehere (id)"));
//            p.sendMessage(text("/hologram list"));
//        } else {
//            if (args[0].equalsIgnoreCase("list")) {
//                StringBuilder builder = new StringBuilder();
//                for (com.desticube.core.api.objects.Hologram holo : SERVER.getHolograms())
//                    builder.append(holo.getId() + ", ");
//                p.sendMessage(text(builder.toString()));
//            } else if (args[0].equalsIgnoreCase("create")) {
//                if (args.length <= 2) return p.sendMessage(text("Correct usage: /hologram create (id) (text)"));
//                if (!Numbers.isInteger(args[1])) return p.sendMessage(text("The id has to be a number"));
//                Integer id = Integer.valueOf(args[1]);
//                String text = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
//                String[] split = text.split("%n");
//                HashMap<Integer, String> textMap = Maps.newHashMap();
//                int line = 1;
//                for (String s : split) {
//                    textMap.put(line, color(s));
//                    line++;
//                }
//                SERVER.createHologram(id, textMap, p.getLocation());
//                return p.sendMessage("Hologram " + args[1] + " created");
//            } else if (args[0].equalsIgnoreCase("edit")) {
//                if (args.length <= 3) return p.sendMessage("Correct usage: /hologram edit (id) (line) (text)");
//                if (!Numbers.isInteger(args[1])) return p.sendMessage("The id has to be a number");
//                if (!Numbers.isInteger(args[2])) return p.sendMessage("The line has to be a number");
//                if (!SERVER.getHologramIDs().contains(Integer.valueOf(args[1])))
//                    return p.sendMessage("Hologram doesnt exist");
//                if (!SERVER.getHologramById(Integer.valueOf(args[1])).getText().containsKey(Integer.valueOf(args[2])))
//                    return p.sendMessage("This hologram doesn't have a line number of " + args[1]);
//
//                com.desticube.core.api.objects.Hologram h = SERVER.getHologramById(Integer.valueOf(args[1]));
//                String text = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
//                h.changeLine(Integer.valueOf(args[2]), text);
//            } else if (args[0].equalsIgnoreCase("add")) {
//                if (args.length <= 2) return p.sendMessage("Correct usage: /hologram add (id) (text)");
//                if (!Numbers.isInteger(args[1])) return p.sendMessage("The id has to be a number");
//                if (!SERVER.getHologramIDs().contains(Integer.valueOf(args[1])))
//                    return p.sendMessage("Hologram doesnt exist");
//                com.desticube.core.api.objects.Hologram h = SERVER.getHologramById(Integer.valueOf(args[1]));
//                String text = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
//                h.addLine(text);
//            } else if (args[0].equalsIgnoreCase("delete")) {
//                if (args.length <= 1) return p.sendMessage("Correct usage: /hologram delete (id)");
//                if (!Numbers.isInteger(args[1])) return p.sendMessage("The id has to be a number");
//                if (SERVER.getHologramById(Integer.valueOf(args[1])) == null)
//                    return p.sendMessage("No hologram with that id");
//                SERVER.deleteHologram(Integer.valueOf(args[1]));
//                return p.sendMessage("Hologram " + args[1] + " deleted");
//            } else if (args[0].equalsIgnoreCase("setrefresh")) {
//                if (args.length <= 2) return p.sendMessage("Correct usage: /hologram create (id) (text)");
//                if (!Numbers.isInteger(args[1])) return p.sendMessage("The id has to be a number");
//                Integer id = Integer.valueOf(args[1]);
//                if (!SERVER.getHologramIDs().contains(id)) return p.sendMessage("Hologram doesnt exist");
//                SERVER.getHologramById(id).setRefreshRate(Integer.valueOf(args[2]));
//
//            } else if (args[0].equalsIgnoreCase("movehere")) {
//                if (args.length <= 2) return p.sendMessage("Correct usage: /hologram movehere (id)");
//                if (!Numbers.isInteger(args[1])) return p.sendMessage("The id has to be a number");
//                Integer id = Integer.valueOf(args[1]);
//                if (!SERVER.getHologramIDs().contains(id)) return p.sendMessage("Hologram doesnt exist");
//                SERVER.getHologramById(id).move(p.getLocation());
//
//            }
//        }
//
//        return false;
//    }
//
//}
