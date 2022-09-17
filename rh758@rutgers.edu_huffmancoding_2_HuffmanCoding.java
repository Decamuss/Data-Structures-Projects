package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                System.exit(1);
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }
    
    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /**
     * Reads a given text file character by character, and returns an arraylist
     * of CharFreq objects with frequency > 0, sorted by frequency
     * 
     * @param filename The text file to read from
     * @return Arraylist of CharFreq objects, sorted by frequency
     */
    public static ArrayList<CharFreq> makeSortedList(String filename)
    {
        StdIn.setFile(filename);
         ArrayList<CharFreq> arr = new ArrayList<CharFreq>();
 
         int[] ch = new int[128];
 
         String tem = StdIn.readAll();
         char[] x = tem.toCharArray();
         Queue<Character> y = new Queue<Character>();
         for(int i = 0; i < x.length; i++)
         {
             y.enqueue(x[i]);
         }
         while(!y.isEmpty())
         {
             ch[y.dequeue()]++;
         }
         double num = 0;
         for(int i = 0; i < ch.length;i++)
         {
             num = num + ch[i];
         }
         for(int i = 0; i < ch.length; i++)
         {
            if (ch[i] != 0)
            {
                char temp = (char)i;
                arr.add(new CharFreq(temp, ch[i]/num));
            }
         }
         Collections.sort(arr);
         if(arr.size() == 1)
         {
             int temp = (int)(arr.get(0).getCharacter()) + 1;
             arr.add(new CharFreq((char)temp,0));
         }
         return arr;
     }

    /**
     * Uses a given sorted arraylist of CharFreq objects to build a huffman coding tree
     * 
     * @param sortedList The arraylist of CharFreq objects to build the tree from
     * @return A TreeNode representing the root of the huffman coding tree
     */
    public static TreeNode makeTree(ArrayList<CharFreq> sortedList)
    {
        Queue<TreeNode> og = new Queue<TreeNode>();
        Queue<TreeNode> finn = new Queue<TreeNode>();

        for(int i = 0; i < sortedList.size();i++){
            TreeNode temp = new TreeNode();
            temp.setData(sortedList.get(i));
            og.enqueue(temp);
        }

        TreeNode first = new TreeNode();
        first.setLeft(og.dequeue());
        first.setRight(og.dequeue());
        CharFreq firstChar = new CharFreq(null,first.getLeft().getData().getProbOccurrence()+first.getRight().getData().getProbOccurrence());
        first.setData(firstChar);
        finn.enqueue(first);

        while(!(og.isEmpty() && finn.size()==1)){
            TreeNode left = new TreeNode();
            TreeNode right = new TreeNode();

            if(og.isEmpty()) left=finn.dequeue();
            else if(finn.isEmpty()) left=og.dequeue();
            else if(og.peek().getData().getProbOccurrence()<=finn.peek().getData().getProbOccurrence())
            {
                left=og.dequeue();
            }
            else left=finn.dequeue();

            if(og.isEmpty()) right=finn.dequeue();
            else if(finn.isEmpty()) right=og.dequeue();
            else if(og.peek().getData().getProbOccurrence()<=finn.peek().getData().getProbOccurrence())
            {
                right=og.dequeue();
            }
            else right=finn.dequeue();

            TreeNode ne = new TreeNode();
            double su = (left.getData().getProbOccurrence() + right.getData().getProbOccurrence());
            CharFreq x = new CharFreq(null,su);
            ne.setLeft(left);
            ne.setRight(right);
            ne.setData(x);
            finn.enqueue(ne);
        }
        return finn.peek();
    }

    /**
     * Uses a given huffman coding tree to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null
     * 
     * @param root The root of the given huffman coding tree
     * @return Array of strings containing only 1's and 0's representing character encodings
     */
    public static String[] makeEncodings(TreeNode root)
    {
        String[] enc = new String[128];

        enc = helper("",root,enc);

        return enc;
    }
    private static String[] helper(String pre, TreeNode node, String[] enc)
    {
        if (node == null)
        {
            return null;
        }

        helper(pre + "0",node.getLeft(),enc);

        if(node.getLeft() == null && node.getRight() == null)
        {
            int place = (int)(node.getData().getCharacter());
            enc[place] = (pre);
        }
        helper(pre + "1",node.getRight(),enc);
        return enc;
    }

    /**
     * Using a given string array of encodings, a given text file, and a file name to encode into,
     * this method makes use of the writeBitString method to write the final encoding of 1's and
     * 0's to the encoded file.
     * 
     * @param encodings The array containing binary string encodings for each ASCII character
     * @param textFile The text file which is to be encoded
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public static void encodeFromArray(String[] encodings, String textFile, String encodedFile)
    {
        StdIn.setFile(textFile);
        String sum = new String();
        String str = StdIn.readAll();

        ArrayList<Character> arr = new ArrayList<Character>();

        for (char x: str.toCharArray())
        {
            arr.add(x);
        }

        for(int i = 0; i < arr.size();i++)
        {
            int java = (int)arr.get(i);
            sum += (encodings[java]);
        }

        writeBitString(encodedFile, sum);
    }
    
    /**
     * Using a given encoded file name and a huffman coding tree, this method makes use of the 
     * readBitString method to convert the file into a bit string, then decodes the bit string
     * using the tree, and writes it to a file.
     * 
     * @param encodedFile The file which contains the encoded text we want to decode
     * @param root The root of your Huffman Coding tree
     * @param decodedFile The file which you want to decode into
     */
    public static void decode(String encodedFile, TreeNode root, String decodedFile)
    {
        StdOut.setFile(decodedFile);
        String str = readBitString(encodedFile);
        ArrayList<Character> arr = new ArrayList<Character>();
        for (char ch: str.toCharArray())
        {
            arr.add(ch);
        }
        Queue<Character> que = new Queue<Character>();
        for(int i = 0; i < arr.size();i++)
        {
            que.enqueue(arr.get(i));
        }

        TreeNode tree = root;
        while(!que.isEmpty())
        {
            if(tree.getData().getCharacter() != null)
            {
                StdOut.print(tree.getData().getCharacter());
                tree = root;
            }
            else if(que.peek() == '0')
            {
                tree = tree.getLeft();
                que.dequeue();
            }
            else if(que.peek() == '1')
            {
                tree = tree.getRight();
                que.dequeue();
            }
            if(tree.getData().getCharacter() != null)
            {
                StdOut.print(tree.getData().getCharacter());
                tree = root;
            }
        }
    }
}
