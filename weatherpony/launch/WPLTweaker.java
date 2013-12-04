package weatherpony.launch;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class WPLTweaker implements ITweaker {
	public static WPLTweaker instance;
	public static boolean isTweak = true;
	
	public WPLTweaker(){
		instance = this;
	}
	ITweaker secondaryTweak;
	public List<String> args;
	public File gameDir;
	public File assetsDir;
	public String profile;
	public LaunchClassLoader classLoader;
	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
		this.args = args;
		this.gameDir = gameDir;
		this.assetsDir = assetsDir;
		this.profile = profile;
		

		//this.classLoader = (LaunchClassLoader) WPLTweaker.class.getClassLoader();
		//this.classLoader.registerTransformer("weatherpony.launch.WPLClassTransformer");
		//load();
	}
	ITweaker getSecondaryTweak(){
		if(args == null)
			return null;//in Forge as a coremod
		
		OptionParser optionParser = new OptionParser();
		ArgumentAcceptingOptionSpec<String> secondaryTweakOption = optionParser.accepts("wpmlSecondaryTweak", "A secondary ITweaker class to be used with WPML").withRequiredArg().ofType(String.class);
		optionParser.allowsUnrecognizedOptions();
        NonOptionArgumentSpec<String> nonOptions = optionParser.nonOptions();
        OptionSet parsedOptions = optionParser.parse(args.toArray(new String[args.size()]));
        if(parsedOptions.has(secondaryTweakOption)){
        	String second = secondaryTweakOption.value(parsedOptions);
        	try{
        		//classLoader.addClassLoaderExclusion(toExclude); //know what? WPML is gon'na edit it >:D
        		//this can lead to some not-so-good states if someone doesn't do something right
        		//however, this can also lead to some wonderful magic if everyone prepares for it
        		Class<? extends ITweaker> tweakClass = (Class<? extends ITweaker>) Class.forName(second);
                return tweakClass.newInstance();
        	}catch(Exception e){
        		Logger.getLogger("WPMLTweak").log(Level.INFO, "Missing secondary tweak class "+second);
        	}
        }
        return null;
	}
	@Override
	public String[] getLaunchArguments(){
		String[] argList = null;
		if(this.secondaryTweak != null){
			argList = this.secondaryTweak.getLaunchArguments();
		}
		if(argList == null){
			if(this.args == null)
				return new String[0];
			argList = this.args.toArray(new String[this.args.size()]);
		}else{
			//if(Arrays.asList(argList).contains(o))
		}
		return this.args.toArray(new String[args.size()]);
		//return new String[0];
	}

	@Override
	public String getLaunchTarget() {
		return "net.minecraft.client.main.Main";
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		this.classLoader = classLoader; 
		this.classLoader.registerTransformer("weatherpony.launch.WPLClassTransformer");
		load();
	}
		
	public void load(){
		WPLClassTransformer.doPreModificationLoadingStuff();
		secondaryTweak = getSecondaryTweak();
		if(secondaryTweak != null){
			secondaryTweak.acceptOptions(args, gameDir, assetsDir, profile);
			secondaryTweak.injectIntoClassLoader(classLoader);
		}
		WPLClassTransformer.doPostModificationLoadingStuff();
	}
	public static void log(String logme){
		System.out.println(logme);
	}
}
