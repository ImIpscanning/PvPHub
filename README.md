<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:111827,50:374151,100:E5E7EB&height=210&section=header&text=PvPHub&fontSize=68&fontColor=FFFFFF&animation=fadeIn&fontAlignY=38&desc=Global%20PvP%20attack%20speed%20controller%20for%20Paper%20servers&descAlignY=58&descSize=16" alt="PvPHub Header" />

<img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&size=20&duration=2600&pause=900&color=E5E7EB&center=true&vCenter=true&width=760&height=40&lines=Control+PvP+attack+speed.;Switch+between+global+PvP+modes.;Built+with+Java+and+Paper+API.;Simple.+Configurable.+Lightweight." alt="Typing Animation" />

<p>
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Paper_API-1.20.4-374151?style=for-the-badge" alt="Paper API" />
  <img src="https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/Minecraft-Plugin-62B47A?style=for-the-badge" alt="Minecraft Plugin" />
</p>

</div>

---

## Overview

**PvPHub** is a lightweight Minecraft Paper plugin that lets server owners control the global PvP attack speed mode.

The plugin changes a player's attack speed when they hold specific configured PvP-related blocks, allowing the server to switch between different combat styles such as **trigger**, **semispam** and **spam**.

---

## Features

- Global PvP mode system
- Configurable attack speed modes
- `/pvphub` command to switch modes
- Applies attack speed when holding special blocks
- Automatically updates players when they switch items
- Restores normal attack speed when needed
- Lightweight event-based logic
- Built with Java 17 and Paper API

---

## PvP Modes

| Mode | Attack Speed | Description |
|---|---:|---|
| `trigger` | `4.0` | Vanilla-like attack speed |
| `semispam` | `6.5` | Faster combat, balanced spam style |
| `spam` | `20.0` | Very fast attack speed |

---

## How It Works

```txt
Player joins or changes held item
        ↓
Plugin checks the item in hand
        ↓
If the item is a special PvP block
        ↓
The current PvP mode is applied
        ↓
Attack speed is updated automatically
```

---

## Special Blocks

The plugin applies the selected PvP mode when the player is holding one of these blocks:

```txt
DIAMOND_BLOCK
EMERALD_BLOCK
IRON_BLOCK
GOLD_BLOCK
NETHERITE_BLOCK
PACKED_ICE
BEACON
COPPER_BLOCK
REDSTONE_BLOCK
AMETHYST_BLOCK
```

---

## Requirements

| Requirement | Version |
|---|---|
| Java | 17+ |
| Server | Paper / Paper-based |
| Minecraft API | 1.20.x |
| Build Tool | Maven |

---

## Installation

1. Download or build the plugin `.jar`.
2. Place it inside your server's `plugins` folder.
3. Restart your server.
4. Edit `config.yml` if needed.
5. Use `/pvphub <mode>` to change the PvP mode.

```txt
server/
└── plugins/
    └── PvPHub.jar
```

---

## Commands

| Command | Description |
|---|---|
| `/pvphub trigger` | Sets PvP mode to trigger |
| `/pvphub semispam` | Sets PvP mode to semispam |
| `/pvphub spam` | Sets PvP mode to spam |

Only server operators can use the command.

---

## Configuration

Default `config.yml`:

```yaml
current-mode: semispam

modes:
  trigger:
    attack-speed: 4.0
  semispam:
    attack-speed: 6.5
  spam:
    attack-speed: 20.0
```

You can adjust each mode by changing the `attack-speed` value.

Example:

```yaml
current-mode: trigger

modes:
  trigger:
    attack-speed: 4.0
  semispam:
    attack-speed: 7.0
  spam:
    attack-speed: 18.0
```

---

## Build From Source

Clone the repository:

```bash
git clone https://github.com/ImIpscanning/PvPHub.git
cd PvPHub
```

Build with Maven:

```bash
mvn clean package
```

The compiled plugin will be generated inside:

```txt
target/
```

---

## Project Structure

```txt
PvPHub/
├── src/
│   └── main/
│       ├── java/
│       │   └── me/
│       │       └── ImIpscanning/
│       │           └── pvphub/
│       │               └── PvPHub.java
│       └── resources/
│           ├── config.yml
│           └── plugin.yml
├── pom.xml
└── README.md
```

---

## Plugin Information

```yaml
name: PvPHub
version: 1.0
main: me.ImIpscanning.pvphub.PvPHub
api-version: 1.20

commands:
  pvphub:
    description: Cambia el modo PvP global
```

---

## Technologies

<p align="center">
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" width="46" height="46" alt="Java" />
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/maven/maven-original.svg" width="46" height="46" alt="Maven" />
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/git/git-original.svg" width="46" height="46" alt="Git" />
  <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/github/github-original.svg" width="46" height="46" alt="GitHub" />
</p>

---

## Code Preview

```java
String mode = getConfig().getString("current-mode", "semispam");
double targetSpeed = getConfig().getDouble("modes." + mode + ".attack-speed", 6.5);

double delta = targetSpeed - 4.0;

AttributeModifier modifier = new AttributeModifier(
        SPEED_MODIFIER_UUID,
        SPEED_MODIFIER_NAME,
        delta,
        AttributeModifier.Operation.ADD_NUMBER
);

attr.addModifier(modifier);
```

---

## Status

```diff
+ Global PvP mode system
+ Configurable attack speed values
+ Special block detection
+ Automatic item switch update
+ Operator-only command
+ Maven build system
```

---

## Author

Developed by **ImIpscanning**.

```txt
Simple configuration.
Fast combat control.
Better PvP customization.
```

<div align="center">

<img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&size=18&duration=2600&pause=1000&color=E5E7EB&center=true&vCenter=true&width=650&height=40&lines=Thanks+for+checking+out+PvPHub.;Built+for+Paper+servers.;Simple.+Useful.+Lightweight." alt="Footer Typing" />

<strong>PvPHub</strong> — Java Minecraft Plugin

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:E5E7EB,50:374151,100:111827&height=110&section=footer" alt="Footer" />

</div>
