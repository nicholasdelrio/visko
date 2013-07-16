/*
 * Created on Apr 15, 2005
 */
package org.mindswap.utils;


/**
 * This class offers a few helpful methods for handling primitive types
 * in the Java Reflection API.
 *
 * @author unascribed
 * @version $Rev: 2269 $; $Author: nick $; $Date: 2011/01/24 06:37:53 $
 */
public class ReflectionUtils {

//	/**
//	 * Takes a Class Variable for primitive Datatype or its Wrapper-Classes and a
//	 * String. Then an instance of the Class (for primitive Datatypes an instance
//	 * of its Wrapper-Class) is created and the string value is assigned to the
//	 * instance.
//	 *
//	 * @param lexicalValue
//	 * @param type
//	 * @return An instance of type with the assigned lexicalValue
//	 */
//	public static Object getCastedObjectFromStringValue(final String lexicalValue, final Class<?> type)
//	{
//
//    	if (String.class.equals(type))
//    		return lexicalValue;
//    	else if (URL.class.equals(type)) {
//    		try {
//    			return new URL(lexicalValue);
//    		}
//    		catch(MalformedURLException e) {
//    			e.printStackTrace();
//    		}
//    	}
//    	else if (short.class.equals(type) || Short.class.equals(type))
//    		return Short.valueOf(lexicalValue);
//    	else if (byte.class.equals(type) || Byte.class.equals(type))
//    		return Byte.valueOf(lexicalValue);
//    	else if (int.class.equals(type) || Integer.class.equals(type))
//    		return Integer.valueOf(lexicalValue);
//    	else if (long.class.equals(type) || Long.class.equals(type))
//    		return Long.valueOf(lexicalValue);
//    	else if (float.class.equals(type) || Float.class.equals(type))
//    		return Float.valueOf(lexicalValue);
//    	else if (double.class.equals(type) || Double.class.equals(type))
//    		return Double.valueOf(lexicalValue);
//    	else if (char.class.equals(type) || Character.class.equals(type))
//    		return Character.valueOf(lexicalValue.charAt(0));
//    	else if (boolean.class.equals(type) || Boolean.class.equals(type))
//    		return Boolean.valueOf(lexicalValue);
//
//    	// TODO dmi catch classes with a constructor with one string param
//		return null;
//	}

	/**
	 * Takes a String and returns a Class Object associated with the given
	 * value of the String (fully qualified class name). Method does also
	 * work for primitive classes (int, float, etc).
	 *
	 * @param fqcn Fully qualified class name.
	 * @return Class Object associated with <code>fqcn</code>
	 */
    public static final Class<?> getClassFromString(final String fqcn)
    {
   	 // treat primitive datatypes
   	 if ("int".equals(fqcn))
   		 return Integer.TYPE;
   	 else if ("long".equals(fqcn))
   		 return Long.TYPE;
   	 else if ("float".equals(fqcn))
   		 return Float.TYPE;
   	 else if ("double".equals(fqcn))
   		 return Double.TYPE;
   	 else if ("boolean".equals(fqcn))
   		 return Boolean.TYPE;
   	 else if ("short".equals(fqcn))
   		 return Short.TYPE;
   	 else if ("char".equals(fqcn))
   		 return Character.TYPE;
   	 else if ("byte".equals(fqcn))
   		 return Byte.TYPE;

   	 // treat real classes
   	 try {
   		 return Class.forName(fqcn);
   	 } catch (Exception e) {
   		 return null;
   	 }
    }

//	/**
//	 * Takes a primitive data type (as a string) and returns the corresponding
//	 * wrapper class (as a string), i.e. returns java.lang.Integer for int.
//	 *
//	 * @return String the name of the wrapper class for the given primitive data type
//	 */
//    public static String getWrapperTypeForPrimitive(final String primitive) {
//    	// treat primitive datatypes
//    	if ("short".equals(primitive))
//    		return Short.class.getName();
//    	else if ("byte".equals(primitive))
//    		return Byte.class.getName();
//    	else if ("int".equals(primitive))
//    		return Integer.class.getName();
//    	else if ("long".equals(primitive))
//    		return Long.class.getName();
//    	else if ("float".equals(primitive))
//    		return Float.class.getName();
//    	else if ("double".equals(primitive))
//    		return Double.class.getName();
//    	else if ("char".equals(primitive))
//    		return Character.class.getName();
//    	else if ("boolean".equals(primitive))
//    		return Boolean.class.getName();
//    	// treat real classes
//		return primitive;
//    }

//	public static Map<String, Class<?>> inferFieldFromGetter(final Class<?> claz)
//	{
//		Map<String, Class<?>> inferredFields = new HashMap<String, Class<?>>();
//
//		Method[] methods = claz.getMethods();
//		for (int i = 0; i < methods.length; i++)
//		{
//			String methodName = methods[i].getName();
//			if (methodName.startsWith("get"))
//			{
//				inferredFields.put(methodName.substring(3), methods[i].getReturnType());
//			}
//			else if (methodName.startsWith("set") && methods[i].getParameterTypes().length == 1)
//			{
//				inferredFields.put(methodName.substring(3), methods[i].getParameterTypes()[0]);
//			}
//		}
//		return inferredFields;
//	}
}
