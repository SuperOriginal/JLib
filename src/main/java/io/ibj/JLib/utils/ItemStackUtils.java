package io.ibj.JLib.utils;


import com.google.gson.*;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Joe on 4/3/2014.
 * As designed by Joe Hirschfeld for Original Cloud.
 */
public class ItemStackUtils {

    public static ItemStack fromBSON(DBObject base){
        Integer id  = (Integer) base.get("id");
        Integer amt = (Integer) base.get("amount");
        Short damage = ((Integer) base.get("damage")).shortValue();
        Byte data = ((Integer) base.get("data")).byteValue();
        ItemStack stack = new ItemStack(id);
        stack.setAmount(amt);
        stack.setData(new MaterialData(id,data));
        stack.setDurability(damage);
        List<DBObject> enchants = (List<DBObject>) base.get("enchantments");
        if(enchants != null){
            for(DBObject obj : enchants){
                stack.addUnsafeEnchantment(Enchantment.getByName(((String) obj.get("name"))), (Integer) obj.get("level"));
            }
        }
        Object d = base.get("displayname");
        if(d != null){
            stack.getItemMeta().setDisplayName(((String) d));
        }
        d = base.get("lore");
        if (d != null) {
            stack.getItemMeta().setLore(((List<String>) d));
        }
        if(id == 403) { //Enchanted Book

            EnchantmentStorageMeta bookmeta = (EnchantmentStorageMeta) stack.getItemMeta();
            List<DBObject> storedEnchants = (List<DBObject>) base.get("storedEnchants");
            if(storedEnchants != null) {
                for (DBObject obj : storedEnchants) {
                    bookmeta.addStoredEnchant(Enchantment.getByName(((String) obj.get("name"))), (Integer) obj.get("level"), true);
                }
            }
            stack.setItemMeta(bookmeta);
        }

        return stack;
    }

    public static DBObject toBSON(ItemStack stack){

        BasicDBObject ret = new BasicDBObject();
        if(stack == null){
            ret.append("id",0);
            ret.append("amount",1);
            ret.append("damage",0);
            ret.append("data",0);
        }
        else
        {
            ret.append("id",stack.getTypeId());
            ret.append("amount",stack.getAmount());

            ret.append("damage",new Integer(stack.getDurability())); //Force Integer
            ret.append("data",stack.getData().getData());
            BasicDBList enchantmentList = new BasicDBList();
            for(Map.Entry<Enchantment, Integer> ench : stack.getEnchantments().entrySet()){
                BasicDBObject enObj = new BasicDBObject();
                enObj.append("name",ench.getKey().getName());
                enObj.append("level",ench.getValue());
                enchantmentList.add(enObj);
            }
            if(stack.hasItemMeta()) {
                ret.append("displayname", stack.getItemMeta().getDisplayName());
                ret.append("lore", stack.getItemMeta().getLore());
            }
            if(enchantmentList.size() > 0) {
                ret.append("enchantments", enchantmentList);
            }
            if(stack.getType() == Material.ENCHANTED_BOOK){
                BasicDBList storedEnchants = new BasicDBList();
                for(Map.Entry<Enchantment, Integer> ench : ((EnchantmentStorageMeta) stack.getItemMeta()).getStoredEnchants().entrySet()){
                    BasicDBObject enObj = new BasicDBObject();
                    enObj.append("name",ench.getKey().getName());
                    enObj.append("level",ench.getValue());
                    storedEnchants.add(enObj);
                }
                ret.append("storedEnchants",storedEnchants);
            }
        }

        return ret;
    }

    public static String toString(ItemStack stack, Gson compiler){
        JsonObject obj = new JsonObject();
        if(stack == null){
            obj.addProperty("id",0);
            obj.addProperty("amount",1);
            obj.addProperty("damage",0);
            obj.addProperty("data",0);
        }
        else
        {
            obj.addProperty("id",stack.getTypeId());
            obj.addProperty("amount",stack.getAmount());
            obj.addProperty("damage",new Integer(stack.getDurability()));
            obj.addProperty("data",stack.getData().getData());
            JsonArray enchantmentList = new JsonArray();
            for(Map.Entry<Enchantment,Integer> ench : stack.getEnchantments().entrySet()){
                JsonObject enObj = new JsonObject();
                enObj.addProperty("name",ench.getKey().getName());
                enObj.addProperty("level",ench.getValue());
                enchantmentList.add(enObj);
            }
            if(enchantmentList.size() > 0){
                obj.add("enchantments",enchantmentList);
            }
            if(stack.hasItemMeta()){
                obj.addProperty("displayname",stack.getItemMeta().getDisplayName());
                if(stack.getItemMeta().hasLore()){
                    JsonArray loreArray = new JsonArray();
                    for(String s : stack.getItemMeta().getLore()){
                        loreArray.add(new JsonPrimitive(s));
                    }
                    obj.add("lore",loreArray);
                }
            }
            if(stack.getType() == Material.ENCHANTED_BOOK){
                JsonArray storedEnchants = new JsonArray();
                for(Map.Entry<Enchantment,Integer> ench : ((EnchantmentStorageMeta) stack.getItemMeta()).getStoredEnchants().entrySet()){
                    JsonObject enObj = new JsonObject();
                    enObj.addProperty("name",ench.getKey().getName());
                    enObj.addProperty("level",ench.getValue());
                    enchantmentList.add(enObj);
                }
                obj.add("storedEnchantments",storedEnchants);
            }
        }
        return compiler.toJson(obj);
    }

