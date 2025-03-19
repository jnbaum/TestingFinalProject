import java.io.*;
import java.nio.file.*;
import java.util.*;

    
    public class Main {
    	
    	 static Map<String, List<String>> testCoverage = new HashMap<>();
         static Set<String> failedTests = new HashSet<>();
         
    	public static void main(String[]args) {
      
            String folderPath = "C:\\Users\\jbaum\\OneDrive\\Documents\\TESTING 6673\\NewCoverageData\\1.txt"; // Update path
            readCoverageData(folderPath);
            markFailedTests(3); // Randomly fail 3 tests
            Map<String, Double> tarantulaScores = calculateTarantula();
            printResults(tarantulaScores);
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
                String testCase = br.readLine().trim(); // First line: test case
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
            Map<String, Double> scores = new HashMap<>();

            int totalPassed = testCoverage.size() - failedTests.size();
            int totalFailed = failedTests.size();

            Map<String, Integer> ef = new HashMap<>();
            Map<String, Integer> ep = new HashMap<>();

            // Count ef and ep
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

            // Compute Tarantula score
            for (String method : ef.keySet()) {
                int efValue = ef.getOrDefault(method, 0);
                int epValue = ep.getOrDefault(method, 0);

                double failRate = (totalFailed > 0) ? ((double) efValue / totalFailed) : 0;
                double passRate = (totalPassed > 0) ? ((double) epValue / totalPassed) : 0;

                double denominator = failRate + passRate;
                double score = (denominator > 0) ? (failRate / denominator) : 0; // Avoid NaN

                scores.put(method, score);
            }

            return scores;
        }


        static void printResults(Map<String, Double> scores) { //this method will print the results.
            scores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) 
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
        }

    }

