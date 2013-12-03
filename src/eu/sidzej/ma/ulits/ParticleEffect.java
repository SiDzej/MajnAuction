package eu.sidzej.ma.ulits;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import eu.sidzej.ma.MajnAuction;

public class ParticleEffect {
	
	private MajnAuction plugin;
	
	private static Class<?> Packet63WorldParticles;
    private static Method getHandleCS;
    private static Method getHandleCW;
    private static Field dimensionField;
    private static Method sendPacketNearby;
    
    public ParticleEffect(MajnAuction plugin){
    	this.plugin = plugin;
    }
	
	public void run(Location l){
		try{
			String pkg = Bukkit.getServer().getClass().getPackage().getName();
	        String [] v = pkg.split("\\.");
	        String version=null;
	        
	        if (v.length==4){
	            version = v[3];
	        }
			Packet63WorldParticles = Class.forName("net.minecraft.server."+version+".Packet63WorldParticles");
			getHandleCS = Class.forName("org.bukkit.craftbukkit."+version+".CraftServer").getMethod("getHandle");
			getHandleCW = Class.forName("org.bukkit.craftbukkit."+version+".CraftWorld").getMethod("getHandle");
			dimensionField = Class.forName("net.minecraft.server."+version+".WorldServer").getField("dimension");
			sendPacketNearby = Class.forName("net.minecraft.server."+version+".DedicatedPlayerList").getMethod("sendPacketNearby", double.class, 
					double.class, double.class, double.class, int.class, Class.forName("net.minecraft.server."+version+".Packet"));
			
            Object sPacket= Packet63WorldParticles.newInstance();    
            for (Field f : Packet63WorldParticles.getDeclaredFields()){
                f.setAccessible(true);
                String fld = f.getName();
                if (fld.equals("a")) f.set(sPacket, "instantSpell"); 
                if (fld.equals("b")) f.set(sPacket, (float) l.getBlockX()+0.5f);
                if (fld.equals("c")) f.set(sPacket, (float) l.getBlockY()+0.5f);
                if (fld.equals("d")) f.set(sPacket, (float) l.getBlockZ()+0.5f);
                if (fld.equals("e")) f.set(sPacket, (float) 0.5);
                if (fld.equals("f")) f.set(sPacket, (float) 0.5);
                if (fld.equals("g")) f.set(sPacket, (float) 0.5);
                if (fld.equals("h")) f.set(sPacket, (float) 0.001);
                if (fld.equals("i")) f.set(sPacket, 10);
            }
            Object handleCraftServer = getHandleCS.invoke(Bukkit.getServer()); //DedicatedPlayerList
            Object worldServer = getHandleCW.invoke(plugin.getServer().getWorld("world"));
            int dimension = dimensionField.getInt(worldServer);
            sendPacketNearby.invoke(handleCraftServer, (float) 0, (float) 100, (float) 0,64,dimension,sPacket);
        } catch (Exception e){
            plugin.logError("Failed to create particles effect.");        }
	}
}
