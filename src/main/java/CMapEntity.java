public class CMapEntity {

    public final double xPosition, yPosition, zPosition;
    public final double xAngle, yAngle, zAngle;
    public final double xScale, yScale, zScale;

    public final String className;
    public final String vmdlModel;

    public CMapEntity(  double xPosition, double yPosition, double zPosition,
                        double xAngle,    double yAngle,    double zAngle,
                        double xScale,    double yScale,    double zScale,
                        String className, String vmdlModel                  ) {
        this.xPosition = xPosition; this.yPosition = yPosition; this.zPosition = zPosition;
        this.xAngle = xAngle;       this.yAngle = yAngle;       this.zAngle = zAngle;
        this.xScale = xScale;       this.yScale = yScale;       this.zScale = zScale;
        this.className = className; this.vmdlModel = vmdlModel;
    }

}
