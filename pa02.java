/*=============================================================================
| Assignment: pa02 - Calculating an 8, 16, or 32 bit
| checksum on an ASCII input file
|
| Author: Camilo Alvarez-Velez
| Language: Java
|
| To Compile: javac pa02.java
|
| To Execute: java -> java pa02 inputFile.txt 8
|
+=============================================================================*/
import java.math.BigInteger;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class pa02
{
    public static String toHex(int decimal)
    {
        String hex = "";
        char [] input = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while (decimal > 0)
        {
            int remainder = decimal%16;
            hex = input[remainder]+hex;
            decimal = decimal / 16;
        }
        return hex;

    }

    public static String hexToBinary(String hex)
    {
        String binary = "";
        hex = hex.toUpperCase();
        HashMap<Character, String> hashMap = new HashMap<>();

        hashMap.put('0', "0000");
        hashMap.put('1', "0001");
        hashMap.put('2', "0010");
        hashMap.put('3', "0011");
        hashMap.put('4', "0100");
        hashMap.put('5', "0101");
        hashMap.put('6', "0110");
        hashMap.put('7', "0111");
        hashMap.put('8', "1000");
        hashMap.put('9', "1001");
        hashMap.put('A', "1010");
        hashMap.put('B', "1011");
        hashMap.put('C', "1100");
        hashMap.put('D', "1101");
        hashMap.put('E', "1110");
        hashMap.put('F', "1111");

        for (int i = 0; i < hex.length(); i++)
        {
            char ch = hex.charAt(i);
            if (hashMap.containsKey(ch))
                binary += hashMap.get(ch);
            else
                return "-1";
        }

        return binary;
    }

    public static String addition(String first, String second,int n)
    {

        BigInteger b1 = new BigInteger(first,2);
        BigInteger b2 = new BigInteger(second,2);
        BigInteger sum = b1.add(b2);

        sum = sum.clearBit(n);
        return sum.toString(2);
    }

    private static String fixPadding(String hex, int n)
    {
        int paddingValue = hex.length() * 4 %n;
        if (paddingValue != 0)
        {
            paddingValue = (n - paddingValue) >> 3;
            for (int i = 0; i < paddingValue; i++)
            {
                System.out.print("X");
                hex += 58; // X(ascii) in hex
            }

        }
        System.out.println();
        return hex;
    }

    private static void printEighty(String line)
    {
        int count = 0;
        while (count < line.length())
        {
            System.out.print(line.charAt(count));
            count++;
            if (count%80 == 0)
                System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args)throws FileNotFoundException
    {
        Scanner scan = new Scanner (new FileReader(args[0]));
        int n;
        System.out.println();
        if (args.length != 2)
            return;

        n = Integer.parseInt(args[1]);
        if (n != 8 && n != 16 && n != 32)
        {
            System.err.println("Valid checksum sizes are 8, 16, or 32\n");
            return;
        }
        if (!scan.hasNextLine())
        {
            System.err.println("Empty File\n");
            return;
        }

        String line = scan.nextLine();
        String hex = "";

        printEighty(line);

        for (int i = 0; i < line.length(); i++)
            hex += toHex(line.charAt(i));
        hex = fixPadding(hex + "0A",n); //Add terminating character to Hex string and adds padding if necessary.
        String rawBinary = hexToBinary(hex);

        String checksum = addBinaryNumbers(rawBinary,n);
        System.out.printf("%2d bit checksum is %8s for all %4d chars\n", n, checksum, hex.length()/2);

    }

    private static String addBinaryNumbers(String rawBinary,int n)
    {
        int blocks = rawBinary.length()/n;
        String result = rawBinary.substring(0, n);

        for (int i = 1; i < blocks; i++)
        {
            int startIndex = i * n;
            int endIndex = startIndex + n;
            String block = rawBinary.substring(startIndex, endIndex);

            result = addition(block,result,n);
        }
        if (blocks == 0)
            result = rawBinary;

        while (result.length()<n)
            result = "0" + result;

        return new BigInteger(result, 2).toString(16);
    }
}
