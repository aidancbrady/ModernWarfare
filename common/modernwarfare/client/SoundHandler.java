package modernwarfare.client;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import cpw.mods.fml.common.Mod.EventHandler;

public class SoundHandler 
{
	@EventHandler
	public void onSoundLoad(SoundLoadEvent event)
	{
		event.manager.addSound("modernwarfare:ak.ogg");
		event.manager.addSound("modernwarfare:deagle.ogg");
		event.manager.addSound("modernwarfare:flamethrower.ogg");
		event.manager.addSound("modernwarfare:grenadebounce.ogg");
		event.manager.addSound("modernwarfare:grunt.ogg");
		event.manager.addSound("modernwarfare:gunempty.ogg");
		event.manager.addSound("modernwarfare:impact.ogg");
		event.manager.addSound("modernwarfare:jetpack.ogg");
		event.manager.addSound("modernwarfare:laser.ogg");
		event.manager.addSound("modernwarfare:m.ogg");
		event.manager.addSound("modernwarfare:mechhurt.ogg");
		event.manager.addSound("modernwarfare:minigun.ogg");
		event.manager.addSound("modernwarfare:mp.ogg");
		event.manager.addSound("modernwarfare:parachute.ogg");
		event.manager.addSound("modernwarfare:reload.ogg");
		event.manager.addSound("modernwarfare:rocket.ogg");
		event.manager.addSound("modernwarfare:sg.ogg");
		event.manager.addSound("modernwarfare:shotgun.ogg");
		event.manager.addSound("modernwarfare:smokegrenade.ogg");
		event.manager.addSound("modernwarfare:smokegrenadebounce.ogg");
		event.manager.addSound("modernwarfare:sniper.ogg");
		event.manager.addSound("modernwarfare:stungrenade.ogg");
		event.manager.addSound("modernwarfare:stungrenadebounce.ogg");
		event.manager.addSound("modernwarfare:wrench.ogg");
	}
}
