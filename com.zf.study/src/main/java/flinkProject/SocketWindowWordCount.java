package flinkProject;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import unit.GetIP;

/**
 * Author: Mr.Deng
 * Date: 2018/10/15
 * Desc: 使用flink对指定窗口内的数据进行实时统计，最终把结果打印出来
 *       先在192.168.176.11机器上执行nc -l 9000 用netcat监听9000端口
 */
public class SocketWindowWordCount {
    public static void main(String[] args) throws Exception {
        //定义socket的端口号
        int port;
        try{
            ParameterTool parameterTool = ParameterTool.fromArgs(args);
            port = parameterTool.getInt("port");
        }catch (Exception e){
            System.err.println("没有指定port参数，使用默认值9000");
            port = 9000;
        }
        //获取运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        String hostname = "";
        String hostinfo = "";
        hostname = "192.168.176.11";
        hostname = "node01";

        GetIP getIP = new GetIP();
        if (!"".equals(hostname)){
            hostinfo = getIP.getIPOrHostName(hostname);
            System.out.println("the host info : " + hostinfo);
        }

        //连接socket获取输入的数据
        DataStreamSource<String> text = env.socketTextStream(hostname, port, "\n");

        System.out.println("the input hostname = "+hostname+ " and port = "+port+" . the hostinfo : "+hostinfo);
        
        //计算数据
        //将用户输入的文本流以非空白符的方式拆开来，得到单个的单词，存入命名为out的Collector中
        DataStream<WordWithCount> windowCount = text.flatMap(new FlatMapFunction<String, WordWithCount>() {
            public void flatMap(String value, Collector<WordWithCount> out) throws Exception {
                System.out.println("socket.window.value = "+value);
                String[] splits = value.split("\\s");
                for (String word:splits) {
                    out.collect(new WordWithCount(word,1L));
                }
            }
        })//打平操作，把每行的单词转为<word,count>类型的c数据
                //针对相同的word数据进行分组
                //将输入的文本分为不相交的分区，每个分区包含的都是具有相同key的元素。也就是说，相同的单词被分在了同一个区域，下一步的reduce就是统计分区中的个数
                .keyBy("word")
                //指定计算数据的窗口大小和滑动窗口大小
                //滑动窗口机制，每1秒计算一次最近5秒
                .timeWindow(Time.seconds(2),Time.seconds(1))
                //timeWindow(Time.seconds(2))，只有一个参数，表示是翻滚时间窗口（Tumbling window），即不重叠的时间窗口，只统计本窗口内的数据
                //timeWindow(Time.seconds(5),Time.seconds(1))，有两个参数，表示是滑动时间窗口（Sliding window），即每过t2时间，统计前t1时间内的数据。 本例中就是，每秒计算前5秒内的数据
//                .sum("count");
                //一个在KeyedDataStream上“滚动”进行的reduce方法。将上一个reduce过的值和当前element结合，产生新的值并发送出。
                //此处是说，对输入的两个对象进行合并，统计该单词的数量和
                .reduce(new ReduceFunction<WordWithCount>() {
                    @Override
                    public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                        return new WordWithCount(a.word, a.count + b.count);
                    }
                });

        //把数据打印到控制台,使用一个并行度
        windowCount.print().setParallelism(1);
        //注意：因为flink是懒加载的，所以必须调用execute方法，上面的代码才会执行
        env.execute("streaming word count");
    }

    /**
     * 主要为了存储单词以及单词出现的次数
     */
    public static class WordWithCount{
        public String word;
        public long count;
        public WordWithCount(){}
        public WordWithCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return "WordWithCount{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }
    }

}
