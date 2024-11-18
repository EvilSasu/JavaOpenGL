# Overview

This project utilizes Java with OpenGL to create a simple 3D graphics engine. The core components of the engine are structured to handle terrain, models (such as trees, rocks, and bushes), particle effects (like clouds and fog), and camera control. The `MainLoop` class sets up and manages the game's main rendering loop, where objects are loaded, positioned, and rendered, while the `Kamera`, `Light`, and `Obiekt` classes handle the camera, lighting, and 3D objects respectively.

---

## `MainLoop.java`

The `MainLoop` class represents the main game loop, where the graphics and interactions are handled.

### Key Elements:
- **Terrain Setup**: Two terrain grids are loaded and positioned.
- **Object Initialization**: 
  - Trees, rocks, and bushes are randomly generated in the world.
  - Each object is created with textures and models, including specific properties like scale and rotation.
- **Particle Systems**: Particle effects for clouds and fog are generated and rendered.
- **Camera Movement**: The camera's position is updated based on keyboard input (W, A, S, D, X, Z).
- **Light**: A light source is created at a fixed position and affects the objects in the scene.
- **Key Bindings**: 
  - `N`: Turns off clouds and fog.
  - `C`: Toggles clouds on.
  - `F`: Toggles fog on.

### Main Game Loop:
- The main loop processes camera movement, updates particle systems, and renders terrains.
- Depending on user input, the particle systems for clouds and fog are generated, and terrain rendering is updated.
- The camera, objects, and particles are rendered in each frame.
  
---

## `Kamera.java`

The `Kamera` class handles the camera's position and movement within the 3D world.

### Key Functions:
- **`move()`**: Updates the camera position based on keyboard input (`W`, `A`, `S`, `D`, `X`, `Z`).
- **Position Variables**: 
  - `pitch`, `yaw`, and `roll` represent the camera's rotation.
  - The camera's position is stored as a `Vector3f`.

---

## `Light.java`

The `Light` class defines a light source in the scene.

### Key Functions:
- **Constructor**: Initializes the light's position and color.
- **Getter/Setters**: Allows access and modification of the light's position and color.

---

## `Obiekt.java`

The `Obiekt` class represents a 3D object (e.g., trees, rocks) in the world.

### Key Properties:
- **Model**: The 3D model associated with the object.
- **Position**: The object's position in the world (stored as a `Vector3f`).
- **Rotation and Scale**: The rotation (X, Y, Z) and scale of the object.

### Methods:
- **`changePosition()`**: Moves the object by a given delta (dx, dy, dz).
- **`changeRotation()`**: Rotates the object by a given delta (dx, dy, dz).

---

## `Terrain.java`

The `Terrain` class generates and manages the terrain grid in the game.

### Key Properties:
- **Grid Size**: The terrain's size and vertex count.
- **Texture**: The texture used for rendering the terrain.

### Methods:
- **`generateTerrain()`**: Generates the terrain's vertices, normals, and texture coordinates based on the terrain's grid size.

---

## `MainRenderer`
**Purpose**: Handles the main rendering process for both objects and terrains in the game.

**Key Features**:
- Initializes shaders and projection matrices for rendering.
- Prepares and renders objects (Obiekt) and terrains (Terrain), calling appropriate shader programs.
- Enables and disables culling to optimize rendering performance (removes objects facing away from the camera).
- Clears the screen and sets up the depth test before each render.
- Uses a map (obiekty) to group objects by their textures for efficient batch rendering.

---

## `OBJLoader`
**Purpose**: Loads 3D models from .obj files and prepares them for rendering.

**Key Features**:
- Reads vertex, texture, and normal data from an OBJ file.
- Stores vertex positions, texture coordinates, and normals in lists.
- Processes face data, which includes the index references for vertices, textures, and normals.
- Converts the collected data into arrays and loads them into a RawModel using a loader.

---

## `ObiektRenderer`
**Purpose**: Responsible for rendering individual 3D objects (Obiekt).

**Key Features**:
- Prepares and binds the model and texture for each object.
- Supports transparency by disabling culling when necessary.
- Loads transformation matrices (position, rotation, scale) for each object.
- Uses GL11 and GL30 to bind vertex data and textures, then renders the object using GL11.glDrawElements.

---

## `Renderer`
**Purpose**: A simplified rendering class, similar to ObiektRenderer, but with fewer features.

**Key Features**:
- Enables depth testing to ensure correct rendering order.
- Prepares and renders a single object (Obiekt), applying a transformation matrix and texture properties before drawing.

---

## `RawModel` 
**Purpose**: Represents a simple 3D model that consists of raw vertex data without any textures.  

**Key Features**:  
- Holds the vertex array object (VAO) ID for OpenGL rendering.  
- Stores the number of vertices in the model.  
- Provides methods to access VAO ID and vertex count.

---

## `TexturedModel` 
**Purpose**: Represents a model with an applied texture, combining `RawModel` with texture data.  

**Key Features**:  
- Contains a `RawModel` for vertex data.  
- Includes a `TextureInfo` object for managing texture data.  
- Provides methods to access the `RawModel` and `TextureInfo` objects.

---

## `Particle`  
**Purpose**: Represents a single particle in the particle system, which can move, be affected by gravity, and have a defined lifetime.  

**Key Features**:  
- Tracks the particle's position, velocity, and gravity effect.  
- Manages the particle’s lifetime and rotation.  
- Stores the particle's scale and texture.  
- Provides methods to update the particle’s position and access all properties.

---

## `ParticleManager`  
**Purpose**: Manages and updates all particles in the system.  

**Key Features**:  
- Organizes particles in a map categorized by texture.  
- Uses a `ParticleRenderer` to render particles.  
- Updates all particles by calling their `update()` method and removes expired particles.  
- Adds new particles based on texture and handles resource cleanup.

---

## `ParticleRenderer`  
**Purpose**: Responsible for rendering the particles in the system.  

**Key Features**:  
- Uses a `RawModel` (quad) for rendering the particles.  
- Utilizes a custom `ParticleShader` to render particles.  
- Updates the model-view matrix for each particle based on position, rotation, and scale.  
- Prepares OpenGL states for rendering and resets states after rendering.

---

## `ParticleShader`  
**Purpose**: Manages the shaders used for rendering particles, including the vertex and fragment shaders.  

**Key Features**:  
- Retrieves and manages uniform locations in the shader.  
- Binds attributes like particle positions to the shader program.  
- Loads model-view and projection matrices into the shader to apply transformations.

---

## `ParticleTextureInfo`  
**Purpose**: Stores information about the texture used by particles.  

**Key Features**:  
- Stores the texture ID for particle rendering.  
- Keeps track of the number of rows in the texture sheet for particle animations.  
- Provides getters and setters for the texture ID and number of rows.



## Summary

The provided code outlines a basic 3D world with random object generation, a particle system for visual effects like fog and clouds, and camera movement based on keyboard inputs. It uses OpenGL for rendering the terrain and models, and provides a simple interface for modifying the visual aspects of the scene (e.g., toggling clouds and fog). The game loop is structured to continuously update the camera position, particle effects, and object rendering in a real-time environment.

---

This structure demonstrates a typical OpenGL application in Java, combining rendering, interaction, and particle systems to create a dynamic 3D world.
