package com.kg.platform.tree;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.kg.platform.tree.annotation.Children;
import com.kg.platform.tree.annotation.ParentId;
import com.kg.platform.tree.annotation.TreeId;
import com.kg.platform.tree.listener.TreeListener;

public final class TreeUtils {

    public static List<Field> getAllField(Class<?> cls, List<Field> fieldList) {
        if (null != cls) {
            Field fields[] = cls.getDeclaredFields();
            Arrays.stream(fields).forEach(field -> {
                fieldList.add(field);
            });
            getAllField(cls.getSuperclass(), fieldList);
        }
        return fieldList;
    }

    /**
     * 将List转换成Tree
     * 
     * @param clazz
     * @return
     */
    public static final <T> List<T> convert2Tree(List<T> data, Class<T> clazz, long rootParentId,
            TreeListener listener) {
        List<T> treeList = new ArrayList<>();
        if (null != data && data.size() > 0) {
            try {
                Map<Long, T> map = new LinkedHashMap<>();
                data.stream().forEach(item -> {
                    // if(null != listener){
                    // listener.each(item, i);
                    // }
                    try {
                        List<Field> fieldList = new ArrayList<>();
                        getAllField(item.getClass(), fieldList);
                        Optional<Field> optional = fieldList.stream().filter(f -> f.getAnnotation(TreeId.class) != null)
                                .findFirst();
                        if (optional.isPresent()) {
                            Field field = optional.get();
                            field.setAccessible(true);
                            map.put((Long) field.get(item), item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                map.forEach((key, instance) -> {
                    try {
                        List<Field> fieldList = new ArrayList<>();
                        getAllField(instance.getClass(), fieldList);
                        Optional<Field> optional = fieldList.stream()
                                .filter(f -> f.getAnnotation(ParentId.class) != null).findFirst();
                        if (optional.isPresent()) {
                            Field field = optional.get();
                            field.setAccessible(true);
                            Long parentId = (Long) field.get(instance);
                            if (parentId.longValue() != rootParentId) {
                                T parent = map.get(parentId);
                                if (null != parent) {
                                    List<Field> parentFieldList = new ArrayList<>();
                                    getAllField(parent.getClass(), parentFieldList);
                                    Optional<Field> parentOptional = parentFieldList.stream()
                                            .filter(f -> f.getAnnotation(Children.class) != null).findFirst();
                                    if (parentOptional.isPresent()) {
                                        Field parentField = parentOptional.get();
                                        parentField.setAccessible(true);
                                        parentField.get(parent).getClass().getMethod("add", Object.class)
                                                .invoke(parentField.get(parent), instance);
                                    }
                                }
                            } else {
                                treeList.add(instance);
                            }
                        } else {
                            treeList.add(instance);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return treeList;
    }

    /**
     * 转换成树，rootParentId为0
     * 
     * @param data
     * @param clazz
     * @param listener
     * @return
     */
    public static final <T> List<T> convert2Tree(List<T> data, Class<T> clazz, TreeListener listener) {
        return convert2Tree(data, clazz, 0, listener);
    }

    /**
     * 将List转换成Tree
     * 
     * @param clazz
     * @return
     */
    public static final <T> List<T> convert2Tree1(List<T> data, Class<T> clazz, int rootParentId,
            TreeListener listener) {
        List<T> treeList = new ArrayList<>();
        if (null != data && data.size() > 0) {
            try {
                Map<Integer, T> map = new LinkedHashMap<>();
                data.stream().forEach(item -> {
                    // if(null != listener){
                    // listener.each(item, i);
                    // }
                    try {
                        List<Field> fieldList = new ArrayList<>();
                        getAllField(item.getClass(), fieldList);
                        Optional<Field> optional = fieldList.stream().filter(f -> f.getAnnotation(TreeId.class) != null)
                                .findFirst();
                        if (optional.isPresent()) {
                            Field field = optional.get();
                            field.setAccessible(true);
                            map.put((Integer) field.get(item), item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                map.forEach((key, instance) -> {
                    try {
                        List<Field> fieldList = new ArrayList<>();
                        getAllField(instance.getClass(), fieldList);
                        Optional<Field> optional = fieldList.stream()
                                .filter(f -> f.getAnnotation(ParentId.class) != null).findFirst();
                        if (optional.isPresent()) {
                            Field field = optional.get();
                            field.setAccessible(true);
                            Integer parentId = (Integer) field.get(instance);
                            if (parentId.intValue() != rootParentId) {
                                T parent = map.get(parentId);
                                List<Field> parentFieldList = new ArrayList<>();
                                getAllField(parent.getClass(), parentFieldList);
                                Optional<Field> parentOptional = parentFieldList.stream()
                                        .filter(f -> f.getAnnotation(Children.class) != null).findFirst();
                                if (parentOptional.isPresent()) {
                                    Field parentField = parentOptional.get();
                                    parentField.setAccessible(true);
                                    parentField.get(parent).getClass().getMethod("add", Object.class)
                                            .invoke(parentField.get(parent), instance);
                                }
                            } else {
                                treeList.add(instance);
                            }
                        } else {
                            treeList.add(instance);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return treeList;
    }

    /**
     * 转换成树，rootParentId为0
     * 
     * @param data
     * @param clazz
     * @param listener
     * @return
     */
    public static final <T> List<T> convert2Tree1(List<T> data, Class<T> clazz, TreeListener listener) {
        return convert2Tree1(data, clazz, 0, listener);
    }
}
