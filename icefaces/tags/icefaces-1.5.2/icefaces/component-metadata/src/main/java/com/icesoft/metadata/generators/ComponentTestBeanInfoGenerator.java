package com.icesoft.metadata.generators;

import com.sun.rave.jsfmeta.beans.*;
import java.io.*;
import java.util.*;

public class ComponentTestBeanInfoGenerator extends AbstractGenerator {

	protected static final String BUNDLE_VARIABLE_NAME = "resources";

	protected static final String BUNDLE_GET_MESSAGE_START = "resources.getString(";

	protected static final String BUNDLE_GET_MESSAGE_END = ")";

	private boolean base;

	private String baseBI;

	private String categoryDescriptors;

	private String defaultMarkupSection;

	protected HashMap bundleMaps;

	private String defaultTaglibPrefix;

	private String defaultTaglibURI;

	private String implBD;

	private String implPD;

	protected boolean useComponentResourceBundles;

	public ComponentTestBeanInfoGenerator() {

		base = false;
		baseBI = null;
		categoryDescriptors = "com.icesoft.faces.ide.creator2.util.CategoryDescriptors";
		defaultMarkupSection = "FORM";
		bundleMaps = new HashMap();
		defaultTaglibPrefix = null;
		defaultTaglibURI = null;
		implBD = null;
		implPD = null;
		useComponentResourceBundles = true;
	}

	public boolean getBase() {
		return base;
	}

	public void setBase(boolean base) {
		this.base = base;
	}

	public String getBaseBI() {
		return baseBI;
	}

	public void setBaseBI(String baseBI) {
		this.baseBI = baseBI;
	}

	public String getCategoryDescriptors() {
		return categoryDescriptors;
	}

	public void setCategoryDescriptors(String categoryDescriptors) {
		this.categoryDescriptors = categoryDescriptors;
	}

	public String getDefaultMarkupSection() {
		return defaultMarkupSection;
	}

	public void setDefaultMarkupSection(String defaultMarkupSection) {
		this.defaultMarkupSection = defaultMarkupSection;
	}

	protected HashMap getBundleMap(String packageName, String languageCode) {
		HashMap packageMap = (HashMap) bundleMaps.get(packageName);
		if (packageMap == null) {
			packageMap = new HashMap();
			bundleMaps.put(packageName, packageMap);
		}
		HashMap map = (HashMap) packageMap.get(languageCode);
		if (map == null) {
			map = new HashMap();
			packageMap.put(languageCode, map);
		}
		return map;
	}

	protected HashMap getBundleMaps() {
		return bundleMaps;
	}

	protected void putBundleMap(String packageName, String languageCode,
			String key, String value) {
		HashMap map = getBundleMap(packageName, languageCode);
		map.put(key, value);
	}

	public String getDefaultTaglibPrefix() {
		return defaultTaglibPrefix;
	}

	public void setDefaultTaglibPrefix(String defaultTaglibPrefix) {
		this.defaultTaglibPrefix = defaultTaglibPrefix;
	}

	public String getDefaultTaglibURI() {
		return defaultTaglibURI;
	}

	public void setDefaultTaglibURI(String defaultTaglibURI) {
		this.defaultTaglibURI = defaultTaglibURI;
	}

	public String getImplBD() {
		return implBD;
	}

	public void setImplBD(String implBD) {
		this.implBD = implBD;
	}

	public String getImplPD() {
		return implPD;
	}

	public void setImplPD(String implPD) {
		this.implPD = implPD;
	}

	public boolean getUseComponentResourceBundles() {
		return useComponentResourceBundles;
	}

	public void setUseComponentResourceBundles(boolean flag) {
		useComponentResourceBundles = flag;
	}

	public void generate() throws IOException {
		ComponentBean cbs[] = getConfig().getComponents();
		for (int i = 0; i < cbs.length; i++)
			if (generated(cbs[i].getComponentClass())) {
				generate(cbs[i]);
			}
	}

