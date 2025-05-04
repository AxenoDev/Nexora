<p align="center">
  <img src="./assets/Nexora.png" width="100">
</p>

<h1 align="center">Nexora - Minecraft Warp Plugin</h1>
**Nexora** is a powerful and flexible plugin designed for managing warps and teleportation features in Minecraft. With a range of commands for warp creation, deletion, teleportation, and customization, Nexora enhances your Minecraft server's navigation system.

---

## Screenshots

### 1. **Warp Menu**
<p align="center">
  <img src="./assets/image 3.png" width="300" alt="Warp Menu">
</p>

### 2. **Warp Config**
<p align="center">
  <img src="./assets/image 4.png" width="300" alt="Warp List">
</p>

### 3. **Warp Icon Customization**
<p align="center">
  <img src="./assets/image 5.png" width="300" alt="Warp Icon Customization">
</p>

---

## Features

- **Create warps**: Set a warp at your position and teleport players to it.
- **Delete warps**: Remove existing warps from your world.
- **Rename warps**: Change the names of your warps.
- **Change warp icons**: Customize warp icons by setting the item in your hand.
- **Warp list**: Display a list of all available warps.
- **Teleport players**: Teleport yourself or others to a specific warp.

---

## Commands

### `/nexora reload`
- **Permission**: `nexora.admin.reload`
- Reloads the plugin's configuration.

### `/warp`
- **Permission**: `nexora.warp`
- Displays the warp menu or teleports you to a warp.
  
### `/warp list`
- **Permission**: `nexora.warp.list`
- Opens the list of available warps.

### `/warp add <name>`
- **Permission**: `nexora.warp.admin.add`
- Creates a warp at your current location.

### `/warp delete <name>`
- **Permission**: `nexora.warp.admin.delete`
- Deletes a specified warp.

### `/warp rename <old> <new>`
- **Permission**: `nexora.warp.admin.rename`
- Renames an existing warp.

### `/warp icon <name>`
- **Permission**: `nexora.warp.admin.changeicon`
- Sets the warp icon to the item in your hand.

### `/warp tp <player> <warp>`
- **Permission**: `nexora.warp.admin.teleport.other`
- Teleports a player to a specified warp.

---

## Permissions

- `nexora.admin.reload`: Permission to reload the plugin configuration.
- `nexora.warp`: Basic permission for using warp commands.
- `nexora.warp.admin.add`: Permission to create new warps.
- `nexora.warp.admin.delete`: Permission to delete warps.
- `nexora.warp.list`: Permission to view the warp list.
- `nexora.warp.admin.rename`: Permission to rename warps.
- `nexora.warp.admin.changeicon`: Permission to change warp icons.
- `nexora.warp.admin.teleport.other`: Permission to teleport other players to warps.

---

## Configuration

Nexora allows you to customize the language and other settings in the `config.yml` file.

```yaml
lang: "en_US"  # Available options: "en_US" and "fr_FR"
