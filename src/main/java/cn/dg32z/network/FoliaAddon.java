package cn.dg32z.network;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
@Getter
@Setter
public final class FoliaAddon extends JavaPlugin {

    boolean isPAPI;
    @Getter
    private static FoliaAddon instance;
    private HookUtil hookUtil;

    @Override
    public void onEnable() {
        instance = this;
        isPAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (isPAPI) {
            hookUtil = new HookUtil();
            new HookUtil().register();
        }
    }
    
@Override
    public void onDisable() {
        instance = null;
        if (isPAPI) {
            new HookUtil().unregister();
        }
    }
}
