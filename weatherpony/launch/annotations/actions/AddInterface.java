package weatherpony.launch.annotations.actions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AddInterface {
	public String[] fullInterfaceName();
}
