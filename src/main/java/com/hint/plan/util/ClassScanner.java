package com.hint.plan.util;

import com.alibaba.fastjson.JSON;
import com.hint.plan.PlanApplication;
import com.hint.plan.annotations.Order;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

public class ClassScanner {
    private static Set<Class<?>> classes;
    private static  Map<Class<?>, Integer> annotationMap;
    public static void main(String[] args) {
        String packageName = PlanApplication.class.getPackage().getName();
        Set<Class<?>> clsList = getAllClasses(packageName);
        System.out.println("--------->:" + JSON.toJSONString(clsList));

        Map<Class<?>, Integer> annotationMap = getPlanInterceptor(packageName);
        System.out.println("----获取被Order注解标注的bean-----" + annotationMap);
    }

    /**
     * 获取注解bean的map
     * @param packageName
     * @return
     */
    public static  Map<Class<?>, Integer> getPlanInterceptor(String packageName) {
        Set<Class<?>> clsList = getAllClasses(packageName);
        if (clsList == null || clsList.isEmpty()) {
             return annotationMap;
        }
        if (annotationMap == null) {
            annotationMap = new HashMap<>(16);
        }

        for (Class<?> cls : clsList) {
            Order annotation = cls.getAnnotation(Order.class);
            if (annotation == null)
                continue;
            System.out.println("annotaion->:" + annotation);
            annotationMap.put(cls, Integer.valueOf(annotation.value()));
        }
        return annotationMap;
    }

    /**
     * 获取所有class文件
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getAllClasses(String packageName) {
        if (classes == null) {
            classes = new HashSet<>(32);
            baseScanner(packageName, classes);
        }

        return classes;
    }

    /**
     * 扫描
     * @param packageName
     * @param set
     */
    public static void baseScanner(String packageName, Set<Class<?>> set) {
        // 扫描指定目录的所有类以获取类上的自定义order注解
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        // 是否递归
        boolean recursive = Boolean.TRUE;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(
                    packageDirName);
            // TODO Question：使用FASTJSON对dirs进行序列化，会导致dirs.hasMoreElements()始终为false
            // System.out.println("------------>:" + JSON.toJSONString(dirs));
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                System.out.println("------------->:" + protocol);
                if ("file".equals(protocol)) {
                    // 如果是文件
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    System.out.println("-------->:" + filePath);
                    findAndAddClassesInPackageByFile(set, packageName, recursive, filePath);
                } else if ("jar".equals(protocol)) {
                    System.out.println("------>jar");
                    // TODO 先不管
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param classes
     * @param packageName
     * @param recursive
     * @param filePath
     */
    private static void findAndAddClassesInPackageByFile(Set<Class<?>> classes, String packageName, boolean recursive, String filePath) {
        // 根据目录创建一个文件
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            // 如果不是不存在或者不是目录直接返回
            return;
        }

        // 如果存在就获取包下所有文件，包括目录
        File[] dirFiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });

        for (File file : dirFiles) {
            if (file.isDirectory()) {
                // 如果是目录则递归继续扫描
                // classes, packageName, recursive, filePath
                findAndAddClassesInPackageByFile(classes,
                        packageName + "." + file.getName(),
                        recursive,
                        file.getAbsolutePath()
                );
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    //classes.add(Class.forName(packageName + '.' + className));
                    //这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }
}
