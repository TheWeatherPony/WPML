package weatherpony.launch.annotationImplementation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import weatherpony.launch.IClassManipulator;
import weatherpony.launch.WPLClassTransformer;
import weatherpony.launch.WPLTweaker;

public final class AnnotationManager {
	Map<String,AnnotationLogic> mapping = new HashMap();
	
	public static final AnnotationManager instance = new AnnotationManager();
	private AnnotationManager(){
		/*addMap(new AddInterfaceLogic());//FIXME
		addMap(new AddPieceLogic());
		addMap(new ChangeFieldTypeLogic());
		addMap(new ChangeInstructionLogic());
		addMap(new EditInstructionLogic());
		addMap(new RemoveInterfaceLogic());
		addMap(new SetExtendsLogic());*/
	}
	public void startNewEdits() {
		currentClass = null;
		hasEdited = false;
	}
	public void setCurrentMod(String modName) {
		currentMod = modName;
	}
	private ClassNode currentClass;
	protected String currentMod;
	public void addEdits(byte[] mergeClass) {
		if(mergeClass == null)
			return;
		ClassReader cr = new ClassReader(mergeClass);
		ClassNode merge = new ClassNode();
		cr.accept(merge, ClassReader.EXPAND_FRAMES | ClassReader.SKIP_FRAMES);//compute everything from scratch
		this.currentClass = this.combine(this.currentClass, merge);
		hasEdited = true;
	}
	
	private ClassNode combine(ClassNode current, ClassNode merge) {
		// TODO actually do something here
		return merge;
	}
	public void workOnTree(List<IClassManipulator> manipulators) {
		for(IClassManipulator manipulator : manipulators){
			currentClass = manipulator.manipulateTree(currentClass);
		}
	}
	public void MERGE(byte[] bytes) {
		ClassReader cr = new ClassReader(bytes);
		ClassNode merge = new ClassNode();
		cr.accept(merge, ClassReader.EXPAND_FRAMES | ClassReader.SKIP_FRAMES);//compute everything from scratch
		
		
		if(this.currentClass == null)
			this.currentClass = merge;
	}
	public void postMerge(){
		
	}
	public byte[] finish(String name, String transformedName){
		if(currentClass == null)
			return null;
		ClassWriter cw = new ClassWriter(0/*ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS*/);//compute everything from scratch
		currentClass.accept(cw);
		currentClass = null;
		byte[] ret = cw.toByteArray();
		ret = temporaryFixOfStuff(name, transformedName, ret);//ret = fixSide(name, transformedName, ret);
		return ret;
	}
	protected void ProcessAnnotations(ClassNode from, ClassNode onto){
		if(from.invisibleAnnotations != null){
			for(AnnotationNode each : from.invisibleAnnotations){
				AnnotationLogic logic = mapping.get(each.desc);
				if(logic != null){
					logic.dealWithAnnotationOnClass(from, onto, each);
				}
			}
		}
		for(FieldNode eachfield : from.fields){
			if(eachfield.invisibleAnnotations != null){
				for(AnnotationNode each : eachfield.invisibleAnnotations){
					AnnotationLogic logic = mapping.get(each.desc);
					if(logic != null){
						logic.dealWithAnnotationOnField(from, eachfield, onto, each);
					}
				}
			}
		}
		for(MethodNode eachmethod : from.methods){
			if(eachmethod.invisibleAnnotations != null){
				for(AnnotationNode each : eachmethod.invisibleAnnotations){
					AnnotationLogic logic = mapping.get(each.desc);
					if(logic != null){
						logic.dealWithAnnotationOnMethod(from, eachmethod, onto, each);
					}
				}
			}
		}
	}
	
	private IClassTransformer sideFixer;
	private boolean sideFixerLoaded = false;
	byte[] fixSide(String name, String transformedName, byte[] clazz){
		if(!sideFixerLoaded){
			try{
				sideFixer = (IClassTransformer) Class.forName("cpw.mods.fml.common.asm.transformers.SideTransformer").newInstance();
			}catch(Exception e){
			}
			sideFixerLoaded = true;
		}
		if(sideFixer != null)
			return sideFixer.transform(name, transformedName, clazz);
		return clazz;
	}
	
	String[] temporaryClassNames = new String[]{
			"cpw.mods.fml.common.asm.transformers.SideTransformer",
			"cpw.mods.fml.common.asm.transformers.AccessTransformer",
			"net.minecraftforge.transformers.EventTransformer",
			"net.minecraft.launchwrapper.IClassNameTransformer"};
	Class[] temporaryClassChecks = new Class[temporaryClassNames.length];
	{
		try{
			int num = temporaryClassNames.length;
			Class[] classChecks = new Class[num];
			for(int cur=0;cur<num;cur++){
				classChecks[cur] = Class.forName(this.temporaryClassNames[cur]);
			}
			this.temporaryClassChecks = classChecks;
		}catch(Exception e){
		}
	}
	Class temporaryClassCheckStop = WPLClassTransformer.class;
	private boolean hasEdited = false;
	byte[] temporaryFixOfStuff(String name, String transformedName, byte[] clazz){
		if(hasEdited){
			Iterator<IClassTransformer> iter = WPLTweaker.instance.classLoader.getTransformers().iterator();
			while(iter.hasNext()){
				IClassTransformer next = iter.next();
				if(this.temporaryClassCheckStop.equals(next.getClass()))
					break;
				for(Class each : temporaryClassChecks){
					if(each.isAssignableFrom(next.getClass())){
						clazz = next.transform(name, transformedName, clazz);
					}
				}
			}
		}
		return clazz;
	}
}
