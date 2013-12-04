package weatherpony.launch.annotationImplementation;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import weatherpony.launch.annotations.actions.SetExtends;

public class SetExtendsLogic implements AnnotationLogic{

	@Override
	public Class type() {
		return SetExtends.class;
	}

	@Override
	public void dealWithAnnotationOnClass(ClassNode from, ClassNode to, AnnotationNode details) {
		if (details.values != null){
            for (int x = 0; x < details.values.size() - 1; x += 2){
                Object key = details.values.get(x);
                Object value = details.values.get(x+1);
                if (key instanceof String && key.equals("fullExtendsName")){
                    if (value instanceof String ){
                    	to.superName = TypeConverter.instance.convertToInternalName((String) value);

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
