package me.jptx.mp.model.test;

import java.util.concurrent.TimeUnit;
import me.jptx.mp.model.test.entity.H2StudentWithLog;
import me.jptx.mp.model.test.entity.H2StudentWithoutLog;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Measurement(iterations = 10, time = 1)
@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
public class TestMain {


  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(TestMain.class.getSimpleName())
        .build();
    new Runner(opt).run();
  }

  /**
   * 测试 - 创建带有log的对象
   */
  @Benchmark
  public H2StudentWithLog testCreateModelWithLog() {
    return new H2StudentWithLog();
  }

  /**
   * 测试 - 创建不带log的对象
   */
  @Benchmark
  public H2StudentWithoutLog testCreateModelWithoutLog() {
    return new H2StudentWithoutLog();
  }


}
