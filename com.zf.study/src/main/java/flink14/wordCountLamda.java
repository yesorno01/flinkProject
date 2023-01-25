package flink14;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class wordCountLamda {

    public static void main(String[] args) {
        //1，创建编程入口，又叫执行环境
        //流式处理环境
        StreamExecutionEnvironment envStream = StreamExecutionEnvironment.getExecutionEnvironment();

        //批计算处理环境
        ExecutionEnvironment envBatch = ExecutionEnvironment.getExecutionEnvironment();


    }
}
