import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Wow2Source2 {

    public static String WowResourcesRoot = "D:/SteamLibrary/steamapps/common/Half-Life Alyx/content/hlvr_addons/limit_break/wowresources";
    public static String Source2ProjectRoot = "D:/SteamLibrary/steamapps/common/Half-Life Alyx/content/hlvr_addons/limit_break";

    public static String Source2ProjectName;

    public static ArrayList<File> fileIndex = new ArrayList<File>();

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

        for(File file : fileIndex) {
            final String fileExtension = file.getName().split("\\.")[1].toLowerCase();
            switch(fileExtension){
                case "png":
                case "tga":
                case "jpg":
                case "jpeg":
                    System.out.print("Converting to vmat: " + file.getName() + " .. ");
                    System.out.println(VmatConverter.convert(file) ? "Success!" : "Failed!");
                    break;
                case "obj":
                    System.out.print("Converting to vmdl: " + file.getName() + " .. ");
                    System.out.println(VmdlConverter.convert(file, true) ? "Success!" : "Failed!");
                    System.out.print("Patching obj file.. " + file.getName() + " .. ");
                    System.out.println(VmdlConverter.objPatch(file) ? "Success!" : "Skipped!");
                    break;
                case "csv":
                    System.out.print("Processing csv: " + file.getName() + " .. ");
                    CMapEntity[] lEntities = VmapConverter.processCsv(file);
                    entityBuffer.addAll(Arrays.stream(lEntities).toList());
                    System.out.println(lEntities.length + " entities found.");
                    break;
            }
        }

        addMapTiles(entityBuffer, new File(""));

        CMapEntity[] entities = entityBuffer.toArray(new CMapEntity[0]);
        System.out.println("Processing entities " + entities.length + ": ");
        for(CMapEntity entity : entities) {
            if(entity == null){continue;}
            System.out.println(" - " + entity.className + " " + entity.vmdlModel);
        }
        if(VmapConverter.create(entities)){
            System.out.println("Done!");
        }else{
            System.out.println("Failed!");
        }
    }

    public static void addMapTiles(ArrayList<CMapEntity> entityBuffer, File mapRoot) {

    }

}
