package io.fagner.product.manager.controller;

import io.fagner.product.manager.handler.BadRequestException;
import io.fagner.product.manager.model.dto.ProductInputRecord;
import io.fagner.product.manager.model.dto.ProductRecord;
import io.fagner.product.manager.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @GetMapping
    public List<ProductRecord> findAll() {
        return productService.findAll();
    }

    @GetMapping("{id}")
    public ProductRecord findById(@PathVariable("id") Long id) {
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ProductRecord save(@RequestBody @Valid ProductInputRecord product) {
        return productService.save(product);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable("id") Long id, @RequestBody @Valid ProductRecord product) {
        if (!id.equals(product.id())) {
            throw new BadRequestException("Não é possível alterar esse produto. As informações enviadas são de um produto diferente.");
        }
        productService.update(product);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        productService.deleteById(id);
    }


}
