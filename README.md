# InvinciblePets plugin for CraftBukkit

## Description

InvinciblePets adds the ability to make your pets invincible.  
This project is on [BukkitDev](http://dev.bukkit.org/server-mods/invinciblepets/). 

## Usage

To toggle invincibility on a single pet, aim the crosshairs at it and type "/petinv".
To toggle invincibility on all pets, type "/petinv all".

## Permissions

Players need InvinciblePets.toggle to use this plugin.

## Config File

Individual pet invincibility is stored in the metadata on that pet, but universal invincibility is stored in the config.yml file.  
**Note**: toggling universal invincibility will not affect any explicitly configured individual invincibility.

Below is an example config.yml file:
```yaml
Users:
  player1:
    All invincible: true
  player2:
    All invincible: false
```

## Source Code

This mod is completely open source. Source files are maintained at https://github.com/andyfries/InvinciblePets/ and are included in every release as well.
