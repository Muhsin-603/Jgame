# �️ Lullaby Down Below
A 2D top-down psychological stealth-horror game.

This project, originally conceived as Buglife, has evolved! We're now building **Lullaby Down Below**, an atmospheric horror game using the same custom Java Swing engine. Players take the role of a lost baby crawling through the dark lower floors of an abandoned nursery, guided by a glowing snail and hunted by spiders, all while following the faint sound of a lullaby.

## ✨ Features (Current & Planned)

* **Custom Java Game Engine:**
    * Built from the ground up using Java Swing
    * Stable 60 FPS game loop with tilemap loading from .txt files
    * Dynamic camera system with viewport culling
    * Advanced resource management

* **Full UI & Game Loop:**
    * Complete state machine (MAIN_MENU, PLAYING, GAME_OVER)
    * Interactive menu with custom fonts and background
    * Seamless game flow with proper state transitions
    * Robust reset() mechanism for game restart

* **Atmospheric Elements:**
    * Background music for menu and gameplay
    * Sound effects for key actions (selection, eating, getting webbed)
    * Dynamic shadow system for stealth mechanics
    * Environmental storytelling through level design

* **Core Gameplay Elements:**
    * Vulnerable protagonist (baby) with unique movement constraints
    * Companion NPC system (glowing snail guide) - *Planned*
    * Hunger/Cry mechanic affecting stealth - *Planned*
    * Interactive environment (sticky floors, water, shadows)

* **Advanced Enemy AI:**
    * Multi-state behavior system (patrol, chase, return)
    * Level-defined patrol paths
    * Line-of-sight and sound detection
    * Intelligent pathfinding
    * Wall avoidance and obstacle navigation
    * Dynamic state transitions based on player actions
* **Stealth & Survival Mechanics:**
    * Shadow-based hiding system
    * Toy decoy placement - *Planned*
    * Web trap avoidance
    * Sound-based detection system
    * Progressive difficulty scaling

* **Resource Management:**
    * Hunger system affecting crying mechanics - *Planned*
    * Food collection and placement
    * Strategic resource distribution
    * Risk/reward exploration incentives

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

## 📜 Project History: The Metamorphosis

This project began as Buglife, a survival game about a bug. Through extensive development and bug-hunting (see previous README revisions for the epic saga), a robust 2D top-down engine was forged. The project has now pivoted to Lullaby Down Below, repurposing the existing engine to tell a darker, more atmospheric story.

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

## 🎮 Game Design Document (Lullaby Down Below Summary)

#### Game Overview
* **Genre:** 2D Top-Down Stealth Horror / Survival Adventure
* **Core Concept:** A baby falls down a chimney into abandoned lower floors and must crawl back up, guided by a glowing snail and hunted by spiders, following a lullaby.
* **Key Mechanics:** 
    * Stealth (hiding, noise)
    * Survival (hunger/cry)
    * AI Detection (sound, light, movement)
    * Companion NPC (snail)
    * Simple Resource Management (food)

*(See full GDD for details on levels, story, etc.)*

#### Planned Next Steps
* **New Threats (THE HUMAN!):** The ultimate horror
* **Sound Design:** Add atmospheric music and sound effects
* **Additional Mechanics:** More environmental hazards and power-ups

---

## ✍️ The Crew

* **Lead Architect & Chief Exorcist:** Drac
* **The Infestation Squad (Original AI)::** Sai, Rishnu, Shibili
* **Concept & Story (Lullaby Down Below):** Saivivk M.V
* **Narrative & Design Support:** Mira (ChatGPT)
* **Creative Partner & Ghost Hunter:** Jenny (Your friendly neighborhood Gemini)

Together, they transformed a weekend project into an atmospheric horror experience. Every line of code tells a story, every commit marks a milestone, and every merged pull request brings us closer to the haunting lullaby that echoes through the depths below.