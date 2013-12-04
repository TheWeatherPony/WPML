package weatherpony.launch.annotationImplementation;

import java.util.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import weatherpony.launch.annotations.actions.ChangeInstruction;

public class ChangeInstructionLogic implements AnnotationLogic{

	@Override
	public Class type(){
		return ChangeInstruction.class;
	}

	@Override
	public void dealWithAnnotationOnClass(ClassNode from, ClassNode to, AnnotationNode details){
		if (details.values != null)
        {
            for (int x = 0; x < details.values.size() - 1; x += 2)
            {
                Object key = details.values.get(x);
                Object value = details.values.get(x+1);
                if (key instanceof String && key.equals("makePublic")){
                    if (value instanceof Boolean){
                    	if((Boolean)value){
                    		if((to.access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE)
                    			to.access &= (~Opcodes.ACC_PRIVATE);
                    		if((to.access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED)
                    			to.access &= (~Opcodes.ACC_PROTECTED);
                    		if((to.access & Opcodes.ACC_PUBLIC) != Opcodes.ACC_PUBLIC)
                    			if((to.access & Opcodes.ACC_PUBLIC) != Opcodes.ACC_PUBLIC)
                    				to.access |= (Opcodes.ACC_PUBLIC);
                    	}
                    }
                }
                if (key instanceof String && key.equals("removeFinal")){
                    if (value instanceof Boolean){
                    	if((Boolean)value){
                    		to.access &= (~Opcodes.ACC_FINAL);
                    	}
                    }
                }
            }
        }
	}

	@Override
	public void dealWithAnnotationOnField(ClassNode from, FieldNode on, ClassNode to, AnnotationNode details){
		String name = on.name;
		Iterator<FieldNode> iter = to.fields.iterator();
		FieldNode onto = null;
		while(iter.hasNext()){
			FieldNode next = iter.next();
			if(next.name.equals(name))
				onto = next;
		}
		if(onto == null){
			//what SHOULD be done here? :/
			return;//?
		}
		if (details.values != null)
        {
            for (int x = 0; x < details.values.size() - 1; x += 2)
            {
                Object key = details.values.get(x);
                Object value = details.values.get(x+1);
                if (key instanceof String && key.equals("makePublic")){
                    if (value instanceof Boolean){
                    	if((Boolean)value){
                    		if((onto.access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE)
                    			onto.access &= (~Opcodes.ACC_PRIVATE);
                    		if((onto.access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED)
                    			onto.access &= (~Opcodes.ACC_PROTECTED);
                    		if((onto.access & Opcodes.ACC_PUBLIC) != Opcodes.ACC_PUBLIC)
                    			onto.access |= (Opcodes.ACC_PUBLIC);
                    	}
                    }
                }
                if (key instanceof String && key.equals("removeFinal")){
                    if (value instanceof Boolean){
                    	if((Boolean)value){
                    		if((onto.access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL)
                    			onto.access &= (~Opcodes.ACC_FINAL);
                    	}
                    }
                }
            }
        }
	}

	@Override
	public void dealWithAnnotationOnMethod(ClassNode from, MethodNode on, ClassNode to, AnnotationNode details){
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
			//what SHOULD be done here? :/
			return;//?
		}
		if (details.values != null)
        {
            for (int x = 0; x < details.values.size() - 1; x += 2)
            {
                Object key = details.values.get(x);
                Object value = details.values.get(x+1);
                if (key instanceof String && key.equals("makePublic")){
                    if (value instanceof Boolean){
                    	if((Boolean)value){
                    		if((onto.access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE)
                    			onto.access &= (~Opcodes.ACC_PRIVATE);
                    		if((onto.access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED)
                    			onto.access &= (~Opcodes.ACC_PROTECTED);
                    		if((onto.access & Opcodes.ACC_PUBLIC) != Opcodes.ACC_PUBLIC)
                    			onto.access |= (Opcodes.ACC_PUBLIC);
                    	}
                    }
                }
                if (key instanceof String && key.equals("removeFinal")){
                    if (value instanceof Boolean){
                    	if((Boolean)value){
                    		if((onto.access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL)
                    			onto.access &= (~Opcodes.ACC_FINAL);
                    	}
                    }
                }
            }
        }
	}

}
