import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelloWorldTest {

    @Test
    void testHelloWorld() {
        assertEquals("Hello, World!", getHelloWorld());
    }

    private String getHelloWorld() {
        return "Hello, World!";
    }
}