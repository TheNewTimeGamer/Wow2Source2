import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

// Vmat essentially replaces mtl.

public class VmatConverter {

    public static final String TEXTURE_COLOR_VALUE = "$TEXTURE_COLOR_VALUE";
    public static final String TEXTURE_FLAG_TRANS = "$TEXTURE_FLAG_TRANS";
    public static final String TEXTURE_TRANS_VALUE = "$TEXTURE_TRANS_VALUE";

    public static boolean convert(String path){
        return convert(new File(path));
    }

    public static boolean convert(File file) {
        boolean hasTransMap = generateTransMap(file);
        System.out.print(hasTransMap + " .. ");
        String filePath = file.getAbsolutePath().split(Wow2Source2.Source2ProjectName)[1].replace("\\", "/");
        filePath = filePath.substring(1);
        String fileName = file.getName();
        String template = new String(FileUtil.readFully("templates/vmat_complex.template"));
        template = template.replace(TEXTURE_COLOR_VALUE, filePath);
        if(hasTransMap) {
            String fileType = filePath.split("\\.")[1];
            String transFileName = filePath.split("\\.")[0] + "_trans." + fileType;
            template = template.replace(TEXTURE_FLAG_TRANS, "1");
            template = template.replace(TEXTURE_TRANS_VALUE, transFileName);
        }else{
            template = template.replace(TEXTURE_FLAG_TRANS, "0");
            template = template.replace(TEXTURE_TRANS_VALUE, "");
        }
        return FileUtil.writeFully(Wow2Source2.Source2ProjectRoot + "/materials/mat_" + fileName.split("\\.")[0] + ".vmat", template.getBytes());
    }

    public static boolean generateTransMap(File file) {
        System.out.print("Transmap ");
        boolean hasTransMap = false;
        int lowest = Integer.MAX_VALUE;
        try {
            BufferedImage texture = ImageIO.read(file);
            System.out.print("type -> " + texture.getType() + " .. ");
            if(texture.getColorModel().getTransparency() == Transparency.OPAQUE){
                System.out.print(" OPAQUE .. ");
                return false;
            }
            for(int y = 0; y < texture.getHeight(); y++){
                for(int x = 0; x < texture.getWidth(); x++){
                    int alpha = new Color(texture.getRGB(x,y), true).getAlpha();
                    if(alpha < lowest) {
                        lowest = alpha;
                    }
                    if(alpha > 0) {
                        texture.setRGB(x, y, 0xFFFFFF);
                    }else{
                        texture.setRGB(x, y, 0);
                        hasTransMap = true;
                    }
                }
            }
            System.out.print(lowest + " ..");
            if(hasTransMap) {
                String fileType = file.getName().split("\\.")[1];
                File transFile = new File(file.getPath().replace("." + fileType, "_trans." + fileType));
                System.out.print("Writing _trans file: " + transFile.getName() + " .. ");
                ImageIO.write(texture, fileType.toUpperCase(), transFile);
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return hasTransMap;
    }

    private static String performMtlSniff(String fileName, File file) {
        String[] lines = new String(FileUtil.readFully(file)).split("\r\n");
        for(String line : lines){
            if(line.contains(fileName.split("\\.")[0])){
                return line.split("usemtl ")[1];
            }
        }
        return fileName;
    }

}
