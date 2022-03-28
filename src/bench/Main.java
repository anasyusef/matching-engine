package bench;

public class Main {

    public static void main(String[] args) {

        MatchingEnginePerformance mePerf = new MatchingEnginePerformance();
        mePerf.benchmarkAddOrdersDenseOrderBook(10_000);
        mePerf.benchmarkAddOrdersDenseOrderBook(20_000);
        mePerf.benchmarkAddOrdersDenseOrderBook(40_000);
        mePerf.benchmarkAddOrdersDenseOrderBook(80_000);
        mePerf.benchmarkAddOrdersDenseOrderBook(160_000);
        mePerf.benchmarkAddOrdersDenseOrderBook(320_000);
        mePerf.benchmarkAddOrdersDenseOrderBook(640_000);
        mePerf.benchmarkAddOrdersDenseOrderBook(1_280_000);
        System.out.println();
        mePerf.benchmarkAddOrdersSparseOrderBook(10_000);
        mePerf.benchmarkAddOrdersSparseOrderBook(20_000);
        mePerf.benchmarkAddOrdersSparseOrderBook(40_000);
        mePerf.benchmarkAddOrdersSparseOrderBook(80_000);
        mePerf.benchmarkAddOrdersSparseOrderBook(160_000);
        mePerf.benchmarkAddOrdersSparseOrderBook(320_000);
        mePerf.benchmarkAddOrdersSparseOrderBook(640_000);
        mePerf.benchmarkAddOrdersSparseOrderBook(1_280_000);
        System.out.println();
        mePerf.benchmarkOrderBookMarketDepth(10_000);
        mePerf.benchmarkOrderBookMarketDepth(20_000);
        System.out.println();
        mePerf.benchmakrOrderBookRemove(10_000);
        mePerf.benchmakrOrderBookRemove(20_000);
        mePerf.benchmakrOrderBookRemove(40_000);
        mePerf.benchmakrOrderBookRemove(80_000);
        mePerf.benchmakrOrderBookRemove(160_000);
        mePerf.benchmakrOrderBookRemove(320_000);
        mePerf.benchmakrOrderBookRemove(640_000);
        mePerf.benchmakrOrderBookRemove(1_280_000);
    }
}
