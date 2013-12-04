package weatherpony.launch.core;

import java.util.Map;

import net.minecraft.launchwrapper.LaunchClassLoader;
import weatherpony.launch.WPLTweaker;
import cpw.mods.fml.relauncher.IFMLCallHook;

public class WPMLLoadFMLCallHook implements IFMLCallHook {

	@Override
	public Void call() throws Exception {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		WPLTweaker.instance.injectIntoClassLoader((LaunchClassLoader)data.get("classLoader"));
	}

}
