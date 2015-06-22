package nl.tudelft.ti2806.riverrush.domain.event;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import nl.tudelft.ti2806.riverrush.domain.event.AbstractAnimalEventTest;

/**
 * A test for {@link AssetsLoadedEvent}
 */
public class AssetsLoadedEventTest extends AbstractAnimalEventTest {

    protected AssetsLoadedEvent event;

    @Override
    @Test
    public void testSerialize() throws Exception {
        event = (AssetsLoadedEvent) getInstance();
        event.setAnimal(getAnimalId());

        String s = event.serialize(protocol);
        super.testSerialize();
    }

    @Override
    public Event getInstance() {
        event = new AssetsLoadedEvent();
        super.addVariables(event);
        return event;
    }

    @Override
    public Map<String, String> getTestMap() {
        Map<String, String> testMap = new HashMap<>();
        testMap.putAll(super.getTestMap());
        return testMap;
    }

    @Test
    public void testDeserializeExtraFunc() throws Exception {
        event = (AssetsLoadedEvent) getInstance();
    }

}
