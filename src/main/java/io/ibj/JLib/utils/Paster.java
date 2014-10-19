package io.ibj.JLib.utils;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.block.Block;

/**
 * Created by Joe on 6/15/2014.
 */
public class Paster {
    public static void pasteClipToSign(Block b, CuboidClipboard c, String wgName, String... members) throws MaxChangedBlocksException, ProtectionDatabaseException {
        Double targetSignRotation = getSignRotation(b.getData());           //Get how rotated our target (real world) sign is
        System.out.println("b.getLocation() = " + b.getLocation());
        if(targetSignRotation == null){
            throw new NullPointerException("No target sign found.");
        }
        Vector signOffset = getSignLocation(c);                             //Retrieve our sign inside the .schematic
        Double sourceSignLocation = getSignRotation((byte) c.getBlock(signOffset).getData());   //Get the .schematic sign rotation
        if(sourceSignLocation == null){
            throw new NullPointerException("Source sign rotation not found.");
        }
        Double rotationFix = targetSignRotation - sourceSignLocation;   //

        Vector cornerA = new Vector(0,0,0);
        Vector cornerB = new Vector(c.getSize().getX() - 1, c.getSize().getY() - 1, c.getSize().getZ() - 1);

        signOffset = signOffset.multiply(-1);

        cornerA = cornerA.add(signOffset);
        cornerB = cornerB.add(signOffset);

        c.setOffset(signOffset);

        cornerA = rotateAroundOrigin(cornerA,-rotationFix);
        cornerB = rotateAroundOrigin(cornerB,-rotationFix);
        c.rotate2D(rotationFix.intValue());

        EditSession session = new EditSession(BukkitUtil.getLocalWorld(b.getWorld()),c.getHeight()*c.getWidth()*c.getLength());

        Vector pasteVector = new Vector(b.getLocation().getBlockX(),b.getLocation().getBlockY(),b.getLocation().getBlockZ());
        c.paste(session, pasteVector, false);

        cornerA = cornerA.add(pasteVector);
        cornerB = cornerB.add(pasteVector);
        if(wgName != null) {
            ProtectedCuboidRegion pastedRegion = new ProtectedCuboidRegion(wgName, new BlockVector(cornerA), new BlockVector(cornerB));

            DefaultDomain memberSet = new DefaultDomain();
            for(String member : members){
                memberSet.addPlayer(member);
            }

            pastedRegion.setMembers(memberSet);

            WorldGuardPlugin.inst().getRegionManager(b.getWorld()).addRegion(pastedRegion);

            WorldGuardPlugin.inst().getRegionManager(b.getWorld()).save();
        }
    }


    private static Double getSignRotation(Byte data){
        switch(data){
            case 2:
                return 0d;
            case 5:
                return 90d;
            case 3:
                return 180d;
            case 4:
                return 270d;
            default:
                System.out.println("data = " + data);
                return null;
        }
    }

    private static Vector getSignLocation(CuboidClipboard c){
        for(int x = 0; x < c.getSize().getX(); x ++){
            for(int y = 0; y < c.getSize().getY(); y++){
                for(int z = 0; z < c.getSize().getZ(); z++){
                    Vector v = new Vector(x,y,z);
                    if(c.getBlock(v).getType() == 68){
                        return v;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Rotates a vector CLOCKWISE around a point in degrees
     * @param v
     * @param rotation
     * @return
     */
    private static Vector rotateAroundOrigin(Vector v, double rotation){
        //Since our needed angle is in radians, and counter clockwise, we need to apply these transformations to our input
        rotation = rotation * Math.PI / -180; //Degrees -> Radians
        double distanceFromOrigin = Math.sqrt((v.getX()*v.getX()) + (v.getZ()*v.getZ()));
        double currentRotation = Math.atan2(v.getZ(),v.getX());
        double newRotation = rotation + currentRotation;
        return new Vector(distanceFromOrigin * Math.cos(newRotation),v.getY(),distanceFromOrigin * Math.sin(newRotation));
    }

}
