package modernwarfare.common;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRope extends TileEntity
{
    public int delay;

    public TileEntityRope()
    {
        delay = 5;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (delay == 0)
        {
            if (worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == 0 || worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == Block.snow.blockID)
            {
                worldObj.setBlock(xCoord, yCoord - 1, zCoord, ModernWarfare.blockRope.blockID);
                worldObj.setBlockMetadataWithNotify(xCoord, yCoord - 1, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), 3);
                delay--;
            }
        }
        else if (delay > 0)
        {
            delay--;
        }

        super.updateEntity();
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        delay = nbttagcompound.getShort("Delay");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("Delay", (short)delay);
    }
}
