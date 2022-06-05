import java.io.File;
import java.nio.charset.StandardCharsets;

public class VmdlConverter {

    public static final String MESH_FILENAME = "$MESH_FILENAME";

    public static boolean convert(String path){
        return convert(new File(path));
    }

    public static boolean convert(File file) {
        String filePath = file.getAbsolutePath().split(Wow2Source2.Source2ProjectName)[1].replace("\\", "/");
        filePath = filePath.substring(1);
        String template = new String(FileUtil.readFully("templates/vmdl.template"));
        template = template.replace(MESH_FILENAME, filePath);
        return FileUtil.writeFully(Wow2Source2.Source2ProjectRoot + "/models/" + file.getName().split("\\.")[0] + ".vmdl", template.getBytes());
    }

    public static boolean objPatch(File file) {
        String raw = new String(FileUtil.readFully(file));
        if(raw.contains("usemtl materials/")){
            return false;
        }
        raw = raw.replace("usemtl ", "usemtl materials/");
        return FileUtil.writeFully(file, raw.getBytes());
    }

}
