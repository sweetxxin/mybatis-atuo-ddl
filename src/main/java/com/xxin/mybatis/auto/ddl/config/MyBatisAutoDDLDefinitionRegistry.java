package com.xxin.mybatis.auto.ddl.config;


import com.baomidou.mybatisplus.annotation.TableName;
import com.xxin.mybatis.auto.ddl.annotation.EnableMyBatisAutoDDL;
import com.xxin.mybatis.auto.ddl.pojo.TableDefine;
import com.xxin.mybatis.auto.ddl.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.*;

/**
 * @description:
 * @author: chenyixin7
 * @create: 2020-07-30 13:48
 */

public class MyBatisAutoDDLDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, EnvironmentAware, ApplicationContextAware,Ordered {
    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private ApplicationContext applicationContext;
    private Environment environment;
    private MetadataReaderFactory metadataReaderFactory;
    private ResourcePatternResolver resourcePatternResolver;
    private Logger logger = LoggerFactory.getLogger(getClass());
    public static List<String> ddl = new ArrayList<>();
    public static String ddlStrategy = "none";
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        String property = getPackagesPath();
        Set<Class<?>> beanClazzs = new HashSet<>();
        if (StringUtils.isBlank(ddlStrategy)){
            logger.info("没有配置自动生成策略，默认不执行");
            return;
        }
        if (StringUtils.isNotBlank(property)) {
            logger.info("用户自定义扫描：{}", property);
            String[] packages = property.split(",");
            for (String aPackage : packages) {
                Set<Class<?>> classes = scannerPackages(aPackage);
                if (!classes.isEmpty()) {
                    beanClazzs.addAll(classes);
                }
            }
        } else {
            Map<String, Object> annotatedBeans = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);
            String name = annotatedBeans.values().toArray()[0].getClass().getName();
            String defaultPackage = name.substring(0,name.lastIndexOf("."));
            logger.info("默认扫描：{}", defaultPackage);
            beanClazzs = scannerPackages(defaultPackage);
        }

        List<TableDefine> tableDefines = Util.parseTableEntity(beanClazzs);
        List<String> tableDDL = Util.createTableDDL(tableDefines,ddlStrategy);
        logger.info("扫描生成表");
        ddl = tableDDL;
    }

    private String getPackagesPath(){
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EnableMyBatisAutoDDL.class);
        if (!beansWithAnnotation.isEmpty()){
            Object o = beansWithAnnotation.values().toArray()[0];
            EnableMyBatisAutoDDL annotation = o.getClass().getAnnotation(EnableMyBatisAutoDDL.class);
            ddlStrategy = annotation.ddl();
            if (StringUtils.isNotBlank(annotation.basePackages())){
                return annotation.basePackages();
            }
        }
        ddlStrategy = environment.getProperty("mybatis-plus.table.ddl.auto.strategy");
        return environment.getProperty("mybatis-plus.table.ddl.auto.scan-package");
    }
    private Set<Class<?>> scannerPackages(String basePackage) {
        Set<Class<?>> set = new LinkedHashSet<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(basePackage) + '/' + DEFAULT_RESOURCE_PATTERN;
        try {
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent(TableName.class)) {
                            set.add(clazz);
                        }
                    } catch (Exception e) {
                        logger.warn("反射到错误的类 {}  - msg : {}", className, e.getMessage());
                        logger.error("反射出错：{}", e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            logger.error("反射出错：" + e.getMessage());
        }
        return set;
    }

    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
