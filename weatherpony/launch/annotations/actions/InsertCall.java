package weatherpony.launch.annotations.actions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InsertCall {
	public String at();
	public String params() default "";
	public String name();
	public Place where();
	
	public static enum Place{
		Beginning,
		End;
	}
}
