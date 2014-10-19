/**
 * Created by Joe on 6/27/2014.
 */
public class ItemStackUtilsTest {
/*
    private void testNegSwitch(ItemStack a, ItemStack b){
        assert !ItemStackUtils.isSimilar(a,b);
        assert !ItemStackUtils.isSimilar(b,a);
    }

    private void testSwitch(ItemStack a, ItemStack b){
        assert ItemStackUtils.isSimilar(a,b);
        assert ItemStackUtils.isSimilar(b,a);
    }

    @Test
    public void itemIDSimilarityTest(){
        ItemStack a = new ItemStack(Material.GRASS);
        ItemStack b = new ItemStack(Material.STONE);
        testNegSwitch(a,b);
        b = new ItemStack(Material.GRASS);
        testSwitch(a,b);
    }

    @Test
    public void itemDataSimilarityTest(){
        ItemStack a = new ItemStack(Material.LONG_GRASS);
        ItemStack b = a.clone();
        testSwitch(a,b);
        b.getData().setData((byte) 1);
        testNegSwitch(a,b);
    }

    @Test
    public void itemStackNullityTest(){
        ItemStack a = null;
        ItemStack b = null;
        testSwitch(a,b);

        a = new ItemStack(Material.GRASS);

        testNegSwitch(a,b);
    }

    @Test
    public void itemStackDurabilityTest(){
        ItemStack a = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack b = a.clone();
        b.setDurability((short) 5);
        testNegSwitch(a,b);
    }

    @Test
    public void itemStackEnchantTest(){
        ItemStack a = new ItemStack(Material.DIAMOND_CHESTPLATE);
        a.addEnchantment(Enchantment.PROTECTION_PROJECTILE,2);
        a.addEnchantment(Enchantment.THORNS,1);
        ItemStack b = a.clone();
        testSwitch(a, b);
        b.getEnchantments().clear();
        testNegSwitch(a,b);
        b.addEnchantment(Enchantment.THORNS,1);
        b.addEnchantment(Enchantment.PROTECTION_PROJECTILE,1);
        testNegSwitch(a,b);
        b.addEnchantment(Enchantment.PROTECTION_PROJECTILE,2);
        testSwitch(a,b);
    }

    @Test
    public void enchBookTest(){
        ItemStack a = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta aS = (EnchantmentStorageMeta) a.getItemMeta();

        ItemStack b = a.clone();
        EnchantmentStorageMeta bS = (EnchantmentStorageMeta) b.getItemMeta();
        aS.addStoredEnchant(Enchantment.ARROW_DAMAGE,1,true);
        bS.addStoredEnchant(Enchantment.ARROW_DAMAGE,1,true);
        testSwitch(a,b);

        aS.removeStoredEnchant(Enchantment.ARROW_DAMAGE);
        testNegSwitch(a,b);
        aS.addStoredEnchant(Enchantment.ARROW_DAMAGE,5,true);
        testNegSwitch(a,b);
    }*/


}
