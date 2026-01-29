package me.tutorial.consumables;

import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import me.tutorial.consumables.interactions.MensajeInteraction;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.logging.Level;

/**
 * ===========================================
 *     TUTORIAL: CONSUMABLES Y EFECTOS
 * ===========================================
 * 
 * Esta es la clase principal del mod.
 * Aquí registramos nuestras interacciones custom.
 * 
 * IMPORTANTE: Esta clase extiende JavaPlugin de Hytale
 */
public class Main extends JavaPlugin {
    
    /**
     * Constructor - Requerido por Hytale
     * Debe recibir JavaPluginInit y pasarlo a super()
     */
    public Main(@NonNullDecl JavaPluginInit init) {
        super(init);
    }
    
    /**
     * Método setup() - Se ejecuta cuando el mod se carga
     * 
     * Aquí es donde registramos todas nuestras interacciones custom.
     * Las interacciones son las acciones que se ejecutan cuando
     * el jugador usa un item consumible.
     */
    @Override
    protected void setup() {
        super.setup();
        
        // =====================================================
        // PASO 1: Registrar interacciones custom
        // =====================================================
        // 
        // Para registrar una interacción usamos:
        //   getCodecRegistry(Interaction.CODEC).register(NOMBRE, CLASE, CODEC)
        //
        // - NOMBRE: El nombre que usaremos en el JSON ("Type": "NombreAqui")
        // - CLASE: La clase Java de nuestra interacción
        // - CODEC: El codec de serialización de la clase
        
        this.getCodecRegistry(Interaction.CODEC)
            .register("MensajeTutorial", MensajeInteraction.class, MensajeInteraction.CODEC);
        
        // =====================================================
        // PASO 2: Log para confirmar que el mod cargó
        // =====================================================
        this.getLogger().atInfo().log("========================================");
        this.getLogger().atInfo().log("   Tutorial Consumables v1.0.0");
        this.getLogger().atInfo().log("   Mod cargado correctamente!");
        this.getLogger().atInfo().log("========================================");
        this.getLogger().atInfo().log("Interacciones registradas:");
        this.getLogger().atInfo().log("  - MensajeTutorial");
    }
}
