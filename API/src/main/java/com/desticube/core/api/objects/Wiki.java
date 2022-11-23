package com.desticube.core.api.objects;

import com.gamerduck.commons.commands.DuckCommand;
import com.gamerduck.commons.general.Classes;
import com.google.common.collect.Lists;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Wiki {
    boolean even;

    public Wiki(Plugin pl) throws IOException {
        pl.saveResource("wiki/wiki.html", true);
        File file = new File(pl.getDataFolder() + "/wiki/wiki.html");
        String htmlString = Files.readString(file.toPath());
        StringBuilder body = new StringBuilder();
        even = false;
        Classes.getClassesWithAnnotation("com.desticube.core.com.desticube.core.commands", pl.getClass().getClassLoader(), DuckCommand.class).forEach(cmd -> {
            DuckCommand annon = cmd.getAnnotation(DuckCommand.class);
            if (annon instanceof DuckCommand) {
                List<String> permlist = Lists.newArrayList(annon.permissions().clone());
                StringBuilder permstring = new StringBuilder();
                permlist.forEach(s -> {
                    if (permlist.get(permlist.size() - 1).equalsIgnoreCase(s)) {
                        permstring.append(s);
                        return;
                    }
                    permstring.append(s + ", ");
                });
                List<String> aliaslist = Lists.newArrayList(annon.aliases().clone());
                StringBuilder aliastring = new StringBuilder();
                aliaslist.forEach(s -> {
                    if (aliaslist.get(aliaslist.size() - 1).equalsIgnoreCase(s)) {
                        aliastring.append(s);
                        return;
                    }
                    aliastring.append(s + ", ");
                });
                if (even == false) {
                    body.append("<tr role=\"row\" class=\"even\">" +
                            "	<td class=\"sorting_1\">" + pl.getName() + "</td>" +
                            "	<td>" + annon.command().toLowerCase() + "</td>" +
                            "	<td>" + aliastring + "</td>" +
                            "	<td>" + annon.description() + "</td><td>\r\n" +
                            "	<code>" + permstring + "</code></td></tr>");
                    even = true;
                } else {
                    body.append("<tr role=\"row\" class=\"odd\">" +
                            "	<td class=\"sorting_1\">" + pl.getName() + "</td>" +
                            "	<td>" + annon.command().toLowerCase() + "</td>" +
                            "	<td>" + aliastring + "</td>" +
                            "	<td>" + annon.description() + "</td><td>\r\n" +
                            "	<code>" + permstring + "</code></td></tr>");
                    even = false;
                }
            }
        });
        htmlString = htmlString.replace("$body", body.toString());
        File newHtmlFile = new File(pl.getDataFolder() + "/wiki/wiki.html");
        Files.write(newHtmlFile.toPath(), htmlString.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }

    public void delete(Plugin pl) {
        File wiki = new File(pl.getDataFolder() + File.separator + "wiki/wiki.html");
        if (wiki.exists()) wiki.delete();
    }
}
