package unit;

import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Freemarker测试类
 *
 * @author yzl
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class FreemarkerTest {
    public static void main(String[] args) throws IOException, TemplateException {
        Reader reader = new FileReader(new File("E:/test.ftl"));
        Template template = new Template("test", reader, null, "utf-8");

        Map<Object, Object> data = new HashMap<Object, Object>();
        data.put("userName", "hello world");
        data.put("list", Arrays.asList("entity1", "entity2"));
        Writer writer = new PrintWriter(System.out);

        template.process(data, writer);

        writer.flush();
        writer.close();
        reader.close();
    }
}