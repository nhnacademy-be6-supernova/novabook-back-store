package store.novabook.store.image.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.image.controller.docs.ImageControllerDocs;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/images")
public class ImageController implements ImageControllerDocs {

}