	private void beanDescriptor(ComponentBean cb, RendererBean rb)
			throws IOException {
		String implBD = this.implBD;
		int period = implBD.lastIndexOf(".");
		if (period >= 0)
			implBD = implBD.substring(period + 1);

		JavaSourceWriter writer = getWriter();

		writer.startJavaDoc();
		writer
				.emitJavaDoc("<p>Return the <code>BeanDescriptor</code> for this bean.</p>");
		writer.endJavaDoc();
		writer.startMethod("getBeanDescriptor", "BeanDescriptor", null, null);
		writer.emitNewline();
		writer.emitExpression("if (beanDescriptor != null) {", true);
		writer.indent();
		writer.emitExpression("return beanDescriptor;", true);
		writer.outdent();
		writer.emitExpression("}", true);
		writer.emitNewline();
		writer.emitExpression(
				"beanDescriptor = new " + implBD + "(beanClass);", true);
		writer.emitNewline();
		writer.emitExpression("return beanDescriptor;", true);
		writer.emitNewline();
		writer.endMethod();
		writer.emitNewline();
	}

	private void body(ComponentBean cb, RendererBean rb) throws IOException {
		String baseBI = this.baseBI;
		int period = baseBI.lastIndexOf(".");
		if (period >= 0)
			baseBI = baseBI.substring(period + 1);
		JavaSourceWriter writer = getWriter();
		String simple = simpleClassName(cb.getComponentClass());
		simple = simple + "BeanInfo";
		writer.startClass(simple, baseBI, null, getBase(), !getBase());
		writer.emitNewline();

		constructor(cb, rb);
		instance(cb, rb);
		beanDescriptor(cb, rb);
		defaultPropertyIndex(cb, rb);
		propertyDescriptors(cb, rb);
		writer.endClass();
	}

	private void constructor(ComponentBean cb, RendererBean rb)
			throws IOException {
		JavaSourceWriter writer = getWriter();
		String simple = simpleClassName(cb.getComponentClass());
		writer.startJavaDoc();
		writer.emitJavaDoc("<p>Construct a new <code>" + simpleClassName(cb)
				+ "</code>.</p>");
		writer.endJavaDoc();
		writer.startMethod(simpleClassName(cb), null, null, null);
		writer.emitNewline();
		writer.emitExpression("beanClass = " + simple + ".class;", true);
		writer.emitNewline();
		writer.endMethod();
		writer.emitNewline();
	}

	private void defaultPropertyIndex(ComponentBean cb, RendererBean rb)
			throws IOException {
		JavaSourceWriter writer = getWriter();
		writer.startJavaDoc();
		writer.emitJavaDoc("<p>Return the index of the default property, or");
		writer.emitJavaDoc("-1 if there is no default property.</p>");
		writer.endJavaDoc();
		writer.startMethod("getDefaultPropertyIndex", "int", null, null);
		writer.emitNewline();
		writer.indent();
		writer.emitExpression("defaultPropertyIndex = -1;", true);
		writer.emitExpression("return defaultPropertyIndex;", true);
		writer.endMethod();
		writer.emitNewline();
		return;

	}

