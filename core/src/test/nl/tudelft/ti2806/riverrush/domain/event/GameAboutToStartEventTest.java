package nl.tudelft.ti2806.riverrush.domain.event;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test for {@link GameAboutToStartEvent}
 */
public class GameAboutToStartEventTest extends AbstractAnimalEventTest {

    protected GameAboutToStartEvent event;

    public final int seconds = 50;

    @BeforeClass
    public static void setup() {
        uglyList.add("seconds");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        uglyList.remove("seconds");
    }

    @Override
    @Test
    public void testSerialize() throws Exception{
        event = (GameAboutToStartEvent) getInstance();
        event.setAnimal(getAnimalId());

        String s = event.serialize(protocol);
        assertTrue(s.contains("seconds" + protocol.getKeyValueSeperator() + seconds));
        super.testSerialize();
    }

    @Override
    public Event getInstance() {
        event = new GameAboutToStartEvent();
        event.setSeconds(seconds);
        super.addVariables(event);
        return event;
    }

    @Override
    public Map<String, String> getTestMap() {
        Map<String, String> testMap = new HashMap<>();
        testMap.putAll(super.getTestMap());
        testMap.put("seconds", Integer.toString(seconds));
        return testMap;
    }

    @Test
    public void testDeserializeExtraFunc() throws Exception {
        event = (GameAboutToStartEvent) getInstance();
        assertEquals(seconds, event.getSeconds());
    }
}
