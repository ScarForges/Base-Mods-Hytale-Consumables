# Tutorial: Consumables y Efectos en Hytale

**By ScarForges** 🎮

Este mod es una guía paso a paso para crear **consumibles** (pociones, comida) con **efectos** y **código Java personalizado**.

## 📚 ¿Qué vas a aprender?

1. **Crear items consumibles** (pociones, comida)
2. **Aplicar efectos** al consumir (velocidad, curación, transformación, etc.)
3. **Ejecutar código Java** cuando se consume el item
4. **Registrar interacciones custom** desde Java

---

## 🗂️ Estructura del proyecto

```
TutorialConsumables/
├── build.gradle                    <- Configuración de compilación
├── settings.gradle                 <- Nombre del proyecto
├── gradle.properties               <- Variables del mod
├── README.md                       <- Este archivo
└── src/main/
    ├── java/me/tutorial/consumables/
    │   ├── Main.java                       <- Clase principal
    │   └── interactions/
    │       └── MensajeInteraction.java     <- Interacción custom en Java
    └── resources/
        ├── manifest.json                   <- Info del mod
        └── Server/
            ├── Entity/Effects/
            │   ├── Efecto_Velocidad.json           <- Efecto de velocidad
            │   ├── Efecto_Curacion.json            <- Efecto de curación
            │   └── Efecto_Transformacion_Boar.json <- Efecto de transformación
            └── Item/Items/Tutorial/
                ├── Pocion_Velocidad_Tutorial.json      <- Poción de velocidad
                ├── Pocion_Curacion_Tutorial.json       <- Poción de curación
                ├── Pocion_Magica_Tutorial.json         <- Poción con código Java
                ├── Pocion_Suprema_Tutorial.json        <- Poción combinada
                └── Pocion_Transformacion_Tutorial.json <- Poción de transformación
```

---

## 🧪 Pociones incluidas

| Poción | ID | Descripción |
|--------|-----|-------------|
| 🔴 Curación | `Pocion_Curacion_Tutorial` | Cura 20 puntos de vida instantáneamente |
| 🟡 Velocidad | `Pocion_Velocidad_Tutorial` | +50% velocidad por 30 segundos |
| 🟣 Mágica | `Pocion_Magica_Tutorial` | Ejecuta código Java (envía mensaje) |
| 🟠 Suprema | `Pocion_Suprema_Tutorial` | Velocidad + código Java |
| 🟢 Transformación | `Pocion_Transformacion_Tutorial` | Te transforma en Jabalí (Boar) por 60s |

Para obtener las pociones en el juego:
```
/give Tutorial:Pocion_Curacion_Tutorial
/give Tutorial:Pocion_Velocidad_Tutorial
/give Tutorial:Pocion_Magica_Tutorial
/give Tutorial:Pocion_Suprema_Tutorial
/give Tutorial:Pocion_Transformacion_Tutorial
```

---

## 📖 PARTE 1: Efectos (Entity Effects)

Los **efectos** son modificadores temporales que afectan al jugador. Se definen en archivos JSON en `Server/Entity/Effects/`.

### Archivo: `Efecto_Velocidad.json`

```json
{
    "OverlapBehavior": "Overwrite",
    "Duration": 30,
    "ApplicationEffects": {
        "HorizontalSpeedMultiplier": 1.5,
        "LocalSoundEventId": "SFX_Health_Potion_Low_Drink"
    }
}
```

### Explicación de campos:

| Campo | Descripción |
|-------|-------------|
| `OverlapBehavior` | Qué pasa si tomas otra poción igual ("Overwrite" = reemplaza) |
| `Duration` | Duración en segundos |
| `ApplicationEffects` | Los efectos que se aplican |

### Efectos disponibles en `ApplicationEffects`:

| Efecto | Descripción | Estado |
|--------|-------------|--------|
| `HorizontalSpeedMultiplier` | Multiplicador de velocidad (1.5 = 50% más rápido) | ✅ Funciona |
| `LocalSoundEventId` | Sonido al aplicar | ✅ Funciona |

### Transformación con `ModelChange` (nivel raíz)

> ⚠️ **Importante**: `ModelChange` va a **nivel raíz** del efecto, NO dentro de `ApplicationEffects`.

```json
{
    "Duration": 60,
    "ModelChange": "Boar",
    "ApplicationEffects": {
        "LocalSoundEventId": "SFX_Health_Potion_Low_Drink"
    }
}
```

