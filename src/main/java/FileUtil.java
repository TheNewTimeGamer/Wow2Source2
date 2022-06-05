import java.io.*;
import java.util.ArrayList;

public class FileUtil {

    public static byte[] readFully(String path) {
        return readFully(new File(path));
    }

    public static byte[] readFully(File file) {
        if(!file.exists() || !file.isFile()){
            System.err.println("File: " + file.getAbsolutePath() + " does not exist.");
            return null;
        }
        byte[] buffer = null;
        DataInputStream in = null;
        try {
            in = new DataInputStream(new FileInputStream(file));
            buffer = in.readAllBytes();
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            in.close();
        }catch(Exception e){
            System.err.println("Failed to close DataInputStream.");
        }
        return buffer;
    }

    public static boolean writeFully(String path, byte[] data) {
        return writeFully(new File(path), data);
    }

    public static boolean writeFully(File file, byte[] data) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
            out.write(data);
            out.flush();
            out.close();
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int buildIndex(ArrayList<File> index, String path){
        return buildIndex(index, new File(path));
    }

    public static int buildIndex(ArrayList<File> index, File root) {
        int count = 0;
        File[] files = root.listFiles();
        for(File file : files){
            if(file.isDirectory()) {
                count += buildIndex(index, file);
            }else if(file.isFile()){
                index.add(file);
                count++;
            }
        }
        return count;
    }

}
