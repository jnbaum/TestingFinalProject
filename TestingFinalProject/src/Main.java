import java.nio.file.*;
import java.io.*;
import java.util.*;

    
    public class Main {         
    	public static void main(String[]args) { //this method will take the input of the .txt files
    		Map<String, Double> hashMap = new HashMap<>();
    		
    		String folderPath = "C:\\Users\\jbaum\\OneDrive\\Documents\\TESTING 6673\\NewCoverageData"; 
    		readCoverageData(folderPath);
    		markFailedTests(3);
    	}
        
        static void readCoverageData(String folderPath) { //reads all of the files
            try {
            	Files.walk(Paths.get(folderPath)) //traverses the directory to find the file that is given in main. Paths.get makes the file string to a path. 
                .filter(Files::isRegularFile) //makes sure no directories are processed, only files
                .forEach(file -> processFile(file.toFile())); //iterates over files and then sends each to processes
            		} catch (IOException e) { //second half of the try/catch block. For exception handling.
            			e.printStackTrace(); //prints the stack trace of any caught exceptions
            		}
        }

        static void processFile(File file) { //processes the files
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

        static void markFailedTests(int numFails) { //marks some as failed
           
        }
        
        static void countLineExecutions(String line, boolean isPassed) {  // Updates execution counts for passing and failing tests.
        
        }

        
        static Map<String, Double> calculateTarantula() { //calculates tarantula
			return tarantulascore;
            
        }
        
        static Map<String, Double> calculateSbi() { //calculates SBI
        	return sbiscore;
        }
        
        static Map<String, Double> calculateJaccard() { //calculates Jaccard
        	return jaccardscore;
        }
        
        static Map<String, Double> calculateOchiai(){ // calculates Ochiai
        	return ochiaiscore;
        }

        
        static Map<String, Double> getSortedSuspiciousness(Map<String, Double> scores) { // Returns a sorted version of the map based on scores in descending order.
        	return sortedScores;
        }

        
        static void printResults(Map<String, Double> scores) { //this method will print the results.
            

    }

}
    