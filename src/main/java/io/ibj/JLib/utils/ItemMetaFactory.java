package io.ibj.JLib.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * io.ibj.MattShops.utils
 * Created by Joe
 * Project: MattShops
 */
public class ItemMetaFactory {

    ItemStack k;
    ItemMeta m;
    List<String> lore;

    public ItemMetaFactory(ItemStack k){
        this.k = k;
        if(k != null && k.getType() != Material.AIR) {
            this.m = k.getItemMeta();
            if (m == null) {
                m = Bukkit.getItemFactory().getItemMeta(k.getType());
            }
            if (m.hasLore()) {
                lore = m.getLore();
            } else {
                lore = new ArrayList<String>();
            }
        }
        else
        {
            m = Bukkit.getItemFactory().getItemMeta(Material.GRASS);//Basic Item Meta, will be nulled since k is null
        }
    }

    public void set(){
        if(k != null) {
            m.setLore(lore);
            k.setItemMeta(m);
        }
    }

    public ItemMetaFactory setDisplayName(String name){
        m.setDisplayName(name);
        return this;
    }

    public ItemMetaFactory setLore(List<String> lore){
        m.setLore(lore);
        this.lore = lore;
        return this;
    }

    public ItemMetaFactory clearLore(){
        lore = new ArrayList<String>();
        return this;
    }

    public ItemMetaFactory addToLore(String s){
        lore.add(s);
        return this;
    }


    public ItemMetaFactory addEnchantment(Enchantment e, Integer level, boolean ambient){
        m.addEnchant(e,level,ambient);
        return this;
    }

    public ItemMetaFactory removeEnchantment(Enchantment e){
        m.removeEnchant(e);
        return this;
    }

    public ItemMetaFactory stripEnchantments(){
        m.getEnchants().clear();
        return this;
    }

    public ItemMetaFactory clean(boolean removeEnchantments){
        m.setDisplayName(null);
        m.setLore(null);
        lore = new ArrayList<>();
        if(removeEnchantments) {
            for (Enchantment e : m.getEnchants().keySet()) {
                m.removeEnchant(e);
            }
        }
        return this;
    }

    public ItemMetaFactory clean(){
        return clean(true);
    }

    public static ItemMetaFactory create(ItemStack k){
        return new ItemMetaFactory(k);
    }

}
