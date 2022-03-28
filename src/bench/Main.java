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
    }
}
