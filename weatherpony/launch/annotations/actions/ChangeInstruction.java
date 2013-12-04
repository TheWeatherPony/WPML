package weatherpony.launch.annotations.actions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeInstruction {
	public boolean makePublic() default false;
	//not constructors below this
	public boolean removeFinal() default false;//does *NOT* work on static final primitives. The compiler makes these inlined.
	/*
	public boolean changeStatic() default false;
	public boolean staticEdit() default false;*/
}
