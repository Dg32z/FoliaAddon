package cn.dg32z.network;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HookUtil extends PlaceholderExpansion {

    private static final long TPS_UPDATE_INTERVAL = 1000L; // 更新间隔，1秒
    private double currentTPS = 0;
    private double allTPS = 0;
    private long lastAllTPSUpdateTime = 0;
    private long lastTPSUpdateTime = 0;

    @Override
    public @NotNull String getAuthor() {
        return "Dg32z_";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "Folia";
    }

    @Override
    public @NotNull String getVersion() {
        return FoliaAddon.getInstance().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        return switch (params.toLowerCase()) {
            case "tps" -> String.format("%.1f", getCurrentTPS(player));
            case "tps_all" -> String.format("%.1f", getAllTPS());
            default -> null;
        };
    }


    // 这个东西的意义是在于这玩意一直读会有性能问题，所以做了个延迟update
    private void updateAllTPS() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAllTPSUpdateTime >= TPS_UPDATE_INTERVAL) {
            double[] tps = Bukkit.getTPS();
            if (tps != null && tps.length > 0) {
                allTPS = tps[0];
            }
            lastAllTPSUpdateTime = currentTime;
        }
    }

    private void updateTPS(OfflinePlayer player) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTPSUpdateTime >= TPS_UPDATE_INTERVAL) {
            double[] tps = player.isOnline() ? Bukkit.getServer().getTPS(Objects.requireNonNull(player.getLocation()).getChunk()) : Bukkit.getTPS();
            if (tps != null && tps.length > 0) {
                currentTPS = tps[0];
            }
            lastTPSUpdateTime = currentTime;
        }
    }

    public double getCurrentTPS(OfflinePlayer player) {
        updateTPS(player);
        return currentTPS;
    }

    public double getAllTPS() {
        updateAllTPS();
        return allTPS;
    }
}
