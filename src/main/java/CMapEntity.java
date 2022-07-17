import java.io.File;

public class CMapEntity {

    public static final String CMAP_GROUP_CHILDREN = "$CMAP_GROUP_CHILDREN";

    public final double xPosition, yPosition, zPosition;
    public final double xAngle, yAngle, zAngle;
    public final double xScale, yScale, zScale;

    public final String className;
    public final String vmdlModel;

    public CMapEntity[] children;

    public CMapEntity(  double xPosition, double yPosition, double zPosition,
                        double xAngle,    double yAngle,    double zAngle,
                        double xScale,    double yScale,    double zScale,
                        String className, String vmdlModel,  CMapEntity[] children) {
        this.xPosition = xPosition; this.yPosition = yPosition; this.zPosition = zPosition;
        this.xAngle = xAngle;       this.yAngle = yAngle;       this.zAngle = zAngle;
        this.xScale = xScale;       this.yScale = yScale;       this.zScale = zScale;
        this.className = className; this.vmdlModel = vmdlModel; this.children = children;
    }

    public String toString(String template, boolean asGroup, boolean processChildren){
        String rawEntity = template.replace(VmapConverter.CMAP_ENTITY_ORIGIN_X, ""+this.xPosition);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_ORIGIN_Y, ""+this.yPosition);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_ORIGIN_Z, ""+this.zPosition);

        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_ANGLE_X, ""+this.xAngle);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_ANGLE_Y, ""+(this.yAngle-90));
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_ANGLE_Z, ""+this.zAngle);

        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_SCALE_X, ""+this.xScale);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_SCALE_Y, ""+this.yScale);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_SCALE_Z, ""+this.zScale);

        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_CLASS_NAME, ""+this.className);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_VMDL_MODEL, ""+this.vmdlModel);

        if(this.children != null && processChildren) {
            StringBuilder rawChildren = new StringBuilder();
            for (CMapEntity entity : this.children) {
                if (entity == null) {
                    continue;
                }
                rawChildren.append(entity.toString(template, false, true));
            }
            if(!asGroup) {
                rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_CHILDREN, rawChildren);
                return rawEntity;
            }
            String groupTemplate = new String(FileUtil.readFully("templates/cmap_group.template"));
            rawEntity = rawEntity.replace("\n\t\t\t", "\n\t\t\t\t\t");
            rawEntity = rawEntity + "," + rawChildren.toString().replace("\n\t\t\t", "\n\t\t\t\t\t");
            rawEntity = groupTemplate.replace(CMAP_GROUP_CHILDREN, rawEntity);
        }

        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_CHILDREN, "");
        return rawEntity;
    }

}
