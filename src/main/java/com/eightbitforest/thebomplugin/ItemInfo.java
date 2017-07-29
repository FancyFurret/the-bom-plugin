package com.eightbitforest.thebomplugin;

public class ItemInfo {
    private String registryName;
    private int damageValue;

    public String getRegistryName() {
        return registryName;
    }

    public int getDamageValue() {
        return damageValue;
    }

    public ItemInfo(String registryName, int damageValue) {
        this.registryName = registryName;
        this.damageValue = damageValue;
    }
}
