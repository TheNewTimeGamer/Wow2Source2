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

    public String toString(String template, boolean asGroup){
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

        if(this.children != null) {
            StringBuilder rawChildren = new StringBuilder();
            for (CMapEntity entity : this.children) {
                if (entity == null) {
                    continue;
                }
                rawChildren.append(entity.toString(template, false));
                rawChildren.append(",");
            }
            if(!asGroup) {
                rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_CHILDREN, rawChildren.substring(0, rawChildren.length() - 1));
                return rawEntity;
            }
            rawEntity = rawEntity.replace("\n\t\t\t", "\n\t\t\t\t\t");
            rawEntity = "\"CMapGroup\"\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"id\" \"elementid\" \"5ee2e7dc-c410-4112-85be-7d8d97a6226d\"\n" +
                        "\t\t\t\t\"origin\" \"vector3\" \"0 0 0\"\n" +
                        "\t\t\t\t\"angles\" \"qangle\" \"0 0 0\"\n" +
                        "\t\t\t\t\"scales\" \"vector3\" \"1 1 1\"\n" +
                        "\t\t\t\t\"nodeID\" \"int\" \"7\"\n" +
                        "\t\t\t\t\"referenceID\" \"uint64\" \"0x5f05a986f7b04310\"\n" +
                        "\t\t\t\t\"children\" \"element_array\" \n" +
                        "\t\t\t\t[\n" +
                        "\t\t\t\t\t" + rawEntity + ",\n\t\t\t\t\t";
            rawEntity = rawEntity + rawChildren.substring(0, rawChildren.length() - 1).replace("\n\t\t\t", "\n\t\t\t\t\t");
            String footer = "\n\t\t\t\t\"editorOnly\" \"bool\" \"0\"\n" +
                    "\t\t\t\t\"force_hidden\" \"bool\" \"0\"\n" +
                    "\t\t\t\t\"transformLocked\" \"bool\" \"0\"\n" +
                    "\t\t\t\t\"variableTargetKeys\" \"string_array\" \n" +
                    "\t\t\t\t[\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t\t\"variableNames\" \"string_array\" \n" +
                    "\t\t\t\t[\n" +
                    "\t\t\t\t]";
            rawEntity = rawEntity + "\n\t\t\t\t]" + footer + "\n\t\t\t}";
        }
        rawEntity = rawEntity.replace(VmapConverter.CMAP_ENTITY_CHILDREN, "");
        return rawEntity;
    }

}
