package com.goodlisteners.performance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = com.goodlisteners.main.App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PerformanceTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String createReviewUrl() {
        return "http://localhost:" + port + "/api/review";
    }

    @BeforeEach
    public void setUp() {
        cleanupTestReviews();
    }

    @AfterEach
    public void tearDown() {
        // Remove as reviews criadas durante os testes
        cleanupTestReviews();
    }

    private void cleanupTestReviews() {
        jdbcTemplate.update("DELETE FROM Reviews WHERE user_id = 1 AND album_id = 1");
    }

    @Test
    public void testReviewEndpointPerformance() {
        int numRequests = 100;
        List<Long> responseTimes = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("userId", 1);
        reviewData.put("albumId", 1);
        reviewData.put("rating", 85);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(reviewData, headers);

        for (int i = 0; i < numRequests; i++) {
            long startTime = System.nanoTime();
            
            ResponseEntity<String> response = restTemplate.postForEntity(
                createReviewUrl(),
                request,
                String.class
            );
            
            long endTime = System.nanoTime();
            long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            responseTimes.add(duration);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        printStatistics(responseTimes, numRequests);
    }

    @Test
    public void testConcurrentReviewSubmissions() throws InterruptedException {
        int numThreads = 10;
        int requestsPerThread = 10;
        List<Thread> threads = new ArrayList<>();
        List<List<Long>> allResponseTimes = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            List<Long> threadResponseTimes = new ArrayList<>();
            allResponseTimes.add(threadResponseTimes);

            Thread thread = new Thread(() -> {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                Map<String, Object> reviewData = new HashMap<>();
                reviewData.put("userId", 1);
                reviewData.put("albumId", 1);
                reviewData.put("rating", 85);

                HttpEntity<Map<String, Object>> request = new HttpEntity<>(reviewData, headers);

                for (int j = 0; j < requestsPerThread; j++) {
                    long startTime = System.nanoTime();
                    
                    restTemplate.postForEntity(
                        createReviewUrl(),
                        request,
                        String.class
                    );
                    
                    long endTime = System.nanoTime();
                    long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
                    threadResponseTimes.add(duration);

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });

            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        List<Long> allTimes = new ArrayList<>();
        allResponseTimes.forEach(allTimes::addAll);
        printConcurrentStatistics(allTimes, numThreads, requestsPerThread);
    }

    private void printStatistics(List<Long> responseTimes, int numRequests) {
        double avgResponseTime = responseTimes.stream()
            .mapToLong(Long::longValue)
            .average()
            .orElse(0.0);

        long minResponseTime = responseTimes.stream()
            .mapToLong(Long::longValue)
            .min()
            .orElse(0);

        long maxResponseTime = responseTimes.stream()
            .mapToLong(Long::longValue)
            .max()
            .orElse(0);

        List<Long> sortedTimes = new ArrayList<>(responseTimes);
        java.util.Collections.sort(sortedTimes);
        int index95 = (int) Math.ceil(0.95 * sortedTimes.size()) - 1;
        long percentile95 = sortedTimes.get(index95);

        System.out.println("\n=== Performance Test Results ===");
        System.out.println("# requests: " + numRequests);
        System.out.println("Average response time: " + String.format("%.2f", avgResponseTime) + "ms");
        System.out.println("Min response time: " + minResponseTime + "ms");
        System.out.println("Max response time: " + maxResponseTime + "ms");
        System.out.println("95th percentile: " + percentile95 + "ms");
        System.out.println("Throughput: " + String.format("%.2f", (1000.0 * numRequests) / responseTimes.stream().mapToLong(Long::longValue).sum()) + " requests/second");
    }

    private void printConcurrentStatistics(List<Long> allTimes, int numThreads, int requestsPerThread) {
        double avgResponseTime = allTimes.stream()
            .mapToLong(Long::longValue)
            .average()
            .orElse(0.0);

        System.out.println("\n=== Concurrent Performance Test Results ===");
        System.out.println("# threads: " + numThreads);
        System.out.println("Requests/thread: " + requestsPerThread);
        System.out.println("Total requests: " + (numThreads * requestsPerThread));
        System.out.println("Avg response time: " + String.format("%.2f", avgResponseTime) + "ms");
        System.out.println("Throughput: " + String.format("%.2f", (1000.0 * numThreads * requestsPerThread) / allTimes.stream().mapToLong(Long::longValue).sum()) + " requests/second");
    }
}