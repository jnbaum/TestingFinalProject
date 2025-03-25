import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Main {
    static Map<String, List<String>> testCoverage = new HashMap<>();
    static Set<String> failedTests = new HashSet<>();

    public static void main(String[] args) {
        String folderPath = "C:\\Users\\jbaum\\OneDrive\\Documents\\TESTING 6673\\NewCoverageData";
        readCoverageData(folderPath);
        markFailedTests(3);
        printResults();
    }

    static void readCoverageData(String folderPath) {
        try {
            Files.walk(Paths.get(folderPath))
                .filter(Files::isRegularFile)
                .forEach(file -> processFile(file.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void processFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String testCase = br.readLine().trim();
            List<String> coveredMethods = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                coveredMethods.add(line.trim());
            }
            testCoverage.put(testCase, coveredMethods);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void markFailedTests(int numFails) {
        List<String> allTests = new ArrayList<>(testCoverage.keySet());
        Collections.shuffle(allTests);
        for (int i = 0; i < numFails && i < allTests.size(); i++) {
            failedTests.add(allTests.get(i));
        }
    }

    static Map<String, Double> calculateTarantula() {
        Map<String, Double> tarantulaScores = new HashMap<>();
        int totalPassed = testCoverage.size() - failedTests.size();
        int totalFailed = failedTests.size();

        Map<String, Integer> ef = new HashMap<>();
        Map<String, Integer> ep = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : testCoverage.entrySet()) {
            boolean isFailed = failedTests.contains(entry.getKey());
            for (String method : entry.getValue()) {
                if (isFailed) {
                    ef.put(method, ef.getOrDefault(method, 0) + 1);
                } else {
                    ep.put(method, ep.getOrDefault(method, 0) + 1);
                }
            }
        }

        for (String method : ef.keySet()) {
            int efValue = ef.getOrDefault(method, 0);
            int epValue = ep.getOrDefault(method, 0);

            double failRate = (totalFailed > 0) ? ((double) efValue / totalFailed) : 0;
            double passRate = (totalPassed > 0) ? ((double) epValue / totalPassed) : 0;

            double denominator = failRate + passRate;
            double score = (denominator > 0) ? (failRate / denominator) : 0;

            tarantulaScores.put(method, score);
        }
        return tarantulaScores;
    }

    static Map<String, Double> calculateSbi() {
        Map<String, Double> sbiScores = new HashMap<>();
        Map<String, Integer> ef = new HashMap<>();
        Map<String, Integer> ep = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : testCoverage.entrySet()) {
            boolean isFailed = failedTests.contains(entry.getKey());
            for (String method : entry.getValue()) {
                if (isFailed) {
                    ef.put(method, ef.getOrDefault(method, 0) + 1);
                } else {
                    ep.put(method, ep.getOrDefault(method, 0) + 1);
                }
            }
        }

        for (String method : ef.keySet()) {
            int efValue = ef.getOrDefault(method, 0);
            int epValue = ep.getOrDefault(method, 0);
            double score = (double) efValue / (efValue + epValue);
            sbiScores.put(method, score);
        }
        return sbiScores;
    }

    static Map<String, Double> calculateJaccard() {
        Map<String, Double> jaccardScores = new HashMap<>();
        Map<String, Integer> ef = new HashMap<>();
        Map<String, Integer> ep = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : testCoverage.entrySet()) {
            boolean isFailed = failedTests.contains(entry.getKey());
            for (String method : entry.getValue()) {
                if (isFailed) {
                    ef.put(method, ef.getOrDefault(method, 0) + 1);
                } else {
                    ep.put(method, ep.getOrDefault(method, 0) + 1);
                }
            }
        }

        for (String method : ef.keySet()) {
            int efValue = ef.getOrDefault(method, 0);
            int epValue = ep.getOrDefault(method, 0);
            double score = (double) efValue / (efValue + epValue + efValue);
            jaccardScores.put(method, score);
        }
        return jaccardScores;
    }

    static Map<String, Double> calculateOchiai() {
        Map<String, Double> ochiaiScores = new HashMap<>();
        Map<String, Integer> ef = new HashMap<>();
        Map<String, Integer> ep = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : testCoverage.entrySet()) {
            boolean isFailed = failedTests.contains(entry.getKey());
            for (String method : entry.getValue()) {
                if (isFailed) {
                    ef.put(method, ef.getOrDefault(method, 0) + 1);
                } else {
                    ep.put(method, ep.getOrDefault(method, 0) + 1);
                }
            }
        }

        for (String method : ef.keySet()) {
            int efValue = ef.getOrDefault(method, 0);
            int epValue = ep.getOrDefault(method, 0);
            double score = (double) efValue / Math.sqrt(efValue * (epValue + efValue));
            ochiaiScores.put(method, score);
        }
        return ochiaiScores;
    }

    static void printResults() {
   
    	    Map<String, Double> tarantulaScores = calculateTarantula();
    	    Map<String, Double> sbiScores = calculateSbi();
    	    Map<String, Double> jaccardScores = calculateJaccard();
    	    Map<String, Double> ochiaiScores = calculateOchiai();

    	    // Print header
    	    System.out.printf("%-10s %-15s %-15s %-15s %-15s%n", "Method", "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTarantula", "\tSBI", "Jaccard", "Ochiai");
    	    System.out.println("-".repeat(300));

    	 
    	    for (String method : tarantulaScores.keySet()) {
    	        double tarantula = tarantulaScores.getOrDefault(method, 0.0);
    	        double sbi = sbiScores.getOrDefault(method, 0.0);
    	        double jaccard = jaccardScores.getOrDefault(method, 0.0);
    	        double ochiai = ochiaiScores.getOrDefault(method, 0.0);

    	        System.out.printf("%-150s %-15.3f %-15.3f %-15.3f %-15.3f%n", method, tarantula, sbi, jaccard, ochiai);
    	    }
    	}

}
