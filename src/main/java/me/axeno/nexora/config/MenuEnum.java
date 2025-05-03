package me.axeno.nexora.config;

import org.bukkit.event.inventory.InventoryType;

import lombok.Getter;

public class MenuEnum {
    public enum Menu {
        LIST("Listes de warp", Slots.F, InventoryType.CHEST),
        WARP_INFO("No Title", Slots.C, InventoryType.CHEST);

        @Getter
        private final String title;
        @Getter
        private final int slots;
        @Getter
        private final InventoryType type;

        Menu(String title, Slots slots, InventoryType type) {
            this.title = title;
            this.slots = slots.getSlots();
            this.type = type;
        }
    }

    public enum Slots {

        A(9),
        B(18),
        C(27),
        D(36),
        E(45),
        F(54);

        @Getter
        private final int slots;

        Slots(int slots) {
            this.slots = slots;
        }
    }
}
