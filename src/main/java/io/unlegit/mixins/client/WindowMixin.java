package io.unlegit.mixins.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.platform.IconSet;
import com.mojang.blaze3d.platform.Window;

import io.unlegit.UnLegit;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.IoSupplier;

@Mixin(Window.class)
public class WindowMixin
{
    @Redirect(method = "setIcon", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/IconSet;getStandardIcons(Lnet/minecraft/server/packs/PackResources;)Ljava/util/List;"))
    public List<IoSupplier<InputStream>> setupIcons(IconSet icons, PackResources packResources) throws IOException
    {
        try
        {
            InputStream x16 = getFile("icon_16x16.png"),
                        x32 = getFile("icon_32x32.png"),
                        x48 = getFile("icon_48x48.png"),
                        x128 = getFile("icon_128x128.png"),
                        x256 = getFile("icon_256x256.png");
            
            return List.of(() -> x16, () -> x32, () -> x48, () -> x128, () -> x256);
        }
        
        catch (IllegalArgumentException e)
        {
            return icons.getStandardIcons(packResources);
        }
    }
    
    @Redirect(method = "setIcon", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/IconSet;getMacIcon(Lnet/minecraft/server/packs/PackResources;)Lnet/minecraft/server/packs/resources/IoSupplier;"))
    public IoSupplier<InputStream> macIcons(IconSet icons, PackResources packResources) throws IOException
    {
        try
        {
            return () -> getFile("minecraft.icns");
        }
        
        catch (IllegalArgumentException e)
        {
            return icons.getMacIcon(packResources);
        }
    }
    
    private InputStream getFile(String name) throws IllegalArgumentException
    {
        InputStream stream = UnLegit.class.getClassLoader().getResourceAsStream("assets/unlegit/icons/" + name);
        if (stream == null) throw new IllegalArgumentException();
        return stream;
    }
}
