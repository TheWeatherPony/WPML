package weatherpony.launch.annotationImplementation;

import java.util.Iterator;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import weatherpony.launch.annotations.actions.AddField;

public class ReplaceStaticInitializerLogic implements AnnotationLogic{

	@Override
	public Class type(){
		return AddField.class;
	}

	@Override
	public void dealWithAnnotationOnClass(ClassNode from, ClassNode to, AnnotationNode details){
		throw new IllegalArgumentException();
	}

	@Override
	public void dealWithAnnotationOnField(ClassNode from, FieldNode on, ClassNode to, AnnotationNode details){
		Iterator<MethodNode> iter = from.methods.iterator();
		MethodNode staticInitF = null;
		while(iter.hasNext()){
			MethodNode next = iter.next();
			if(next.name.equals("<clinit>")){
				staticInitF = next;
				break;//only one class (static) init
			}
		}
		if(staticInitF == null){
			//what SHOULD be done here? :/
			return;//?
		}
		
		Iterator<FieldNode> iter2 = to.fields.iterator();
		FieldNode targetField = null;
		while(iter2.hasNext()){
			FieldNode next = iter2.next();
			if(next.name.equals(on.name)){
				targetField = next;
				break;
			}
		}
		if(targetField == null){
			//what SHOULD be done here? :/
			return;//?
		}
		throw new UnsupportedOperationException();
		/*staticInitF.instructions.
		StaticInitMerger.
		*/
	}
	/*
	 L19
    LINENUMBER 98 L19
    SIPUSH 4096
    NEWARRAY T_BOOLEAN
    PUTSTATIC net/minecraft/block/Block.useNeighborBrightness : [Z
   L20
    LINENUMBER 99 L20
    NEW net/minecraft/block/BlockGrass
    ICONST_5
    INVOKESPECIAL net/minecraft/block/BlockGrass.<init>(I)V
   L21
    LINENUMBER 100 L21
    NEW net/minecraft/block/BlockStone
    DUP
    ICONST_1
    INVOKESPECIAL net/minecraft/block/BlockStone.<init>(I)V
    LDC 1.5
    INVOKEVIRTUAL net/minecraft/block/BlockStone.setHardness(F)Lnet/minecraft/block/Block;
    LDC 10.0
    INVOKEVIRTUAL net/minecraft/block/Block.setResistance(F)Lnet/minecraft/block/Block;
    GETSTATIC net/minecraft/block/Block.soundStoneFootstep : Lnet/minecraft/block/StepSound;
    INVOKEVIRTUAL net/minecraft/block/Block.setStepSound(Lnet/minecraft/block/StepSound;)Lnet/minecraft/block/Block;
    LDC "stone"
    INVOKEVIRTUAL net/minecraft/block/Block.setUnlocalizedName(Ljava/lang/String;)Lnet/minecraft/block/Block;
    LDC "stone"
    INVOKEVIRTUAL net/minecraft/block/Block.func_111022_d(Ljava/lang/String;)Lnet/minecraft/block/Block;
    PUTSTATIC net/minecraft/block/Block.stone : Lnet/minecraft/block/Block;
   L22
    LINENUMBER 101 L22
    NEW net/minecraft/block/BlockGrass
    DUP
    ICONST_2
    INVOKESPECIAL net/minecraft/block/BlockGrass.<init>(I)V
    LDC 0.6
    INVOKEVIRTUAL net/minecraft/block/BlockGrass.setHardness(F)Lnet/minecraft/block/Block;
    GETSTATIC net/minecraft/block/Block.soundGrassFootstep : Lnet/minecraft/block/StepSound;
    INVOKEVIRTUAL net/minecraft/block/Block.setStepSound(Lnet/minecraft/block/StepSound;)Lnet/minecraft/block/Block;
    LDC "grass"
    INVOKEVIRTUAL net/minecraft/block/Block.setUnlocalizedName(Ljava/lang/String;)Lnet/minecraft/block/Block;
    LDC "grass"
    INVOKEVIRTUAL net/minecraft/block/Block.func_111022_d(Ljava/lang/String;)Lnet/minecraft/block/Block;
    CHECKCAST net/minecraft/block/BlockGrass
    PUTSTATIC net/minecraft/block/Block.grass : Lnet/minecraft/block/BlockGrass;
   L23
	 */

	@Override
	public void dealWithAnnotationOnMethod(ClassNode from, MethodNode on, ClassNode to, AnnotationNode details){
		throw new IllegalArgumentException();
	}
}
	//<clinit>