    public static ItemStack toStack(String str, Gson compiler){
        JsonObject base = compiler.fromJson(str,JsonObject.class);
        Integer id  = base.get("id").getAsInt();
        Integer amt = base.get("amount").getAsInt();
        Short damage = ((Integer) base.get("damage").getAsInt()).shortValue();
        Byte data = base.get("data").getAsByte();
        ItemStack stack = new ItemStack(id);
        stack.setAmount(amt);
        stack.setData(new MaterialData(id,data));
        stack.setDurability(damage);
        JsonArray enchants = base.get("enchantments").getAsJsonArray();
        if(enchants != null){
            for(int i = 0; i<enchants.size(); i++){
                JsonObject ench = enchants.get(i).getAsJsonObject();
                stack.addUnsafeEnchantment(Enchantment.getByName(ench.get("name").getAsString()), ench.get("level").getAsInt());
            }
        }
        JsonElement d = base.get("displayname");
        if(d != null){
            stack.getItemMeta().setDisplayName(d.getAsString());
        }
        d = base.get("lore");
        if (d != null) {
            JsonArray r = d.getAsJsonArray();
            List<String> lore = new ArrayList<>();
            for(int i = 0; i<r.size(); i++){
                lore.add(r.get(i).getAsString());
            }
            stack.getItemMeta().setLore(lore);
        }
        if(id == 403) { //Enchanted Book

            EnchantmentStorageMeta bookmeta = (EnchantmentStorageMeta) stack.getItemMeta();
            JsonArray enchants2 = base.get("storedEnchantments").getAsJsonArray();
            if(enchants != null){
                for(int i = 0; i<enchants2.size(); i++){
                    JsonObject ench = enchants2.get(i).getAsJsonObject();
                    stack.addUnsafeEnchantment(Enchantment.getByName(ench.get("name").getAsString()), ench.get("level").getAsInt());
                }
            }
            stack.setItemMeta(bookmeta);
        }

        return stack;
    }

    public static List<ItemStack> splitStack(ItemStack model, int size){
        return splitStack(model,size,false);
    }

    public static List<ItemStack> splitStack(ItemStack model, int size, boolean force64){
        if(model == null){
            return new ArrayList<>();
        }
        if(model.getType() == Material.AIR){
            return new ArrayList<>();
        }
        List<ItemStack> col = new ArrayList<>();

        int sizeLeft = size;
        while(sizeLeft > 0){
            int nextSize = sizeLeft;
            int max = (force64 ? 64 : model.getMaxStackSize());
            if(nextSize > max){
                nextSize = max;
            }
            ItemStack k = model.clone();
            k.setAmount(nextSize);

            col.add(k);
            sizeLeft -= nextSize;
        }
        return col;
    }

    public static boolean isEmpty(Inventory i){
        for(ItemStack k : i){
            if(k != null){
                return false;
            }
        }
        return true;
    }

    public static boolean isSimilar(ItemStack a, ItemStack b){
        if(a == b) return true;
        //If one of these are null, and they both aren't, (caught above) they are not equal
        if(a == null || b == null) return false;
        if(a.getType() != b.getType()) return false;
        if(a.getData().getData() != b.getData().getData()) return false;
        if(a.getDurability() != b.getDurability()) return false;
        Map<Enchantment, Integer> aEnchantments = a.getEnchantments();
        Map<Enchantment, Integer> bEnchantments = b.getEnchantments();
        if(aEnchantments.size() != bEnchantments.size()) return false;
        for(Map.Entry<Enchantment,Integer> e : aEnchantments.entrySet())
            if (bEnchantments.get(e.getKey()) != e.getValue()) return false;
        if(a.hasItemMeta() != b.hasItemMeta()) return false;
        if(a.hasItemMeta()){
            if(a.getItemMeta() instanceof EnchantmentStorageMeta){
                EnchantmentStorageMeta sA = (EnchantmentStorageMeta) a.getItemMeta();
                EnchantmentStorageMeta sB = (EnchantmentStorageMeta) b.getItemMeta();
                if(sA.getStoredEnchants().size() != sB.getStoredEnchants().size()){
                    return false;
                }
                for(Map.Entry<Enchantment,Integer> e : sA.getStoredEnchants().entrySet()){
                    if(sB.getStoredEnchants().get(e.getKey()) != e.getValue()){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
