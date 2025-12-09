package kr.co.dimillion.lcapp.interfaces.api;

import kr.co.dimillion.lcapp.application.FileSystem;
import kr.co.dimillion.lcapp.application.ManualRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileRestController {
    private final FileSystem fileSystem;
    private final ManualRepository manualRepository;

    @GetMapping("/v1/file")
    public ResponseEntity<byte[]> downloadFile(@RequestParam Integer fileId, @RequestParam String subFolder, @RequestParam String fileName) {
        byte[] data = fileSystem.downloadFile(subFolder, fileName);
        String name = "";
        if (subFolder.equals("menual_store")) {
            name = manualRepository.findById(fileId)
                    .orElseThrow()
                    .getName();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(
                ContentDisposition.attachment().filename(name, StandardCharsets.UTF_8).build()
        );
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}
