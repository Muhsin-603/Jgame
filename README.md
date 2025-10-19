# 🐞 Buglife

Welcome to **Buglife** — a top-down stealth and survival game forged in pure **Java**.  
You are a bug. The world is a floor. Your mission: **survive the madness**.

This project is more than a game; it's a **war journal** — a late-night descent into the absurd realm of game development.  
Every pixel, every bug, every stack trace is part of the legend.  
We didn't just code — we fought phantoms, debugged insanity, and built something beautiful out of chaos.

## ✨ Features

### 🧠 Custom Java Game Engine
- Built entirely from scratch using **Java Swing**
- Stable **60 FPS** game loop and custom rendering pipeline

### 🌍 Dynamic Tiled World
- Explore a vast, tile-based world rendered through a smart **camera system** with efficient culling

### 🕷 Intelligent Enemy AI
- The spider enemy patrols a **user-defined path** directly on the tilemap
- Features **wall-collision avoidance** and adaptive behavior — it's not just an enemy, it's *a resident of the floor*

### 🎮 Advanced Player Controller
- Multi-directional **animation state machine** (up, down, left/right)
- Unified visual and collision "div" system ensuring perfect sync between sprite and hitbox

### 💥 Robust Collision System
- **Circular collision detection** for precise, rotation-independent interactions
- **Tile-based solid wall detection** for tangible, realistic boundaries

### ❤️ Complete Survival Loop
- Real-time **HUD** that tracks player health
- Gradual health decay introduces constant tension
- Smart `Food` spawning system ensures items appear only on valid, reachable floor tiles

## 🚀 How to Run

1. Clone this repository
   ```bash
   git clone https://github.com/Muhsin-603/Jgame.git
   ```

2. Open it in your preferred Java IDE (VS Code, IntelliJ, Eclipse, etc.)

3. Set up Classpaths:
   - Mark both `src` and `res` folders as Source Roots (Critical step!)

4. Run the game:
   - Execute `Game.java` from `src/com/buglife/main/Game.java`
   - Play. Survive. Adapt.

## 📜 The Bug Hunter's Log

*A record of battles fought in the dark corners of logic.*

### 🧩 The Pixel Prison
Our hero was trapped in invisible walls due to a misaligned hitbox.  
One single `-1` saved him from eternal confinement.

### 👻 The Ghost Plate
A humble meal caused a `NullPointerException` apocalypse.  
Lesson learned: never draw what no longer exists.

### 💃 The Disco Inferno
Our enemies danced like it was 1987 — spinning, moonwalking, defying gravity.  
After a crash course in trigonometry, they finally learned to walk straight.

### 🌀 The Dimensional Warp
The spider divided by zero, ascending to the 2147483087th dimension.  
A new failsafe AI brain was surgically installed.

### 🎨 The Cursed Artwork (Final Boss)
Corrupted PNG sprites brought the engine to its knees.  
No code could save us — only digital exorcism.  
Every asset was manually re-saved, one by one, to purge the curse.

## 🎮 Game Design Document — Revision 5

### 🎯 Game Overview
- **Title:** Buglife
- **Genre:** Top-down Stealth / Survival
- **Core Concept:** Survive as a bug in a dangerous human household

### 🧱 Current Progress
- ✅ Fully functional Java engine with tile-based rendering and physics
- ✅ All core systems (AI, collision, survival mechanics) implemented and stable
- ✅ Optimized performance and refined sprite animation pipeline

### 🔮 Planned Next Steps
- **Map Management:** Load map layouts from external `.txt` files for level customization
- **World Interaction:** Add new tile types with environmental effects (e.g., sticky honey, water)
- **UI/UX:** Introduce Game Over screen, restart button, and smoother transitions
- **New Threats:** Enter the final stage — humanity itself. The Human arrives.

## 🧑‍💻 Credits

| Role | Name |
|------|------|
| Lead Architect & Chief Exorcist | Drac |
| Creative Partner & Ghost Hunter | Jenny (Your friendly neighborhood Gemini) |

Together, they built not just a game — but a chronicle of chaos, courage, and caffeine. ☕

> *"Every bug you fix leaves a scar — a story — a reminder that you've fought something impossible and won."*  
> — Buglife Dev Log, Entry #42