package org.stormtrooper007.WorldGen;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.stormtrooper007.WorldGen.projection.GeographicProjection;
import org.stormtrooper007.WorldGen.projection.ModifiedAirocean;

import java.util.logging.Logger;

public class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Logger logger = Logger.getLogger("tpll");
        logger.info("loaded tpll");
    }

    @Override
    public void onDisable() {
        getLogger().info("Unloading tpll");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GeographicProjection projection;
        projection = new ModifiedAirocean();
        if (command.getName().equalsIgnoreCase("tpll")) {
            if(args.length==0) sender.sendMessage("Pls privde the Coordinates you want to tpll");

            String[] splitCoords = args[0].split(",");
            String alt = null;
            if(splitCoords.length==2&&args.length<3) { // lat and long in single arg
                if(args.length>1) alt = args[1];
                args = splitCoords;
            } else if(args.length==3) {
                alt = args[2];
            }
            if(args[0].endsWith(","))
                args[0] = args[0].substring(0, args[0].length() - 1);
            if(args.length>1&&args[1].endsWith(","))
                args[1] = args[1].substring(0, args[1].length() - 1);
            if(args.length!=2&&args.length!=3) {
                sender.sendMessage("Pls provide valid Coordinates");
            }

            double lon, lat;
            lat = Double.parseDouble(args[0]);
            lon = Double.parseDouble(args[1]);
            if(alt!=null) alt = Double.toString(Double.parseDouble(alt));
            double proj[] = projection.fromGeo(lon, lat);
            Location location = new Location(((Player) sender).getWorld(), proj[0], quickElev(((Player) sender).getWorld(), proj[0], proj[1])+1, proj[1]);
            ((Player) sender).teleport(location);
            return true;
        }
        return false;

    }
    private double quickElev(World world, double x, double z) {
        double low = 0;
        double high = 256;
        high++;
        Material defState = Material.AIR;
        while(low < high-1) {
            double y = low + (high - low) / 2;
            Location loc = new Location(world, x, y, z);
            Block block = loc.getBlock();
            if(block.getType().equals(defState))
                high = y;
            else low = y;
        }

        return low;
    }


}
