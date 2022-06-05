import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class Wow2Source2 {

    public static String WowResourcesRoot = "D:/SteamLibrary/steamapps/common/Half-Life Alyx/content/hlvr_addons/limit_break/wowresources";
    public static String Source2ProjectRoot = "D:/SteamLibrary/steamapps/common/Half-Life Alyx/content/hlvr_addons/limit_break";

    public static String Source2ProjectName;

    public static ArrayList<File> fileIndex = new ArrayList<File>();

    public static void main(String[] args) {
        buildVmap();
    }

    public static void buildVmap() {
        System.out.println("Building Vmap..");
        CMapEntity[] entities = new CMapEntity[3];
        entities[0] = new CMapEntity(0,0,0,0,0,0,1,1,1,"prop_static","models/7fk_darkmoon_forsakendrumset01.vmdl");
        entities[1] = new CMapEntity(64,0,0,0,0,0,1,1,1,"prop_static","models/7fk_darkmoon_forsakendrumset01.vmdl");
        entities[2] = new CMapEntity(128,0,0,0,0,0,1,1,1,"prop_static","models/7fk_darkmoon_forsakendrumset01.vmdl");
        VmapConverter.create(entities);
        System.out.println("Done!");
    }

    public static void buildProject() {
        String[] parts = Source2ProjectRoot.split("/");
        Source2ProjectName = parts[parts.length-1];
        System.out.println("Source2 Project Name: " + Source2ProjectName);

        System.out.print("Building file index.. ");
        System.out.println(FileUtil.buildIndex(fileIndex, WowResourcesRoot) + " entries.");

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
            }
        }
    }

}
