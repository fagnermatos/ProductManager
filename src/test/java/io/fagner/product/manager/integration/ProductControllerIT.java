package io.fagner.product.manager.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fagner.product.manager.dao.DaoFactory;
import io.fagner.product.manager.model.Product;
import io.fagner.product.manager.model.dto.ProductInputRecord;
import io.fagner.product.manager.model.dto.ProductRecord;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DaoFactory daoFactory;

    @Before
    @Transactional
    public void cleanDatabase() {
        daoFactory.productDao().deleteAll();
    }

    @Test
    public void shouldFindAllProducts() throws Exception {
        var product1 = new Product(null, "Product 1", "Description 1", new BigDecimal("1.00"), 10);
        var product2 = new Product(null, "Product 2", "Description 2", new BigDecimal("2.00"), 20);

        List.of(product1, product2).forEach(daoFactory.productDao()::save);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/products").contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Product 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Product 2"));
    }

    @Test
    public void shouldFindProductById() throws Exception {
        var product = new Product(null, "Product 1", "Description 1", new BigDecimal("1.00"), 10);
        System.out.println(daoFactory.productDao().findAll());
        var savedProduct = daoFactory.productDao().save(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", savedProduct.getId())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Product 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description 1"));
    }

    @Test
    public void shouldThrowExceptionWhenTryGetNonExistentProduct() throws Exception {
        var resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldSaveProduct() throws Exception {
        var product = new ProductInputRecord( "Product 1", "Description 1", new BigDecimal("1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());

        var products = daoFactory.productDao().findAll();
        Assertions.assertThat(products).extracting("name").contains("Product 1");
    }

    @Test
    public void shouldFailWhenNameIsBlankDuringSave() throws Exception {
        var product = new ProductInputRecord( "", "Description 1", new BigDecimal("1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"O nome do produto é obrigatório\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenNameIsNullDuringSave() throws Exception {
        var product = new ProductInputRecord( null, "Description 1", new BigDecimal("1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"O nome do produto é obrigatório\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenDescriptionIsBlankDuringSave() throws Exception {
        var product = new ProductInputRecord( "Product", "", new BigDecimal("1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"A descrição do produto é obrigatório\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenDescriptionIsNullDuringSave() throws Exception {
        var product = new ProductInputRecord( "Product", null, new BigDecimal("1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"A descrição do produto é obrigatório\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenPriceIsNullDuringSave() throws Exception {
        var product = new ProductInputRecord( "Product", "Description", null, 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"O preço do produto é obrigatório\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenPriceIsNegativeDuringSave() throws Exception {
        var product = new ProductInputRecord( "Product", "Description", new BigDecimal("-1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"O preço não pode ser negativo.\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenStockIsNullDuringSave() throws Exception {
        var product = new ProductInputRecord( "Product", "Description", new BigDecimal("1.00"), -10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"A quantidade de produto em estoque não pode ser negativo.\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenStockIsNegativeDuringSave() throws Exception {
        var product = new ProductInputRecord( "Product", "Description", new BigDecimal("1.00"), -10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"A quantidade de produto em estoque não pode ser negativo.\",\"code\":400}"));
    }

    @Test
    public void shouldThrowsExceptionWhenTrySaveProductsWithNotValidArgument() throws Exception {
        var product = new ProductInputRecord( "Product 1", "Description 1", new BigDecimal("-1.00"), -10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldThrowsExceptionWhenTrySaveDuplicatedProduct() throws Exception {
        var product = new ProductInputRecord( "Product 1", "Description 1", new BigDecimal("1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void shouldUpdateProduct() throws Exception {
        var product = new Product(null, "Product 1", "Description 1", new BigDecimal("300.00"), 10);
        var savedProduct = daoFactory.productDao().save(product);

        var productToUpdate = new ProductRecord( savedProduct.getId(), "Updated Product", "Description 1", new BigDecimal("1.00"), 10);
        String updatedJsonBody = objectMapper.writeValueAsString(productToUpdate);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", savedProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());

        var updatedProduct = daoFactory.productDao().findById(savedProduct.getId()).orElse(null);
        Assertions.assertThat(updatedProduct).isNotNull();
        Assertions.assertThat(updatedProduct.getName()).isEqualTo("Updated Product");
    }

    @Test
    public void shouldFailWhenNameIsBlankDuringUpdate() throws Exception {
        var product = new ProductRecord( 1L, "", "Description 1", new BigDecimal("1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"O nome do produto é obrigatório\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenNameIsNullDuringUpdate() throws Exception {
        var product = new ProductRecord( 1L, null, "Description 1", new BigDecimal("1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"O nome do produto é obrigatório\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenDescriptionIsBlankDuringUpdate() throws Exception {
        var product = new ProductRecord( 1L, "Product", "", new BigDecimal("1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"A descrição do produto é obrigatório\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenDescriptionIsNullDuringUpdate() throws Exception {
        var product = new ProductRecord( 1L, "Product", null, new BigDecimal("1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"A descrição do produto é obrigatório\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenPriceIsNullDuringUpdate() throws Exception {
        var product = new ProductRecord( 1L, "Product", "Description", null, 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"O preço do produto é obrigatório\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenPriceIsNegativeDuringUpdate() throws Exception {
        var product = new ProductRecord( 1L, "Product", "Description", new BigDecimal("-1.00"), 10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"O preço não pode ser negativo.\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenStockIsNullDuringUpdate() throws Exception {
        var product = new ProductRecord( 1L, "Product", "Description", new BigDecimal("1.00"), -10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"A quantidade de produto em estoque não pode ser negativo.\",\"code\":400}"));
    }

    @Test
    public void shouldFailWhenStockIsNegativeDuringUpdate() throws Exception {
        var product = new ProductRecord( 1L, "Product", "Description", new BigDecimal("1.00"), -10);
        String jsonBody = objectMapper.writeValueAsString(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"message\":\"A quantidade de produto em estoque não pode ser negativo.\",\"code\":400}"));
    }

    @Test
    public void shouldThrowExceptionOnUpdateWhenIdsDiverge() throws Exception {
        var product = new Product(null, "Product 1", "Description 1", new BigDecimal("300.00"), 10);
        var savedProduct = daoFactory.productDao().save(product);

        var productToUpdate = new ProductRecord( savedProduct.getId(), "Updated Product", "Description 1", new BigDecimal("1.00"), 10);
        String updatedJsonBody = objectMapper.writeValueAsString(productToUpdate);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", 1000)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJsonBody));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldDeleteProduct() throws Exception {
        var product = new Product(null, "Product 1", "Description 1", new BigDecimal("300.00"), 10);

        var savedProduct = daoFactory.productDao().save(product);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", savedProduct.getId())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertThat(daoFactory.productDao().findById(savedProduct.getId())).isEmpty();
    }
}
