package com.aofei.kettle.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {

	public static String getString(Object obj, String name) throws Exception {
		return (String) get(obj, name);
	}
	
	public static Boolean getBoolean(Object obj, String name) throws Exception {
		return (Boolean) get(obj, name);
	}
	
	public static Object get(Object obj, String name) throws Exception {
		Class clazz = obj.getClass();
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if(name.equals(propertyDescriptor.getName())) {
				Method readMethod = propertyDescriptor.getReadMethod();
				return readMethod.invoke(obj, null);
			}
		}
		
		System.out.println("not found: [" + obj.getClass().getName() + "] read method: " + name);
		return null;
	}
	
	public static Object[] getArray(Object obj, String name) throws Exception {
		Class clazz = obj.getClass();
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if(name.equals(propertyDescriptor.getName())) {
				Method readMethod = propertyDescriptor.getReadMethod();
				return (Object[]) readMethod.invoke(obj, null);
			}
		}
		
		System.out.println("not found: [" + obj.getClass().getName() + "] read method: " + name);
		return null;
	}
	
	public static void set(Object obj, String name, Object value) throws Exception {
		Class clazz = obj.getClass();
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if(name.equals(propertyDescriptor.getName())) {
				Method writeMethod = propertyDescriptor.getWriteMethod();
				writeMethod.invoke(obj, value);
				return;
			}
		}
		
		System.out.println("not found: [" + obj.getClass().getName() + "] write method: " + name);
	}
	
	public static void setArray(Object obj, String name, Object[] value) throws Exception {
		Class clazz = obj.getClass();
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if(name.equals(propertyDescriptor.getName())) {
				Method writeMethod = propertyDescriptor.getWriteMethod();
				writeMethod.invoke(obj, value);
				return;
			}
		}
		
		System.out.println("not found: [" + obj.getClass().getName() + "] write method: " + name);
	}
	
	public static Object call(Object obj, String method, Object... value) throws Exception {
		Class clazz = obj.getClass();
		
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if(method.equals(m.getName())) {
				return m.invoke(obj, value);
			}
			
		}
		System.out.println("not found: [" + obj.getClass().getName() + "] method: " + method);
		return null;
	}
	
	public static Object callStatic(Class clazz, String method, Object... value) throws Exception {
		
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if(method.equals(m.getName())) {
				return m.invoke(null, value);
			}
			
		}
		return null;
	}
	
	public static Object getEnumName(Object obj) throws Exception {
		
		Method[] methods = obj.getClass().getMethods();
		for (Method m : methods) {
			if("name".equals(m.getName())) {
				return m.invoke(obj);
			}
			
		}
		return null;
	}
	
	public static Object getFieldValue(Object obj, String name) throws Exception {
		Field nameField = obj.getClass().getDeclaredField(name);
		boolean flag = nameField.isAccessible();
		nameField.setAccessible(true);
		Object val = nameField.get(obj);
		nameField.setAccessible(flag);
		return val;
		
	}
	
	public static String getFieldString(Object obj, String name) throws Exception {
		return (String) getFieldValue(obj, name);
		
	}
	
	public static boolean getFieldBoolean(Object obj, String name) throws Exception {
		return (Boolean) getFieldValue(obj, name);
	}
	
	public static void setFieldValue(Object obj, String name, Object value) throws Exception {
		Field nameField = obj.getClass().getDeclaredField(name);
		boolean flag = nameField.isAccessible();
		nameField.setAccessible(true);
		nameField.set(obj, value);
		nameField.setAccessible(flag);
	}
	
	public static Object callInternalEnumMethod(Class parent, String name, String method, Object... value) throws Exception {
		for (Class clazz : parent.getClasses()) {
			if(clazz.getSimpleName().equals(name)) {
				for (Method m : clazz.getMethods()) {
					if(m.getName().equals(method)) {
						return m.invoke(null, value);
					}
				}
			}
		}
		
		return null;
	}

}
