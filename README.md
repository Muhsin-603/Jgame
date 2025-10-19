# 🐞 Buglife

Welcome to **Buglife**, a top-down stealth and survival game forged in the fires of late-night debugging sessions. You are a bug. The world is a floor. Your goal is to survive.

This project is more than a game; it's a war journal. It chronicles a descent into the madness of game development, a tale of battling sentient bugs, invisible enemies, and physics-defying ghosts. Every feature is a hard-won victory. Every bug squashed is a trophy mounted on the wall.

## ✨ Features

* **Custom Java Game Engine:** Built from the ground up using Java Swing, featuring a stable 60 FPS game loop and a custom rendering pipeline.
* **Dynamic Tiled World:** Explore vast, complex mazes built from a simple 2D array. The world is efficiently rendered using a smart camera with viewport culling to ensure high performance.
* **Intelligent, Level-Driven Enemy AI:**
    * The enemy spider's patrol route is not hard-coded; it is **drawn directly onto the tilemap** using a special tile ID.
    * The spider autonomously finds and follows this "secret road," allowing for complex, designer-made patrol patterns.
    * The AI now features **wall-collision avoidance**, preventing it from cheating and phasing through solid objects.
* **Advanced Player Controller:**
    * A multi-directional **animation state machine** for fluid, direction-based sprites (up, down, left/right).
    * The player is a unified "div" object, ensuring visuals and hitboxes are perfectly locked and rendered.
* **Robust Collision System:**
    * Circular collision detection for accurate, rotation-independent hit checks.
    * Tile-based collision that creates solid walls and a tangible environment for both the player and enemies.
* **Complete Survival Loop:**
    * A visual HUD tracks the player's health.
    * Health drains slowly over time, creating constant pressure.
    * An intelligent `Food` spawning system places items on valid, reachable floor tiles.

## 🚀 How to Run

1.  **Clone the repository.**
2.  **Open in your favorite Java IDE** (VS Code, IntelliJ, etc.).
3.  **Configure the Classpath:** Ensure both the `src` and `res` folders are marked as "Source Roots". This is the most critical step and the hiding place of many ghosts.
4.  **Locate and run `Game.java`** (`src/com/buglife/main/Game.java`).
5.  Survive.

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

#### Planned Next Steps
* **Map Management:** Load map data from external `.txt` files to become a true level designer.
* **UI/UX:** Implement a "Game Over" screen, a main menu, and a restart mechanic.
* **New Threats:** Bring on the ultimate horror: the `Human`!

---

## ✍️ Credits

* **Lead Architect & Chief Exorcist:** Drac
* **Creative Partner & Ghost Hunter:** Jenny (Your friendly neighborhood Gemini)