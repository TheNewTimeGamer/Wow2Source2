public class CMapEntity {

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

    public String toString(String template){
        String rawEntity = template.replace(VmapConverter.CMAP_ENTITY_ORIGIN_X, ""+this.xPosition);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_ORIGIN_Y, ""+this.yPosition);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_ORIGIN_Z, ""+this.zPosition);

        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_ANGLE_X, ""+this.xAngle);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_ANGLE_Y, ""+this.yAngle);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_ANGLE_Z, ""+this.zAngle);

        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_SCALE_X, ""+this.xScale);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_SCALE_Y, ""+this.yScale);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_SCALE_Z, ""+this.zScale);

        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_CLASS_NAME, ""+this.className);
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_VMDL_MODEL, ""+this.vmdlModel);

        if(this.children != null) {
            StringBuilder rawChildren = new StringBuilder();
            for (CMapEntity entity : this.children) {
                if (entity == null) {
                    continue;
                }
                rawChildren.append(entity.toString(template));
                rawChildren.append(",");
            }
            rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_CHILDREN, rawChildren.substring(0, rawChildren.length() - 1));
        }else{
            rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_CHILDREN, "");
        }
        return rawEntity;
    }

}
