import java.io.File;
import java.nio.charset.StandardCharsets;

// Vmat essentially replaces mtl.

public class VmatConverter {

    public static final String TEXTURE_COLOR_VALUE = "$TEXTURE_COLOR_VALUE";

    public static boolean convert(String path){
        return convert(new File(path));
    }

    public static boolean convert(File file) {
        String filePath = file.getAbsolutePath().split(Wow2Source2.Source2ProjectName)[1].replace("\\", "/");
        filePath = filePath.substring(1);
        String fileName = file.getName();
        String template = new String(FileUtil.readFully("templates/vmat.template"));
        template = template.replace(TEXTURE_COLOR_VALUE, filePath);
        return FileUtil.writeFully(Wow2Source2.Source2ProjectRoot + "/materials/mat_" + fileName.split("\\.")[0] + ".vmat", template.getBytes());
    }

    private static String performMtlSniff(String fileName, File file) {
        String[] lines = new String(FileUtil.readFully(file)).split("\r\n");
        for(String line : lines){
            if(line.contains(fileName.split("\\.")[0])){
                return line.split("usemtl ")[1];
            }
        }
        return fileName;
    }

}
