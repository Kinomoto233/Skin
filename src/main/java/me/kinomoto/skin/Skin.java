package me.kinomoto.skin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Skin.MODID, name = Skin.NAME, version = Skin.VERSION)
public class Skin {
    static final String MODID = "skin";
    static final String NAME = "Skin";
    static final String VERSION = "300";
    private int x = 0, y = 0;
    private Minecraft mc = Minecraft.getMinecraft();

    @Mod.EventHandler
    public void on(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void on(GuiScreenEvent.DrawScreenEvent.Post e) {
        if (e.getGui() instanceof GuiContainer) {
            GuiContainer gui = (GuiContainer) e.getGui();
            Slot slot = gui.getSlotUnderMouse();

            //是否是有皮肤的物品
            if (slot == null) return;
            ItemStack item = slot.getStack();
            NBTTagCompound nbt = item.getTagCompound() == null ? new NBTTagCompound() : item.getTagCompound();
            final String tagName = "skin";
            if (!nbt.hasKey(tagName)) return;

            //读取信息,绘制图案
            String[] skinArgs = nbt.getString(tagName).split("\\|");
            ResourceLocation texture = new ResourceLocation(MODID, "textures/" + skinArgs[0]);
            int offsetX = Integer.parseInt(skinArgs[1]), offsetY = Integer.parseInt(skinArgs[2]), width = Integer.parseInt(skinArgs[3]), height = Integer.parseInt(skinArgs[4]);
            drawQuads(texture, x + offsetX, y + offsetY, width, height, gui);
        }
    }

    private void drawQuads(ResourceLocation texture, int x, int y, int width, int height, GuiContainer gui) {
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        gui.drawTexturedModalRect(x, y, 0, 0, width, height);
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
    }

    @SubscribeEvent
    public void on(RenderTooltipEvent.PostText e) {
        //更新渲染坐标
        x = e.getX();
        y = e.getY();
    }
}
