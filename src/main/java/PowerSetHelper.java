import Exceptions.PowerSetException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Vicky on 28/09/16.
 */
public class PowerSetHelper {

    public static List<Integer> parseInputSet(String filePath, Boolean isTestFile) {
        //Get file from resources folder
        File inputFile = null;
        try {
            if (isTestFile) {
                ClassLoader classLoader = new PowerSetHelper().getClass().getClassLoader();
                inputFile = new File(classLoader.getResource(filePath).getFile());
            } else {
                inputFile = new File(filePath);
            }
        } catch (NullPointerException ne) {
            throw new PowerSetException("Null Pointer Exception", ne);
        } catch (Exception e) {
            throw new PowerSetException("Exception thrown!!", e);
        }

        String line = null;
        try {
            Scanner scanner = new Scanner(inputFile);
            line = scanner.nextLine();
            scanner.close();
        } catch (IOException e) {
            throw new PowerSetException("IOException", e);
        }
        //parsing "1,2,3" from a file format of "{1,2,3}"
        String[] setStringArray = line.split("\\{")[1].split("\\}")[0].split(",");
        List<Integer> integerList = new ArrayList<Integer>();
        for(String number: setStringArray) {
            integerList.add(Integer.parseInt(number));
        }
        return integerList;
    }

    public static String prepareSinglePowerSet(BitSet bs, List<Integer> list) {
        // check to see if the list is null
        if (list == null) {
            throw new PowerSetException("Empty Input Set");
        }

        StringBuilder stringBuilder = new StringBuilder("{ ");
        for (int i = 0; i < list.size(); i++) {
            if (bs.get(i)) {
                // adding "," after each integer
                stringBuilder.append(list.get(i)).append(",");
            }
        }
        //removing last "," for proper formatting
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }

    public static String getPowerSetContent(List<Integer> list) {
        // maximum number of combinations possible for n items is 2^n.
        long max = (long) Math.pow(2, list.size());
        StringBuilder powerSetContentBuffer = new StringBuilder();
        for (long currentNumber = 0; currentNumber < max; currentNumber++) {
            // Convert number to bit set using native java method available.
            BitSet numberConvertedBitSet = BitSet.valueOf(new long[]{currentNumber});

            // Use Binary Value of the combination to print its corresponding subset.
            powerSetContentBuffer.append(prepareSinglePowerSet(numberConvertedBitSet, list)).append("\n");
        }
        return powerSetContentBuffer.toString();
    }

    public static void writePowerSet(String filePath, String content) throws PowerSetException {
        File file = new File(filePath);
        BufferedWriter outputBufferWriter = null;
        try {
            // if file doesnt exists, then create it else throw power set exception.
            if (!file.exists()) {
                if(!file.createNewFile()) {
                    throw new PowerSetException("Unable to create output file");
                }
            }
            FileWriter outputFileWriter = new FileWriter(file.getAbsoluteFile());
            outputBufferWriter = new BufferedWriter(outputFileWriter);
            outputBufferWriter.write(content);
            outputBufferWriter.close();
        } catch (IOException e) {
            throw new PowerSetException("IO exception in writing output file", e);
        } finally {
            if(outputBufferWriter != null) {
                try {
                    outputBufferWriter.close();
                } catch (IOException e) {
                    throw new PowerSetException("IOException: Unable to close BufferWriter",e);
                }
            }
        }
    }
}
