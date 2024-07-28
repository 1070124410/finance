package com.finance.anubis.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtField;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.ConstPool;
import org.apache.ibatis.javassist.bytecode.FieldInfo;
import org.apache.ibatis.javassist.bytecode.annotation.Annotation;
import org.apache.ibatis.javassist.bytecode.annotation.MemberValue;
import org.apache.ibatis.javassist.bytecode.annotation.StringMemberValue;

import java.util.Arrays;
import java.util.Set;

@Slf4j
public class AddAnnotation {

    /**
     * 功能：动态的给类属性添加注解
     * 用于本来没有注解，直接添加上一行整注解
     * （可以持久化到内存，可作为工具类使用）
     *
     * @param className     类名
     * @param attributeName 类属性
     * @param typeName      注解类型
     */
    public static void addAnnotation(String className, String attributeName, String typeName) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ct = pool.get(className);
            CtField cf = ct.getField(attributeName);
            FieldInfo fieldInfo = cf.getFieldInfo();
            AnnotationsAttribute attribute = (AnnotationsAttribute) fieldInfo.getAttribute(AnnotationsAttribute.visibleTag);
            ConstPool cp = fieldInfo.getConstPool();
            Annotation annotation = new Annotation(typeName, cp);
            log.info("添加注解" + annotation);
            attribute.addAnnotation(annotation);
            log.info("添加后的所有注解" + Arrays.toString(attribute.getAnnotations()));
        } catch (NotFoundException e) {
            log.info("此类不存在" + e);
        }
    }

    /**
     * 遍历注解所有属性
     *
     * @param className     类名
     * @param attributeName 类属性
     */
    public static void queryAnnotation(String className, String attributeName) {
        log.info("====开始遍历注解所有属性=====");
        try {
            ClassPool pool = ClassPool.getDefault();
            //获取实体类
            CtClass ct = pool.get(className);
            //获取属性
            CtField cf = ct.getField(attributeName);
            //获取属性字段信息
            FieldInfo fieldInfo = cf.getFieldInfo();
            //获取属性字段的运行时可见注释
            AnnotationsAttribute attribute = (AnnotationsAttribute) fieldInfo.getAttribute(AnnotationsAttribute.visibleTag);
            //获取所有注解
            Annotation[] annotations = attribute.getAnnotations();
            int sum = 0;
            //遍历注解
            for (Annotation annotation : annotations) {
                sum++;
                System.out.println("注解" + sum + "：" + annotation.toString());
                //如果没有属性名，就下一个循环
                if (annotation.getMemberNames() == null) {
                    log.info("！无属性名跟属性值！");
                    continue;
                }
                //获取注解的所有属性名，跟属性值
                for (String memberName : annotation.getMemberNames()) {
                    MemberValue memberValue = annotation.getMemberValue(memberName);
                    log.info("获取到的注解的属性名：" + memberName);
                    log.info("获取到的注解的属性值：" + memberValue);
                }
            }
        } catch (NotFoundException e) {
            log.info("此类不存在" + e);
        }
    }

    /**
     * 给字段注解添加属性，添加单一注解的属性
     * （不能持久化到内存，只能作为代码一部分使用）
     *
     * @param className     类名
     * @param attributeName 类属性
     * @param name          指定注解名
     * @param key           给注解添加的属性名
     * @param value         给注解添加的属性的值
     */
    public static void editAnnotation(String className, String attributeName, String name, String key, String value) {
        try {
            ClassPool pool = ClassPool.getDefault();
            //获取实体类
            CtClass ct = pool.get(className);
            //获取属性
            CtField cf = ct.getField(attributeName);
            //获取属性字段信息
            FieldInfo fieldInfo = cf.getFieldInfo();
            //获取该字段的常量池
            ConstPool constPool = cf.getFieldInfo().getConstPool();
            //获取属性字段的运行时可见注释
            AnnotationsAttribute attribute = (AnnotationsAttribute) fieldInfo.getAttribute(AnnotationsAttribute.visibleTag);
            //获取所有注解
            Annotation[] annotations = attribute.getAnnotations();
            for (Annotation annotation : annotations) {
                //如果没有属性名，就下一个循环
                if (annotation.getMemberNames() == null) {
                    continue;
                }
                if (annotation.toString().contains(name)) {
                    log.info("添加属性");
                    //给指定的注解添加属性
                    annotation.addMemberValue(key, new StringMemberValue(value, constPool));
                }
            }

            //遍历检查是否添加上注解
            for (Annotation annotation : annotations) {
                Set<String> memberNames = annotation.getMemberNames();
                if (annotation.getMemberNames() == null) {
                    log.info("！无属性名跟属性值！");
                    continue;
                }
                for (String memberName : memberNames) {
                    log.info("名称：" + memberName + "值：" + annotation.getMemberValue(memberName));
                }
            }
        } catch (Exception e) {
            log.info("捕获异常" + e.getMessage());
        }
    }

    /**
     * 获取指定字段的指定注解
     *
     * @param className     类名
     * @param attributeName 类属性
     * @param annotionName  指定注解名
     */
    public static Annotation getAnnotation(String className, String attributeName, String annotionName) {
        Annotation tableField = null;
        try {
            ClassPool pool = ClassPool.getDefault();
            //获取实体类
            CtClass ct = pool.get(className);
            //获取属性
            CtField cf = ct.getField(attributeName);
            //获取属性字段信息
            FieldInfo fieldInfo = cf.getFieldInfo();
            //获取该字段的常量池
            ConstPool constPool = cf.getFieldInfo().getConstPool();
            //获取属性字段的运行时可见注释
            AnnotationsAttribute attribute = (AnnotationsAttribute) fieldInfo.getAttribute(AnnotationsAttribute.visibleTag);
            //获取该字段所有注解
            Annotation[] annotations = attribute.getAnnotations();
            for (Annotation annotation : annotations) {
                //注解名包含字段，就命中返回
                if (annotation.toString().contains(annotionName)) {
                    tableField = annotation;
                    return tableField;
                }
            }
        } catch (NotFoundException e) {
            log.info("捕获异常" + e.getMessage());
        }
        return tableField;
    }

    /**
     * 根据给定的注解体，给其添加属性
     * （不能持久化到内存，只能作为代码一部分使用）
     *
     * @param className     类名
     * @param attributeName 类属性
     * @param annotation    注解实体
     * @param key           给注解添加的属性
     * @param value         给注解添加的属性的值
     * @return
     */
    public static Annotation editAnnotationBy(String className, String attributeName, Annotation annotation, String key, String value) {
        try {
            ClassPool pool = ClassPool.getDefault();
            //获取实体类
            CtClass ct = pool.get(className);
            //获取属性
            CtField cf = ct.getField(attributeName);
            //获取该字段的常量池
            ConstPool constPool = cf.getFieldInfo().getConstPool();
            //给指定的注解添加属性
            annotation.addMemberValue(key, new StringMemberValue(value, constPool));
            return annotation;
        } catch (Exception e) {
            log.info("捕获异常" + e.getMessage());
        }
        return annotation;
    }

    /**
     * 功能：使用生成的注解体 ；动态的给类注解添加属性
     * （可以持久化到内存，可作为工具类使用）
     *
     * @param className     类名
     * @param attributeName 类属性
     */
    public static void addAnnotationBy(String className, String attributeName, Annotation annotation) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ct = pool.get(className);
            CtField cf = ct.getField(attributeName);
            FieldInfo fieldInfo = cf.getFieldInfo();
            AnnotationsAttribute attribute = (AnnotationsAttribute) fieldInfo.getAttribute(AnnotationsAttribute.visibleTag);
            attribute.addAnnotation(annotation);
            log.info("添加后的所有注解" + Arrays.toString(attribute.getAnnotations()));
        } catch (NotFoundException e) {
            log.info("此类不存在" + e);
        }
    }
}

