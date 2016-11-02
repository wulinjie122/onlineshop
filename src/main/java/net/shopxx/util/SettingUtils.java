package net.shopxx.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.shopxx.CommonAttributes;
import net.shopxx.EnumConverter;
import net.shopxx.Setting;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.io.IOUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.ClassPathResource;

public final class SettingUtils {
	private static final CacheManager CACHEMANAGER = CacheManager.create();;
	private static final BeanUtilsBean BEANUTILS;

	static {
		//SettingUtils sLocal = new SettingUtils();
		_cls1 _lcls1 = new _cls1();
		DateConverter localDateConverter = new DateConverter();
		localDateConverter.setPatterns(CommonAttributes.DATE_PATTERNS);
		_lcls1.register(localDateConverter, Date.class);
		BEANUTILS = new BeanUtilsBean(_lcls1);
	}
	
	private SettingUtils(){

    }

	public static Setting get() {
		Ehcache ehcache = CACHEMANAGER.getEhcache("setting");
		net.sf.ehcache.Element element = ehcache.get(Setting.CACHE_KEY);
		Setting localSetting;
		if (element != null) {
			localSetting = (Setting) element.getObjectValue();
		} else {
			localSetting = new Setting();
			try {
				File localFile = new ClassPathResource("/shopxx.xml").getFile();
				Document document = new SAXReader().read(localFile);
				List list = document.selectNodes("/shopxx/setting");
				Iterator iterator = list.iterator();
				while (iterator.hasNext()) {
					org.dom4j.Element element1 = (org.dom4j.Element) iterator.next();
					String str1 = element1.attributeValue("name");
					String str2 = element1.attributeValue("value");
					try {
						BEANUTILS.setProperty(localSetting, str1, str2);
					} catch (IllegalAccessException localIllegalAccessException) {
						localIllegalAccessException.printStackTrace();
					} catch (InvocationTargetException localInvocationTargetException) {
						localInvocationTargetException.printStackTrace();
					}
				}
			} catch (Exception localException) {
				localException.printStackTrace();
			}
			ehcache.put(new net.sf.ehcache.Element(Setting.CACHE_KEY, localSetting));
		}
		return localSetting;
	}

	public static void set(Setting setting) {
		try {
			//读取shopxx.xml文件
			File localFile = new ClassPathResource("/shopxx.xml").getFile();
			Document localDocument = new SAXReader().read(localFile);
			List localList = localDocument.selectNodes("/shopxx/setting");
			Iterator  iterator= localList.iterator();
			while(iterator.hasNext()) {
				Element element = (Element)iterator.next();
				try {
					String str1 = element.attributeValue("name");
					String str2 = BEANUTILS.getProperty(setting, str1);
					Attribute attribute = element.attribute("value");
					attribute.setValue(str2);
				} catch (IllegalAccessException localIllegalAccessException1) {
					localIllegalAccessException1.printStackTrace();
					
				} catch (InvocationTargetException localInvocationTargetException1) {
					localInvocationTargetException1.printStackTrace();
					
				} catch (NoSuchMethodException localNoSuchMethodException1) {
					localNoSuchMethodException1.printStackTrace();
					
				}
			}
			FileOutputStream fileoutputstream = null;
			XMLWriter xmlwriter = null;
			try {
				OutputFormat localOutputFormat = OutputFormat.createPrettyPrint();
				localOutputFormat.setEncoding("UTF-8");
				localOutputFormat.setIndent(true);
				localOutputFormat.setIndent("\t");
				localOutputFormat.setNewlines(true);
				fileoutputstream = new FileOutputStream(localFile);
				xmlwriter = new XMLWriter(fileoutputstream, localOutputFormat);
				xmlwriter.write(localDocument);
			} catch (Exception localException3) {
				localException3.printStackTrace();
				
			} finally {
				if ( xmlwriter != null) {
					try {
						xmlwriter.close();
					} catch (IOException localIOException4) {
						localIOException4.printStackTrace();
					}
				}
				IOUtils.closeQuietly(fileoutputstream);
			}
			Ehcache localEhcache = CACHEMANAGER.getEhcache("setting");
			localEhcache.put(new net.sf.ehcache.Element(Setting.CACHE_KEY,setting));
		} catch (Exception localException2) {
			localException2.printStackTrace();
		}
	}

	/**
	 * 内部类
	 * 
	 * @author Administrator
	 */
	private static class _cls1 extends ConvertUtilsBean {

		_cls1() {
			
		}

		public String convert(Object value) {
			if (value != null) {
				Class localClass = value.getClass();
				if ((localClass.isEnum()) && (super.lookup(localClass) == null)) {
					super.register(new EnumConverter(localClass), localClass);
				} else if ((localClass.isArray())
						&& (localClass.getComponentType().isEnum())) {
					if (super.lookup(localClass) == null) {
						ArrayConverter localObject = new ArrayConverter(
								localClass, new EnumConverter(localClass
										.getComponentType()), 0);
						((ArrayConverter) localObject)
								.setOnlyFirstToString(false);
						super.register((Converter) localObject, localClass);
					}
					Object localObject = super.lookup(localClass);
					return (String) ((Converter) localObject).convert(
							String.class, value);
				}
			}
			return super.convert(value);
		}

		public Object convert(String value, Class clazz) {
			if ((clazz.isEnum()) && (super.lookup(clazz) == null)) {
				super.register(new EnumConverter(clazz), clazz);
			}
			return super.convert(value, clazz);
		}

		public Object convert(String[] values, Class clazz) {
			if ((clazz.isArray()) && (clazz.getComponentType().isEnum())
					&& (super.lookup(clazz.getComponentType()) == null)) {
				super.register(new EnumConverter(clazz.getComponentType()),
						clazz.getComponentType());
			}
			return super.convert(values, clazz);
		}

		public Object convert(Object value, Class targetType) {
			if (super.lookup(targetType) == null) {
				if (targetType.isEnum()) {
					super.register(new EnumConverter(targetType), targetType);
				} else if ((targetType.isArray())
						&& (targetType.getComponentType().isEnum())) {
					ArrayConverter localArrayConverter = new ArrayConverter(
							targetType, new EnumConverter(targetType
									.getComponentType()), 0);
					localArrayConverter.setOnlyFirstToString(false);
					super.register(localArrayConverter, targetType);
				}
			}
			return super.convert(value, targetType);
		}
	}
}