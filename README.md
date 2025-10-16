# 🐞 Buglife

Welcome to **Buglife**, a top-down stealth and survival game with a dark, humorous twist.  
You are a bug. Your world is a kitchen. Your goal is to survive the horrors of a human household.  
Good luck. You'll need it.

This project is a journey into **game development from scratch** using a custom **Java Swing engine**.  
It's a tale of pixel prisons, ghost plates, and the triumphant birth of a digital universe — one late-night coding session at a time.

---

## ✨ Features

- **Custom Java Game Engine:**  
  Built from the ground up using Java Swing, featuring a stable 60 FPS game loop.

- **Dynamic Tiled World:**  
  Explore a vast, tile-based world that's much larger than the screen.  
  The level is currently hard-coded but designed for easy expansion.

- **Player-Centric Camera:**  
  A smooth camera system that follows the player, revealing the world as you explore.

- **Complex Player Controller:**
  - Smooth WASD movement  
  - Sprite animation for walking  
  - 4-way rotation to face the direction of movement  
  - Unified "div" object design — visuals and hitboxes are perfectly synced

- **Robust Collision System:**
  - Circular collision detection for accurate, rotation-independent hit checks  
  - Tile-based collision that creates solid walls and a tangible environment

- **Core Survival Loop:**
  - **Health System:** A visual HUD health bar tracks the bug's vitality  
  - **Entropy:** Health slowly drains over time, creating constant pressure  
  - **Foraging:** Edible food items spawn randomly on valid floor tiles, allowing the player to heal and extend their survival

- **Simple Enemy AI:**  
  A menacing `Spider` enemy patrols a set area, posing a constant threat.

---

## 🚀 How to Run

1. **Clone the repository:**
   ```bash
   git clone <your-repo-url>
