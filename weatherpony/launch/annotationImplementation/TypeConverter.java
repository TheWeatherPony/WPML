package weatherpony.launch.annotationImplementation;

import java.util.HashMap;
import java.util.Map;

public class TypeConverter{
	public static final TypeConverter instance = new TypeConverter();
	private Map<String,String> converts = new HashMap();
	
	private TypeConverter(){
		addBasics();
	}
	private void addMap(String code, String descriptor){
		this.converts.put(code, descriptor);
	}
	private void addBasics(){
		addMap("String","Ljava/lang/String;");
		addMap("Boolean","Ljava/lang/Boolean;");
		addMap("Byte","Ljava/lang/Byte;");
		addMap("Short","Ljava/lang/Short;");
		addMap("Integer","Ljava/lang/Integer;");
		addMap("Long","Ljava/lang/Long;");
		addMap("Float","Ljava/lang/Float;");
		addMap("Double","Ljava/lang/Double;");
		addMap("Character","Ljava/lang/Character;");
	}
	public String convertToDescriptor(String start){
		int size = start.length();
		int arrays = 0;
		for(;;){
			if(size <= 2){
				throw new RuntimeException();
			}
			if(start.charAt(size-2) == '[' && start.charAt(size-1) == ']'){
				arrays++;
				size-=2;
			}else{
				break;
			}
		}
		String noArrays = start.subSequence(0, size).toString();
		String convertedName = convertBase(noArrays);
		if(arrays == 0)
			return convertedName;
		StringBuffer nameMaker = new StringBuffer();
		for(int cur=0;cur<arrays;cur++)
			nameMaker.append('[');
		nameMaker.append(convertedName);
		return nameMaker.toString();
	}
	private String convertBase(String start){
		String conv = converts.get(start);
		if(conv != null)
			return conv;
		return 'L' + convertToInternalName(start) +';';
	}
	public String convertToInternalName(String name){
		return name.replace('.', '/');
	}
}
