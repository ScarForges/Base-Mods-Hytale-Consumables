package me.tutorial.consumables.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.WaitForDataFrom;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.awt.Color;

/**
 * ===========================================
 *     INTERACCIÓN CUSTOM: MENSAJE
 * ===========================================
 * 
 * Esta clase muestra cómo crear una interacción personalizada
 * que se ejecuta cuando el jugador consume un item.
 * 
 * IMPORTANTE: Debe extender SimpleInstantInteraction
 * 
 * PASOS PARA CREAR UNA INTERACCIÓN:
 * 1. Extender SimpleInstantInteraction
 * 2. Crear el CODEC estático (requerido para serialización)
 * 3. Implementar firstRun() con tu lógica
 * 4. Registrar en Main.java
 * 5. Usar en el JSON del item con {"Type": "NombreRegistrado"}
 */
public class MensajeInteraction extends SimpleInstantInteraction {
    
    // =====================================================
    // CODEC - Necesario para que Hytale pueda cargar la interacción
    // =====================================================
    // Esto es como el "serializador" de la clase.
    // Siempre sigue este patrón básico.
    public static final BuilderCodec<MensajeInteraction> CODEC = BuilderCodec.builder(
            MensajeInteraction.class, 
            MensajeInteraction::new,
            SimpleInstantInteraction.CODEC
        ).build();
    
    /**
     * Constructor - Debe llamar a super()
     */
    public MensajeInteraction() {
        super();
    }
    
    /**
     * Indica desde dónde se esperan los datos.
     * Casi siempre será WaitForDataFrom.Server
     */
    @Override
    public WaitForDataFrom getWaitForDataFrom() {
        return WaitForDataFrom.Server;
    }
    
    /**
     * =====================================================
     * firstRun() - EL MÉTODO PRINCIPAL
     * =====================================================
     * 
     * Este método se ejecuta cuando la interacción se activa.
     * Aquí va TODO tu código personalizado.
     * 
     * @param interactionType - Tipo de interacción (Primary, Secondary, etc.)
     * @param context - Contexto con información del jugador, item, etc.
     * @param cooldownHandler - Manejador de cooldowns
     */
    @Override
    protected void firstRun(InteractionType interactionType, InteractionContext context, 
                            CooldownHandler cooldownHandler) {
        try {
            // =====================================================
            // PASO 1: Obtener el jugador
            // =====================================================
            // El jugador está dentro del contexto de la interacción.
            // Debemos navegar a través de entityRef -> store -> component
            
            Ref<EntityStore> entityRef = context.getEntity();
            if (entityRef == null) {
                System.out.println("[Tutorial] Error: No hay entidad en el contexto");
                return;
            }
            
            Store<EntityStore> store = entityRef.getStore();
            if (store == null) {
                System.out.println("[Tutorial] Error: No hay store en la entidad");
                return;
            }
            
            // Obtener el componente Player de la entidad
            Player player = store.getComponent(entityRef, Player.getComponentType());
            if (player == null) {
                System.out.println("[Tutorial] Error: La entidad no es un jugador");
                return;
            }
            
            // =====================================================
            // PASO 2: ¡TU CÓDIGO AQUÍ!
            // =====================================================
            // Ahora que tienes el Player, puedes hacer lo que quieras:
            // - Enviar mensajes
            // - Teletransportar
            // - Modificar stats
            // - Invocar entidades
            // - Y mucho más...
            
            // Ejemplo: Enviar un mensaje al jugador
            // Usa Message.raw() con .color() para colores
            player.sendMessage(Message.raw("Pocion consumida! Tu codigo Java se ejecuto correctamente.").color(Color.GREEN));
            player.sendMessage(Message.raw("Esto es un mensaje desde MensajeInteraction.java").color(Color.GRAY));
            
            // Ejemplo: Mostrar en la consola del servidor
            System.out.println("[Tutorial] ========================================");
            System.out.println("[Tutorial] El jugador " + player.getDisplayName() + " consumio la pocion!");
            System.out.println("[Tutorial] ========================================");
            
            // =====================================================
            // EJEMPLOS DE OTRAS COSAS QUE PODRÍAS HACER:
            // =====================================================
            
            // Obtener posición del jugador:
            // var position = context.getPosition();
            // double x = position.x();
            // double y = position.y();
            // double z = position.z();
            
            // Teletransportar al jugador:
            // player.teleport(x, y + 10, z);  // Sube 10 bloques
            
            // Obtener el mundo:
            // var world = entityRef.getStore().getWorld();
            
        } catch (Exception e) {
            System.out.println("[Tutorial] Error en MensajeInteraction: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
