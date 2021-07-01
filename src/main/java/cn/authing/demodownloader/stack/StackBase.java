package cn.authing.demodownloader.stack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public abstract class StackBase {

    protected byte[] buffer = new byte[1024];

    public abstract String templateSource();

    public void addFileToZip(String sourceFolder, String file, ZipOutputStream zos) throws IOException {
        FileInputStream in = new FileInputStream(sourceFolder + File.separator + file);
        addFileToZip(in, file, zos);
    }

    public void addFileToZip(InputStream in, String file, ZipOutputStream zos) {
        ZipEntry ze = new ZipEntry(templateSource() + File.separator + file);

        try {
            zos.putNextEntry(ze);
            int len;
            while ((len = in .read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
