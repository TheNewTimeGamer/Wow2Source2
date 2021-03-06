import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class Wow2Source2 {

    public static String ProjectName = "sneaky";
    public static String MapName = "2444";

    public static String WowResourcesRoot = "D:/SteamLibrary/steamapps/common/Half-Life Alyx/content/hlvr_addons/" + ProjectName + "/wowresources";
    public static String Source2ProjectRoot = "D:/SteamLibrary/steamapps/common/Half-Life Alyx/content/hlvr_addons/" + ProjectName;
    public static String MapRoot = "D:/SteamLibrary/steamapps/common/Half-Life Alyx/content/hlvr_addons/" + ProjectName + "/models/maps/" + MapName;

    public static String Source2ProjectName;

    public static ArrayList<File> fileIndex = new ArrayList<File>();

    public static boolean skipProcessing = true;

    public static void main(String[] args) {
        buildProject();
    }

    public static void buildProject() {
        String[] parts = Source2ProjectRoot.split("/");
        Source2ProjectName = parts[parts.length-1];
        System.out.println("Source2 Project Name: " + Source2ProjectName);

        System.out.print("Building file index.. ");
        System.out.println(FileUtil.buildIndex(fileIndex, WowResourcesRoot) + " entries.");

        ArrayList<CMapEntity> entityBuffer = new ArrayList<CMapEntity>();
        HashMap<String, CMapEntity[]> relations = new HashMap<String, CMapEntity[]>();

        for(File file : fileIndex) {
            final String fileExtension = file.getName().split("\\.")[1].toLowerCase();
            switch(fileExtension){
                case "png":
                case "tga":
                case "jpg":
                case "jpeg":
                    processVmat(file);
                    break;
                case "obj":
                    processObj(file);
                    break;
                case "csv":
                    processCsv(file, relations);
                    break;
            }
        }

        System.out.println("Adding map tiles from " + MapRoot + ":");
        addMapTiles(entityBuffer, new File(MapRoot));
        System.out.println("Done!");

        CMapEntity[] entities = entityBuffer.toArray(new CMapEntity[0]);
        System.out.println("Processing entities " + entities.length + ": ");
        for(CMapEntity entity : entities) {
            if(entity == null){continue;}
            System.out.println(" - " + entity.className + " " + entity.vmdlModel);
        }
        if(VmapConverter.create(entities, relations)){
            System.out.println("Done!");
        }else{
            System.out.println("Failed!");
        }
    }

    public static void processVmat(File file) {
        if(skipProcessing){return;}
        System.out.print("Converting to vmat: " + file.getName() + " .. ");
        System.out.println(VmatConverter.convert(file) ? "Success!" : "Failed!");
    }

    public static void processObj(File file){
        if(skipProcessing){return;}
        System.out.print("Converting to vmdl: " + file.getName() + " .. ");
        System.out.println(VmdlConverter.convert(file, true) ? "Success!" : "Failed!");
        System.out.print("Patching obj file.. " + file.getName() + " .. ");
        System.out.println(VmdlConverter.objPatch(file) ? "Success!" : "Skipped!");
    }

    public static void processCsv(File file, HashMap<String, CMapEntity[]> relations){
        System.out.println("Processing csv: " + file.getName() + " .. ");
        VmapConverter.processCsv(relations, file);
        System.out.println(relations.size() + " entity relations found.");
    }

    public static void addMapTiles(ArrayList<CMapEntity> entityBuffer, File mapRoot) {
        File[] files = mapRoot.listFiles();
        for(int i = 0; i < files.length; i++){
            if(files[i] == null || !files[i].isFile()) {
                continue;
            }
            String path = mapRoot.getPath().split(Source2ProjectName)[1].replace("\\", "/").substring(1);
            path = path + "/" + files[i].getName();
            System.out.println(" - " + path);
            entityBuffer.add(new CMapEntity(0,0,0,0,-90,0,1,1,1,"prop_static",path,null));
        }
    }

}
