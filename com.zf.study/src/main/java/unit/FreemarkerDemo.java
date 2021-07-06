package unit;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class FreemarkerDemo {
    //搭建freemarker
    public static void main(String[] args) throws IOException, TemplateException {
        //配置对象  ftl freemarker template 的简写，demo.ftl建议，但是demo.xml,demo.html等等都行
        Configuration conf=new Configuration();
        //模板路径
        String dir="F:\\developSource\\eclipse\\workSpaceX86_64\\freemarker\\ftl\\";//freemarker.html
        //导入模板目录
        conf.setDirectoryForTemplateLoading(new File(dir));
        //获取模板
        Template template = conf.getTemplate("freemarker.html");

        //数据
        Map root=new HashMap();
        root.put("world", "世界你好");

        //对象数据
//        Student student=new Student(1,"小米");
//        root.put("student", student);
        //list 集合数据
        List<String> persons=new ArrayList<String>();
        persons.add("小米1");
        persons.add("小米2");
        persons.add("小米3");
        root.put("persons", persons);

        //map 集合数据
        Map mx=new HashMap();
        mx.put("小米1", "小米1");
        mx.put("小米2", "小米2");
        mx.put("xm3", "小米3");
        root.put("mx", mx);

        //list<map>数据
        List<Map> listMap=new ArrayList<Map>();
        Map mx1=new HashMap();
        Map mx2=new HashMap();
        mx1.put("小米1", "小米1");
        mx1.put("小米2", "小米2");
        mx2.put("xm3", "小米3");
        listMap.add(mx1);
        listMap.add(mx2);
        root.put("listMap", listMap);

        //时间格式
        root.put("curTime", new Date());
        //null格式
        root.put("testNull", null);


        //输出
        Writer out=new FileWriter(new File(dir+"hello.html"));

        //生成开始
        template.process(root, out);
        //关闭流
        out.flush();
        out.close();


        System.out.println("生成了");


    }
}