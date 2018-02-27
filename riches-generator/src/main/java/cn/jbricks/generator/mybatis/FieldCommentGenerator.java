package cn.jbricks.generator.mybatis;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;

import java.util.Date;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * mybatis generator 自定义comment生成器. 基于MBG 1.3.2.
 * 
 * @author haoting.wang 2017-01-03
 */
public class FieldCommentGenerator implements CommentGenerator {

    private Properties properties;
    private boolean    suppressDate;
    private boolean    suppressAllComments;

    public FieldCommentGenerator(){
        super();
        properties = new Properties();
        suppressDate = false;
        suppressAllComments = false;
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        String comment = introspectedColumn.getRemarks();
        StringBuilder sb = new StringBuilder();

        if (comment != null && comment.trim().length() > 0) {
            comment = comment.replaceAll("\"", "'");
            field.addJavaDocLine("//" + comment); //$NON-NLS-1$

        }
    }

    public void addJavaFileComment(CompilationUnit compilationUnit) {
        // add no file level comments by default
        return;
    }

    /**
     * Adds a suitable comment to warn users that the element was generated, and when it was generated.
     */
    public void addComment(XmlElement xmlElement) {

    }

    public void addRootComment(XmlElement rootElement) {
        // add no document level comments by default
        return;
    }

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);

        suppressDate = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));

        suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
    }

    /**
     * This method adds the custom javadoc tag for. You may do nothing if you do not wish to include the Javadoc tag -
     * however, if you do not include the Javadoc tag then the Java merge capability of the eclipse plugin will break.
     *
     * @param javaElement the java element
     */
    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {

    }

    /**
     * This method returns a formated date string to include in the Javadoc tag and XML comments. You may return null if
     * you do not want the date in these documentation elements.
     *
     * @return a string representing the current timestamp, or null
     */
    protected String getDateString() {
        if (suppressDate) {
            return null;
        } else {
            return new Date().toString();
        }
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {

    }

    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {

    }

    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {

    }

    public void addGetterComment(Method method, IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {

    }

    public void addSetterComment(Method method, IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {

    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {

    }
}
