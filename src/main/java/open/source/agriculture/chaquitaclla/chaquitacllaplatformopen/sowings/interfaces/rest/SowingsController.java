package open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.crops.domain.model.queries.GetCropByIdQuery;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.crops.interfaces.REST.resources.CropResource;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.forum.domain.model.queries.GetAllQuestionsQuery;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.forum.interfaces.rest.resources.QuestionResource;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.forum.interfaces.rest.transform.QuestionResourceFromEntityAssembler;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.products.domain.services.ProductCommandService;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.products.infrastructure.persistence.jpa.repositories.ProductRepository;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.products.interfaces.rest.resources.CreateProductResource;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.products.interfaces.rest.resources.ProductResource;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.products.interfaces.rest.transform.CreateProductCommandFromResourceAssembler;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.products.interfaces.rest.transform.ProductResourceFromEntityAssembler;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.domain.model.queries.GetAllSowingsQuery;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.domain.model.queries.GetSowingByIdQuery;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.domain.services.SowingCommandService;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.domain.services.SowingQueryService;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.infrastructure.persistence.jpa.repositories.SowingRepository;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.interfaces.rest.resources.CreateSowingResource;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.interfaces.rest.resources.SowingResource;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.interfaces.rest.transform.CreateSowingCommandFromResourceAssembler;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.interfaces.rest.transform.SowingResourceFromEntityAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/sowings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Sowings", description = "Sowing Management Endpoints")
public class SowingsController {
    private final SowingCommandService sowingCommandService;
    private final SowingQueryService sowingQueryService;
    private final SowingRepository sowingRepository;

    private final ProductCommandService productCommandService;
    private final ProductRepository productRepository;

    public SowingsController(SowingCommandService sowingCommandService,
                             SowingRepository sowingRepository,
                             SowingQueryService sowingQueryService,
                             ProductCommandService productCommandService,
                             ProductRepository productRepository) {
        this.sowingCommandService = sowingCommandService;
        this.sowingRepository = sowingRepository;
        this.sowingQueryService = sowingQueryService;
        this.productCommandService = productCommandService;
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<SowingResource> createSowing(@RequestBody CreateSowingResource createSowingResource) {
        var createSowingCommand = CreateSowingCommandFromResourceAssembler.fromResource(createSowingResource);
        var sowingId = sowingCommandService.handle(createSowingCommand);
        if(sowingId == 0L) return ResponseEntity.badRequest().build();

        // Retrieve the newly created Sowing from the database
        var sowing = sowingRepository.findById(sowingId).orElseThrow();

        // Convert the Sowing entity to a SowingResource
        var sowingResourceCreated = SowingResourceFromEntityAssembler.fromEntity(sowing);

        return new ResponseEntity<>(sowingResourceCreated, HttpStatus.CREATED);
    }
    @GetMapping()
    public ResponseEntity<List<SowingResource>> getAllSowings(){
        var getAllSowingsQuery = new GetAllSowingsQuery();
        var sowings = sowingQueryService.handle(getAllSowingsQuery);
        var sowingResource = sowings.stream()
                .map(SowingResourceFromEntityAssembler::fromEntity)
                .toList();
        return ResponseEntity.ok(sowingResource);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SowingResource> getSowing(@PathVariable Long id) {
        return sowingQueryService.handle(new GetSowingByIdQuery(id))
                .map(SowingResourceFromEntityAssembler::fromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/{sowingId}/products")
    public ResponseEntity<ProductResource> createProduct(@PathVariable Long sowingId, @RequestBody CreateProductResource createProductResource) {
        // Check if the sowing exists
        var sowing = sowingRepository.findById(sowingId);
        if (sowing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Create the product
        var createProductCommand = CreateProductCommandFromResourceAssembler.toCommandFromResource(createProductResource);
        var productId = productCommandService.handle(createProductCommand);
        if(productId == 0L) return ResponseEntity.badRequest().build();

        // Retrieve the newly created Product from the database
        var product = productRepository.findById(productId).orElseThrow();

        // Convert the Product entity to a ProductResource
        var productResourceCreated = ProductResourceFromEntityAssembler.toResourceFromEntity(product);

        return new ResponseEntity<>(productResourceCreated, HttpStatus.CREATED);
    }
}