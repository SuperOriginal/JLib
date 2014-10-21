package io.ibj.JLib.gson;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Joe on 10/19/2014.
 */
public class ItemStackGsonSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject base = jsonElement.getAsJsonObject();
        Integer id  = base.get("id").getAsInt();
        Integer amt = base.get("amount").getAsInt();
        Short damage = ((Integer) base.get("damage").getAsInt()).shortValue();
        Byte data = base.get("data").getAsByte();
        ItemStack stack = new ItemStack(id);
        stack.setAmount(amt);
        stack.setData(new MaterialData(id,data));
        stack.setDurability(damage);
        JsonElement enchants = base.get("enchantments");
        if(enchants != null){
            JsonArray enchantments = enchants.getAsJsonArray();
            for(int i = 0; i<enchantments.size(); i++){
                JsonObject ench = enchantments.get(i).getAsJsonObject();
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

    @Override
    public JsonElement serialize(ItemStack stack, Type type, JsonSerializationContext jsonSerializationContext) {
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
        return obj;
    }
}
