package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudyMaterialTest {

    @Test
    public void testStudyMaterial() {
        StudyMaterial sm = new StudyMaterial("test.pdf", "test", 2);
        assertEquals("test.pdf", sm.getMaterial());
        assertEquals("test", sm.getCategory());
        assertEquals(2, sm.getUserId());

        sm.setMaterials("test2.pdf");
        sm.setCategory("test2");
        assertEquals("test2.pdf", sm.getMaterial());
        assertEquals("test2", sm.getCategory());
    }

}
