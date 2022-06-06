import java.io.File;
import java.nio.charset.StandardCharsets;

public class VmdlConverter {

    public static final String MESH_FILENAME = "$MESH_FILENAME";

    public static boolean convert(String path, boolean mapOverride){
        return convert(new File(path), mapOverride);
    }

    public static boolean convert(File file, boolean mapOverride) {
        String filePath = file.getAbsolutePath().split(Wow2Source2.Source2ProjectName)[1].replace("\\", "/");
        String overridePath = "";
        if(filePath.contains("/maps/")){
            if(!mapOverride){
                System.out.print("map file -> use import .. ");
                return false;
            }
            System.out.print("map file -> override active .. ");
            overridePath = "maps/" + file.getParentFile().getName() + "/";
            new File(Wow2Source2.Source2ProjectRoot + "/models/" + overridePath).mkdirs();
        }
        filePath = filePath.substring(1);
        String template = new String(FileUtil.readFully("templates/vmdl.template"));
        template = template.replace(MESH_FILENAME, filePath);
        return FileUtil.writeFully(Wow2Source2.Source2ProjectRoot + "/models/" + overridePath + file.getName().split("\\.")[0] + ".vmdl", template.getBytes());
    }

    public static boolean objPatch(File file) {
        String raw = new String(FileUtil.readFully(file));
        if(raw.contains("usemtl materials/")){
            if(raw.contains("usemtl materials/mat_")){
                return false;
            }
            raw = raw.replace("usemtl materials/", "usemtl materials/mat_");
            System.out.print("partial patch .. ");
            return FileUtil.writeFully(file, raw.getBytes());
        }
        if(raw.contains("usemtl mat_")){
            raw = raw.replace("usemtl ", "usemtl materials/");
        }else{
            raw = raw.replace("usemtl ", "usemtl materials/mat_");
        }
        return FileUtil.writeFully(file, raw.getBytes());
    }

}
