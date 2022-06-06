import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class VmapConverter {

    // To-Do: Figure out exact scaling values for imports
    // Create vmap writer.
    // Check this:

    /*
        ModelFile;PositionX;PositionY;PositionZ;RotationX;RotationY;RotationZ;RotationW;ScaleFactor;ModelId;Type;FileDataID
        ..\..\world\expansion03\doodads\skywall\cloud_black\skywall_cloud_black_01.obj;11157.23828125;115.2093276977539;21294.857421875;0;338.5;0;0;40.609375;6633217;m2;528520
        ..\..\world\expansion03\doodads\gilneas\trees\darkforesttree01.obj;11190.1953125;118.06371307373047;21284.560546875;-5.423647403717041;263.1670227050781;6.371989727020264;0;2.259765625;6622653;m2;255413
        ..\..\world\generic\nightelf\passivedoodads\pottery\elvenpottery04.obj;11227.115234375;1.2740103006362915;21423.984375;-72.4320068359375;0;44.73794937133789;0;1;6622655;m2;198804
        ..\..\world\wmo\dungeon\md_shipwreck\shipwreck_d_set0.obj;11255.1181640625;-35.53384017944336;21599.619140625;-4.946645736694336;208;21.16083526611328;0;1;6620771;wmo;111520
        ..\..\world\wmo\dungeon\md_caveden\md_mushroomden_set0.obj;11234.62109375;5.478230953216553;21381.78515625;0;50.5;0;0;1;6784782;wmo;110497
     */

    public static final String CMAP_WORLD_CHILDREN = "$CMAP_WORLD_CHILDREN";

    public static final String CMAP_ENTITY_ORIGIN_X = "$ORIGIN_X";
    public static final String CMAP_ENTITY_ORIGIN_Y = "$ORIGIN_Y";
    public static final String CMAP_ENTITY_ORIGIN_Z = "$ORIGIN_Z";

    public static final String CMAP_ENTITY_ANGLE_X = "$ANGLE_X";
    public static final String CMAP_ENTITY_ANGLE_Y = "$ANGLE_Y";
    public static final String CMAP_ENTITY_ANGLE_Z = "$ANGLE_Z";

    public static final String CMAP_ENTITY_SCALE_X = "$SCALE_X";
    public static final String CMAP_ENTITY_SCALE_Y = "$SCALE_Y";
    public static final String CMAP_ENTITY_SCALE_Z = "$SCALE_Z";

    public static final String CMAP_ENTITY_CLASS_NAME = "$CLASS_NAME";
    public static final String CMAP_ENTITY_VMDL_MODEL = "$VMDL_MODEL";

    public static boolean create(CMapEntity[] entities) {
        String vmapTemplate = new String(FileUtil.readFully(new File("templates/vmap.template")));
        vmapTemplate = vmapTemplate.replace(CMAP_WORLD_CHILDREN, buildEntities(entities));
        return FileUtil.writeFully(new File("out.vmap"), vmapTemplate.getBytes());
    }

    private static String buildEntities(CMapEntity[] entities) {
        String vmapChildTemplate = new String(FileUtil.readFully(new File("templates/vamp_child.template")));
        StringBuilder builder = new StringBuilder();
        for(CMapEntity entity : entities){
            if(entity == null){continue;}
            String rawEntity = vmapChildTemplate.replace(CMAP_ENTITY_ORIGIN_X, ""+entity.xPosition);
            rawEntity = rawEntity.replace(CMAP_ENTITY_ORIGIN_Y, ""+entity.yPosition);
            rawEntity = rawEntity.replace(CMAP_ENTITY_ORIGIN_Z, ""+entity.zPosition);

            rawEntity = rawEntity.replace(CMAP_ENTITY_ANGLE_X, ""+entity.xAngle);
            rawEntity = rawEntity.replace(CMAP_ENTITY_ANGLE_Y, ""+entity.yAngle);
            rawEntity = rawEntity.replace(CMAP_ENTITY_ANGLE_Z, ""+entity.zAngle);

            rawEntity = rawEntity.replace(CMAP_ENTITY_SCALE_X, ""+entity.xScale);
            rawEntity = rawEntity.replace(CMAP_ENTITY_SCALE_Y, ""+entity.yScale);
            rawEntity = rawEntity.replace(CMAP_ENTITY_SCALE_Z, ""+entity.zScale);

            rawEntity = rawEntity.replace(CMAP_ENTITY_CLASS_NAME, ""+entity.className);
            rawEntity = rawEntity.replace(CMAP_ENTITY_VMDL_MODEL, ""+entity.vmdlModel);
            builder.append(rawEntity);
            builder.append(",");
        }
        return builder.substring(0, builder.length()-1);
    }

    // ModelFile;PositionX;PositionY;PositionZ;RotationX;RotationY;RotationZ;RotationW;ScaleFactor;ModelId;Type;FileDataID
    public static CMapEntity[] processCsv(File file) {
        if(!file.getName().contains("ModelPlacementInformation")){
            return null;
        }
        String raw = new String(FileUtil.readFully(file));
        String[] lines = raw.split("\n");
        CMapEntity[] entities = new CMapEntity[lines.length];
        for(int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(";");
            double xPos = Double.parseDouble(parts[1]);
            double yPos = Double.parseDouble(parts[2]);
            double zPos = Double.parseDouble(parts[3]);
            double xRot = Double.parseDouble(parts[4]);
            double yRot = Double.parseDouble(parts[5]);
            double zRot = Double.parseDouble(parts[6]);
            double scale = Double.parseDouble(parts[8]);
            parts = parts[0].split(Pattern.quote("\\"));
            entities[i] = new CMapEntity(xPos,yPos,zPos,xRot,yRot,zRot,scale,scale,scale,"prop_static","models/"+parts[parts.length-1].split("\\.")[0] + ".vmdl");
        }
        return entities;
    }

}
