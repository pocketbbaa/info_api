package com.kg.platform.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.kg.platform.common.idgen.IDGen;

public class IdWorkerTest {

    public static HashSet<Long> idSet = new HashSet<>();

    public static void main(String[] args) {
        // IdWorkerTest test = new IdWorkerTest();
        // test.test2();
        //
        long t = System.nanoTime();
        long t6 = System.currentTimeMillis();

        IDGen snowflakeIdWorker = new IDGen();
        snowflakeIdWorker.setWorkerId(1L);
        snowflakeIdWorker.setDatacenterId(1L);

        for (long i = 0; i < 5000; i++) {
            // new Thread(new Worker(new IdWorker(1, 0))).start();

            new Thread(new Worker(snowflakeIdWorker)).start();
        }

        System.err.println(System.nanoTime() - t);
        System.err.println(System.currentTimeMillis() - t6);
    }

    static class Worker implements Runnable {

        private IDGen snowflakeIdWorker;

        public Worker(IDGen snowflakeIdWorker) {
            this.snowflakeIdWorker = snowflakeIdWorker;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1; i++) {
                Long id = snowflakeIdWorker.nextId();
                // synchronized (SnowflakeIdWorkerTest.class) {
                if (!idSet.add(id)) {
                    System.err.println("存在重复id:" + id);
                } else {
                    // System.out.println(id);
                }
                // }
            }
        }
    }

    public void test2() {
        final IDGen w = new IDGen();
        w.setWorkerId(1L);
        w.setDatacenterId(1L);

        // final CyclicBarrier cdl = new CyclicBarrier(100);
        final List<Long> ids = new ArrayList<Long>();
        long t = System.nanoTime();
        long t6 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            // new Thread(new Runnable() {
            // @Override
            // public void run() {
            // try {
            // cdl.await();
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // } catch (BrokenBarrierException e) {
            // e.printStackTrace();
            // }
            Long id = w.nextId();
            // System.out.println(id);
            if (ids.contains(id)) {
                System.err.println(id + "重复");
            } else {
                ids.add(id);
            }
        }
        System.err.println(System.nanoTime() - t);
        System.err.println(System.currentTimeMillis() - t6);
        // }).start();
        // }
        // try {
        // TimeUnit.SECONDS.sleep(5);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

    }
}
