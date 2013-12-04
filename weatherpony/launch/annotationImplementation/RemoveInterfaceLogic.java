package weatherpony.launch.annotationImplementation;

import java.util.List;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import weatherpony.launch.annotations.actions.RemoveInterface;

public class RemoveInterfaceLogic implements AnnotationLogic{

	@Override
	public Class type() {
		return RemoveInterface.class;
	}

	@Override
	public void dealWithAnnotationOnClass(ClassNode from, ClassNode to, AnnotationNode details) {
		if (details.values != null)
        {
            for (int x = 0; x < details.values.size() - 1; x += 2)
            {
                Object key = details.values.get(x);
                Object value = details.values.get(x+1);
                if (key instanceof String && key.equals("fullInterfaceName")){
                    if (value instanceof List){
                    	List<String> removeInterfaces = (List) value;
                        for(String each : removeInterfaces){
                        	to.interfaces.remove(each);
                        }
                        //to.visibleAnnotations = AnnotationManager.instance.markModEdit(to.visibleAnnotations);
                    }
                }
            }
        }
	}

	@Override
	public void dealWithAnnotationOnField(ClassNode from, FieldNode on, ClassNode to, AnnotationNode details) {
		throw new IllegalArgumentException();
	}

	@Override
	public void dealWithAnnotationOnMethod(ClassNode from, MethodNode on, ClassNode to, AnnotationNode details) {
		throw new IllegalArgumentException();
	}
}
