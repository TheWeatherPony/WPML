package weatherpony.launch.annotations.actions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ReplaceInitializer {
	public boolean CLASS() default false;
	public boolean INSTANCE() default false;
}
