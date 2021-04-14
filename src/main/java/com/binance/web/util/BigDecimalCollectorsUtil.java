package com.binance.web.util;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class BigDecimalCollectorsUtil {
    static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    private BigDecimalCollectorsUtil() {
    }

    @SuppressWarnings("unchecked")
    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
                      Function<A, R> finisher, Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }

    //求和方法
    public static <T> Collector<T, ?, BigDecimal> summingBigDecimal(ToBigDecimalFunction<? super T> mapper) {
        return new CollectorImpl<>(
                () -> new BigDecimal[]{BigDecimal.ZERO},
                (a, t) -> { a[0] = a[0].add(mapper.applyAsBigDecimal(t)); },
                (a, b) -> { a[0] = a[0].add(b[0]) ; return a; },
                a -> a[0], CH_NOID);
    }

    //求最大值
    public static <T> Collector<T, ?, BigDecimal> maxBy(ToBigDecimalFunction<? super T> mapper) {
        return new CollectorImpl<>(
                () -> new BigDecimal[]{new BigDecimal(Long.MIN_VALUE)},
                (a, t) -> { a[0] = a[0].max(mapper.applyAsBigDecimal(t)); },
                (a, b) -> { a[0] = a[0].max(b[0]) ; return a; },
                a -> a[0], CH_NOID);
    }

    //求最小值
    public static <T> Collector<T, ?, BigDecimal> minBy(ToBigDecimalFunction<? super T> mapper) {
        return new CollectorImpl<>(
                () -> new BigDecimal[]{new BigDecimal(Long.MAX_VALUE)},
                (a, t) -> { a[0] = a[0].min(mapper.applyAsBigDecimal(t)); },
                (a, b) -> { a[0] = a[0].min(b[0]) ; return a; },
                a -> a[0], CH_NOID);
    }

    //求平均值
    public static <T> Collector<T, ?, BigDecimal> averagingBigDecimal(ToBigDecimalFunction<? super T> mapper, int newScale, int roundingMode) {
        return new CollectorImpl<>(
                () -> new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO},
                (a, t) -> { a[0] = a[0].add(mapper.applyAsBigDecimal(t)); a[1] = a[1].add(BigDecimal.ONE); },
                (a, b) -> { a[0] = a[0].add(b[0]) ; return a; },
                a -> a[0].divide(a[1],BigDecimal.ROUND_HALF_UP).setScale(newScale, roundingMode), CH_NOID);
    }


    public static void main(String[] args) {
        List<BigDecimal> list = Lists.newArrayList(new BigDecimal(10),new BigDecimal(40),new BigDecimal(30),new BigDecimal(20));
        BigDecimal ave = list.stream().collect(averagingBigDecimal(p -> p, 2, BigDecimal.ROUND_HALF_UP));
        System.out.println("ave: "+ave);
        BigDecimal sum = list.stream().collect(summingBigDecimal(p -> p));
        System.out.println("sum :"+sum);
        BigDecimal max = list.stream().collect(maxBy(p -> p));
        System.out.println("max :"+max);
        BigDecimal min = list.stream().collect(minBy(p -> p));
        System.out.println("min :"+min);
    }
}
