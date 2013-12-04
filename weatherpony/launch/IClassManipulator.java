package weatherpony.launch;

import org.objectweb.asm.tree.ClassNode;

public interface IClassManipulator {
	public ClassNode manipulateTree(ClassNode tree);
}
