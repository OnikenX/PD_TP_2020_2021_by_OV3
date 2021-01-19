package pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.ApiRest.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.lei.pd.tp20_21.Server.Model.Connectivity.ApiRest.RestApiGetFileApplication;

@RestController
public class FileController {    


//    public byte[] download(@PathVariable(value="filename") String filename)throws IOException
//    {
//        Path path = Paths.get(RestApiGetFileApplication.directoryPath + File.separator + filename);
//        return Files.readAllBytes(path);
//    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> download(@PathVariable(value="filename") String filename)throws IOException
    {
        File file = new File(RestApiGetFileApplication.directoryPath + File.separator + filename);
        Path path = Paths.get(file.getCanonicalPath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        //header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment");
        //header.add(HttpHeaders.CONTENT_DISPOSITION, "inline");
        
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
