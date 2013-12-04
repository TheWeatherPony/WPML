package weatherpony.launch;

import java.io.DataInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.google.common.io.ByteStreams;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import weatherpony.launch.annotationImplementation.AnnotationManager;

public class WPLClassTransformer implements IClassTransformer{
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		//bytes may be null
		if(mods.isEmpty())
			return bytes;
		
		List<ModTransformation<List>> merges = this.getWPMMerges(name, transformedName);
		/*if(merges.isEmpty())
			return bytes;*/
		
		List<ModTransformation<List<IClassManipulator>>> posts = this.getPostWPMMerges(name, transformedName);
		AnnotationManager.instance.startNewEdits();
		for(ModTransformation<List> merge : merges){
			AnnotationManager.instance.setCurrentMod(merge.mod.modName());
			for(Object toMerge : merge.data){
				byte[] mergeClass = null;
				if(toMerge instanceof byte[]){
					mergeClass = (byte[]) toMerge;
				}else if(toMerge instanceof InputStream){
					InputStream stream = (InputStream) toMerge;
					try {
						mergeClass = ByteStreams.toByteArray(stream);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if(mergeClass == null){
					//and what should I do here...?
				}else{
					AnnotationManager.instance.addEdits(mergeClass);
				}
			}
		}
		
		AnnotationManager.instance.MERGE(bytes);
		for(ModTransformation<List<IClassManipulator>> customMerge : posts){
			AnnotationManager.instance.setCurrentMod(customMerge.mod.modName());
			AnnotationManager.instance.workOnTree(customMerge.data);
		}
		for(WPMModContainer eachMod : mods){
			eachMod.getMod().closeOpenWPMMergeRequests(name, transformedName);
		}
		return AnnotationManager.instance.finish(name, transformedName);
	}
	public List<ModTransformation<List>> getWPMMerges(String name, String transformedName){
		ArrayList<ModTransformation<List>> ret = new ArrayList();
		for(WPMModContainer eachMod : mods){
			WPMLmod mod = eachMod.getMod();
			List list = mod.getWPMMergeRequests(name, transformedName);
			if(list != null && !list.isEmpty()){
				ret.add(new ModTransformation<List>(mod,list));
			}
		}
		return ret;
	}
	private List<ModTransformation<List<IClassManipulator>>> genericPosts;
	public List<ModTransformation<List<IClassManipulator>>> getPostWPMMerges(String name, String transformedName){
		ArrayList<ModTransformation<List<IClassManipulator>>> ret = new ArrayList();
		for(WPMModContainer eachMod : mods){
			WPMLmod mod = eachMod.getMod();
			List<IClassManipulator> list = mod.postWPMLMergers(name, transformedName);
			if(list != null && !list.isEmpty()){
				ret.add(new ModTransformation<List<IClassManipulator>>(mod,list));
			}
		}
		ret.addAll(getGenerics());
		return ret;
	}
	private List<ModTransformation<List<IClassManipulator>>> getGenerics(){
		if(genericPosts == null){
			genericPosts = new ArrayList();
			for(WPMModContainer eachMod : mods){
				WPMLmod mod = eachMod.getMod();
				List<IClassManipulator> list = mod.postWPMLMergers();
				if(list != null && !list.isEmpty()){
					genericPosts.add(new ModTransformation<List<IClassManipulator>>(mod,list));
				}
			}
		}
		return genericPosts;
	}
	public static class ModTransformation<type>{
		ModTransformation(WPMLmod mod, type thing){
			this.mod = mod;
			this.data = thing;
		}
		public type data;
		public WPMLmod mod;
	}
	static ArrayList<WPMModContainer> mods = new ArrayList();
	static void doPreModificationLoadingStuff(){
		File modsFolder = new File(WPLTweaker.instance.gameDir, "mods");
		if (!modsFolder.exists())
			modsFolder.mkdirs();
		String[] modsToLoad = modsFolder.list(new WPMFileFilter());
		for(String eachMod : modsToLoad){
			WPMModContainer mod = WPMModContainer.generate(new File(modsFolder, eachMod).toURI());
			if(mod != null)
				mods.add(mod);
		}
		for(WPMModContainer eachMod : mods){
			eachMod.getMod().informOfWPMods(mods);
		}
		for(WPMModContainer eachMod : mods){
			eachMod.getMod().preSecondaryInjection();
		}
	}
	
	static void doPostModificationLoadingStuff(){
		for(WPMModContainer eachMod : mods){
			eachMod.getMod().postSecondaryInjection();
		}
	}
	
	public static byte[] read(ZipFile zip, ZipEntry entry) throws IOException{
		int size = (int)entry.getCompressedSize();
		if(size == -1){
			//there's a problem.
		}
		return read(zip.getInputStream(entry), size);
	}
	
	public static byte[] read(InputStream stream, int size) throws IOException{
		byte[] data = new byte[size];
		DataInputStream dataIs = new DataInputStream(stream);
		dataIs.readFully(data);
		return data;
	}

	
	static class WPMFileFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return name.toLowerCase().endsWith(".wpm");//WeatherPony Modification
		}
	}
}
