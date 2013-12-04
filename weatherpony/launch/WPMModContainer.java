package weatherpony.launch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

import com.google.common.io.ByteStreams;

public class WPMModContainer{
	private WPMModContainer(){}
	
	private WPMLmod mod;
	private URI location;
	public WPMLmod getMod(){
		return mod;
	}
	public boolean isMod(String name){
		return name.equals(mod.modName());
	}
	public boolean isMod(Collection<String> names){
		return names.contains(mod.modName());
	}
	public URI getLocation(){
		return this.location;
	}
	public static WPMModContainer generate(URI location){
		try {
			ZipFile modzip = new ZipFile(new File(location));
			ZipEntry infoFile = modzip.getEntry("WPM.info");
			if(infoFile == null){
				modzip.close();
				System.out.println("testing file, not valid WPML mod: "+location.getPath());
				return null;//not a valid WPML mod
			}
			InputStream info = modzip.getInputStream(infoFile);
			BufferedReader binfo = new BufferedReader(new InputStreamReader(info));
			String wpmModFile = binfo.readLine();
			String MCv = binfo.readLine();
			if(!MCv.equals(VersionControl.version)){//wrong MC version
				info.close();
				modzip.close();
				System.out.println("tested wpml file, but it's for wrong MC version: "+location.getPath());
				return null;
			}
			WPMModContainer mod = new WPMModContainer();
			mod.location = location;
			WPLTweaker.instance.classLoader.addURL(location.toURL());
			mod.mod = (WPMLmod) Class.forName(wpmModFile).newInstance();
			info.close();
			modzip.close();
			mod.mod.giveSelf(mod);
			return mod;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static File extractNestedZip(File pathToZip, ZipFile zip, String entrypath, boolean outmost){
		File destPre;
		if(!outmost){
			destPre = pathToZip.getParentFile();
		}else{
			destPre = getExtractionFolder();
		}
		return extract(pathToZip, destPre, zip, entrypath);
	}
	private static File extract(File base, File destPre, ZipFile zip, String entrypath){
		ZipEntry entry = zip.getEntry(entrypath);
		if(entry == null)
			return null;
		String name = base.getName();
		File destDir = new File(destPre, name+"_ext");
		File dest = new File(destDir, entrypath);
		if(dest.exists())
			return dest;
		File destPar = dest.getParentFile();
		if(!destPar.exists()){
			destPar.mkdirs();
		}
		try {
			InputStream in = zip.getInputStream(entry);
			OutputStream out = new FileOutputStream(dest);
			//IOUtils.copy(in,out);//this library isn't loaded until after this code is run
			byte[] buffer = new byte[1024];//1 kilobyte at most
			boolean finished = false;
			while(!finished){
				int read = in.read(buffer);
				if(read != -1)
					out.write(buffer, 0, read);
				else//end of file
					finished = true;
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return dest;
	}
	private static File getExtractionFolder(){
		File ret = new File("WPML_extraction_folder");
		if(!ret.exists()){
			ret.mkdir();
			ret.deleteOnExit();
		}
		return ret;
	}
}
