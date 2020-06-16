package com.example.androidutils;

import android.text.TextUtils;

import com.example.androidutils.customLog.LogManager;
import com.example.androidutils.customLog.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.example.androidutils.MainActivity.MODULE_NAME;

/**
 * Created by Bingo on 2018/3/8.
 */
public class ReflectionUtils {
    private static final String TAG = ReflectionUtils.class.getSimpleName();
    private static Logger sLogger = LogManager.getLogger(MODULE_NAME);

    /**
     * 会抛异常的反射调用
     */
    public static Object invokeMethodThrow(String className, String methodName, Class[] argClass,
                                           Object object, Object[] args) throws Exception {
        Class<?> cla = getClassThrow(className, object);
        Method method = null;
        method = cla.getDeclaredMethod(methodName, argClass);
        method.setAccessible(true);
        return method.invoke(object, args);
    }

    /**
     * 不抛异常的反射调用
     * @param className 反射的类名
     * @param object 返回实例的类, {@code null}为调用静态方法
     * @param methodName 方法名
     * @param args 参数数组
     * @param argClass 参数对应的class.
     * @return 返回对应的object
     * 注:argClass参数不能从args中获取。int.class和Integer.class是不一样的。外界传进来的int类型我们使用getClass
     * 会返回Integer.class
     * 不需要同时提供完整类名和实例对象，提供其中一个即可。
     */
    public static Object invokeMethod(String className, String methodName, Class[] argClass,
                                      Object object, Object[] args){
        try {
            return invokeMethodThrow(className, methodName, argClass, object, args);
        } catch (Exception e) {
            sLogger.e(TAG, "invokeMethod caught exception: " , e);
        }
        return null;
    }

    /*--- multi params ---*/
    // static method
    public static Object invokeMethod(String className, String methodName, Class[] argClass, Object[] args) {
        return invokeMethod(className, methodName, argClass, null, args);
    }

    public static Object invokeMethod(Object object, String methodName, Class[] argClass, Object[] args) {
        return invokeMethod(null, methodName, argClass, object, args);
    }

    /*--- single param ---*/
    // static method
    public static Object invokeMethod(String className, String methodName) {
        return invokeMethod(className, methodName, null, null, null);
    }

    public static Object invokeMethod(Object object, String methodName) {
        return invokeMethod(null, methodName, null, object, null);
    }

    // param boolean
    public static Object invokeMethod(String className, String methodName, boolean boolParam) {
        return invokeMethod(className, methodName, new Class[]{boolean.class}, null, new Object[]{boolParam});
    }

    public static Object invokeMethod(Object object, String methodName, boolean boolParam) {
        return invokeMethod(null, methodName, new Class[]{boolean.class}, object, new Object[]{boolParam});
    }

    // param int
    public static Object invokeMethod(String className, String methodName, int intParam) {
        return invokeMethod(className, methodName, new Class[]{int.class}, null, new Object[]{intParam});
    }

    public static Object invokeMethod(Object object, String methodName, int intParam) {
        return invokeMethod(null, methodName, new Class[]{int.class}, object, new Object[]{intParam});
    }

    // param String
    public static Object invokeMethod(String className, String methodName, String strParam) {
        return invokeMethod(className, methodName, new Class[]{String.class}, null, new Object[]{strParam});
    }

    public static Object invokeMethod(Object object, String methodName, String strParam) {
        return invokeMethod(null, methodName, new Class[]{String.class}, object, new Object[]{strParam});
    }

    // param long
    public static Object invokeMethod(String className, String methodName, long longParam) {
        return invokeMethod(className, methodName, new Class[]{long.class}, null, new Object[]{longParam});
    }

    public static Object invokeMethod(Object object, String methodName, long longParam) {
        return invokeMethod(null, methodName, new Class[]{long.class}, object, new Object[]{longParam});
    }

    // param float
    public static Object invokeMethod(String className, String methodName, float floatParam) {
        return invokeMethod(className, methodName, new Class[]{float.class}, null, new Object[]{floatParam});
    }

    public static Object invokeMethod(Object object, String methodName, float floatParam) {
        return invokeMethod(null, methodName, new Class[]{float.class}, object, new Object[]{floatParam});
    }

    // param double
    public static Object invokeMethod(String className, String methodName, double doubleParam) {
        return invokeMethod(className, methodName, new Class[]{double.class}, null, new Object[]{doubleParam});
    }

    public static Object invokeMethod(Object object, String methodName, double doubleParam) {
        return invokeMethod(null, methodName, new Class[]{double.class}, object, new Object[]{doubleParam});
    }

    // other param
    public static Object invokeMethod(String className, String methodName, Class paramClass, Object param) {
        return invokeMethod(className, methodName, new Class[]{paramClass}, null, new Object[]{param});
    }

    public static Object invokeMethod(Object object, String methodName, Class paramClass, Object param) {
        return invokeMethod(null, methodName, new Class[]{paramClass}, object, new Object[]{param});
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        }  catch (Exception e) {
            sLogger.e(TAG, "forName caught exception: " , e);
        }
        return null;
    }

    public static Class<?> getClassThrow(String className, Object object) throws Exception {
        Class<?> cla = null;
        if ((object == null) && TextUtils.isEmpty(className)) {
            throw new IllegalArgumentException("Both className and object is empty!");
        }

        if (object == null) {
            cla = Class.forName(className);
        } else {
            cla = object.getClass();
        }
        return cla;
    }

    public static Object newInstance(String className) {
        return newInstance(className, null, null);
    }

    public static Object newInstance(Class clazz) {
        return newInstance(clazz, null, null);
    }

    public static Object newInstance(Class<?> clazz, Class[] argClass, Object[] args) {
        try {
            return clazz.getConstructor(argClass).newInstance(args);
        } catch (Exception e) {
            sLogger.e(TAG, "newInstance caught exception: " , e);
        }
        return null;
    }

    public static Object newInstance(String className, Class[] argClass, Object[] args) {
        return newInstance(forName(className), argClass, args);
    }

    public static Object getMemberThrow(String fieldName, Object objOrCla) throws Exception {
        Class<?> cla = objOrCla instanceof Class ? (Class<?>) objOrCla :
                (objOrCla instanceof String ? Class.forName((String) objOrCla) : objOrCla.getClass());
        Field field = cla.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(objOrCla);
    }

    public static Object getPublicMemberThrow(String fieldName, Object objOrCla) throws Exception {
        Class<?> cla = objOrCla instanceof Class ? (Class<?>) objOrCla :
                (objOrCla instanceof String ? Class.forName((String) objOrCla) : objOrCla.getClass());
        Field field = cla.getField(fieldName);
        field.setAccessible(true);
        return field.get(objOrCla);
    }

    public static Object getMember(String memberName, Object objOrCla) {
        try {
            return getMemberThrow(memberName, objOrCla);
        } catch (Exception e) {
            sLogger.e(TAG, "getMember caught exception: " , e);
        }
        return null;
    }

    public static Object getPublicMember(String fieldName, Object objOrCla) {
        try {
            return getPublicMemberThrow(fieldName, objOrCla);
        } catch (Exception e) {
            sLogger.e(TAG, "getPublicMember caught exception: " , e);
        }
        return null;
    }
}
