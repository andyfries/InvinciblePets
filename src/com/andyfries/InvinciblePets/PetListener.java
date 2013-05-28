package com.andyfries.InvinciblePets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.logging.Logger;

public class PetListener implements Listener {
    private final Logger log = Logger.getLogger("Minecraft");
    private final InvinciblePets plugin;

    public PetListener(InvinciblePets _plugin){
        this.plugin = _plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Tameable){
            //has an owner
            if (((Tameable) entity).getOwner() != null){
                String ownerName = ((Tameable) entity).getOwner().getName();
                String path = "Users." + ownerName + ".All invincible";

                //check in config if owner has set all pets to invincible (default to false if not found), or pet has invincible metadata
                if (plugin.config.getBoolean(path, false) || entity.hasMetadata("invincible")){
                    event.setCancelled(true);

                    //clicking on a pet always unsits it, so reset sitting state
                    if (entity instanceof Wolf){
                        ((Wolf)entity).setSitting(((Wolf) entity).isSitting());
                    }
                    else if (entity instanceof Ocelot){
                        ((Ocelot)entity).setSitting(((Ocelot) entity).isSitting());
                    }
                }
            }
        }
    }
}
