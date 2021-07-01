package cn.authing.demodownloader;

import cn.authing.demodownloader.stack.JavaSpringBoot;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping("samples")
public class DownloadController {

    @ResponseBody
    @RequestMapping("/java-spring-boot")
    public ResponseEntity<Resource> downloadJavaSpringBoot(HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           @RequestParam String appId) throws IOException {
        JavaSpringBoot javaSpringBoot = new JavaSpringBoot();
        javaSpringBoot.setAppId(appId);
        ZipUtil zip = new ZipUtil(javaSpringBoot);

        String fileName = appId + ".zip";
        zip.zipIt("cache/java-spring-boot" + File.separator + fileName);
        return getResponse(request, "cache/java-spring-boot", fileName);
    }

    private static ResponseEntity<Resource> getResponse(HttpServletRequest request, String path, String fileName) {
        try {
            Path fileStorageLocation = Path.of(path);
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                String contentType = null;
                try {
                    contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // Fallback to the default content type if type could not be determined
                if(contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
