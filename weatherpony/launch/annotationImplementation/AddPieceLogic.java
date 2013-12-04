package weatherpony.launch.annotationImplementation;

import java.util.Iterator;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import weatherpony.launch.annotations.actions.AddField;

public class AddPieceLogic implements AnnotationLogic{

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
		//on.invisibleAnnotations.remove(details);
		//on.accept(to);
		to.fields.add(on);
	}

	@Override
	public void dealWithAnnotationOnMethod(ClassNode from, MethodNode on, ClassNode to, AnnotationNode details){
		//on.invisibleAnnotations.remove(details);
		//on.accept(to);
		String name = on.name;
		String signature = on.signature;
		Iterator<MethodNode> iter = to.methods.iterator();
		MethodNode onto = null;
		while(iter.hasNext()){
			MethodNode next = iter.next();
			if(next.name.equals(name)){
				if((signature == null & next.signature == null) || (signature.equals(next.signature)))
					onto = next;
			}
		}
		if(onto == null){
			to.methods.add(on);
			return;
		}
		if (details.values != null)
        {
            for (int x = 0; x < details.values.size() - 1; x += 2)
            {
                MethodNode newM = new MethodNode(onto.access, name, onto.desc, signature, on.exceptions.toArray(new String[on.exceptions.size()]));
        		to.methods.remove(onto);
        		to.methods.add(newM);
        		newM.instructions = on.instructions;
        		newM.localVariables = on.localVariables;
        		newM.invisibleParameterAnnotations = onto.invisibleParameterAnnotations;
        		newM.annotationDefault = onto.annotationDefault;
        		newM.attrs = onto.attrs;
        		newM.maxLocals = on.maxLocals;
        		newM.maxStack = on.maxStack;
        		newM.tryCatchBlocks = on.tryCatchBlocks;
        		
        		
        		newM.invisibleAnnotations = onto.invisibleAnnotations;
        		newM.visibleAnnotations = onto.visibleAnnotations;
        		
        		//newM.visibleAnnotations = AnnotationManager.instance.markModEdit(on.visibleAnnotations);
            }
        }
		
	}
}
