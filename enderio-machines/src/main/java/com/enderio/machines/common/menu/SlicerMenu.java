package com.enderio.machines.common.menu;

import com.enderio.machines.common.blockentity.SlicerBlockEntity;
import com.enderio.machines.common.init.MachineMenus;
import com.enderio.machines.common.menu.base.MachineMenu;
import com.enderio.machines.common.menu.base.PoweredMachineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

public class SlicerMenu extends PoweredMachineMenu<SlicerBlockEntity> {
    public static int INPUTS_INDEX = 3;
    public static int INPUT_COUNT = 6;
    public static int LAST_INDEX = 9;

    public SlicerMenu(int pContainerId, @Nullable SlicerBlockEntity blockEntity, Inventory inventory) {
        super(MachineMenus.SLICE_N_SPLICE.get(), pContainerId, blockEntity, inventory);
        
        if (blockEntity != null) {
            // Capacitor slot
            addSlot(new MachineSlot(getMachineInventory(), blockEntity.getCapacitorSlot(), 8, 89))
                .setBackground(InventoryMenu.BLOCK_ATLAS, EMPTY_CAPACITOR_SLOT);

            // Tool inputs TODO: Shadow slots to show compatible tools?
            addSlot(new MachineSlot(getMachineInventory(), SlicerBlockEntity.AXE, 48, 28));
            addSlot(new MachineSlot(getMachineInventory(), SlicerBlockEntity.SHEARS, 66, 28));

            for (int i = 0; i < 6; i++) {
                addSlot(new MachineSlot(getMachineInventory(), SlicerBlockEntity.INPUTS.get(i), 38 + 18 * (i % 3), i < 3 ? 52 : 70));
            }
            addSlot(new MachineSlot(getMachineInventory(), SlicerBlockEntity.OUTPUT, 128, 61));
        }

        addPlayerInventorySlots(8,126);
    }

    public float getCraftingProgress() {
        if (getBlockEntity() == null) {
            throw new IllegalStateException("BlockEntity is null");
        }

        return getBlockEntity().getCraftingProgress();
    }

    public static SlicerMenu factory(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (entity instanceof SlicerBlockEntity castBlockEntity) {
            return new SlicerMenu(pContainerId, castBlockEntity, inventory);
        }

        LogManager.getLogger().warn("couldn't find BlockEntity");
        return new SlicerMenu(pContainerId, null, inventory);
    }
}
