package com.zk.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.Timer;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * In addition to JMX and HTTP, Metrics also has reporters for the following outputs:
 * <p>
 * STDOUT, using ConsoleReporter from metrics-core
 * CSV files, using CsvReporter from metrics-core
 * SLF4J loggers, using Slf4jReporter from metrics-core
 * Graphite, using GraphiteReporter from metrics-graphite
 */
public class TestTimers {

    private static final MetricRegistry metrics = new MetricRegistry();

    private static final Slf4jReporter slf4jReporter = Slf4jReporter.forRegistry(metrics)//
            .outputTo(LoggerFactory.getLogger("metrics_zkLog"))//
            .withLoggingLevel(Slf4jReporter.LoggingLevel.INFO)//
            .convertRatesTo(TimeUnit.MINUTES)// 表示【指定的单位时间】调用的次数，如mean_rate
//      .convertDurationsTo(TimeUnit.MILLISECONDS)//
            .build();
    //  private static final Timer timer = metrics.timer(name(TestTimers.class, "timer"));
    private static final Timer timer = metrics.timer(name("timer_name"));


    public static void handleRequest(int i) throws InterruptedException {
        final Timer.Context context = timer.time();
        try {
            // mock do something ;
            Thread.sleep(1000 + (i * 100));
        } finally {
            context.stop();
        }
    }

    @Test
    public void test() throws InterruptedException {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
        reporter.start(2, TimeUnit.SECONDS);
        Thread.sleep(10);
        slf4jReporter.start(2, TimeUnit.SECONDS);
        for (int i = 0; i < 20; i++) {
            handleRequest(i);
        }
    }

    @Test
    public void testSlf4jReport() throws InterruptedException {
        slf4jReporter.start(2, TimeUnit.SECONDS);
        for (int i = 0; i < 20; i++) {
            handleRequest(i);
        }
    }

}