	private void generate(ComponentBean cb) throws IOException {
		if ("javax.faces.component.UIComponentBase".equals(cb
				.getComponentClass()))
			return;
		if (cb.isSuppressed())
			return;
		RendererBean rb = renderer(cb);
		if (rb == null) {
			rb = new RendererBean();
		}
		File outputFile = outputFile(cb.getComponentClass() + "BeanInfo");

		outputFile.mkdirs();
		outputFile.delete();
		try {
			getWriter().setOutputWriter(
					new BufferedWriter(new FileWriter(outputFile)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		header(cb, rb);
		body(cb, rb);
		getWriter().flush();
		getWriter().close();
	}

	private void header(ComponentBean cb, RendererBean rb) throws IOException {
		JavaSourceWriter writer = getWriter();
		String packageName = packageName(cb);
		if (packageName.length() > 0) {
			writer.emitPackage(packageName);
			writer.emitNewline();
		}
		writer.emitImport("java.awt.Image");
		writer.emitImport("java.beans.BeanDescriptor");
		writer.emitImport("java.beans.BeanInfo");
		writer.emitImport("java.beans.IntrospectionException");
		writer.emitImport("java.beans.PropertyDescriptor");
		writer.emitImport("java.util.Locale");
		writer.emitImport("java.util.ResourceBundle");
		writer.emitNewline();
		writer.emitNewline();
		writer.emitImport(baseBI);
		if (!"java.beans.BeanDescriptor".equals(implBD)) {
			writer.emitImport(implBD);
		}
		if (!"java.beans.PropertyDescriptor".equals(implPD)) {
			writer.emitImport(implPD);
		}
		writer.emitNewline();
		writer.startJavaDoc();
		writer.emitJavaDoc("<p>Auto-generated design time metadata class.");
		writer.emitJavaDoc("Do <strong>NOT</strong> modify; all changes");
		writer.emitJavaDoc("<strong>will</strong> be lost!</p>");
		writer.endJavaDoc();
		writer.emitNewline();
	}

	private void instance(ComponentBean cb, RendererBean rb) throws IOException {

		JavaSourceWriter writer = getWriter();
		writer.startJavaDoc();
		writer.emitJavaDoc("<p>The bean class that this BeanInfo represents.");
		writer.endJavaDoc();
		writer.emitExpression("protected Class beanClass;", true);
		writer.emitNewline();
		writer.startJavaDoc();
		writer.emitJavaDoc("<p>The cached BeanDescriptor.</p>");
		writer.endJavaDoc();
		writer.emitExpression("protected BeanDescriptor beanDescriptor;", true);
		writer.emitNewline();
		writer.startJavaDoc();
		writer.emitJavaDoc("<p>The index of the default property.</p>");
		writer.endJavaDoc();
		writer.emitExpression("protected int defaultPropertyIndex = -1;", true);
		writer.emitNewline();
		writer.startJavaDoc();
		writer.emitJavaDoc("<p>The name of the default property.</p>");
		writer.endJavaDoc();
		writer.emitExpression("protected String defaultPropertyName;", true);
		writer.emitNewline();
		writer.startJavaDoc();
		writer.emitJavaDoc("<p>The cached property descriptors.</p>");
		writer.endJavaDoc();
		writer.emitExpression(
				"protected PropertyDescriptor[] propDescriptors;", true);
		writer.emitNewline();
		return;

	}

	private String packageName(ComponentBean cb) {
		String componentClass = cb.getComponentClass();
		int last = componentClass.lastIndexOf('.');
		if (last >= 0)
			return componentClass.substring(0, last);
		else
			return "";
	}

	private void propertyDescriptor(ComponentBean cb, RendererBean rb,
			PropertyBean pb, ComponentBean componentBeanBeingGenerated)
			throws IOException {
		String implPD = this.implPD;
		int period = implPD.lastIndexOf(".");
		if (period >= 0)
			implPD = implPD.substring(period + 1);
		JavaSourceWriter writer = getWriter();
		DescriptionBean description = pb.getShortDescription("");
		if (description == null)
			description = pb.getDescription("");
		DescriptionBean descriptions[] = pb.getShortDescriptions();
		if (descriptions == null)
			descriptions = pb.getDescriptions();
		String name = pb.getPropertyName();
		String readMethod = pb.getReadMethod();
		if (pb.isWriteOnly())
			readMethod = null;
		else if (readMethod == null)
			if ("boolean".equals(pb.getPropertyClass()))
				readMethod = "is" + Character.toUpperCase(name.charAt(0))
						+ name.substring(1);
			else
				readMethod = "get" + Character.toUpperCase(name.charAt(0))
						+ name.substring(1);
		String writeMethod = pb.getWriteMethod();
		if (pb.isReadOnly())
			writeMethod = null;
		else if (writeMethod == null)
			writeMethod = "set" + Character.toUpperCase(name.charAt(0))
					+ name.substring(1);
		StringBuffer psb = new StringBuffer("PropertyDescriptor prop_");
		psb.append(name);
		psb.append(" = new ");
		psb.append(implPD);
		psb.append("(");
		psb.append(JavaSourceWriter.toJavaString(name));
		psb.append(",beanClass,");
		psb.append(readMethod != null ? JavaSourceWriter
				.toJavaString(readMethod) : "null");
		psb.append(",");
		psb.append(writeMethod != null ? JavaSourceWriter
				.toJavaString(writeMethod) : "null");
		psb.append(");");
		writer.emitExpression(psb.toString(), true);
		writer.emitNewline();
	}

	private void propertyDescriptors(ComponentBean cb, RendererBean rb)
			throws IOException {
		JavaSourceWriter writer = getWriter();
		TreeMap map = new TreeMap();
		writer.startJavaDoc();
		writer
				.emitJavaDoc("<p>Return the <code>PropertyDescriptor</code>s for this bean.</p>");
		writer.endJavaDoc();
		writer.startMethod("getPropertyDescriptors", "PropertyDescriptor[]",
				null, null);
		writer.emitNewline();
		writer.emitExpression("if (propDescriptors != null) {", true);
		writer.indent();
		writer.emitExpression("return propDescriptors;", true);
		writer.outdent();
		writer.emitExpression("}", true);
		writer.emitNewline();
		writer.emitExpression("try {", true);
		writer.emitNewline();
		writer.indent();
		propertyDescriptors(cb, rb, ((Map) (map)), cb);
		writer.emitExpression("propDescriptors = new PropertyDescriptor[] {",
				true);
		writer.indent();
		String prop;
		for (Iterator props = map.keySet().iterator(); props.hasNext(); writer
				.emitExpression("prop_" + prop + ",", true))
			prop = (String) props.next();

		writer.outdent();
		writer.emitExpression("};", true);

		writer.emitExpression("return propDescriptors;", true);
		writer.emitNewline();
		writer.outdent();
		writer.emitExpression("} catch (IntrospectionException e) {", true);
		writer.indent();
		writer.emitExpression("e.printStackTrace();", true);
		writer.emitExpression("return null;", true);
		writer.outdent();
		writer.emitExpression("}", true);
		writer.emitNewline();
		writer.endMethod();
		writer.emitNewline();
	}

	private void propertyDescriptors(ComponentBean cb, RendererBean rb,
			Map map, ComponentBean componentBeanBeingGenerated)
			throws IOException {
		PropertyBean pbs[] = cb.getProperties();
		for (int i = 0; i < pbs.length; i++) {
			if (map.containsKey(pbs[i].getPropertyName())
					|| "valid".equals(pbs[i].getPropertyName()))
				continue;
			PropertyBean pb = merge(pbs[i], rb.getAttribute(pbs[i]
					.getPropertyName()));
			if (!pb.isSuppressed()) {
				propertyDescriptor(cb, rb, pb, componentBeanBeingGenerated);
				map.put(pb.getPropertyName(), null);
			}
		}

		String baseComponentType = cb.getBaseComponentType();
		if (baseComponentType != null) {
			ComponentBean bcb = getConfig().getComponent(baseComponentType);
			propertyDescriptors(bcb, rb, map, componentBeanBeingGenerated);
		}
	}

	private String simpleClassName(ComponentBean cb) {
		return simpleClassName(cb.getComponentClass()) + "BeanInfo";
	}

	protected String trimWhitespace(String string) {
		StringBuffer buffer = new StringBuffer(string.length());
		boolean eatingWhite = true;
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (eatingWhite) {
				if (Character.isWhitespace(c))
					continue;
				eatingWhite = false;
			}
			if (Character.isWhitespace(c)) {
				buffer.append(" ");
				eatingWhite = true;
			} else {
				buffer.append(c);
			}
		}

		return buffer.toString();
	}

}
