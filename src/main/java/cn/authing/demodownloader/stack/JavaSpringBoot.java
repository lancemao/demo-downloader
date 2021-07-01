package cn.authing.demodownloader.stack;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipOutputStream;

public class JavaSpringBoot extends StackBase {

    private static final String TARGET = "src/main/java/cn/authing/demo/Application.java";

    private String appId;

    @Override
    public String templateSource() {
        return "authing-demo-java-spring-boot";
    }

    @Override
    public void addFileToZip(String sourceFolder, String file, ZipOutputStream zos) throws IOException {
        if (file != null && file.equals(TARGET)) {
            Path fileName = Path.of(sourceFolder + File.separator + file);
            String data = Files.readString(fileName);
            data = data.replace("{appId}", appId);
            addFileToZip(new ByteArrayInputStream(data.getBytes()), file, zos);
        } else {
            super.addFileToZip(sourceFolder, file, zos);
        }
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
