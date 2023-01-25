package flink14;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;


public class wordCountBatch {

    public static void main(String[] args) throws Exception {

        //1，创建程序入口程序，环境变量
        //批处理入口环境
        ExecutionEnvironment envBatch = ExecutionEnvironment.getExecutionEnvironment();

        //2，读数据
        //去掉文件名【\word.txt】，则读取目录下所有文件
        //批计算得到的数据抽象是一个数据集DataSet
        DataSource<String> dataSource = envBatch.readTextFile("com.zf.study/src/main/data/wc/input");

        //除了实现匿名内部类，还可以在新文件里或者次文件里创建有名的内部，或者外部类，用new的方式实现类的调用
        //可以在括号里用Ctrl+p查看参数类型，如groupBy("f0")可以改成groupBy(0)，用角标方式传递参数
        //在dataSource上调用各种算子
        dataSource
                //分词
                .flatMap(new MyFlatMapFunction())
                //分组
                .groupBy(0)
                //求和
                .sum(1)
                //行动算子：打印
                .print();
    }

}

class MyFlatMapFunction implements FlatMapFunction<String, Tuple2<String , Integer>>{

    @Override
    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
        //按空格拆词
        String[] words = s.split("\\s+");
        for (String word : words) {
            //加入到集合中
            collector.collect(Tuple2.of(word,1));
        }
    }
}
