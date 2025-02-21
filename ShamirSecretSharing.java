import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.JSONObject;

public class ShamirSecretSharing {
    
    // Function to convert a string from a given base to an integer
    public static BigInteger convertToDecimal(String value, int base) {
        return new BigInteger(value, base);
    }
    
    // Function to perform Lagrange interpolation to find the constant term
    public static BigInteger lagrangeInterpolation(List<int[]> points) {
        int k = points.size();
        BigInteger secret = BigInteger.ZERO;
        
        for (int i = 0; i < k; i++) {
            int xi = points.get(i)[0];
            BigInteger yi = new BigInteger(String.valueOf(points.get(i)[1]));
            BigInteger term = yi;
            
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    int xj = points.get(j)[0];
                    term = term.multiply(BigInteger.valueOf(-xj));
                    term = term.divide(BigInteger.valueOf(xi - xj));
                }
            }
            secret = secret.add(term);
        }
        return secret;
    }
    
    public static void main(String[] args) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("input.json")));
            JSONObject jsonObject = new JSONObject(content);
            
            List<int[]> points = new ArrayList<>();
            JSONObject keys = jsonObject.getJSONObject("keys");
            int k = keys.getInt("k");
            
            for (String key : jsonObject.keySet()) {
                if (key.equals("keys")) continue;
                JSONObject valueObject = jsonObject.getJSONObject(key);
                int x = Integer.parseInt(key);
                int base = Integer.parseInt(valueObject.getString("base"));
                BigInteger y = convertToDecimal(valueObject.getString("value"), base);
                points.add(new int[]{x, y.intValue()});
            }
            
            points = points.subList(0, k); // Take only the required k points
            System.out.println("Secret constant term (c): " + lagrangeInterpolation(points));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
