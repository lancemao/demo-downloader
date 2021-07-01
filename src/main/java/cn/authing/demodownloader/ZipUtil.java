package cn.authing.demodownloader;

import cn.authing.demodownloader.stack.StackBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);

    private StackBase stack;

    private final List <String> fileList = new ArrayList<>();
    private String sourceFolder;

    public ZipUtil(StackBase stack) {
        try {
            this.stack = stack;

            String src = stack.templateSource();
            File file = new File(src);
            sourceFolder = file.getAbsolutePath();
            generateFileList(file);

            logger.info("Files for " + src);

            for (String f : fileList) {
                logger.info(f);
            }
        } catch (Exception e) {
            logger.error("cannot find template project for " + stack, e);
        }
    }

    private void generateFileList(File node) {
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            if (subNote != null) {
                for (String filename : subNote) {
                    generateFileList(new File(node, filename));
                }
            }
        }
    }

    private String generateZipEntry(String file) {
        return file.substring(stack.templateSource().length() + 1);
    }

    public void zipIt(String zipFile) {
        FileOutputStream fos;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            for (String file: this.fileList) {
                stack.addFileToZip(sourceFolder, file, zos);
            }

            zos.closeEntry();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (zos != null)
                    zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
