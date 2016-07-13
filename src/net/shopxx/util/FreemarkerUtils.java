package net.shopxx.util;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.utility.DeepUnwrap;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.shopxx.CommonAttributes;
import net.shopxx.EnumConverter;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

public final class FreemarkerUtils {

	private static final ConvertUtilsBean IIIllIlI;

	private FreemarkerUtils() {

	}

	static {
		IIIllIlI = new FreemarkerUtils._cls1();
		DateConverter dateconverter = new DateConverter();
		dateconverter.setPatterns(CommonAttributes.DATE_PATTERNS);
		IIIllIlI.register(dateconverter, Date.class);
	}

	public static String process(String template, Map<String, ?> model) {
		Configuration localConfiguration = null;
		ApplicationContext localApplicationContext = SpringUtils
				.getApplicationContext();
		if (localApplicationContext != null) {
			FreeMarkerConfigurer localFreeMarkerConfigurer = (FreeMarkerConfigurer) SpringUtils
					.getBean("freeMarkerConfigurer", FreeMarkerConfigurer.class);
			if (localFreeMarkerConfigurer != null) {
				localConfiguration = localFreeMarkerConfigurer
						.getConfiguration();
			}
		}
		return process(template, model, localConfiguration);
	}

	public static String process(String template, Map<String, ?> model,
			Configuration configuration) {
		if (template == null) {
			return null;
		}
		if (configuration == null) {
			configuration = new Configuration();
		}
		StringWriter localStringWriter = new StringWriter();
		try {
			new Template("template", new StringReader(template), configuration)
					.process(model, localStringWriter);
		} catch (TemplateException localTemplateException) {
			localTemplateException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return localStringWriter.toString();
	}

	public static <T> T getParameter(String name, Class<T> type,
			Map<String, TemplateModel> params) {
		Assert.hasText(name);
		Assert.notNull(type);
		Assert.notNull(params);
		Object localObject = null;
		try {
			TemplateModel localTemplateModel = (TemplateModel) params.get(name);
			if (localTemplateModel == null) {
				return null;
			}
			localObject = DeepUnwrap.unwrap(localTemplateModel);

		} catch (Exception e) {

		}
		return (T) IIIllIlI.convert(localObject, type);
	}

	public static TemplateModel getVariable(String name, Environment env) {
		Assert.hasText(name);
		Assert.notNull(env);
		try {
			return env.getVariable(name);
		} catch (Exception e) {

		}
		return null;
	}

	public static void setVariable(String name, Object value, Environment env) {
		Assert.hasText(name);
		Assert.notNull(env);
		try {
			if ((value instanceof TemplateModel)) {
				env.setVariable(name, (TemplateModel) value);
			} else {
				env.setVariable(name, ObjectWrapper.BEANS_WRAPPER.wrap(value));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void setVariables(Map<String, Object> variables,
			Environment env) {
		Assert.notNull(variables);
		Assert.notNull(env);
		Iterator iterator = variables.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String str = (String) entry.getKey();
			Object obj = entry.getValue();
			if ((obj instanceof TemplateModel)) {
				env.setVariable(str, (TemplateModel) obj);
			} else {
				// 临时处理
				try {
					env.setVariable(str, ObjectWrapper.BEANS_WRAPPER.wrap(obj));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}

	public static class _cls1 extends ConvertUtilsBean {

		_cls1() {

		}

		public String convert(Object value) {
			if (value != null) {

				Class class1 = value.getClass();

				if ((class1.isEnum()) && (super.lookup(class1) == null)) {
					super.register(new EnumConverter(class1), class1);

				} else if ((class1.isArray())
						&& (class1.getComponentType().isEnum())) {

					if (super.lookup(class1) == null) {

						ArrayConverter arrayconverter = new ArrayConverter(
								class1, new EnumConverter(class1
										.getComponentType()), 0);
						((ArrayConverter) arrayconverter)
								.setOnlyFirstToString(false);
						super.register((Converter) arrayconverter, class1);

					}

					Converter converter = super.lookup(class1);
					return (String) converter.convert(String.class, value);
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