package io.fagner.product.manager.unit;

import io.fagner.product.manager.handler.APIError;
import io.fagner.product.manager.handler.BadRequestException;
import io.fagner.product.manager.handler.EntityNotFoundException;
import io.fagner.product.manager.handler.ExceptionHandler;
import io.fagner.product.manager.model.dto.ProductInputRecord;
import io.fagner.product.manager.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerTest {

    @InjectMocks
    private ExceptionHandler exceptionHandler;


   @Test
    public void shouldHandleResourceBadRequestException() {
       var runtimeException = new BadRequestException("Erro durante requisição");
       APIError responseEntity = exceptionHandler.handleResourceBadRequestException(runtimeException);

       assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.code());
       assertEquals("Erro durante requisição", responseEntity.message());
    }


   @Test
    public void shouldHandleResourceEntityNotFoundException() {
       EntityNotFoundException runtimeException = new EntityNotFoundException("A entidade não foi encontrada.");
       APIError responseEntity = exceptionHandler.handleResourceEntityNotFoundException(runtimeException);

       assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.code());
       assertEquals("A entidade não foi encontrada.", responseEntity.message());
    }

   @Test
    public void shouldHandleResourceMethodArgumentNotValidException() throws NoSuchMethodException {
       MethodParameter parameter = new MethodParameter(ProductService.class.getMethod("save", ProductInputRecord.class), 0);
       BindingResult bindingResult = new BeanPropertyBindingResult( new Object(), "productInput");
       bindingResult.addError(new ObjectError("ProductInputRecord", "Argumento inválido!"));

       MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);
       APIError responseEntity = exceptionHandler.handleResourceMethodArgumentNotValidException(exception);

       assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.code());
       assertEquals("Argumento inválido!", responseEntity.message());
    }

   @Test
    public void shouldHandleResourceDataIntegrityViolationException() {
       DataIntegrityViolationException runtimeException = new DataIntegrityViolationException("");
       APIError responseEntity = exceptionHandler.handleResourceDataIntegrityViolationException(runtimeException);

       assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.code());
       assertEquals("As informações já foram cadastradas previamente.", responseEntity.message());
    }
}
