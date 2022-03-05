package com.hadroncfy.proxywhitelist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WhitelistCommand {
    private Whitelist whitelist;

    WhitelistCommand(Whitelist w){
        whitelist = w;
    }

    public List<String> doCompletion(String[] args) {
        List<String> ret = new ArrayList<>();
        if (args.length == 0 || args.length == 1){
            ret.add("on");
            ret.add("off");
            ret.add("list");
            ret.add("reload");
            ret.add("remove");
            ret.add("add");
            if (args.length == 1){
                ret = ret.stream().filter(c -> c.startsWith(args[0])).collect(Collectors.toList());
            }
        }
        else if (args.length == 2){
            if (args[0].equals("remove")){
                ret = whitelist.getPlayers().stream()
                    .filter(p -> p.name.startsWith(args[1]))
                    .map(p -> p.name)
                    .collect(Collectors.toList());
            }
        }

        return ret;
    }

    public void exec(ICommandSender sender, String[] args){
        IPlugin ctx = whitelist.getContext();
        if (args.length == 1) {
            if (args[0].equals("on")) {
                whitelist.setEnabled(true);
                sender.sendResultMessage("White list is now enabled");
                return;
            } else if (args[0].equals("off")) {
                whitelist.setEnabled(false);
                sender.sendResultMessage("White list is now disabled");
                return;
            } else if (args[0].equals("reload")) {
                whitelist.loadWhitelist();
                sender.sendResultMessage("Reloaded white list");
                return;
            } else if (args[0].equals("list")) {
                List<Profile> players = whitelist.getPlayers();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < players.size(); i++) {
                    if (i > 0)
                        sb.append(", ");
                    sb.append(players.get(i).name);
                }
                if (players.size() == 0) {
                    sender.sendResultMessage("There're no players in the white list.");
                } else {
                    sender.sendResultMessage(sb.toString());
                }
                return;
            }
        } else if (args.length == 2) {
            if (args[0].equals("add")) {
                new Thread(() -> {
                    try {
                        Profile p = whitelist.createUUID(args[1]);
                        if (p != null){
                            whitelist.update(p.uuid, p.name);
                            sender.sendResultMessage(String.format("Added %s (%s) to whitelist.", p.name, p.uuid));
                        }
                        else {
                            sender.sendResultMessage(String.format("Player %s not found.", args[1]));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        sender.sendResultMessage("Failed to retrieve UUID.");
                    }
                }).start();
                return;
            }
            else if (args[0].equals("remove")){
                if (whitelist.removeByName(args[1])){
                    sender.sendResultMessage(String.format("Removed %s from white list.", args[1]));
                }
                else {
                    sender.sendResultMessage("Player not in the white list.");
                }
                return;
            }
        }
        sender.sendResultMessage("Incorrect arguments.");
    }
}