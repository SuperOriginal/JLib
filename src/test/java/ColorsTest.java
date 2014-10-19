import io.ibj.JLib.utils.Colors;
import org.bukkit.ChatColor;
import org.junit.Test;

/**
 * Created by Joe on 6/27/2014.
 */
public class ColorsTest {
    @Test
    public void colorifyTest(){
        assert Colors.colorify("&fHi").equals(ChatColor.WHITE+"Hi");
    }

    @Test
    public void decolorifyTest(){
        assert Colors.decolorify(ChatColor.WHITE+"Hi").equals("&fHi");
    }

    @Test
    public void stripTest(){
        assert Colors.stripSpecialCharacters("&f"+ChatColor.RED+"Hi").equals("Hi");
    }
}
