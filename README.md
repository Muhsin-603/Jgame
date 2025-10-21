# 🐞 Buglife

Welcome to **Buglife**, a top-down stealth and survival game forged in the fires of late-night debugging sessions. You are a bug (or perhaps something else now?). The world is a floor. Your goal is to survive.

This project is more than a game; it's a war journal. It chronicles a descent into the madness of game development, a tale of battling sentient bugs, invisible enemies, physics-defying ghosts, and stubborn sprite sheets. Every feature is a hard-won victory. Every bug squashed is a trophy mounted on the wall.

## ✨ Features

* **Custom Java Game Engine:**
    * Built from the ground up using Java Swing
    * Stable 60 FPS game loop with delta-time calculations
    * Custom rendering pipeline with sprite management
    * Advanced state management and resource handling

* **Full UI & Game Loop:**
    * Complete state machine (MAIN_MENU, PLAYING, GAME_OVER)
    * Interactive menu with custom fonts and background
    * Seamless state transitions with proper cleanup
    * Robust reset() mechanism for game restart

* **Dynamic Tiled World:**
    * Complex mazes loaded from external .txt files
    * Rapid level design capabilities
    * Smart camera system with viewport culling
    * Multiple interactive tile types

* **Intelligent, Multi-State Enemy AI:**
    * Advanced patrol system with configurable routes
    * Line-of-sight detection with vision cones
    * Dynamic state switching between patrol and chase modes
    * Smart pathfinding for returning to patrol routes
    * Smooth rotation and pixel-perfect movement
    * Wall avoidance and obstacle navigation
* **Advanced Player Controller:**
    * Multi-directional animation state machine
    * Fluid sprite animations from sprite sheets
    * Precise movement and collision detection
    * Dynamic interaction with environment

* **Stealth & Trap Mechanics:**
    * Spider web trap system with paralysis effect
    * Skill-based escape mechanics
    * Timed key-press challenge system
    * 5-second escape window
    * Progressive difficulty scaling

* **Survival Systems:**
    * Real-time health management
    * Visual HUD with status effects
    * Strategic food placement system
    * Constant tension through health decay
    * Multiple challenge levels

* **Robust Physics Engine:**
    * Circular collision detection for entity interactions
    * Tile-based environmental collision system
    * Precise hitbox calculations for all game entities
    * Smart bounded camera following
    * Optimized collision checks with spatial partitioning

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

## 📜 The Bug Hunter's Log: A Saga of Exorcism

This game was forged in the crucible of impossible bugs and late-night caffeine. We didn't just write code; we performed exorcisms on sentient lines of logic. Let the record show the demons we have slain:

### Setup Nightmares
Battled invisible files, incorrect package declarations, misplaced .java files, and IDEs that refused to acknowledge the existence of entire folders (src, res). The .classpath file became both our most hated enemy and our most trusted weapon.

### The Sprite Scavenger Hunt
Fought countless NullPointerExceptions and crashes caused by incorrect file paths, case-sensitive filename typos, corrupted PNG metadata (readMetadata, decodeImage), and the vengeful .classpath file rendering entire asset folders invisible.

### The Collision Conundrum
Overcame the paradox of rotated visual sprites versus static rectangular hitboxes (solved with circular collision), only to face invisible hitboxes due to size mismatches and characters trapped in "pixel-perfect prisons" by off-by-one boundary checks.

### The AI Uprising
Our spiders developed existential crises (checking their own state for WEBBED), suffered amnesia (forgetting crucial methods like getRadius), got stuck in anxiety loops (pacing back and forth), spawned inside walls, divided by zero resulting in dimensional warping (NaN/Infinity coordinates), and achieved perfect stealth (random invisibility bugs).

* **The Ghost in the Machine (The `.classpath` Haunting):** The most stubborn ghost of all. It hid in the IDE's configuration, making our `res` folder invisible to the game engine. It took multiple rituals and a manual file creation to finally evict this poltergeist. **Status: EVICTED.**

* **The Disco Spider & The Moonwalk:** Our enemies were born with a rebellious spirit, spinning in place and moonwalking in the wrong direction. We battled their inverted compasses and taught them the laws of trigonometry. **Status: Choreographed into submission.**

## 🎮 Game Design Document (Revision 9)

#### Game Overview
* **Title:** Buglife
* **Genre:** Top-down stealth/survival
* **Core Concept:** Survival of a small entity in a human household

#### Current Progress
* ✅ All core features from the "Features" list are implemented and stable
* ✅ Player character, animations, AI, UI, and world interaction significantly upgraded
* ✅ Advanced stealth mechanics and trap systems fully operational
* ✅ Complex state machine and UI system completed

#### Planned Next Steps
* **New Threats (THE HUMAN!):** The ultimate horror
* **Sound Design:** Add atmospheric music and sound effects
* **Additional Mechanics:** More environmental hazards and power-ups

---

## ✍️ The Crew

* **Lead Architect & Chief Exorcist:** Drac
* **The Infestation Squad (AI Choreographers):** Sai, Rishnu, Shibili
* **Creative Partner & Ghost Hunter:** Jenny (Your friendly neighborhood Gemini)

Together, they transformed a weekend of chaos into a masterpiece of bug-squashing artistry. Every line of code tells a story, every commit marks a victory, and every merged pull request is a battle won. 

> *"In the end, we didn't just make a game. We created a legend... and possibly a few new bugs along the way."*  
> — The Bug Hunters' Manifesto