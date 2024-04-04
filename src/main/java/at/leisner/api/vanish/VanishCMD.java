package at.leisner.api.vanish;

import at.leisner.api.Api;

import static at.leisner.api.rang.RangManager.*;
import static at.leisner.api.util.Translator.translate;

import at.leisner.api.rang.RangManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VanishCMD extends Command {
    private static List<String> arg1 = new ArrayList<>();

    static {
        arg1.add("off");
        arg1.add("on");
        arg1.add("list");
        arg1.add("on");
        arg1.add("hide");
        arg1.add("show");
        arg1.add("info");
        arg1.add("toggle");
        arg1.add("items");
        arg1.add("reset");
        arg1.add("login");
        arg1.add("logout");
    }
    public VanishCMD() {
        super("vanish","Vanish Command", "/vanish [args]", new ArrayList<>());
        this.setPermission("cmd.vanish");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        VanishManager vanishManager = Api.getInstance().getVanishManager();
        boolean isPlayer;
        Player sender = null;
        if (commandSender instanceof Player) {
            sender = (Player) commandSender;
            isPlayer = true;
        } else {
            isPlayer = false;
        }

        if (args.length == 0 && isPlayer) {
            vanishManager.toggleVanish(sender);
//            if (vanishManager.isVanish(sender)) {
//                sender.sendMessage(translate("vanish.on"));
//            } else {
//                sender.sendMessage(translate("vanish.off"));
//            }
            if (vanishManager.isVanish(sender)) {
                sender.sendMessage(translate("vanish.vanish_on"));
            } else {
                sender.sendMessage(translate("vanish.vanish_off"));
            }
            return true;
        }
        if (args[0].equals("list")) {
            StringBuilder players = new StringBuilder();
            for (Player player : vanishManager.getVanishPlayers()) {
                if (players.length() == 0) {
                    players.append(getDisplayName(player));
                } else {
                    players.append(ChatColor.BLUE + ", ").append(getDisplayName(player));
                }
            }
            commandSender.sendMessage(translate("vanish.players")+players);
            return true;
        }
        if (args[0].equals("items")) {
            if (args.length == 1 && isPlayer) {
                sender.sendMessage(translate(vanishManager.canPickUpItems(sender)? "vanish.item_off" : "vanish.item_on"));
                vanishManager.setItemPickup(sender, !vanishManager.canPickUpItems(sender));
            } else if (args.length == 2) {
                Player player = Bukkit.getPlayerExact(args[1]);
                if (player == null) {
                    commandSender.sendMessage(translate("vanish.wrong.player"));
                    return false;
                }
                sender.sendMessage(vanishManager.vanishPrefix + " " + ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " kann nun " + (vanishManager.canPickUpItems(sender) ? ChatColor.RED + "keine " : "") + ChatColor.YELLOW + "Items aufheben");
                vanishManager.setItemPickup(player, !vanishManager.canPickUpItems(player));
            }
        }
        if (args[0].equals("hide")) {
            if (args.length == 3) {
                Player player1 = Bukkit.getPlayerExact(args[1]);
                Player player2 = Bukkit.getPlayerExact(args[2]);
                if (player2 == null || player1 == null) {
                    commandSender.sendMessage(translate("vanish.wrong.players"));
                    return false;
                }
                vanishManager.hideOnlyPlayer(player2,player1);
                sender.sendMessage(translate("vanish.hide").replace("%player1%", getDisplayName(player2)).replace("%player2%",getDisplayName(player1)));
            } else if (args.length == 2 && isPlayer) {
                Player player1 = Bukkit.getPlayerExact(args[1]);
                if (player1 == null) {
                    commandSender.sendMessage(translate("vanish.wrong.player"));
                    return false;
                }
                vanishManager.hideOnlyPlayer(sender, player1);
                sender.sendMessage(translate("vanish.hide_for_you").replace("%player1%",getDisplayName(player1)));
            }
        }

        if (args[0].equals("show")) {
            if (args.length == 3) {
                Player player1 = Bukkit.getPlayerExact(args[1]);
                Player player2 = Bukkit.getPlayerExact(args[2]);
                if (player2 == null || player1 == null) {
                    commandSender.sendMessage(translate("vanish.wrong.players"));
                    return false;
                }
                vanishManager.showOnlyPlayer(player2,player1);
                sender.sendMessage(translate("vanish.show").replace("%player1%", getDisplayName(player2)).replace("%player2%",getDisplayName(player1)));
            } else if (args.length == 2 && isPlayer) {
                Player player1 = Bukkit.getPlayerExact(args[1]);
                if (player1 == null) {
                    commandSender.sendMessage(translate("vanish.wrong.player"));
                    return false;
                }
                vanishManager.showOnlyPlayer(sender, player1);
                sender.sendMessage(translate("vanish.show_for_you").replace("%player1%",getDisplayName(player1)));
            }
        }

//        if (args[0].equals("logout")) {
//            if (args.length == 1 && isPlayer) {
//                Bukkit.broadcastMessage(vanishManager.getQuitMSG(sender,All.getInstance().getChatManager().getRang(sender)));
//                return true;
//            } else if (args.length != 1) {
//                Player target = Bukkit.getPlayerExact(args[1]);
//                if (target == null) {
//                    commandSender.sendMessage(vanishManager.getPrefix()+ChatColor.RED+" Der spieler ist ungültig");
//                    return false;
//                }
//                Bukkit.broadcastMessage(vanishManager.getQuitMSG(target,All.getInstance().getChatManager().getRang(target)));
//                return true;
//            }
//        }

//        if (args[0].equals("login")) {
//            if (args.length == 1 && isPlayer) {
//                Bukkit.broadcastMessage(vanishManager.getJoinMSG(sender,All.getInstance().getChatManager().getRang(sender)));
//                return true;
//            } else if (args.length != 1) {
//                Player target = Bukkit.getPlayerExact(args[1]);
//                if (target == null) {
//                    commandSender.sendMessage(vanishManager.getPrefix()+ChatColor.RED+" Der spieler ist ungültig");
//                    return false;
//                }
//                Bukkit.broadcastMessage(vanishManager.getJoinMSG(target,All.getInstance().getChatManager().getRang(target)));
//                return true;
//            }
//        }

        if (args[0].equals("info")) {
            if (args.length == 1 && isPlayer) {
                List<Player> cansee = vanishManager.canSee(sender);
                StringBuilder players = new StringBuilder();
                if (cansee.isEmpty()) {
                    commandSender.sendMessage(translate("vanish.no_body"));
                    return true;
                }
                for (Player player : cansee) {
                    players.append(getDisplayName(player)).append(", ");
                }
                players.replace(players.length()-2,players.length(),"");
                commandSender.sendMessage(translate("vanish.can_see").replace("%players%", players).replace("%size%",String.valueOf(cansee.size())));
                return true;
            }
            if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    commandSender.sendMessage(translate("vanish.wrong.player"));
                    return false;
                }
                List<Player> cansee = vanishManager.canSee(target);
                StringBuilder players = new StringBuilder();
                if (cansee.isEmpty()) {
                    commandSender.sendMessage(translate("vanish.no_body_other").replace("%player%",target.getName()));
                    return true;
                }
                for (Player player : cansee) {
                    players.append(player.getDisplayName()).append(", ");
                }
                players.replace(players.length()-2,players.length(),"");
                commandSender.sendMessage(translate("vanish.can_see").replace("%players%", players).replace("%size%",String.valueOf(cansee.size())).replace("%player%",target.getName()));

            }
        }

        if (args[0].equals("on")) {
            if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target != null) {
                    vanishManager.setVanishState(target, true);
                    commandSender.sendMessage(/*vanishManager.getPrefix()+" "+ChatColor.GOLD+All.getInstance().getChatManager().getDisplayName(target)+ChatColor.GREEN+*/" im Vanish modus!");
                    return true;
                } else {
                    commandSender.sendMessage(translate("vanish.wrong.player"));
                }
            } else if (isPlayer) {
                vanishManager.setVanishState(sender, true);
                sender.sendMessage(translate("vanish.vanish_on"));
            }
        } else if (args[0].equals("off")) {
            if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target != null) {
                    vanishManager.setVanishState(target, false);
                    commandSender.sendMessage(/*vanishManager.getPrefix()+" "+ChatColor.GOLD+All.getInstance().getChatManager().getDisplayName(target)+ChatColor.RED+*/" ist nicht mehr im Vanish modus!");
                    return true;
                } else {
                    commandSender.sendMessage(translate("vanish.wrong.player"));
                }
            } else if (isPlayer) {
                vanishManager.setVanishState(sender, false);
                sender.sendMessage(translate("vanish.vanish_off"));
            }
        }

//        if (args[0].equals("reloadStats")) {
//            vanishManager.reloadAllStats();
//            commandSender.sendMessage(vanishManager.getPrefix()+ChatColor.BLUE+" Reload all Vanish Stats!");
//            return true;
//        }
        return false;
    }


    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return arg1;
        }
        if (args.length == 2) {
            switch (args[0]) {
                case "off":
                case "on":
                case "hide":
                case "show":
                case "info":
                case "reset":
                case "login":
                case "logout":
                case "items":
                case "toggle":
                    if (sender instanceof Player) return Api.getInstance().getVanishManager().listOfSeenPlayerFrom((Player) sender);
                    List<String> arg2 = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) arg2.add(p.getName());
                    return arg2;
            }
        }
        if (args.length == 3) {
            switch (args[0]) {
                case "hide":
                case "show":
                    if (sender instanceof Player) return Api.getInstance().getVanishManager().listOfSeenPlayerFrom((Player) sender);
                    List<String> arg3 = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) arg3.add(p.getName());
                    return arg3;
            }
        }
        return new ArrayList<>();
    }
}
