# 🐞 Buglife

Welcome to **Buglife**, a top-down stealth and survival game forged in the fires of late-night debugging sessions. You are a bug. The world is a floor. Your goal is to survive.

This project is more than a game; it's a war journal. It chronicles a descent into the madness of game development, a tale of battling sentient bugs, invisible enemies, and physics-defying ghosts. Every feature is a hard-won victory. Every bug squashed is a trophy mounted on the wall.

## ✨ Features

* **Custom Java Game Engine:** Built from the ground up using Java Swing, featuring a stable 60 FPS game loop and a custom rendering pipeline.
* **Dynamic Tiled World:** 
    * Explore vast mazes built using a smart 2D array system
    * Efficient viewport culling for high performance
    * Multiple tile types: floors, walls, and special spider patrol markers
* **Spider AI System:**
    * Path-following AI using special tile markers (ID: 2)
    * Smooth rotation and pixel-perfect movement
    * Intelligent wall collision avoidance
    * Automated patrol path detection from the map
* **Advanced Player Controller:**
    * Multi-directional animation state machine (IDLE_DOWN, WALKING_UP, WALKING_DOWN, WALKING_HORIZONTAL)
    * Precise circular hitbox system for accurate collisions
    * Smooth movement with wall collision detection
* **Survival Mechanics:**
    * Real-time health system with visual HUD
    * Gradual health decay for constant tension
    * Strategic food placement system on valid floor tiles
* **Robust Physics:**
    * Circular collision detection for entity interactions
    * Tile-based collision for environmental obstacles
    * Precise hitbox calculations for all game entities

## 🚀 How to Run

1.  **Clone the repository.**
2.  **Open in your favorite Java IDE** (VS Code, IntelliJ, etc.).
3.  **Configure the Classpath:** Ensure both the `src` and `res` folders are marked as "Source Roots". This is the most critical step and the hiding place of many ghosts.
4.  **Locate and run `Game.java`** (`src/com/buglife/main/Game.java`).
5.  Survive.

## 🔧 Technical Implementation

### Core Systems
* **Game Loop**: Stable 60 FPS with delta time calculations
* **Rendering Pipeline**: Efficient Java Swing-based graphics system
* **Input Handling**: Responsive keyboard controls with state management

### Entity Framework
* **Player System**: Health management, movement states, sprite animations
* **Spider AI**: Waypoint following, rotation calculations, collision avoidance
* **Food System**: Strategic placement with validity checking

### World Management
* **Tile System**: 64x64 pixel tiles with type-based properties
* **Camera**: Player-following viewport with culling optimization
* **Collision**: Multi-layer detection (entity-to-entity, entity-to-wall)

## 📜 The Bug Hunter's Log: The Weekend War

This game was reforged in the crucible of a single weekend. We faced down a legion of the damned and emerged victorious. Let the record show the demons we have slain:

* **The Spider-pocalypse (The Infinite Cloning Vat):** A misplaced line of code in `updateGame()` was creating a new spider 60 times a second, unleashing a memory-leaking plague that brought the universe to its knees. **Status: Annihilated.**

* **The Dimensional Warp:** Our spider, in a fit of mathematical rage, learned to divide by zero. This corrupted its coordinates (`Pos: [0, 2147483087]`), teleporting it to the edge of reality and crashing the game. **Status: Banished.**

* **The Cursed Artwork:** A series of corrupted PNG files haunted our image loader, causing the game to crash on startup with a `decodeImage` error. This demon was immune to code fixes and required a full digital exorcism of the art assets themselves. **Status: Cleansed.**

* **The Ghost in the Machine (The `.classpath` Haunting):** The most stubborn ghost of all. It hid in the IDE's configuration, making our `res` folder invisible to the game engine. It took multiple rituals and a manual file creation to finally evict this poltergeist. **Status: EVICTED.**

* **The Disco Spider & The Moonwalk:** Our enemies were born with a rebellious spirit, spinning in place and moonwalking in the wrong direction. We battled their inverted compasses and taught them the laws of trigonometry. **Status: Choreographed into submission.**

## 🎮 Game Design Document (Revision 6)

#### Game Overview
* **Title:** Buglife
* **Genre:** Top-down stealth/survival
* **Core Concept:** Survival of a bug in a human household.

#### Current Progress
* ✅ All core features from the "Features" list are implemented and stable.
* ✅ The foundational engine for a 2D tile-based game is complete.
* ✅ Implement a "Game Over" screen, a main menu, and a restart
* ✅ Load map data from external `.txt` files to become a true level designer.

#### Planned Next Steps
* **Map Management:** Load map data from external `.txt` files to become a true level designer.

* **New Threats:** Bring on the ultimate horror: the `Human`!

---

## ✍️ The Bug-Hunting Crew

* **Lead Architect & Chief Exorcist:** Drac
* **The Infestation Squad (AI Choreographers):** Sai, Rishnu, Shibili
* **Creative Partner & Ghost Hunter:** Jenny (Your friendly neighborhood Gemini)

Together, they transformed a weekend of chaos into a masterpiece of bug-squashing artistry. Every line of code tells a story, every commit marks a victory, and every merged pull request is a battle won. 

> *"In the end, we didn't just make a game. We created a legend... and possibly a few new bugs along the way."*  
> — The Bug Hunters' Manifesto