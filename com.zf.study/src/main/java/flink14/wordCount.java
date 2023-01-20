package flink14;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 通过socket数据源，请求一个socket服务（10.19.36.136 9000）得到无界数据流
 * 统计数据流中的单词及其个数
 */
public class wordCount {

    public static void main(String[] args) throws Exception {


        //框架4步法：
        //1、创建编程入口环境env（固定）
        //批计算环境变量:ExecutionEnvironment，后期官a方统一流批一体使用StreamExecutionEnvironment
//        ExecutionEnvironment varEnv = ExecutionEnvironment.getExecutionEnvironment();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        /**
         * 本地运行时程序默认并行度取决于cpu逻辑核数
         * 可以通过setParallelism修改并行度，也可以通过提交程序是参数指定并行度
         */
        env.setParallelism(1);

        //2、通过source算子，映射数据源元为一个dataStream（数据流）
        //练习是用socket流映射一个dataStream
        //[hadoop@node36136 ~]$ nc -lk 9000
        DataStreamSource<String> source = env.socketTextStream("10.19.36.136", 9000);


        //3、通过算子对数据流进行计算逻辑
        //切单词、按单词分组、统计单词，Tuple-元组，flink为了跟scala统一，封装了元组类型Tuple，实际上就是多元数组，从Tuple0-25
        SingleOutputStreamOperator <Tuple2<String, Integer>> words = source.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {

                //切单词
                String[] split = s.split("\\s+");
                for (String word : split) {
                    //返回每一对（单词，1）
                    collector.collect(Tuple2.of(word, 1));
                }
            }
        });

        //分组
        KeyedStream<Tuple2<String, Integer>, String> keyed = words.keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
            @Override
            public String getKey(Tuple2<String, Integer> tuple2) throws Exception {
                //返回tuplew2按谁分组，此刻按入参的第一个String，单词分组
                return tuple2.f0;
            }
        });

        //聚合统计 keyed.sum(1);也可以，是tuple2的角标1
        SingleOutputStreamOperator<Tuple2<String, Integer>> resultStream = keyed.sum("f1");

        //4、通过sink算子将结果输出
        //输出屏幕
        DataStreamSink<Tuple2<String, Integer>> wordPrint = resultStream.print();

        //5、触发程序提交运行
        env.execute();

    }
}
