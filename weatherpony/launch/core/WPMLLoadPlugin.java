package weatherpony.launch.core;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

import weatherpony.launch.VersionControl;
import weatherpony.launch.WPLTweaker;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.launcher.FMLTweaker;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion(VersionControl.version)
@IFMLLoadingPlugin.TransformerExclusions({"weatherpony.launch."})
@IFMLLoadingPlugin.Name("WPML "+VersionControl.build)
public class WPMLLoadPlugin implements IFMLLoadingPlugin{
	public WPMLLoadPlugin() {
		new WPLTweaker().isTweak = false;
	}
	
	@Override
	//@Deprecated //this method doesn't do anything
	public String[] getLibraryRequestClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		//in Forge, this gets called before the coremod knows anything that's going on, which is the reverse of the launcher
		return null;
	}
	
	/*private boolean hasInjected = false;
	private void tryInject(){
		if(!hasInjected){
			hasInjected = true;
			WPLTweaker.instance.injectIntoClassLoader(null);
		}
	}*/
	
	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return "weatherpony.launch.core.WPMLLoadFMLCallHook";
	}

	@Override
	public void injectData(Map<String, Object> data) {
		try{
			Field[] fields = new Field[]{
					FMLLaunchHandler.class.getDeclaredField("INSTANCE"),
					FMLLaunchHandler.class.getDeclaredField("tweaker"),
					FMLTweaker.class.getDeclaredField("assetsDir"),
					FMLTweaker.class.getDeclaredField("profile"),
			};
			Field.setAccessible(fields, true);
			int x = 0;
			FMLLaunchHandler fmllaunchInst = (FMLLaunchHandler) fields[x++].get(null);
			FMLTweaker fmlTweak = (FMLTweaker) fields[x++].get(fmllaunchInst);
			File assets = (File) fields[x++].get(fmlTweak);
			String profile = (String) fields[x++].get(fmlTweak);
			WPLTweaker.instance.acceptOptions(null, fmlTweak.getGameDir(), assets, profile);
		}catch(Exception e){
			e.printStackTrace();
			throw Throwables.propagate(e);
		}
	}

}
