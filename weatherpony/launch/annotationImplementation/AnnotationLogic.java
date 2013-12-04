package weatherpony.launch.annotationImplementation;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public interface AnnotationLogic {
	public Class type();
	public void dealWithAnnotationOnClass(ClassNode from, ClassNode to, AnnotationNode details);
	public void dealWithAnnotationOnField(ClassNode from, FieldNode on, ClassNode to, AnnotationNode details);
	public void dealWithAnnotationOnMethod(ClassNode from, MethodNode on, ClassNode to, AnnotationNode details);
	
}
