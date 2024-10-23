package is.lab1.is_lab1.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

// @Configuration
// @EnableAsync
// public class AsyncConfig implements AsyncConfigurer {

//     @Override
//     @Bean(name = "taskExecutor")
//     public Executor getAsyncExecutor() {
//         ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//         executor.setCorePoolSize(10);
//         executor.setMaxPoolSize(100);
//         executor.setQueueCapacity(500);
//         executor.setThreadNamePrefix("Sse-Executor-");
//         executor.initialize();
//         return executor;
//     }
// }