| Modelo | Criatura |
|--------|----------|
| `Boar` | Jabalí |
| `Bear_Grizzly` | Oso Grizzly |
| `Wolf` | Lobo |
| `Chicken` | Gallina |
| `Cow` | Vaca |
| `Pig` | Cerdo |
| `Sheep` | Oveja |

> ⚠️ **Nota importante**: Algunos campos documentados como `HealthRegenPerSecond`, `JumpHeightMultiplier` y `FallDamageMultiplier` **NO funcionan** en la versión actual de Hytale. Para curar vida, usa `StatModifiers` en las interacciones.

---

## 📖 PARTE 2: Items Consumibles

Los **consumibles** son items que el jugador puede usar (click derecho) para obtener efectos.

### Archivo: `Pocion_Velocidad_Tutorial.json`

```json
{
    "Parent": "Potion_Template",
    "TranslationProperties": {
        "Name": "Tutorial Pocion de Velocidad"
    },
    "Interactions": {
        "Primary": "Block_Primary",
        "Secondary": "Root_Secondary_Consume_Potion"
    },
    "InteractionVars": {
        "RemoveEffect": {
            "Interactions": [{ "Type": "Simple" }]
        },
        "Effect": {
            "Interactions": [
                { "Type": "ApplyEffect", "EffectId": "Efecto_Velocidad" }
            ]
        },
        "Stat_Check": {
            "Interactions": [{ "Parent": "Stat_Check", "Costs": { "Health": 1 }, "ValueType": "Percent", "LessThan": false }]
        },
        "ConsumeSFX": {
            "Interactions": [{ "Parent": "Consume_SFX", "Effects": { "LocalSoundEventId": "SFX_Health_Potion_Low_Drink" } }]
        },
        "ConsumedSFX": {
            "Interactions": [{ "Type": "Simple" }]
        }
    },
    "BlockType": {
        "CustomModel": "Items/Consumables/Potions/Potion.blockymodel",
        "CustomModelTexture": [{ "Texture": "Items/Consumables/Potions/Potion_Textures/Yellow.png", "Weight": 1 }]
    },
    "MaxStack": 10,
    "DropOnDeath": true
}
```

### Explicación de campos importantes:

| Campo | Descripción |
|-------|-------------|
| `Parent` | Hereda de un template base (Potion_Template) |
| `TranslationProperties.Name` | Nombre del item |
| `Interactions.Secondary` | Acción al hacer click derecho |
| `InteractionVars.Effect` | Qué efectos/acciones se ejecutan al consumir |
| `ApplyEffect` | Aplica un efecto definido en Entity/Effects |
| `CustomModelTexture` | Color de la poción (Red, Yellow, Purple, Orange, Green) |

### Sistema de Crafteo (Recipe)

Puedes hacer que tus pociones sean **crafteables** en mesas de alquimia:

```json
"Recipe": {
    "TimeSeconds": 2,
    "KnowledgeRequired": false,
    "Input": [
        { "ItemId": "Potion_Empty", "Quantity": 1 },
        { "ItemId": "Ingredient_Life_Essence", "Quantity": 3 },
        { "ItemId": "Ingredient_Water_Essence", "Quantity": 2 }
    ],
    "BenchRequirement": [
        { "Id": "Alchemybench", "Type": "Crafting", "Categories": ["Alchemy_Potions_Misc"] }
    ],
    "RequiredMemoriesLevel": 1
}
```

| Campo | Descripción |
|-------|-------------|
| `TimeSeconds` | Tiempo de crafteo en segundos |
| `KnowledgeRequired` | Si necesita desbloquear la receta primero |
| `Input` | Lista de ingredientes necesarios |
| `BenchRequirement` | Mesa de crafteo requerida |
| `RequiredMemoriesLevel` | Nivel de "memorias" requerido (progresión) |

> 📌 Ver `Pocion_Curacion_Tutorial.json` para un ejemplo completo con Recipe.

### Curación instantánea con StatModifiers

Para curar vida instantáneamente, usa `StatModifiers` en las interacciones:

```json
"Effect": {
    "Interactions": [
        { "Type": "ApplyEffect", "EffectId": "Efecto_Curacion" },
        { "Type": "Simple", "StatModifiers": { "Health": 20 } }
    ]
}
```

