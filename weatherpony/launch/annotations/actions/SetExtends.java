package weatherpony.launch.annotations.actions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SetExtends {
	public String fullExtendsName();
}
