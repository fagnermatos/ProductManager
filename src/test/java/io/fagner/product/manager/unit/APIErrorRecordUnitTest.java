package io.fagner.product.manager.unit;

import io.fagner.product.manager.handler.APIError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class APIErrorRecordUnitTest {

    @Test
    public void testBuildToString() {
        var message = APIError.builder().message("message").code(200).toString();
        assertThat(message).isEqualTo("APIError.APIErrorBuilder(message=message, code=200)");
    }


}