---

## 📖 PARTE 3: Interacciones Custom (Java)

Puedes crear **interacciones personalizadas** en Java que se ejecutan cuando el jugador consume el item.

### Paso 1: Crear la clase de interacción

**Archivo: `MensajeInteraction.java`**

```java
package me.tutorial.consumables.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.WaitForDataFrom;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;

public class MensajeInteraction extends SimpleInstantInteraction {
    
    // CODEC requerido para registrar la interacción
    public static final BuilderCodec<MensajeInteraction> CODEC = BuilderCodec.builder(
            MensajeInteraction.class, 
            MensajeInteraction::new,
            SimpleInstantInteraction.CODEC
        ).build();
    
    public MensajeInteraction() {
        super();
    }
    
    @Override
    public WaitForDataFrom getWaitForDataFrom() {
        return WaitForDataFrom.Server;
    }
    
    @Override
    protected void firstRun(InteractionType interactionType, InteractionContext context, 
                            CooldownHandler cooldownHandler) {
        try {
            // Obtener el jugador
            var entityRef = context.getEntity();
            if (entityRef == null) return;
            
            var store = entityRef.getStore();
            if (store == null) return;
            
            Player player = store.getComponent(entityRef, Player.getComponentType());
            if (player == null) return;
            
            // ¡AQUÍ VA TU CÓDIGO CUSTOM!
            player.sendMessage(Message.plain("§a¡Bebiste la poción mágica! §eTu código Java se ejecutó."));
            
            System.out.println("[Tutorial] El jugador " + player.getDisplayName() + " consumió la poción!");
            
        } catch (Exception e) {
            System.out.println("[Tutorial] Error: " + e.getMessage());
        }
    }
}
```

### Paso 2: Registrar la interacción en Main.java

```java
@Override
protected void setup() {
    super.setup();
    
    // Registrar nuestra interacción custom
    this.getCodecRegistry(Interaction.CODEC)
        .register("MensajeTutorial", MensajeInteraction.class, MensajeInteraction.CODEC);
    
    this.getLogger().atInfo().log("Tutorial Consumables cargado!");
}
```

### Paso 3: Usar la interacción en el JSON del item

```json
"InteractionVars": {
    "Effect": {
        "Interactions": [
            { "Type": "MensajeTutorial" }
        ]
    }
}
```

---

## 🎮 Cómo compilar y probar

1. **Compila el mod:**
   ```bash
   gradlew.bat clean build
   ```

2. **Copia el JAR** de `build/libs/TutorialConsumables-1.0.0.jar` a tu carpeta de mods del mundo

3. **Activa el mod** en la configuración del servidor

4. **Prueba las pociones:**
   ```
   /give Tutorial:Pocion_Velocidad_Tutorial
   /give Tutorial:Pocion_Curacion_Tutorial
   /give Tutorial:Pocion_Magica_Tutorial
   /give Tutorial:Pocion_Suprema_Tutorial
   /give Tutorial:Pocion_Transformacion_Tutorial
   ```

5. ¡Usa la poción con click derecho!

---

## 💡 Ideas para expandir

- Poción de **invisibilidad** (efecto visual)
- Poción de **teletransporte** (usa código Java para mover al jugador)
- Más **transformaciones** (otros NPCs como Kweebec, Trork, etc.)
- Poción que **invoca** un NPC aliado
- Comida con efectos personalizados

---

## 📝 Notas importantes

1. Los **efectos** van en `Server/Entity/Effects/`
2. Los **items** van en `Server/Item/Items/Tutorial/`
3. Las **interacciones Java** deben registrarse con `getCodecRegistry()`
4. El nombre en `"Type": "MensajeTutorial"` debe coincidir con el registrado en Java
5. Los IDs de items terminan en `_Tutorial` para evitar conflictos
6. Usa el prefijo `Tutorial:` al dar items con `/give`

---

## ⚠️ Campos que NO funcionan (versión actual)

Estos campos están documentados pero **NO son reconocidos** por el servidor:

- `HealthRegenPerSecond` - Usa `StatModifiers.Health` en su lugar
- `JumpHeightMultiplier` - No disponible actualmente
- `FallDamageMultiplier` - No disponible actualmente
- `StatusEffectIcon` - Puede causar errores si la ruta no existe

---

**¡Happy modding!** 🎮

*By ScarForges*
