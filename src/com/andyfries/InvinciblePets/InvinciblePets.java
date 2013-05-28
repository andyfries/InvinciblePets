package com.andyfries.InvinciblePets;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class InvinciblePets extends JavaPlugin{
    public PluginDescriptionFile pdfFile;
    private final Logger log = Logger.getLogger("Minecraft");
    public FileConfiguration config = new YamlConfiguration();
    private File configFile;

    public void onEnable(){
        //Get the information from the plugin.yml file.
        if (!this.getDataFolder().isDirectory()) {
            this.getDataFolder().mkdir();
        }

        configFile = new File(this.getDataFolder(), "config.yml");
        writeInitialConfig();
        loadConfig();

        getServer().getPluginManager().registerEvents(new PetListener(this), this);
    }

    public void onDisable(){
        log.info(this.getName() + " disabled");
    }

    public void writeInitialConfig(){
        if (!configFile.exists()){
            configFile.getParentFile().mkdirs();
            copyResource(getResource("config.yml"), configFile);
        }
    }

    private void loadConfig(){
        try {
            config.options().copyDefaults(true);
            config.load(configFile);

        }
        catch (Exception e){
            log.info("Error reading config file.");
            e.printStackTrace();
        }
    }

    private void writeConfig(){
        try {
            config.save(configFile);
        }
        catch (IOException e){
            log.info("Error saving config file.");
            e.printStackTrace();
        }
    }

    private void copyResource(InputStream in, File out){
        try {
            OutputStream outStream = new FileOutputStream(out);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                outStream.write(buf,0,len);
            }
            outStream.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPetInvincible(Player player){
        if (!config.contains("Users." + player.getName())){
            //user not in config, add with default options
            config.set("Users." + player.getName() + ".All invincible", false);
            writeConfig();
        }

        boolean petFound = false;
        //loop over all nearby entities
        for (Entity entity : player.getNearbyEntities(16, 16, 16)){
            //only interested in pets with owners
            if (entity instanceof Tameable && ((Tameable) entity).getOwner() != null){
                //owned by this player
                if (((Tameable) entity).getOwner().getName().equals(player.getName())){
                    //player is looking at location close to pet
                    if (entity.getLocation().distance(player.getTargetBlock(null,16).getLocation()) < 2){
                        String animalType = entity.getType().toString();
                        if (entity.hasMetadata("invincible")){
                            //already has metadata, so toggle it off
                            player.sendMessage(animalType.substring(0, 1) + animalType.substring(1).toLowerCase() + " no longer invincible");
                            entity.removeMetadata("invincible", this);
                        }
                        else {
                            player.sendMessage(animalType.substring(0, 1) + animalType.substring(1).toLowerCase() + " now invincible");
                            entity.setMetadata("invincible", new FixedMetadataValue(this, true));
                        }
                        //only set invincibility on one pet
                        petFound = true;
                        break;
                    }
                    else {
                        //not close enough to cursor
                        //log.info(entity.getLocation().distance(player.getTargetBlock(null,16).getLocation()) + "");
                    }
                }
            }
        }
        if (!petFound){
            player.sendMessage("No pets found nearby");
        }
    }

    public void allInvincible(Player player){
        String path = "Users." + player.getName() + ".All invincible";

        //get option from config file, set to default if not found
        boolean invincible = config.getBoolean(path, false);
        config.set(path, !invincible);

        if (!config.getBoolean(path)){
            player.sendMessage("Invulnerability on all pets: disabled");
        }
        else {
            player.sendMessage("Invulnerability on all pets: enabled");
        }
        writeConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if (sender instanceof Player){
            Player player = (Player) sender;

            if (cmd.getName().toLowerCase().equals("petinv")){
                if (!player.hasPermission(cmd.getPermission())){
                    sender.sendMessage(cmd.getPermissionMessage());
                }
                else {
                    if (args.length == 0){
                        setPetInvincible(player);
                    }
                    else if (args.length == 1) {
                        allInvincible(player);
                    }
                    else {
                        sender.sendMessage("Usage: /petinv [all]");
                    }
                }
                return true;
            }
        }
        return false;
    }
}
